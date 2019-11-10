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

package colesico.framework.example.config.source;

import colesico.framework.config.Config;
import colesico.framework.config.PropertiesSource;
import colesico.framework.config.UseSource;

import static colesico.framework.config.PropertiesSource.FILE;
import static colesico.framework.config.PropertiesSource.PREFIX;

/**
 * Custom properties file and params name prefix example.
 * See {@link PropertiesSource} for all possible customization params for this source type.
 */
@Config
@UseSource(bindAll = true,
    // params in format: name1,value1, name2,value2 ...
    params = {
        FILE, "config.properties",
        PREFIX, "the_prefix"
    })
public class SourcePrefixConfig {

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
