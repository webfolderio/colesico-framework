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

package colesico.framework.weblet.teleapi.reader;

import colesico.framework.http.HttpContext;
import colesico.framework.router.RouterContext;
import colesico.framework.weblet.teleapi.ReaderContext;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Provider;

public final class CharReader extends AbstractReader<Character> {

    public CharReader(Provider<RouterContext> routerContextProv, Provider<HttpContext> httpContextProv) {
        super(routerContextProv, httpContextProv);
    }

    @Override
    public Character read(ReaderContext ctx) {
        String val = ctx.getString(getRouterContext(), getHttpRequest());
        if (StringUtils.isNotEmpty(val)) {
            return val.charAt(0);
        } else {
            return null;
        }
    }
}
