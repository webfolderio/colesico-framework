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

package colesico.framework.htmlrenderer;

import colesico.framework.assist.StrUtils;
import colesico.framework.weblet.HtmlResponse;
import org.apache.commons.lang3.StringUtils;

abstract public class ResourceHtmlRenderer implements HtmlRenderer<String> {

    protected final String templatesRootPath;

    public ResourceHtmlRenderer(String templatesRootPath) {
        while (StringUtils.startsWith(templatesRootPath, "/")) {
            templatesRootPath = StringUtils.substring(templatesRootPath, 1);
        }
        this.templatesRootPath = templatesRootPath;
    }

    abstract protected <M> String doRender(String templateFullPath, M model);

    @Override
    public final <M> HtmlResponse render(String templatePath, M model) {
        String templateFullPath;
        if (StringUtils.startsWith(templatePath, "/")) {
            templateFullPath = templatePath;
        } else if (StringUtils.startsWith(templatePath, ".")) {
            templateFullPath = StrUtils.concatPath(templatesRootPath, templatePath.substring(1), "/");
        } else {
            templateFullPath = StrUtils.concatPath(templatesRootPath, templatePath, "/");
        }

        String html = doRender(templateFullPath, model);
        return HtmlResponse.of(html);
    }

}
