package com.hetao.grasseed.common.https;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpsUtils {
	private static HttpClient client = new HttpClientFactory().build();

	public static String get(String url) {
		HttpGet get = new HttpGet(url);
		HttpResponse res = null;
		try {
			res = client.execute(get);
		} catch (IOException e) {
			log.error("get IOException:",e);
		}
		String result = "";
		try {
			result = EntityUtils.toString(res.getEntity(), "UTF-8");
			log.info("result=" + result);
		} catch (UnsupportedOperationException e) {
			log.error("get UnsupportedOperationException:",e);
		} catch (IOException e) {
			log.error("get IOException2:",e);
		}
		return result;
	}

	public static String get(String url, Map<String, String> paramsMap) {
		if (null != paramsMap && !paramsMap.isEmpty()) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> map : paramsMap.entrySet()) {
				params.add(new BasicNameValuePair(map.getKey(), map.getValue()));
			}
			String querystring = URLEncodedUtils.format(params, "UTF-8");
			url += "?" + querystring;
		}
		HttpGet get = new HttpGet(url);
		HttpResponse res = null;
		try {
			res = client.execute(get);
		} catch (IOException e) {
			log.error("get IOException:",e);
		}
		String result = "";
		try {
			result = EntityUtils.toString(res.getEntity(), "UTF-8");
			log.info("result=" + result);
		} catch (UnsupportedOperationException e) {
			log.error("get UnsupportedOperationException:",e);
		} catch (IOException e) {
			log.error("get IOException2:",e);
		}
		return result;
	}

	public static String postJSON(String url, String params) {
		StringEntity entity = new StringEntity(params, "utf-8");
		entity.setContentType("application/json");
		entity.setContentEncoding("utf-8");

		HttpPost post = new HttpPost(url);
		post.setEntity(entity);
		HttpResponse res = null;
		try {
			res = client.execute(post);
		} catch (IOException e) {
			log.error("get IOException:",e);
		}
		String result = "";
		try {
			result = EntityUtils.toString(res.getEntity(), "UTF-8");
			log.info("result=" + result);
		} catch (UnsupportedOperationException e) {
			log.error("get UnsupportedOperationException:",e);
		} catch (IOException e) {
			log.error("get IOException2:",e);
		}
		return result;
	}
	
	public static String post(String url, Map<String,? super String> paramsMap) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (Map.Entry<String, ? super String> map : paramsMap.entrySet()) {
			params.add(new BasicNameValuePair(map.getKey(), (String)map.getValue()));
		}
		UrlEncodedFormEntity formEntity = null;
		try {
			formEntity = new UrlEncodedFormEntity(params, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("get UnsupportedEncodingException:",e);
		}

		HttpPost post = new HttpPost(url);
		post.setEntity(formEntity);
		HttpResponse res = null;
		try {
			res = client.execute(post);
		} catch (IOException e) {
			log.error("get IOException:",e);
		}
		String result = "";
		try {
			result = EntityUtils.toString(res.getEntity(), "UTF-8");
			log.info("result=" + result);
		} catch (UnsupportedOperationException e) {
			log.error("get UnsupportedOperationException:",e);
		} catch (IOException e) {
			log.error("get IOException2:",e);
		}
		return result;
	}
}
