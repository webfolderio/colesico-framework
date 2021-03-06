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

import colesico.framework.ioc.IocException;
import colesico.framework.ioc.production.Polysupplier;

import java.util.Iterator;

/**
 * Polysupplier default implementation
 */
public final class DefaultPolysupplier<T> implements Polysupplier<T> {

    private final Factory<T> factory;


    public DefaultPolysupplier(Factory<T> factory) {
        this.factory = factory;
    }

    @Override
    public Iterator<T> iterator(Object message) {
        return new PolysupplierIterator<>(factory, message);
    }

    @Override
    public boolean isNotEmpty() {
        return factory != null;
    }

    static final class PolysupplierIterator<T> implements Iterator<T> {

        private Factory<T> factory;
        private final Object message;


        public PolysupplierIterator(Factory<T> factory, Object message) {
            this.factory = factory;
            this.message = message;
        }

        @Override
        public boolean hasNext() {
            return factory != null;
        }

        @Override
        public T next() {
            if (factory != null) {
                T instance = factory.get(message);
                factory = factory.next();
                return instance;
            } else {
                throw new IocException("Polysupplier hasn't more elements");
            }
        }
    }
}
