package com.shangpin.iog.smets.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtils {

	private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

	public static String URI_PREFIX = "http://";
	private static final Integer CONNECTION_TIMEOUT = 30000;
	private static final Integer SO_TIMEOUT = 300000;
	public static final String APPLICATION_XML = "application/xml";

	public static HttpResponse get(String uri) throws Exception {
		return get(uri, null, null);
	}

	public static HttpResponse get(String uri, String encoding) throws Exception {
		return get(uri, null, encoding);
	}

	public static HttpResponse get(String uri, Header[] headers) throws Exception {
		return get(uri, headers, null);
	}

	public static HttpResponse get(String uri, Header[] headers, String encoding) throws Exception {
		HttpResponse httpResponse = new HttpResponse();

		uri = buildFullUri(uri);
		GetMethod getMethod = new GetMethod(uri);
		if (encoding != null) {
			getMethod.getParams().setHttpElementCharset(encoding);
			getMethod.getParams().setCredentialCharset(encoding);
			getMethod.getParams().setContentCharset(encoding);
		}
		if (headers != null) {
			for (Header header : headers) {
				getMethod.addRequestHeader(header);
			}
		}

		HttpClient httpclient = new HttpClient();
		httpclient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		// timeout
		httpclient.getHttpConnectionManager().getParams().setSoTimeout(SO_TIMEOUT);
		httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIMEOUT);
		try {
			int status = httpclient.executeMethod(getMethod);
			String response = new String(getMethod.getResponseBody(), "utf-8");
			Cookie[] cookies = httpclient.getState().getCookies();
			for (int i = 0; i < cookies.length; i++) {
				if ("JSESSIONID".equals(cookies[i].getName())) {
					httpResponse.setSessionId(cookies[i].getValue());
				}
			}
			httpResponse.setStatus(status);
			httpResponse.setHeaders(getMethod.getResponseHeaders());
			httpResponse.setResponse(response);
		} catch (Exception e) {
			throw e;
		} finally {
			getMethod.releaseConnection();
			// 真正关闭连接
			((SimpleHttpConnectionManager) httpclient.getHttpConnectionManager()).shutdown();
		}

		return httpResponse;
	}

	public static HttpResponse post(String uri, Map<String, String> map) throws Exception {
		return post(uri, null, map, null, null);
	}
	
	public static HttpResponse post(String uri, Map<String, String> map, Header[] headers) throws Exception {
		return post(uri, null, map, headers, null);
	}
	
	public static HttpResponse post(String uri, NameValuePair[] data) throws Exception {
		return post(uri, data, null, null, null);
	}
	
	public static HttpResponse post(String uri, NameValuePair[] data, String encoding) throws Exception {
		return post(uri, data,null, null, encoding);
	}
	
	public static HttpResponse post(String uri, NameValuePair[] data, Header[] headers)
	throws Exception {
		return post(uri, data, null, headers, null);
	}

	public static HttpResponse post(String uri, NameValuePair[] data, Map<String, String> map, Header[] headers, String encoding)
			throws Exception {
		HttpResponse httpResponse = new HttpResponse();
		
		uri = buildFullUri(uri);
		PostMethod postMethod = new PostMethod(uri);
		if(encoding != null) {
			postMethod.getParams().setHttpElementCharset(encoding);
			postMethod.getParams().setCredentialCharset(encoding);
			postMethod.getParams().setContentCharset(encoding);
		}
		if (headers != null) {
			for (Header header : headers) {
				postMethod.addRequestHeader(header);
			}
		}
		if (map != null && !map.isEmpty()){
			data = new NameValuePair[map.size()];
			Iterator<Map.Entry<String,String>> it = map.entrySet().iterator();
			int i = 0;
			while(it.hasNext()){
				Map.Entry<String,String> entry = it.next();
				data[i] = new NameValuePair(entry.getKey(),entry.getValue());
				i++;
			}
		}
		postMethod.setRequestBody(data);
		HttpClient httpclient = new HttpClient();
		httpclient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		// timeout
		httpclient.getHttpConnectionManager().getParams().setSoTimeout(SO_TIMEOUT);
		httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIMEOUT);
		try {
			int status = httpclient.executeMethod(postMethod);
			String response = new String(postMethod.getResponseBody(), "utf-8");
			Cookie[] cookies = httpclient.getState().getCookies();
			for (int i = 0; i < cookies.length; i++) {
				if ("JSESSIONID".equals(cookies[i].getName())) {
					httpResponse.setSessionId(cookies[i].getValue());
				}
			}
			httpResponse.setStatus(status);
			httpResponse.setHeaders(postMethod.getResponseHeaders());
			httpResponse.setResponse(response);
		} catch (Exception e) {
			throw e;
		} finally {
			postMethod.releaseConnection();
			// 真正关闭连接
			((SimpleHttpConnectionManager)httpclient.getHttpConnectionManager()).shutdown();  
		}
		
		return httpResponse;
	}

	public static HttpResponse post(String uri, Part[] parts) throws Exception {
		return post(uri, parts, null, null);
	}
	
	public static HttpResponse post(String uri, Part[] parts, String encoding) throws Exception {
		return post(uri, parts, null, encoding);
	}
	
	public static HttpResponse post(String uri, Part[] parts, Header[] headers) throws Exception {
		return post(uri, parts, headers, null);
	}

	public static HttpResponse post(String uri, Part[] parts, Header[] headers, String encoding)
			throws Exception {
		HttpResponse httpResponse = new HttpResponse();
		
		uri = buildFullUri(uri);
		PostMethod postMethod = new PostMethod(uri);
		if(encoding != null) {
			postMethod.getParams().setHttpElementCharset(encoding);
			postMethod.getParams().setCredentialCharset(encoding);
			postMethod.getParams().setContentCharset(encoding);
		}
		if (headers != null) {
			for (Header header : headers) {
				postMethod.addRequestHeader(header);
			}
		}
		postMethod.setRequestEntity(new MultipartRequestEntity(parts,
				postMethod.getParams()));
		HttpClient httpclient = new HttpClient();
		// timeout
		httpclient.getHttpConnectionManager().getParams().setSoTimeout(SO_TIMEOUT);
		httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIMEOUT);
		try {
			int status = httpclient.executeMethod(postMethod);
			String response = new String(postMethod.getResponseBody(), "utf-8");
			
			httpResponse.setStatus(status);
			httpResponse.setHeaders(postMethod.getResponseHeaders());
			httpResponse.setResponse(response);
		} catch (Exception e) {
			throw e;
		} finally {
			postMethod.releaseConnection();
			// 真正关闭连接
			((SimpleHttpConnectionManager)httpclient.getHttpConnectionManager()).shutdown();  
		}
		
		return httpResponse;
	}
	
	
	public static HttpResponse post(String uri, String data) throws Exception {
		return post(uri, data, null);
	}

	public static HttpResponse post(String uri, String data, String encoding) throws Exception {
		HttpResponse httpResponse = new HttpResponse();

		uri = buildFullUri(uri);
		URL send_url = new URL(uri);
		HttpURLConnection conn = (HttpURLConnection) send_url.openConnection();
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
		conn.setRequestMethod("POST");
		conn.connect();
		InputStream in = null;
		try {

			OutputStream raw = conn.getOutputStream();
			OutputStream buf = new BufferedOutputStream(raw);
			OutputStreamWriter out = new OutputStreamWriter(buf, "UTF-8");
			out.write(data);
			out.flush();
			out.close();

			in = conn.getInputStream();

			// 采用byte流读取

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] bytes = new byte[1];
			while (in.read(bytes) != -1) {
				baos.write(bytes);
			}
			in.close();

			String response = new String(baos.toByteArray(), "utf-8");
			logger.debug("response=" + response);
			httpResponse.setResponse(response);
			httpResponse.setStatus(200);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
				if (conn != null)
					conn.disconnect();
			} catch (IOException e) {
				logger.debug(e.getMessage());
			}
		}

		return httpResponse;
	}

	public static HttpResponse postString(String uri, String content, String contentType) throws Exception {
		return postString(uri, content, contentType, null, null);
	}

	public static HttpResponse postString(String uri, String content, String contentType, String encoding) throws Exception {
		return postString(uri, content, contentType, null, encoding);
	}

	public static HttpResponse postString(String uri, String content, String contentType, Header[] headers) throws Exception {
		return postString(uri, content, contentType, headers, null);
	}

	public static HttpResponse postString(String uri, String content, String contentType, Header[] headers, String encoding)
			throws Exception {
		HttpResponse httpResponse = new HttpResponse();

		uri = buildFullUri(uri);
		PostMethod postMethod = new PostMethod(uri);
		if (encoding != null) {
			postMethod.getParams().setHttpElementCharset(encoding);
			postMethod.getParams().setCredentialCharset(encoding);
			postMethod.getParams().setContentCharset(encoding);
		}
		if (headers != null) {
			for (Header header : headers) {
				postMethod.addRequestHeader(header);
			}
		}
		String entityEncoding = encoding;
		if (entityEncoding == null) {
			entityEncoding = "utf-8";
		}
		// 设置内容
		postMethod.setRequestEntity(new StringRequestEntity(content, contentType, encoding));

		HttpClient httpclient = new HttpClient();
		// timeout
		httpclient.getHttpConnectionManager().getParams().setSoTimeout(SO_TIMEOUT);
		httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIMEOUT);
		try {
			int status = httpclient.executeMethod(postMethod);
			String response = new String(postMethod.getResponseBody(), "utf-8");

			httpResponse.setStatus(status);
			httpResponse.setHeaders(postMethod.getResponseHeaders());
			httpResponse.setResponse(response);
		} catch (Exception e) {
			throw e;
		} finally {
			postMethod.releaseConnection();
			// 真正关闭连接
			((SimpleHttpConnectionManager) httpclient.getHttpConnectionManager()).shutdown();
		}

		return httpResponse;
	}

	public static String buildFullUri(String uri) {
		String lcUri = uri.toLowerCase();
		if (lcUri.startsWith("http://") || lcUri.startsWith("https://")) {
			return uri;
		}
		return URI_PREFIX + uri;
	}
}
