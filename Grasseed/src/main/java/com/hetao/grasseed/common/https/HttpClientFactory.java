package com.hetao.grasseed.common.https;

import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Consts;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.InitializingBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpClientFactory implements InitializingBean {

	private static final int DEFAULT_CONN_TIMEOUT = 10000;
	private static final int DEFAULT_CONN_REQUEST_TIMEOUT = 10000;
	private static final int DEFAULT_SOCKET_TIMEOUT = 10000;
	private static final int MAX_TOTAL = 1000;
	private static final int MAX_CONNECTION_PER_ROUTE = 500;

	private PoolingHttpClientConnectionManager connManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.buildConnManager();
	}

	/**
	 * 绕过验证
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext sc = SSLContext.getInstance("SSLv3");

		// 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
		X509TrustManager trustManager = new X509TrustManager() {
			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
					String paramString) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
					String paramString) throws CertificateException {
			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};

		sc.init(null, new TrustManager[] { trustManager }, null);
		return sc;
	}

	public void buildConnManager() {
		try {
			SSLContext ctx = createIgnoreVerifySSL();
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.INSTANCE)
					.register("https", new SSLConnectionSocketFactory(ctx)).build();
			connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		} catch (KeyManagementException e) {
			log.error("KeyManagementException:", e);
		} catch (NoSuchAlgorithmException e) {
			log.error("NoSuchAlgorithmException:", e);
		}

		// Create message constraints
		MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200)
				.setMaxLineLength(2000).build();

		// Create connection configuration
		ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE)
				.setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8)
				.setMessageConstraints(messageConstraints).build();

		// Configure the connection manager to use connection configuration either
		// by default or for a specific host.
		connManager.setDefaultConnectionConfig(connectionConfig);
		connManager.setDefaultMaxPerRoute(MAX_CONNECTION_PER_ROUTE);
		connManager.setMaxTotal(MAX_TOTAL);
	}

	public CloseableHttpClient build() {
		long startTime = System.currentTimeMillis();
		CloseableHttpClient httpclient = build(DEFAULT_CONN_TIMEOUT, DEFAULT_CONN_REQUEST_TIMEOUT,
				DEFAULT_SOCKET_TIMEOUT);
		long costTime = System.currentTimeMillis() - startTime;

		log.debug("Build HttpClient cost time [{}]", costTime);

		return httpclient;
	}

	public CloseableHttpClient build(int connTimeout, int reqTimeout, int socketTimeout) {
		if (connManager == null) {
			buildConnManager();
		}
		//HttpHost proxy = new HttpHost("10.36.232.125", 8080, "http");
		// Create global request configuration
		RequestConfig defaultRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.BEST_MATCH)
				.setExpectContinueEnabled(true).setStaleConnectionCheckEnabled(true)// 测试连接是否可用
				.setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
				.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).setSocketTimeout(socketTimeout)// .setProxy(proxy)
				.setConnectTimeout(connTimeout).setConnectionRequestTimeout(reqTimeout).build();

		CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(connManager)
				.setDefaultRequestConfig(defaultRequestConfig).build();

		return httpclient;
	}

}
