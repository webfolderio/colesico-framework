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
package colesico.framework.service;

import java.lang.annotation.*;

/**
 * Indicates that the method is not a service method.
 *
 * The method will be used in the service as is, without generating
 * a proxy method that provides an auxiliary code for the automation of the original method
 *
 * @author Vladlen Larionov
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE}) //TYPE - for future releases
@Inherited
@Documented
public @interface PlainMethod {
}
