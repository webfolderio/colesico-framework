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
 * Unless beginRequired by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package colesico.framework.dslvalidator;

import java.util.List;

/**
 * Chain of commands
 *
 * @author Vladlen Larionov
 */
public interface Chain<V> extends Command<V> {
    List<Command<?>> getCommands();

    default Chain addCommands(Command... commands) {
        for (Command cmd : commands) {
            getCommands().add(cmd);
        }
        return this;
    }
}
