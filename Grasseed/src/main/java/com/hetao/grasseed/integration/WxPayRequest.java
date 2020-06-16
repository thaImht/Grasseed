package com.hetao.grasseed.integration;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;

public class WxPayRequest {

    private static final int DEFAULT_CONNECT_TIMEOUT_MS = 5* 1000;
    private static final int DEFAULT_READ_TIMEOUT_MS = 5* 1000;

    // 证书
    private KeyStore ks;
    private char[] password;

    WxPayRequest(InputStream pkcs12CertStream, char[] password) throws Exception {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(pkcs12CertStream, password);
        this.ks = ks;
        this.password = password;
    }

    /**
     * 请求，只请求一次，不做重试
     *
     * @param url
     * @param data
     * @param connectTimeoutMs
     * @param readTimeoutMs
     * @param useCert          是否使用证书，针对退款、撤销等操作
     * @return
     * @throws Exception
     */
    private String requestOnce(String url, String data, int connectTimeoutMs, int readTimeoutMs, boolean useCert) throws IOException {
        BasicHttpClientConnectionManager connManager;
        if (useCert) {
            SSLContext sslContext;
            try {
                // 实例化密钥库 & 初始化密钥工厂
                KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                kmf.init(ks, password);

                // 创建 SSLContext
                sslContext = SSLContext.getInstance("TLS");
                sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());
            } catch(KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | KeyManagementException e) {
                throw new RuntimeException(e);
            }

            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                    sslContext,
                    new String[]{"TLSv1"},
                    null,
                    new DefaultHostnameVerifier());

            connManager = new BasicHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                            .register("https", sslConnectionSocketFactory)
                            .build(),
                    null,
                    null,
                    null
            );
        } else {
            connManager = new BasicHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                            .register("https", SSLConnectionSocketFactory.getSocketFactory())
                            .build(),
                    null,
                    null,
                    null
            );
        }

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connManager)
                .build();

        try {
            HttpPost httpPost = new HttpPost(url);

            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeoutMs).setConnectTimeout(connectTimeoutMs).build();
            httpPost.setConfig(requestConfig);

            StringEntity postEntity = new StringEntity(data, "UTF-8");
            httpPost.addHeader("Content-Type", "text/xml");
            httpPost.addHeader("User-Agent", "wxpay sdk java v1.0 ");
            httpPost.setEntity(postEntity);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            return EntityUtils.toString(httpEntity, "UTF-8");
        }finally {
            IOUtils.closeQuietly(httpClient);
        }

    }

    /**
     * 非双向认证的请求
     *
     * @param url
     * @param data
     * @return
     * @throws Exception
     */
    String requestWithoutCert(String url, String data) throws IOException {
        return requestWithoutCert(url, data, DEFAULT_CONNECT_TIMEOUT_MS, DEFAULT_READ_TIMEOUT_MS);
    }

    /**
     * 非双向认证的请求
     *
     * @param url
     * @param data
     * @return
     */
    String requestWithoutCert(String url, String data, int connectTimeoutMs, int readTimeoutMs) throws IOException {
        return requestOnce(url, data, connectTimeoutMs, readTimeoutMs, false);
    }


    /**
     * 双向认证的请求
     *
     * @param url
     * @param data
     * @return
     * @throws Exception
     */
    String requestWithCert(String url, String data) throws IOException {
        return requestWithCert(url, data, DEFAULT_CONNECT_TIMEOUT_MS, DEFAULT_READ_TIMEOUT_MS);
    }

    /**
     * 双向认证的请求
     *
     * @param url
     * @param data
     * @param connectTimeoutMs
     * @param readTimeoutMs
     * @return
     */
    String requestWithCert(String url, String data, int connectTimeoutMs, int readTimeoutMs) throws IOException {
        return requestOnce(url, data, connectTimeoutMs, readTimeoutMs, true);
    }
}
