package com.okestro.symphony.dashboard.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Slf4j
@Component
public class SslUtil {
    /**
     * get public key from ssl
     */
    public String getPublicKey(String host) {
        StringBuffer key = new StringBuffer();

        try {
            URL url = new URL(host);

            // 사설 인증서 신뢰 설정
            SSLContext sslCtx = SSLContext.getInstance("TLS");
            sslCtx.init(null, new TrustManager[] { new X509TrustManager() {
                private X509Certificate[] accepted;

                @Override
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                    accepted = xcs;
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return accepted;
                }
            }}, null);

            // open url connection
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            // set verifier
            connection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String string, SSLSession ssls) {
                    return true;
                }
            });

            // set ssl socket
            connection.setSSLSocketFactory(sslCtx.getSocketFactory());

            if (connection.getResponseCode() == 200 || connection.getResponseCode() == 300) {
                Certificate[] certificates = connection.getServerCertificates();

                key.append("-----BEGIN CERTIFICATE-----\n");
                for (int i = 0; i < certificates.length; i++) {
                    Certificate certificate = certificates[i];

                    byte[] bytes = certificate.getEncoded();
                    key.append(new String(Base64.encodeBase64(bytes)) + "\n");
                }
                key.append("-----END CERTIFICATE-----");
            }

            connection.disconnect();
        } catch (Exception e) {
            // log
            log.error(e.getMessage(), e);
        }

        return key.toString();
    }
}
