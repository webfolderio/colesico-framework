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

package colesico.framework.rpc.teleapi;

import colesico.framework.teleapi.DataPort;

import java.lang.reflect.Type;

public class RpcDataPort implements DataPort<RpcTDRContext, RpcTDWContext> {

    @Override
    public <V> V read(Type valueType, RpcTDRContext context) {
        return null;
    }

    @Override
    public <V> void write(Type valueType, V value, RpcTDWContext context) {

    }
}