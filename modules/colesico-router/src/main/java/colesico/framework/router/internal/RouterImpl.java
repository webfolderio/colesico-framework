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

package colesico.framework.router.internal;

import colesico.framework.assist.StrUtils;
import colesico.framework.http.HttpMethod;
import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.router.*;
import colesico.framework.router.assist.RouteTrie;
import colesico.framework.teleapi.TeleFacade;
import colesico.framework.teleapi.TeleMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Vladlen Larionov
 */
public class RouterImpl implements Router {

    protected final Logger log = LoggerFactory.getLogger(Router.class);

    protected final ThreadScope threadScope;

    protected RouteTrie<RouteAction> routeTrie;
    protected RoutesIndex routesIndex;

    @Inject
    public RouterImpl(ThreadScope threadScope) {
        this.threadScope = threadScope;
    }

    protected void loadRoutesMapping(Polysupplier<TeleFacade> teleFacadeSupp) {
        log.debug("Lookup routing tele-facades... ");

        routeTrie = new RouteTrie<>(null);
        routesIndex = new RoutesIndex();

        Iterator<TeleFacade> it = teleFacadeSupp.iterator(null);
        while (it.hasNext()) {
            TeleFacade teleFacade = it.next();
            log.debug("Found routing tele-facade: " + teleFacade.getClass().getName());
            RoutingLigature ligature = (RoutingLigature) teleFacade.getLigature();

            for (RoutingLigature.RouteInfo routeInfo : ligature.getRoutesInfo()) {
                if (log.isDebugEnabled()) {
                    log.debug("Route '" + routeInfo.getRoute() + "' mapped to tele-method '" +
                            ligature.getServiceClass().getName() + "->" + routeInfo.getTeleMethodName());

                }
                RouteTrie.Node<RouteAction> node = routeTrie.addRoute(
                        routeInfo.getRoute(),
                        new RouteAction(routeInfo.getTeleMethodRef(), routeInfo.getRouteAttributes())
                );

                HttpMethod httpMethod = HttpMethod.of(node.getRoot().getName());
                routesIndex.addNode(toRouteId(ligature.getServiceClass(), routeInfo.getTeleMethodName(), httpMethod), node);
            }
        }
    }

    protected void addCustomAction(HttpMethod httpMethod, String route, Class<?> targetClass, TeleMethod targetMethodRef, String targetMethodName, Map<String, String> routeAttributes) {
        RouteTrie.Node<RouteAction> node = routeTrie.addRoute(route, new RouteAction(targetMethodRef, routeAttributes));
        routesIndex.addNode(toRouteId(targetClass, targetMethodName, httpMethod), node);
        if (log.isDebugEnabled()) {
            log.debug("Route '" + httpMethod.getName() + route + "' mapped to custom action method '" +
                    targetClass.getName() + "->" + targetMethodName + "()");

        }
    }

    protected String toRouteId(Class<?> serviceClass, String teleMethodName, HttpMethod httpMethod) {
        return serviceClass.getName() + ':' + teleMethodName + ':' + httpMethod.getName();
    }


    @Override
    public List<String> getSlicedRoute(Class<?> targetClass, String targetMethodName, HttpMethod httpMethod, Map<String, String> parameters) {
        return routesIndex.getSlicedRoute(toRouteId(targetClass, targetMethodName, httpMethod), parameters);
    }

    @Override
    public ActionResolution resolveAction(HttpMethod requestHttpMethod, String requestUri) {
        RouteTrie.RouteResolution<RouteAction> routeResolution = routeTrie.resolveRoute(StrUtils.concatPath(requestHttpMethod.getName(), requestUri, RouteTrie.SEGMENT_DELEMITER));
        if (routeResolution == null || routeResolution.getNode() == null || routeResolution.getNode().getValue() == null) {
            throw new UnknownRouteException(requestHttpMethod, requestUri);
        }

        return new ActionResolution(requestHttpMethod,
                requestUri,
                routeResolution.getNode().getValue(),
                routeResolution.getParams());
    }

    @Override
    public void performAction(ActionResolution resolution) {
        TeleMethod teleMethod = resolution.getRouteAction().getTeleMethod();

        if (teleMethod == null) {
            throw new UnknownRouteException(resolution.getRequestMethod(), resolution.getRequestUri());
        }

        RouterContext routerContext = new RouterContext(resolution.getRequestUri(), resolution.getRouteParameters());
        threadScope.put(RouterContext.SCOPE_KEY, routerContext);

        teleMethod.invoke();
    }


}
