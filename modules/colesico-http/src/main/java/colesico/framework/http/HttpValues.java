/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.http;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author Vladlen Larionov
 */
public final class HttpValues<K, V>   {

    private final Map<K, MultiValue<V>> valuesMap;

    public HttpValues(Map<K, MultiValue<V>> valuesMap) {
        this.valuesMap = valuesMap;
    }

    public V get(Object key) {
        MultiValue<V> multiValue = valuesMap.get(key);
        if (multiValue == null) {
            return null;
        }
        return multiValue.value();
    }

    public Set<K> getKeys(){
        return Collections.unmodifiableSet(valuesMap.keySet());
    }

    public MultiValue<V> getAll(Object key){
        return valuesMap.get(key);
    }

    public int size(){
        return valuesMap.size();
    }

    public boolean isEmpty(){
        return valuesMap.isEmpty();
    }

}
