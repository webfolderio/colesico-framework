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

module colesico.framework.resource {

    requires org.slf4j;

    requires org.apache.commons.lang3;

    // Inherited in client projects
    requires transitive colesico.framework.config;
    requires transitive colesico.framework.profile;


    // Exports
    exports colesico.framework.resource;
    exports colesico.framework.resource.assist;
    exports colesico.framework.resource.internal to colesico.framework.ioc;
}