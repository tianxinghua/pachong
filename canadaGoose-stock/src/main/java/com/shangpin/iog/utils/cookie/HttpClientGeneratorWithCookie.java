package com.shangpin.iog.utils.cookie;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.IOException;

/**
 * httpClientWithCookieAndProxy
 * 
 * @author njt
 * @date 2018年12月6日 下午7:15:13
 * @desc HttpClientGeneratorWithCookie
 */
public class HttpClientGeneratorWithCookie {

	private PoolingHttpClientConnectionManager connectionManager;

	public HttpClientGeneratorWithCookie() {
		SSLContext sslcontext = SSLContexts.createSystemDefault();
		Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", new SSLConnectionSocketFactory(sslcontext)).build();
		connectionManager = new PoolingHttpClientConnectionManager(reg);
		connectionManager.setDefaultMaxPerRoute(100);
	}

	public HttpClientGeneratorWithCookie setPoolSize(int poolSize) {
		connectionManager.setMaxTotal(poolSize);
		return this;
	}

	public CloseableHttpClient getClient(CookieStore cookieStore, String ipProxy) {
		return generateClient(cookieStore, ipProxy);
	}

	private CloseableHttpClient generateClient(CookieStore cookieStore, String ipProxy) {
		HttpClientBuilder httpClientBuilder = HttpClients.custom();
		httpClientBuilder.setConnectionManager(connectionManager);
		httpClientBuilder.setUserAgent(
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
		httpClientBuilder.addInterceptorFirst(new HttpRequestInterceptor() {
			public void process(final HttpRequest request, final HttpContext context)
					throws HttpException, IOException {
				if (!request.containsHeader("Accept-Encoding")) {
					request.addHeader("Accept-Encoding", "gzip");
				}
			}
		});
		httpClientBuilder.setRedirectStrategy(new LaxRedirectStrategy());
		SocketConfig.Builder socketConfigBuilder = SocketConfig.custom();
		socketConfigBuilder.setSoKeepAlive(true).setTcpNoDelay(true);
		socketConfigBuilder.setSoTimeout(0);
		SocketConfig socketConfig = socketConfigBuilder.build();
		httpClientBuilder.setDefaultSocketConfig(socketConfig);
		connectionManager.setDefaultSocketConfig(socketConfig);
		httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(3, true));
		generateCookie(httpClientBuilder, cookieStore, ipProxy);
		return httpClientBuilder.build();
	}

	private void generateCookie(HttpClientBuilder httpClientBuilder, CookieStore cookieStore, String ipProxy) {
//      cookie  
		/*
		 * Map<String, String> cookieMap = new HashMap<String, String>(); for
		 * (Map.Entry<String, String> cookieEntry : cookieMap.entrySet()) {
		 * BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(),
		 * cookieEntry.getValue()); cookie.setDomain(""); cookieStore.addCookie(cookie);
		 * } // 各domain对应的cookie Map<String, Map<String, String>> allCookieMap = new
		 * HashMap<String, Map<String, String>>(); for (Map.Entry<String, Map<String,
		 * String>> domainEntry : allCookieMap.entrySet()) { for (Map.Entry<String,
		 * String> cookieEntry : domainEntry.getValue().entrySet()) { BasicClientCookie
		 * cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
		 * cookie.setDomain(domainEntry.getKey()); cookieStore.addCookie(cookie); } }
		 */
//		Registry<CookieSpecProvider> cookieSpecProviderRegistry = RegistryBuilder.<CookieSpecProvider>create()
//                .register("myCookieSpec", context -> new MyCookieSpec()).build();//注册自定义CookieSpec
//		httpClientBuilder.setDefaultCookieSpecRegistry(cookieSpecProviderRegistry);
		httpClientBuilder.setDefaultCookieStore(cookieStore);
//		设置cookie策略及ip代理（可扩展IP池)
		Builder requestConfig = RequestConfig.custom();
		requestConfig.setCookieSpec(CookieSpecs.STANDARD);
		if(StringUtils.isNotBlank(ipProxy)) {
			String host = "";
			int port = 0;
			if(ipProxy.contains(":")) {
				host = ipProxy.trim().substring(0, ipProxy.indexOf(":"));
				port = Integer.parseInt(ipProxy.trim().substring(ipProxy.indexOf(":")+1,ipProxy.length()));
				requestConfig.setProxy(new HttpHost(host, port));
			}else {
				System.err.println("-----ip格式有误！");
			}
		}else {
			System.err.println("-----不设置代理ip");
		}
		httpClientBuilder.setDefaultRequestConfig(requestConfig.build());

	}

	/*
	 * class MyCookieSpec extends DefaultCookieSpec {
	 * 
	 * @Override public List<Cookie> parse(Header header, CookieOrigin cookieOrigin)
	 * throws MalformedCookieException { String value = header.getValue(); String
	 * prefix = "Expires="; if (value.contains(prefix)) { String expires =
	 * value.substring(value.indexOf(prefix) + prefix.length()); expires =
	 * expires.substring(0, expires.indexOf(";")); SimpleDateFormat format = new
	 * SimpleDateFormat("EEE, dd MMM YYYY HH:mm:ss 'GMT'", Locale.US); Date date2 =
	 * null; try { date2 = format.parse(expires); } catch (ParseException e) {
	 * e.printStackTrace(); } String date = DateUtils.formatDate(new
	 * Date(date2.getTime()),"EEE, dd-MMM-yy HH:mm:ss z"); value =
	 * value.replaceAll(prefix + ".*GMT;", prefix + date + ";"); } header = new
	 * BasicHeader(header.getName(), value); return super.parse(header,
	 * cookieOrigin); } }
	 */

}
