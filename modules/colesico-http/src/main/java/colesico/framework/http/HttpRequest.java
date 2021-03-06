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
package colesico.framework.http;

import java.io.InputStream;
import java.io.Writer;
import java.util.Map;

/**
 * @author Vladlen Larionov
 */
public interface HttpRequest {

    /**
     * Returns the http method. validation.e GET or POST, etc.
     *
     * @return http method as uppercase string
     */
    HttpMethod getRequestMethod();

    /**
     * Http map uri protocol string: http, https etc.
     *
     * @return
     */
    String getRequestScheme();

    String getHost();

    Integer getPort();

    /**
     * Part of the query string behind the name of the partition and to the parameters (prior to ? sign).
     *
     * @return
     */
    String getRequestURI();

    String getQueryString();

    /**
     * Returns read-only http headers
     *
     * @return
     */
    Map<String, String> getHeaders();

    /**
     * Returns read-only cookies
     *
     * @return
     */
    Map<String, HttpCookie> getCookies();

    /**
     * Returns read-only query string parameters
     *
     * @return
     */
    HttpValues<String,String> getQueryParameters();

    /**
     * Returns read-only string parameters model from post map with Content-Type: application/x-www-form-urlencoded
     * or  multipart/form-data except files
     *
     * @return
     */
    HttpValues<String,String> getPostParameters();

    /**
     * Returns read-only file model from post map with Content-Type: multipart/form-data
     *
     * @return
     */
    HttpValues<String, HttpFile> getPostFiles();

    /**
     * Returns body input stream
     *
     * @return
     */
    InputStream getInputStream();

    /**
     * Dump request data to characters output for further logging
     */
    void dump(Writer out);
}
