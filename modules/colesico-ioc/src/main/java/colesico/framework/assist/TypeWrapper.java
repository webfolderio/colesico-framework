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

package colesico.framework.assist;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Wrapper to obtain generic type from its definition.
 * Usage: new TypeWrapper<List<String>>>(){}.unwrap() -> type of List<String>
 * @param <T>
 */
abstract public class TypeWrapper<T> {

    public static final String UNWRAP_METHOD="unwrap";

    protected final Type type;

    public TypeWrapper(Type type) {
        this.type = type;
    }

    public TypeWrapper() {
        this.type = getTypeParameter(getClass());
    }

    public static Type getTypeParameter(Class<?> clazz) {
        Type superClass = clazz.getGenericSuperclass();
        if (superClass instanceof Class) {
            throw new RuntimeException("Missing generic type parameter");
        }
        ParameterizedType parameterizedType = (ParameterizedType) superClass;
        return parameterizedType.getActualTypeArguments()[0];
    }

    public Type unwrap() {
        return type;
    }
}
