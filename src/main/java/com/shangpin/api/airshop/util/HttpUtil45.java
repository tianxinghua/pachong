package com.shangpin.api.airshop.util;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;





public class HttpUtil45 {
	private static SSLConnectionSocketFactory socketFactory=null;
	static volatile boolean poolShutDown=false;
	public static String errorResult = "{\"error\":\"发生异常错误\"}";
	private static CloseableHttpClient httpClient=null;
	static volatile PoolingHttpClientConnectionManager connManager=null;
	static{
		init();
	}
	public static void init(){
		try {
			connManager=getPoolingConnectionManager(null);
			httpClient=getHttpClient(connManager);
		} catch (KeyManagementException | NoSuchAlgorithmException
				| KeyStoreException e) {
			e.printStackTrace();
		}
	}
	private static CloseableHttpClient getHttpClient(PoolingHttpClientConnectionManager connManager2) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException{
		CloseableHttpClient httpclient = HttpClients.custom()
				//.setDefaultCredentialsProvider(createCredentials(url))
				.setSSLSocketFactory(getSslConnectionSocketFactory())
						//TODO 设置连接池
				.setConnectionManager(connManager2)
				.setRetryHandler(getRetryHandler())
				.build();
		//httpclient =HttpClients.custom().build();
		return httpclient;
	}
	private static HttpRequestRetryHandler getRetryHandler() {
		HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {
			public boolean retryRequest(
					IOException exception,
					int executionCount,
					HttpContext context) {
				if (executionCount >= 3) {
					// Do not retry if over max retry count
					return false;
				}
				if (exception instanceof InterruptedIOException) {
					// Timeout
					return false;
				}
				if (exception instanceof UnknownHostException) {
					// Unknown host
					return false;
				}
				if (exception instanceof ConnectTimeoutException) {
					// Connection refused
					return true;
				}
				if (exception instanceof SSLException) {
					// SSL handshake exception
					return false;
				}
				if(exception instanceof ConnectionPoolTimeoutException){
					return true;
				}
				if(exception instanceof SocketTimeoutException){
					return true;
				}
				HttpClientContext clientContext = HttpClientContext.adapt(context);
				HttpRequest request = clientContext.getRequest();
				boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
				if (idempotent) {
					// Retry if the request is considered idempotent
					return true;
				}
				return false;
			}

		};
		return myRetryHandler;
	}
	/**
	 * 关闭连接池
	 */
	public static void closePool(){
		poolShutDown=true;
		if(null!=connManager) connManager.close();
	}
	private static PoolingHttpClientConnectionManager getPoolingConnectionManager(String url) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException{
		if(connManager ==null || poolShutDown){
			//TODO 注册socket连接策略，http,https认证方式
			connManager = new PoolingHttpClientConnectionManager(getDefaultRegistry());
			connManager.setMaxTotal(200);//设置最大连接数200
			connManager.setDefaultMaxPerRoute(50);//设置每个路由默认连接数
			SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(10000).build();
			connManager.setDefaultSocketConfig(socketConfig);
			if(StringUtils.isNotBlank(url)){
				HttpHost host = url2Host(url);
				connManager.setMaxPerRoute(new HttpRoute(host), 50);//每个路由器对每个服务器允许最大50个并发访问
			}
			return connManager;
		}
		else
			return connManager;

	}
	private static Registry<ConnectionSocketFactory> getDefaultRegistry() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		return RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", getSslConnectionSocketFactory())
				.build();
	}
	/**
	 * 获取ssl连接，针对https的请求设置信任所有
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 */
	private static SSLConnectionSocketFactory getSslConnectionSocketFactory()
			throws NoSuchAlgorithmException, KeyStoreException,
			KeyManagementException {
		if(socketFactory!=null) return socketFactory;
		SSLContextBuilder sb = SSLContexts.custom().loadTrustMaterial(
				new TrustStrategy() {
					@Override
					public boolean isTrusted(X509Certificate[] chain,
											 String authType) throws CertificateException {
						return true;
					}
				});
		SSLContext sslcontext = sb.build();
		SSLConnectionSocketFactory sslsf =null;
		sslsf= new SSLConnectionSocketFactory(sslcontext,new NoopHostnameVerifier());
		socketFactory=sslsf;
		return socketFactory;
	}
	public static  String operateData(String operatorType,String transParaType ,String url,OutTimeConfig outTimeConf,Map<String,String> param,String jsonValue ,String username,String password) {
		HttpClientContext localContext =null;
		//验证
		if(StringUtils.isNotBlank(username)){
			localContext = getAuthContext(url, username, password);
		}else{
			localContext = getPlainContext(url);
		}

		HttpUriRequest request = null;
		if("get".equals(operatorType.toLowerCase())){
			String urlStr=paramGetUrl(url, param);
			request  = new HttpGet(urlStr);
		}else if("post".equals(operatorType.toLowerCase())){

			HttpPost post=new HttpPost(url);
			setTransParam(transParaType, param, jsonValue, post);

			return getResultWithStatusCode(post, outTimeConf, null, localContext);


		}else if("put".equals(operatorType.toLowerCase())){

			HttpPut putMothod = new HttpPut(url);
			setTransParam(transParaType, param, jsonValue, putMothod);


			return getResultWithStatusCode(putMothod, outTimeConf, null, localContext);


		}else if("patch".equals(operatorType.toLowerCase())){

			HttpPatch patch  = new HttpPatch(url);

			setTransParam(transParaType, param, jsonValue, patch);


			return getResultWithStatusCode(patch, outTimeConf, null,localContext);


		}else if("delete".equals(operatorType.toLowerCase())){
			String urlStr=paramGetUrl(url, param);
            request = new HttpDelete(urlStr);
		}else{
		}


		return getResultWithStatusCode(request, outTimeConf, null,localContext);

	}
	/**
	 * get请求方式的url带上参数
	 * @param url 请求url
	 * @param param get参数
	 * @return
	 * @throws ServiceException
	 */
	private static String paramGetUrl(String url, Map<String, String> param) {
		if(param==null) return url;
		Set<String> keys =param.keySet();
		StringBuffer sb = new StringBuffer(url);sb.append("?");
		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			String key = iterator.next();
			sb.append(key).append("=").append(param.get(key)).append("&");
		}
		return sb.toString();
	}

	private static String getResultWithStatusCode(HttpUriRequest request, OutTimeConfig outTimeConf,Map<String,String> headMap, HttpClientContext localContext) {

		String result=null;
		CloseableHttpResponse resp=null;
		try {
			setHeader(headMap,request);
			localContext.setRequestConfig(defaultRequestConfig(outTimeConf));

			resp=httpClient.execute(request,localContext);

			getResponseStatus(resp);

			HttpEntity entity=resp.getEntity();
			result= EntityUtils.toString(entity,"UTF-8");
			EntityUtils.consume(entity);
		}catch(Exception e){

		}finally{
			try {
				if(resp!=null)
					resp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result==null?errorResult:result;
	}
	 private static int getResponseStatus(CloseableHttpResponse resp) {
		 int stateCode = resp.getStatusLine().getStatusCode();
		 System.out.println("返回状态码：" + stateCode);
		 if(200==stateCode||201==stateCode||202==stateCode){

		 }else{

				 HttpEntity entity=resp.getEntity();

		 }
		 return stateCode;
	 }

	private static RequestConfig defaultRequestConfig(OutTimeConfig outTimeConf){
		OutTimeConfig outCnf=outTimeConf;
		if(outTimeConf==null) outCnf=OutTimeConfig.defaultOutTimeConfig();
		return RequestConfig.custom()
				.setConnectionRequestTimeout(outCnf.getConnectOutTime())
				.setConnectTimeout(outCnf.getConnectOutTime())
				.setSocketTimeout(outCnf.getSocketOutTime())
				.build();
	}
	private static void  setHeader(Map<String,String> headMap,HttpUriRequest request ){
		if(null!=headMap&&headMap.size()>0){

			for(Iterator<Map.Entry<String,String>> itor = headMap.entrySet().iterator();itor.hasNext();){
				Map.Entry<String,String> entry = itor.next();
				request.setHeader(entry.getKey(),entry.getValue());
			}
		}

	}

	//设置传递参数
		private static void setTransParam(String transParaType, Map<String, String> param, String value, HttpEntityEnclosingRequestBase method) {
			if("json".equals(transParaType.toLowerCase())){
	            if(StringUtils.isNotBlank(value)){
	                StringEntity s = null;
	                try {
	                    s = new StringEntity(value);
	                    s.setContentEncoding("UTF-8");
	                    s.setContentType("application/json");//发送json数据需要设置contentType
						method.setEntity(s);
	                } catch (UnsupportedEncodingException e) {
	                    e.printStackTrace();
	                }
	            }
	        }else if("form".equals(transParaType.toLowerCase())){
				if(StringUtils.isNotBlank(value)){
					StringEntity s = null;
					try {
						s = new StringEntity(value);
						s.setContentEncoding("UTF-8");
						s.setContentType("application/x-www-form-urlencoded");//发送json数据需要设置contentType
						method.setEntity(s);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}

			}else if("soap".equals(transParaType.toLowerCase())){
				if(StringUtils.isNotBlank(value)){
					StringEntity s = null;
					try {
						s = new StringEntity(value);
						s.setContentEncoding("UTF-8");
						s.setContentType("application/soap+xml");//发送json数据需要设置contentType
						method.setEntity(s);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}

			}else{
	            if(param!=null){
	                Iterable<? extends NameValuePair> nvs = map2NameValuePair(param);
					method.setEntity(new UrlEncodedFormEntity(nvs, Charset
	                        .forName("UTF-8")));
	            }

	        }
		}
		/**
		 * @param param
		 * @return
		 */
		private static Iterable<? extends NameValuePair> map2NameValuePair(
				Map<String, String> param) {
			Iterator<Entry<String, String>> kvs=param.entrySet().iterator();
			List<NameValuePair> nvs = new ArrayList<>(param.size());
			while(kvs.hasNext()){
				Entry<String, String> kv = kvs.next();
				NameValuePair nv = new BasicNameValuePair(kv.getKey(), kv.getValue());
				nvs.add(nv);
			}
			return nvs;
		}

	/**
	 * 普通的无需认证的请求上下文
	 * @param url 请求url nullable
	 * @return
	 */
	private static HttpClientContext getPlainContext(String url){
		HttpClientContext localContext = HttpClientContext.create();
		return localContext;
	}
	private static HttpClientContext getAuthContext(String url,String userName,String password){
		HttpHost target = url2Host(url);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(
				new AuthScope(target.getHostName(), target.getPort(), AuthScope.ANY_REALM),
				new UsernamePasswordCredentials(userName, password));
		AuthCache authCache = new BasicAuthCache();
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(target, basicAuth);
		HttpClientContext localContext = HttpClientContext.create();
		localContext.setAuthCache(authCache);
		localContext.setCredentialsProvider(credsProvider);
		return localContext;
	}
	private static HttpHost url2Host(String url) {
		HttpHost target = new HttpHost(getHost(url), getPort(url),
				url.substring(0, url.indexOf(":")));
		return target;
	}
	/**
	 * 根据url获取主机
	 *
	 * @param url
	 * @return
	 */
	private static String getHost(String url) {
		int s = url.indexOf("://");
		int e = url.indexOf("/", s + 3);
		String host="";
		if(e>0){
			host = url.substring(s + 3, e);
			//排除端口
			host=host.indexOf(":")>0?host.substring(0,host.indexOf(":")):host;
		}else{
			host= url.substring(s + 3);
		}
//		String host = e > 0 ? url.substring(s + 3, e) : url.substring(s + 3);
		return host;
	}
	/**
	 * 获取url端口，默认http:80,https:443
	 * @param url
	 * @return
	 */
	private static int getPort(String url) {
		String regex = "//(.*?):(\\d+)(.*)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(url);
		String port = null;
		while (m.find()) {
			port = m.group(2);
		}
		if (port == null) {
			return isSSL(url) ? 443 : 80;
		} else {
			return Integer.parseInt(port);
		}
	}
	/**
	 * 判断是否走https的
	 *
	 * @param url
	 * @return
	 */
	private static boolean isSSL(String url) {
		return url.startsWith("https");
	}
}
