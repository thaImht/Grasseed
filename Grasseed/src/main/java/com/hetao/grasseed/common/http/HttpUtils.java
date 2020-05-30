package com.hetao.grasseed.common.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpUtils {

	private static final String CHARSET_UTF8 = "UTF-8";

	private static CloseableHttpClient httpClient;
	private static final int SOCKET_TIMEOUT = 30000;
	private static final int CONNECT_TIMEOUT = 4000;
	private static final int CONNECT_REQUEST_TIMEOUT = 4000;
	private static final int MAX_TOTAL = 1000;
	private static final int MAX_CONNECTION_PER_ROUTE = 500;

	// static {
	// RequestConfig config = RequestConfig.custom()
	// .setProxy(new HttpHost("10.36.232.125", 8080))
	// .setProxy(new HttpHost("10.17.171.11",8080))
	// .setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout)
	// .setConnectionRequestTimeout(connectionRequestTimeout).build();
	// httpClient = HttpClients.custom().setDefaultRequestConfig(config).build();
	// }

	static {
		RequestConfig config = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT)
				.setConnectionRequestTimeout(CONNECT_REQUEST_TIMEOUT).build();
		httpClient = HttpClients.custom().setDefaultRequestConfig(config).setMaxConnTotal(MAX_TOTAL)
				.setMaxConnPerRoute(MAX_CONNECTION_PER_ROUTE).build();
	}

	public static CloseableHttpClient getClient() {
		return httpClient;
	}

	public static String get(String url) throws IOException {
		return get(url, null, null);
	}

	public static String get(String url, Map<String, String> map) throws IOException {
		return get(url, map, null);
	}

	public static String get(String url, String charset) throws IOException {
		return get(url, null, charset);
	}

	public static String get(String url, Map<String, String> paramsMap, String charset) throws IOException {
		if (url == null || url.isEmpty()) {
			return null;
		}
		charset = (charset == null ? CHARSET_UTF8 : charset);
		if (null != paramsMap && !paramsMap.isEmpty()) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> map : paramsMap.entrySet()) {
				params.add(new BasicNameValuePair(map.getKey(), map.getValue()));
			}
			String querystring = URLEncodedUtils.format(params, charset);
			url += "?" + querystring;
		}
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("Accept-Encoding", "*");
		CloseableHttpResponse response = getClient().execute(httpGet);
		// 状态不为200的异常处理。
		return EntityUtils.toString(response.getEntity(), charset);
	}

	public static String post(String url, String request) throws IOException {
		return post(url, request, null);
	}

	public static String post(String url, String request, String charset) throws IOException {
		if (url == null || url.isEmpty()) {
			return null;
		}
		charset = (charset == null ? CHARSET_UTF8 : charset);
		CloseableHttpResponse response = null;
		String res = null;
		try {
			StringEntity entity = new StringEntity(request, charset);
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Accept-Encoding", "*");
			httpPost.setEntity(entity);
			response = getClient().execute(httpPost);
			// 状态不为200的异常处理。
			res = EntityUtils.toString(response.getEntity());
		} catch (ParseException e) {
			log.error("ParseException:", e);
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			if (response != null) {
				response.close();
			}
		}
		return res;
	}

	public static String post(String url, Map<String,? super String> map) throws IOException {
		return post(url, map, null);
	}

	public static String post(String url, Map<String, ? super String> paramsMap, String charset) throws IOException {
		if (url == null || url.isEmpty()) {
			return null;
		}
		charset = (charset == null ? CHARSET_UTF8 : charset);
		CloseableHttpResponse response = null;
		String res = null;
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			for (Map.Entry<String, ? super String> map : paramsMap.entrySet()) {
				params.add(new BasicNameValuePair(map.getKey(), (String)map.getValue()));
			}
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, charset);
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Accept-Encoding", "*");
			httpPost.setEntity(formEntity);
			response = getClient().execute(httpPost);
			res = EntityUtils.toString(response.getEntity());
			// 状态不为200的异常处理。
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new IOException(res);
			}
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			if (response != null) {
				response.close();
			}
		}
		return res;
	}

	public static String postWithJSONHeader(String url, String request) throws IOException {
		if (url == null || url.isEmpty()) {
			return null;
		}
		CloseableHttpResponse response = null;
		String res = null;
		try {
			StringEntity entity = new StringEntity(request, CHARSET_UTF8);
			entity.setContentType("application/json");
			entity.setContentEncoding("utf-8");

			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(entity);

			response = getClient().execute(httpPost);
			// 状态不为200的异常处理。
			res = EntityUtils.toString(response.getEntity());
		} catch (ParseException e) {
			log.error("ParseException:", e);
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			if (response != null) {
				response.close();
			}
		}
		return res;
	}

	public static String post(HttpPost httpPost) throws IOException {
		CloseableHttpResponse response = null;
		String res = null;
		try {
			response = getClient().execute(httpPost);
			// 状态不为200的异常处理。
			res = EntityUtils.toString(response.getEntity());
		} catch (ParseException e) {
			log.error("ParseException:", e);
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			if (response != null) {
				response.close();
			}
		}
		return res;
	}
	
	public static String postForShort(String url, Map<String, String> paramsMap) throws IOException {
		if (url == null || url.isEmpty()) {
			return null;
		}
		CloseableHttpResponse response = null;
		String res = null;
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> map : paramsMap.entrySet()) {
				params.add(new BasicNameValuePair(map.getKey(), map.getValue()));
			}
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, CHARSET_UTF8);
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Accept-Encoding", "*");
			httpPost.addHeader("Connection", "close");
			httpPost.setEntity(formEntity);
			response = getClient().execute(httpPost);
			res = EntityUtils.toString(response.getEntity());
			// 状态不为200的异常处理。
			if (response.getStatusLine().getStatusCode() != 200) {
				log.error("response status code: {}", response.getStatusLine().getStatusCode());
				throw new IOException(res);
			}
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			if (response != null) {
				response.close();
			}
		}
		return res;
	}
	
	public static String postFor91Kds(String url) throws IOException {
		if (url == null || url.isEmpty()) {
			return null;
		}

		CloseableHttpResponse response = null;
		String res = null;
		try {
			StringEntity entity = new StringEntity("", CHARSET_UTF8);
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Accept-Encoding", "gzip, deflate");
			httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.87 Safari/537.36");
			httpPost.setEntity(entity);
			response = getClient().execute(httpPost);
			// 状态不为200的异常处理。
			res = EntityUtils.toString(response.getEntity());
		} catch (ParseException e) {
			log.error("ParseException:", e);
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			if (response != null) {
				response.close();
			}
		}
		return res;
	}
}
