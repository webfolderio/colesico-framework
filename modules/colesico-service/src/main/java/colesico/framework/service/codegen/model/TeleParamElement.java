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

package colesico.framework.service.codegen.model;

import colesico.framework.assist.codegen.model.VarElement;
import com.squareup.javapoet.CodeBlock;

public final class TeleParamElement extends TeleVarElement {

    private CodeBlock readingContext;

    protected final Integer paramIndex;

    public TeleParamElement(VarElement originVariable, Boolean isLocal, Integer paramIndex) {
        super(originVariable, isLocal);
        this.paramIndex = paramIndex;
    }

    public CodeBlock getReadingContext() {
        return readingContext;
    }

    public void setReadingContext(CodeBlock readingContext) {
        this.readingContext = readingContext;
    }

    public Integer getParamIndex() {
        return paramIndex;
    }
}
