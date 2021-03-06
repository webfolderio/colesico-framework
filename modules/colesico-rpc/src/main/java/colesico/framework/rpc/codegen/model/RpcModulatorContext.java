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

package colesico.framework.rpc.codegen.model;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.assist.codegen.model.MethodElement;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.model.TeleMethodElement;

import javax.lang.model.element.ExecutableElement;
import java.util.ArrayList;
import java.util.List;

public class RpcModulatorContext {
    private final ServiceElement serviceElement;

    private final ClassType rpcInterface;

    private List<RpcTeleMethodElement> teleMethods = new ArrayList<>();

    public RpcModulatorContext(ServiceElement serviceElement, ClassType rpcInterface) {
        this.serviceElement = serviceElement;
        this.rpcInterface = rpcInterface;
    }

    public void addTeleMethod(RpcTeleMethodElement rpcTeleMethodElement) {
        teleMethods.add(rpcTeleMethodElement);
    }

    public List<RpcTeleMethodElement> getTeleMethods() {
        return teleMethods;
    }

    public ServiceElement getServiceElement() {
        return serviceElement;
    }

    public ClassType getRpcInterface() {
        return rpcInterface;
    }
}
