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

package colesico.framework.asynctask.internal;


import colesico.framework.asynctask.TaskPerformer;
import colesico.framework.eventbus.EventBus;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Event bus based task consumer.
 */
@Singleton
public final class DefaultTaskPerformer implements TaskPerformer<Object> {

    private final EventBus eventBus;

    @Inject
    public DefaultTaskPerformer(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void perform(Object taskPayload) {
        eventBus.send(taskPayload);
    }
}
