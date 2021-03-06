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

package colesico.framework.ioc.message;

/**
 * Automatically generated IoC message that can be used for example for Logger factories (or other)
 * to  get the class name for which the logger instance  is intended.
 *
 * @see Message
 */
public final class InjectionPoint {
    private final Class<?> targetClass;

    public InjectionPoint(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    @Override
    public String toString() {
        return "InjectionPoint{" +
                "targetClass=" + targetClass +
                '}';
    }
}
