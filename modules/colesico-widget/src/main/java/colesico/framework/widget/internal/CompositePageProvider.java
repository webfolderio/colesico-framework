/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package colesico.framework.widget.internal;

import colesico.framework.ioc.ThreadScope;
import colesico.framework.widget.CompositePage;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 *
 * @author Vladlen Larionov
 */
@Singleton
public class CompositePageProvider implements Provider<CompositePage> {

    private final ThreadScope scope;

    @Inject
    public CompositePageProvider(ThreadScope scope) {
        this.scope = scope;
    }

    @Override
    public CompositePage get() {
        return (CompositePage) scope.get(CompositePage.SCOPE_KEY, CompositePageImpl::new,null);

    }
}
