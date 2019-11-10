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

package colesico.framework.example.ioc.multiplugin;

import colesico.framework.ioc.Polyproduce;
import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;
import colesico.framework.ioc.Rank;


@Producer(Rank.RANK_MINOR)
@Produce(Plugin1.class)
@Produce(Plugin2.class)
@Produce(MainBean.class)
public class MultipluginProducer {

    // @Polyproduce indicates multiple MyPluginInterface implementations
    @Polyproduce
    public PluginInterface getMyPluginInterface1(Plugin1 impl) {
        return impl;
    }

    @Polyproduce
    public PluginInterface getMyPluginInterface2(Plugin2 impl) {
        return impl;
    }
}
