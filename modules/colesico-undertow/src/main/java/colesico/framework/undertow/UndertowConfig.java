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
package colesico.framework.undertow;


import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;
import colesico.framework.http.HttpMethod;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;

/**
 * @author Vladlen Larionov
 */
@ConfigPrototype(model = ConfigModel.SINGLE)
abstract public class UndertowConfig {
    /**
     * Use this method to setup undertow builder options
     * @param builder
     */
    abstract public void applyOptions(Undertow.Builder builder);

    public HttpHandler getRootHandler(HttpHandler nextHandler) {
        return nextHandler; // use default root handler
    }

    public int getMaxIndividualFileSize() {
        return 1024 * 1024 * 10; // 10 MB
    }
}
