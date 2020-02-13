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

package colesico.framework.ioc.ioclet;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.Key;

/**
 * Factories catalog.
 * Used by the ioclet to add factories to the IoC container when ioclets are loading by IoC container.
 *
 * @author Vladlen Larionov
 * @see Ioclet
 * @see Ioc
 */
public interface Catalog {
    String ACCEPT_METHOD = "accept";
    String ADD_METHOD = "add";

    <T> void add(Factory<T> factory);

    <T> boolean accept(Entry<T> entry);

    class Entry<T> {
        public static final String OF_METHOD = "of";

        private final Key<T> key;
        private final boolean polyproducing;

        public Entry(Key<T> key, boolean polyproducing) {
            this.key = key;
            this.polyproducing = polyproducing;
        }

        public final Key<T> getKey() {
            return key;
        }

        public final boolean isPolyproducing() {
            return polyproducing;
        }

        public static <T> Entry<T> of(Key<T> key, boolean polyproducing) {
            return new Entry<>(key, polyproducing);
        }
    }
}
