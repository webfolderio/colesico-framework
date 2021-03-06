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

package colesico.framework.profile;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Default profile implementation
 */
public class DefaultProfile implements Profile {

    private Locale locale;
    private ObjectiveQualifiers qualifiers;
    private final Map<Class<?>, Object> extensions = new HashMap<>();

    public DefaultProfile(Locale locale) {
        this.locale = locale;
        createQualifiers();
    }

    private void createQualifiers() {
        this.qualifiers = new ObjectiveQualifiers(new String[]{
                StringUtils.isBlank(locale.getLanguage()) ? null : locale.getLanguage(),
                StringUtils.isBlank(locale.getCountry()) ? null : locale.getCountry(),
                StringUtils.isBlank(locale.getVariant()) ? null : locale.getVariant()
        });
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        createQualifiers();
    }

    @Override
    public ObjectiveQualifiers getQualifiers() {
        return qualifiers;
    }

    public void setExtension(Class<?> keyClass, Object value) {
        extensions.put(keyClass, value);
    }

    public <T> T getExtension(Class<T> keyClass) {
        return (T) extensions.get(keyClass);
    }

}
