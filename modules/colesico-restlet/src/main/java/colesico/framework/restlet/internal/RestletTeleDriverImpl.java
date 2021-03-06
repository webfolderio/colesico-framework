/*
 * Copyright © 2014-2020 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.restlet.internal;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpException;
import colesico.framework.http.HttpRequest;
import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.restlet.RestletConfigPrototype;
import colesico.framework.restlet.RestletErrorResponse;
import colesico.framework.restlet.teleapi.*;
import colesico.framework.security.AuthorityRequiredException;
import colesico.framework.security.PrincipalRequiredException;
import colesico.framework.service.ApplicationException;
import colesico.framework.teleapi.DataPort;
import colesico.framework.teleapi.MethodInvoker;
import colesico.framework.validation.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vladlen Larionov
 */
@Singleton
public class RestletTeleDriverImpl implements RestletTeleDriver {

    public static final String X_REQUESTED_WITH_HEADER = "X-Requested-With";
    public static final String X_REQUESTED_WITH_HEADER_VAL = "XMLHttpRequest";

    protected final Logger log = LoggerFactory.getLogger(RestletTeleDriver.class);

    protected final RestletConfigPrototype config;

    protected final ThreadScope threadScope;
    protected final Provider<HttpContext> httpContextProv;
    protected final RestletDataPort dataPort;
    protected final Polysupplier<RestletRequestListener> reqListenerSup;
    protected final Polysupplier<RestletResponseListener> respListenerSup;


    @Inject
    public RestletTeleDriverImpl(RestletConfigPrototype config,
                                 ThreadScope threadScope,
                                 Provider<HttpContext> httpContextProv,
                                 RestletDataPort dataPort,
                                 Polysupplier<RestletRequestListener> reqListenerSup,
                                 Polysupplier<RestletResponseListener> respListenerSup) {
        this.config = config;
        this.threadScope = threadScope;
        this.httpContextProv = httpContextProv;
        this.dataPort = dataPort;
        this.reqListenerSup = reqListenerSup;
        this.respListenerSup = respListenerSup;
    }

    @Override
    public <S> void invoke(S service, MethodInvoker<S, RestletDataPort> invoker, RestletTIContext invCtx) {
        // Set data port to be accessible
        threadScope.put(DataPort.SCOPE_KEY, dataPort);
        // Retrieve http context
        HttpContext httpCtx = httpContextProv.get();
        try {
            invokeImpl(service, invoker, invCtx, httpCtx);
        } catch (Exception ex) {
            handleCommonError(ex, 500, httpCtx);
        }
    }

    protected void guardCSFR(HttpRequest httpRequest) {
        String xRequestedWith = httpRequest.getHeaders().get(X_REQUESTED_WITH_HEADER);
        if (!X_REQUESTED_WITH_HEADER_VAL.equals(xRequestedWith)) {
            throw new ApplicationException("Http header '" + X_REQUESTED_WITH_HEADER + "=" + X_REQUESTED_WITH_HEADER_VAL + "' required");
        }
    }

    protected void notifyRequestListener(final HttpContext context, final Object service) {
        if (reqListenerSup.isNotEmpty()) {
            reqListenerSup.forEach(s -> s.onRequest(context, dataPort, service), null);
        }
    }

    protected void notifyResponseListener(final HttpContext context) {
        if (respListenerSup.isNotEmpty()) {
            respListenerSup.forEach(s -> s.onResponse(context, dataPort), null);
        }
    }


    protected <S> void invokeImpl(S service, MethodInvoker binder, RestletTIContext invCtx, final HttpContext httpCtx) {
        HttpRequest httpRequest = httpCtx.getRequest();
        try {
            // Request listener notification
            notifyRequestListener(httpCtx, service);

            // CSRF protection
            if (config.enableCSFRProtection()) {
                guardCSFR(httpRequest);
            }

            // Invoke tele-method
            binder.invoke(service, dataPort);

        } catch (HttpException hex) {
            if (hex.getCause() != null) {
                if (hex.getCause() instanceof ValidationException) {
                    handleValidationError((ValidationException) hex.getCause(), hex.getHttpCode(), httpCtx);
                } else {
                    handleCommonError(hex.getCause(), hex.getHttpCode(), httpCtx);
                }
            } else {
                handleCommonError(hex, hex.getHttpCode(), httpCtx);
            }
        } catch (ValidationException vex) {
            handleValidationError(vex, 400, httpCtx);
        } catch (PrincipalRequiredException prex) {
            handleCommonError(prex, 401, httpCtx);
        } catch (AuthorityRequiredException arex) {
            handleCommonError(arex, 403, httpCtx);
        } finally {
            notifyResponseListener(httpCtx);
        }
    }

    protected String getMessage(Throwable ex) {
        Throwable e = ex;
        List<String> messages = new ArrayList<>();

        int depth = 0;
        while (e != null) {
            String message = e.getMessage();
            if (message == null) {
                message = e.getClass().getName();
            }
            messages.add(message);
            if (e.getCause() == e) {
                e = null;
            } else {
                if (depth++ < 8) {
                    e = e.getCause();
                } else {
                    e = null;
                }
            }
        }
        return StringUtils.join(messages, "; ");
    }

    protected void handleCommonError(Throwable ex, int httpCode, HttpContext context) {
        String errMsg = MessageFormat.format("Restlet error: {0}", ExceptionUtils.getRootCauseMessage(ex));
        if (ex instanceof ApplicationException) {
            log.warn(errMsg, ex);
        } else {
            log.error(errMsg, ex);
        }
        RestletErrorResponse response = new RestletErrorResponse(context.getRequest().getRequestURI(), httpCode, getMessage(ex));
        dataPort.writeError(response, httpCode);
    }

    protected void handleValidationError(ValidationException ex, int httpCode, HttpContext context) {
        String errMsg = MessageFormat.format("Restlet validation error: {0}", ExceptionUtils.getRootCauseMessage(ex));
        log.warn(errMsg);
        RestletErrorResponse response = new RestletErrorResponse(context.getRequest().getRequestURI(), httpCode, ex.getIssue());
        dataPort.writeError(response, httpCode);
    }

}
