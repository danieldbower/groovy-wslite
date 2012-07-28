/* Copyright 2011-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wslite.http

import wslite.http.auth.*
import java.util.zip.GZIPInputStream

class HTTPClient {

    int connectTimeout
    int readTimeout
    boolean followRedirects = true
    boolean useCaches
    boolean sslTrustAllCerts
    String sslTrustStoreFile
    String sslTrustStorePassword

    Proxy proxy = Proxy.NO_PROXY

    def defaultHeaders = [Connection:'Close', 'Accept-Encoding':'gzip']

    HTTPConnectionFactory httpConnectionFactory
    HTTPAuthorization authorization

    HTTPClient() {
        httpConnectionFactory = new HTTPConnectionFactory()
    }

    HTTPClient(HTTPConnectionFactory httpConnectionFactory) {
         this.httpConnectionFactory = httpConnectionFactory
    }

    HTTPResponse execute(HTTPRequest httpRequest) {
        assertValidArgument(httpRequest)
        HTTPRequest request = buildRequest(httpRequest)
        HTTPResponse response = null
        def connection = null
        try {
            connection = httpConnectionFactory.getConnection(request)
            configureConnection(connection, request)
            response = buildResponse(connection, getResponseStream(connection))
        } catch(Exception ex) {
            if (!connection) {
                throw new HTTPClientException(ex.message, ex, request, response)
            } else {
                response = buildResponse(connection, getErrorResponseStream(connection))
                throw new HTTPClientException(response.statusCode + ' ' + response.statusMessage,
                        ex, request, response)
            }
        } finally {
            connection?.disconnect()
        }
        return response
    }

    private boolean assertValidArgument(HTTPRequest request) throws IllegalArgumentException {
        if (!request?.url || !request?.method) {
            throw new IllegalArgumentException('HTTP Request must contain a url and method')
        }
    }

    private HTTPRequest buildRequest(HTTPRequest request) {
        HTTPRequest req = new HTTPRequest(request)
        req.with {
            if (!request.isConnectTimeoutSet) connectTimeout = this.connectTimeout
            if (!request.isReadTimeoutSet) readTimeout = this.readTimeout
            if (!request.isFollowRedirectsSet) followRedirects = this.followRedirects
            if (!request.isUseCachesSet) useCaches = this.useCaches
            if (!request.proxy) request.proxy = this.proxy
            if (!request.sslTrustStoreFile) {
                sslTrustStoreFile = this.sslTrustStoreFile
                sslTrustStorePassword = this.sslTrustStorePassword
            }
            if (!request.isSSLTrustAllCertsSet) sslTrustAllCerts = this.sslTrustAllCerts
            addDefaultRequestProperties(headers)
        }
        return req
    }

    private void addDefaultRequestProperties(Map headers) {
        for (entry in defaultHeaders) {
            if (!headers.containsKey(entry.key)) {
                headers[entry.key] = entry.value
            }
        }
    }

    private void configureConnection(connection, HTTPRequest request) {
        connection.setRequestMethod(request.method.toString())
        connection.setConnectTimeout(request.connectTimeout)
        connection.setReadTimeout(request.readTimeout)
        connection.setUseCaches(request.useCaches)
        connection.setInstanceFollowRedirects(request.followRedirects)
        setRequestHeaders(connection, request)
        setAuthorizationHeader(connection)
        if (request.data) {
            connection.setDoOutput(true)
            if (connection.getRequestProperty(HTTP.CONTENT_LENGTH_HEADER) == null) {
                connection.setRequestProperty(HTTP.CONTENT_LENGTH_HEADER, "${request.data.size()}")
            }
            connection.outputStream.bytes = request.data
        }
    }

    private void setRequestHeaders(connection, request) {
        for (entry in request.headers) {
            setConnectionRequestProperty(connection, entry.key, entry.value)
        }
    }

    private void setConnectionRequestProperty(connection, String key, List values) {
        for (val in values) {
            setConnectionRequestProperty(connection, key, val.toString())
        }
    }

    private void setConnectionRequestProperty(connection, String key, String value) {
        connection.setRequestProperty(key, value)
    }

    private void setAuthorizationHeader(conn) {
        if (authorization) {
            authorization.authorize(conn)
        }
    }

    private HTTPResponse buildResponse(connection, data) {
        def response = new HTTPResponse()
        response.data = data?.bytes
        response.statusCode = connection.responseCode
        response.statusMessage = connection.responseMessage
        response.url = connection.URL
        response.contentEncoding = connection.contentEncoding
        response.contentLength = connection.contentLength
        ContentTypeHeader contentTypeHeader = new ContentTypeHeader(connection.contentType)
        response.contentType = contentTypeHeader.mediaType
        response.charset = contentTypeHeader.charset
        response.date = new Date(connection.date)
        response.expiration = new Date(connection.expiration)
        response.lastModified = new Date(connection.lastModified)
        response.headers = headersToMap(connection)
        return response
    }

    private getResponseStream(connection) {
        return getEncodedStream(connection.inputStream, connection.contentEncoding)
    }

    private getErrorResponseStream(connection) {
        return getEncodedStream(connection.errorStream, connection.contentEncoding)
    }

    private getEncodedStream(stream, contentEncoding) {
        if (stream && contentEncoding == 'gzip') {
            return new GZIPInputStream(stream)
        }
        return stream
    }

    private Map headersToMap(connection) {
        def headers = [:]
        for (entry in connection.headerFields) {
            headers[entry.key ?: ''] = entry.value.size() > 1 ? entry.value : entry.value[0]
        }
        return headers
    }

}
