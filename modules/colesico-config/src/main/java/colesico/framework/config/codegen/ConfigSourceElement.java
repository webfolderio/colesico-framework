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

package colesico.framework.config.codegen;

import colesico.framework.assist.codegen.model.ClassType;

import java.util.ArrayList;
import java.util.List;

public class ConfigSourceElement {
    private final ClassType driver;
    private final String[] params;
    private final boolean bindAll;

    private final List<SourceValueElement> sourceValues = new ArrayList<>();

    public ConfigSourceElement(ClassType driver, String[] params, boolean bindAll) {
        this.driver = driver;
        this.params = params;
        this.bindAll = bindAll;
    }

    public void addSourceValue(SourceValueElement sv) {
        sourceValues.add(sv);
    }

    public ClassType getDriver() {
        return driver;
    }

    public String[] getParams() {
        return params;
    }

    public List<SourceValueElement> getSourceValues() {
        return sourceValues;
    }

    public boolean isBindAll() {
        return bindAll;
    }
}