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

package colesico.framework.ioc.production;

import colesico.framework.ioc.key.ClassedKey;

import javax.inject.Qualifier;
import java.lang.annotation.*;

/**
 * Classes-based injection qualifier.
 * Use this annotation to differentiate injection between different objects of the same type.
 * This annotation is equivalent to @Named
 *
 * @author Vladlen Larionov
 * @see ClassedKey
 * @see javax.inject.Named
 */
@Qualifier
@Documented
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Classed {
    Class<?> value();
}
