/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
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
 *
 */
package colesico.framework.http.internal;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpRequest;
import colesico.framework.http.HttpResponse;
import colesico.framework.ioc.Producer;
import colesico.framework.ioc.ThreadScope;

import static colesico.framework.ioc.Rank.RANK_MINOR;

/**
 * Dagger partition for dispatcher service group
 *
 * @author Vladlen Larionov
 */
@Producer(RANK_MINOR)
public class HttpProducer {

    public HttpContext getHttpContext(ThreadScope scope) {
        return scope.get(HttpContext.SCOPE_KEY);
    }

    public HttpRequest getHttpRequest(HttpContext prov) {
        return prov.getRequest();
    }

    public HttpResponse getHttpResponse(HttpContext prov) {
        return prov.getResponse();
    }
}
