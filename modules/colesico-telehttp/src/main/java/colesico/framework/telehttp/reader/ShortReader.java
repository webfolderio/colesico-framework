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

package colesico.framework.telehttp.reader;

import colesico.framework.http.HttpContext;
import colesico.framework.telehttp.HttpTRContext;
import colesico.framework.telehttp.HttpTeleReader;
import colesico.framework.telehttp.t9n.Messages;
import colesico.framework.router.RouterContext;
import colesico.framework.teleapi.TeleException;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * @author Vladlen Larionov
 */
@Singleton
public final class ShortReader<C extends HttpTRContext> extends HttpTeleReader<Short, C> {

    private final Messages messages;

    @Inject
    public ShortReader(Provider<RouterContext> routerContextProv, Provider<HttpContext> httpContextProv, Messages messages) {
        super(routerContextProv, httpContextProv);
        this.messages = messages;
    }

    @Override
    public Short read(C ctx) {
        try {
            String val = ctx.getString(getRouterContext(), getRequest());
            if (StringUtils.isEmpty(val)) {
                return null;
            }
            return Short.parseShort(val);
        } catch (Exception ex) {
            throw new TeleException(messages.invalidNumberFormat(ctx.getName()));
        }
    }
}
