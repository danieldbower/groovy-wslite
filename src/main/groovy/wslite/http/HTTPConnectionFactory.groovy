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

import javax.net.ssl.*
import java.security.KeyStore
import java.security.SecureRandom

class HTTPConnectionFactory {

    def getConnection(HTTPRequest request) {
        if (isSecureConnectionRequest(request)) {
            return getSecureConnection(request)
        }
        return createConnection(request)
    }

    private isSecureConnectionRequest(HTTPRequest request) {
        return request.url.protocol.toLowerCase() == 'https'
    }

    private createConnection(HTTPRequest request) {
        //TODO: handle proxies better
        URL target = request.url
        Proxy proxy = request.proxy
        if (!proxy || proxy == Proxy.NO_PROXY) {
            return target.openConnection()
        } else {
            return target.openConnection(proxy)
        }

    }

    private getSecureConnection(HTTPRequest request) {
        URL target = request.url
        if (shouldTrustAllSSLCerts(request)) {
            return getConnectionTrustAllSSLCerts(request)
        } else if (shouldTrustSSLCertsUsingTrustStore(request)) {
            return getConnectionUsingTrustStore(request)
        } else {
            return createConnection(request)
        }
    }

    private shouldTrustAllSSLCerts(HTTPRequest request) {
        return request.isSSLTrustAllCertsSet ? request.sslTrustAllCerts : sslTrustAllCerts
    }

    private shouldTrustSSLCertsUsingTrustStore(HTTPRequest request) {
        return request.sslTrustStoreFile !=null || sslTrustStoreFile !=null
    }

    private getConnectionTrustAllSSLCerts(HTTPRequest request) {
        def trustingTrustManager = [
                getAcceptedIssuers: {},
                checkClientTrusted: { arg0, arg1 -> },
                checkServerTrusted: {arg0, arg1 -> }
        ] as X509TrustManager
        SSLContext sc = SSLContext.getInstance('SSL')
        sc.init(null, [trustingTrustManager] as TrustManager[], null)
        def conn = createConnection(request)
        conn.setSSLSocketFactory(sc.getSocketFactory())
        conn.setHostnameVerifier({arg0, arg1 -> return true} as HostnameVerifier)
        return conn
    }

    private getConnectionUsingTrustStore(HTTPRequest request) {
        String trustStoreFile = request.sslTrustStoreFile
        String trustStorePassword = request.sslTrustStorePassword
        InputStream tsFile = new FileInputStream(new File(trustStoreFile))
        char[] tsPassword = trustStorePassword?.getChars()

        def keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(tsFile, tsPassword)

        def kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        kmf.init(keyStore, tsPassword)

        def tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(keyStore)

        def sc = SSLContext.getInstance('SSL')
        sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom())

        def conn = createConnection(request)
        conn.setSSLSocketFactory(sc.getSocketFactory())

        return conn
    }

}
