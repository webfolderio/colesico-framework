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
import colesico.framework.router.RouterContext;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * @author Vladlen Larionov
 */
@Singleton
public final class CharacterReader<C extends HttpTRContext> extends HttpTeleReader<Character, C> {

    @Inject
    public CharacterReader(Provider<RouterContext> routerContextProv, Provider<HttpContext> httpContextProv) {
        super(routerContextProv, httpContextProv);
    }

    @Override
    public Character read(C ctx) {
        String str = StringUtils.trim(ctx.getString(getRouterContext(), getRequest()));
        return StringUtils.isNotEmpty(str) ? str.charAt(0) : null;
    }
}
