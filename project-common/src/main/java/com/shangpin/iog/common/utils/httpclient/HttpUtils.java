package com.shangpin.iog.common.utils.httpclient;

import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * httpcomponents-client-4.4.x的工具类
 * @description 
 * @author 陈小峰
 * <br/>2015年6月3日
 */
public class HttpUtils {
	/**
	 *
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param proxy
	 *            是否代理
	 * @param isAuth
	 *            是否验证
	 * @param user
	 *            用户名
	 * @param password
	 *            密码
	 * @return
	 * @throws Exception
	 */
	public static String post(String url, List<NameValuePair> params,
			Boolean proxy, Boolean isAuth, String user,	String password) throws Exception {
		CloseableHttpClient httpclient = getSSLClient(proxy);
		HttpClientContext context =createContext(url, user, password,isAuth);
		HttpPost post = new HttpPost(url);
		String result = "{\"error\":\"发生异常错误\"}";
		CloseableHttpResponse response = null;
		try {
			if (null != params) {
				post.setEntity(new UrlEncodedFormEntity(params, Charset
						.forName("UTF-8")));
			}
			response = httpclient.execute(post, context);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			response.close();
		}
		return result;
	}
	/**
	 * 简单的post
	 * @param url post的url
	 * @param param post参数 可null
	 * @return 
	 * @throws Exception
	 */
	public static String post(String url,Map<String,String> param) throws Exception{
		List<NameValuePair> nvs = map2NameValuePair(param);
		return post(url,nvs,false,false,null,null);
	}
	/**
	 * @param param
	 * @return
	 */
	private static List<NameValuePair> map2NameValuePair(
			Map<String, String> param) {
		Set<String> keys=param.keySet();
		List<NameValuePair> nvs = null;
		if(param!=null)
			nvs=new ArrayList<>();
		for (String key : keys) {
			nvs.add(new BasicNameValuePair(key, param.get(key))); 
		}
		return nvs;
	}
	/**
	 * 普通的get数据
	 * @param url 
	 * @return
	 */
	public static String get(String url) {
		HttpGet getMethod = new HttpGet(url);
		String result = "{\"error\":\"发生异常错误\"}";
		try {
			CloseableHttpClient httpclient = getSSLClient(false);
			HttpResponse response = httpclient.execute(getMethod);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return result;

	}
	/**
	 * 创建httpclient 上下文<br/>
	 * 根据url是否https,http来设置ssl认证<br/>
	 * 若是https，则默认ssl认证所有 
	 * @param url 请求url
	 * @param user 认证用户名
	 * @param password 认证用户密码
	 * @param isAuth 是否强制认证 ，url是https则默认都认证
	 */
	private static HttpClientContext createContext(String url, String user, String password,boolean isAuth) {
		HttpClientContext context=HttpClientContext.create();
		if((isAuth || isSSL(url)) && StringUtils.isNotEmpty(user) && null!=password){
			HttpHost host = new HttpHost(getHost(url), getPort(url), url.substring(0, url.indexOf(":")));
			CredentialsProvider creds = new BasicCredentialsProvider();
			creds.setCredentials(AuthScope.ANY,
					new UsernamePasswordCredentials(user, password));
			AuthCache authCache = new BasicAuthCache();
			BasicScheme basicAuth = new BasicScheme();
			authCache.put(host, basicAuth);
			context.setCredentialsProvider(creds);
			context.setAuthCache(authCache);
		}
		return context;
	}

	/**
	 * 根据url获取主机
	 * @param url
	 * @return
	 */
	private static String getHost(String url) {
		int s = url.indexOf("://");
		int e = url.indexOf("/", s + 3);
		String host = e>0?url.substring(s + 3, e):url.substring(s+3);
		return host;
	}
	private static int getPort(String url){
		String regex="//(.*?):(\\d+)(.*)";
		Pattern p=Pattern.compile(regex);
		Matcher m=p.matcher(url);
		String port=null;
		while(m.find()){
			port=m.group(2);
		}
		if(port==null){
			return isSSL(url)?443:80;
		}else{
			return Integer.parseInt(port);
		}
	}
	/**
	 * 判断是否走https的
	 * @param url
	 * @return
	 */
	private static boolean isSSL(String url){
		return url.startsWith("https");
	}
	/**
	 * 获取默认的代理主机配置
	 * @return
	 */
	private static DefaultProxyRoutePlanner getProxHost(){
		ResourceBundle bundle = ResourceBundle.getBundle("proxy");
        String proxyAddress = bundle.getString("proxy.address");
        Integer proxyPort = Integer.parseInt(bundle.getString("proxy.port"));
		HttpHost proxy = new HttpHost(proxyAddress, proxyPort);
		DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
		return routePlanner;
	}
	
	private static CloseableHttpClient getSSLClient(Boolean proxy)
			throws KeyManagementException, NoSuchAlgorithmException,
			KeyStoreException {
		SSLContextBuilder sslContextBuilder = SSLContexts.custom()
				.loadTrustMaterial(null, new TrustStrategy() {
					@Override
					public boolean isTrusted(X509Certificate[] chain,
							String authType) throws CertificateException {
						return true;
					}
				});
		SSLConnectionSocketFactory sslf = new SSLConnectionSocketFactory(
				sslContextBuilder.build(),
				SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		HttpClientBuilder bd=HttpClients.custom();
		bd.setSSLSocketFactory(sslf);
		if(proxy) 
			bd.setRoutePlanner(getProxHost());
		return bd.build();
	}
	
	
	public static void main(String[] args) {
		String url = "https://api.orderlink.it/v1/user/token";
		String user="SHANGPIN";String pwd="12345678";
		String rs="";
		try {

			String kk=HttpUtils.get("http://www.acanfora.it/api_ecommerce_v2.aspx");//.getData("https://api.orderlink.it/v1/user/token?username=SHANGPIN&password=12345678",false);System.out.println("content = " + kk);
		    System.out.println("kk = " + kk);
        } catch (Exception e) {
			e.printStackTrace();
		}
	}
}
