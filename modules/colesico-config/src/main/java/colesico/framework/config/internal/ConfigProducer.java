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

package colesico.framework.config.internal;

import colesico.framework.config.ConfigSource;
import colesico.framework.config.PropertiesSource;
import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;

import javax.inject.Singleton;

import static colesico.framework.ioc.Rank.RANK_MINOR;

@Producer(RANK_MINOR)
@Produce(PropertiesSource.class)
public class ConfigProducer {

    @Singleton
    public ConfigSource getDefaultConfigSource(PropertiesSource impl){
        return impl;
    }
}