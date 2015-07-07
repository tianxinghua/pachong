package com.shangpin.iog.common.utils.httpclient;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
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
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
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
     * GET 方式获取信息
     * @param url 地址
     * @param isProxy   是否使用代理
     * @param socketTimeout  传输超时时间  null 或 0 给默认时间 6苗
     * @return
     */
    public static String get(String url,Boolean isProxy,Integer socketTimeout) {
        if(null==socketTimeout||0==socketTimeout)  socketTimeout=6000;
        HttpGet getMethod = new HttpGet(url);
        String result = "{\"error\":\"发生异常错误\"}";
        CloseableHttpClient httpClient =  null;
        CloseableHttpResponse response = null;
                StringBuffer buffer = new StringBuffer();
        try {

            httpClient = getSSLClient(isProxy);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(2000).build();//设置请求和传输超时时间
            getMethod.setConfig(requestConfig);

             response = httpClient.execute(getMethod);

            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);
			EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null!=response) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
		return bd.setKeepAliveStrategy(getKeepAliveHttpClient()).build();
		//.setConnectionManager(getPoolingConnectionManager(null))
	}


	/**
	 * 使用长连接
	 * 使用方法
	 *
	 *  CloseableHttpClient client = HttpClients.custom().setKeepAliveStrategy(myStrategy).build();
	 *
	 */
	private static  ConnectionKeepAliveStrategy getKeepAliveHttpClient() {

		ConnectionKeepAliveStrategy strategy = new DefaultConnectionKeepAliveStrategy() {

			/**
			 * 服务器端配置（以tomcat为例）：keepAliveTimeout=60000，表示在60s内内，服务器会一直保持连接状态。
			 * 也就是说，如果客户端一直请求服务器，且间隔未超过60s，则该连接将一直保持，如果60s内未请求，则超时。
			 *
			 * getKeepAliveDuration返回超时时间；
			 */
			@Override
			public long getKeepAliveDuration(HttpResponse response, HttpContext context) {


				//如果服务器指定了超时时间，则以服务器的超时时间为准
				HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
				while (it.hasNext()) {
					HeaderElement he = it.nextElement();
					String param = he.getName();
					String value = he.getValue();
					if (value != null && param.equalsIgnoreCase("timeout")) {
						try {
//							System.out.println("服务器指定的超时时间：" + value + " 秒");
							return Long.parseLong(value) * 1000;
						} catch (NumberFormatException ignore) {
							ignore.printStackTrace();
						}
					}
				}


				long keepAlive = super.getKeepAliveDuration(response, context);

				// 如果服务器未指定超时时间，则客户端默认30s超时
				if (keepAlive == -1) {
					keepAlive = 30 * 1000;
				}

				return keepAlive;

                /*  单独设置 某个网址的时间
                如果访问A.com.cn主机，则超时时间5秒，其他主机超时时间30秒
                HttpHost host = (HttpHost) context.getAttribute(HttpClientContext.HTTP_TARGET_HOST);
                if ("A.com.cn".equalsIgnoreCase(host.getHostName())) {
                    keepAlive =  10 * 1000;
                } else {
                    keepAlive =  30 * 1000;
                }*/


			}
		};

		return strategy;



	}

	/**
	 *  异常恢复机制：
	 *
	 *  HttpRequestRetryHandler连接失败后，可以针对相应的异常进行相应的处理措施；
	 *  HttpRequestRetryHandler接口须要用户自己实现；
	 *
	 *  使用方法
	 *
	 *  CloseableHttpClient client = HttpClients.custom().setRetryHandler(retryHandler).build()
	 *
	 */
	private HttpRequestRetryHandler getRetryHandler(){

		HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {

			/**
			 * exception异常信息；
			 * executionCount：重连次数；
			 * context：上下文
			 */
			@Override
			public boolean retryRequest(IOException exception, int executionCount,HttpContext context) {

				System.out.println("重连接次数："+executionCount);

				if (executionCount >= 5) {//如果连接次数超过5次，就不进行重复连接
					return false;
				}
				if (exception instanceof InterruptedIOException) {//io操作中断
					return false;
				}
				if (exception instanceof UnknownHostException) {//未找到主机
					// Unknown host
					return false;
				}
				if (exception instanceof ConnectTimeoutException) {//连接超时
					return true;
				}
				if (exception instanceof SSLException) {
					// SSL handshake exception
					return false;
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
		return  retryHandler;

	}

	/**
	 * 设置并发管理
	 * @param urlHost  连接域名
	 * 使用方法
	 *
	 *
	 * @return
	 */
	private static PoolingHttpClientConnectionManager getPoolingConnectionManager(String urlHost){

		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
		connManager.setMaxTotal(200);//设置最大连接数200
		connManager.setDefaultMaxPerRoute(3);//设置每个路由默认连接数
		if(StringUtils.isNotBlank(urlHost)){
			HttpHost host = new HttpHost(urlHost);//针对的主机
			connManager.setMaxPerRoute(new HttpRoute(host), 5);//每个路由器对每个服务器允许最大5个并发访问
		}

		return connManager;

	}


	//

	/**
	 *
	 * 这个线程负责使用连接管理器清空失效连接和过长连接
	 *
	 * 使用方法
	 *
	 *
	 *  HttpClientConnectionManager manager = new BasicHttpClientConnectionManager();

	 new IdleConnectionMonitorThread(manager).start();//启动线程，5秒钟清空一次失效连接

	 CloseableHttpClient client = HttpClients.custom().setConnectionManager(manager).build();
	 *
	 */


	private static class IdleConnectionMonitorThread extends Thread {

		private final HttpClientConnectionManager connMgr;
		private volatile boolean shutdown;

		public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
			super();
			this.connMgr = connMgr;
		}

		@Override
		public void run() {
			try {
				while (!shutdown) {
					synchronized (this) {
						wait(5000);
						System.out.println("清空失效连接...");
						// 关闭失效连接
						connMgr.closeExpiredConnections();
						//关闭空闲超过30秒的连接
						connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
					}
				}
			} catch (InterruptedException ex) {
			}
		}

		public void shutdown() {
			shutdown = true;
			synchronized (this) {
				notifyAll();
			}
		}
	}


	/**
	 * Http连接使用java.net.Socket类来传输数据。这依赖于ConnectionSocketFactory接口来创建、初始化和连接socket。
	 * 预留用
	 */
	private  void test12() throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {


		HttpClientContext clientContext = HttpClientContext.create();
		PlainConnectionSocketFactory sf = PlainConnectionSocketFactory.getSocketFactory();//Plain：简洁的
		Socket socket = sf.createSocket(clientContext);
		HttpHost target = new HttpHost("localhost");
		InetSocketAddress remoteAddress = new InetSocketAddress(InetAddress.getByAddress(new byte[]{127, 0, 0, 1}), 80);
		sf.connectSocket(1000, socket, target, remoteAddress, null,clientContext);


		//创建通用socket工厂
		ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();

		//Creates default SSL context based on system properties(HttpClient没有自定义任何加密算法。它完全依赖于Java加密标准)
		SSLContext sslcontext = SSLContexts.createSystemDefault();

		//创建ssl socket工厂
		LayeredConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslcontext,
				SSLConnectionSocketFactory.STRICT_HOSTNAME_VERIFIER);

		//自定义的socket工厂类可以和指定的协议（Http、Https）联系起来，用来创建自定义的连接管理器。
		Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", plainsf)
				.register("https", sslsf)
				.build();
		HttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(r);
		CloseableHttpClient client = HttpClients.
				custom().
				setConnectionManager(cm).
				build();


		////////////////////////////////////////////////////////////
		//自定义SSLContext
//      KeyStore myTrustStore = null;
//      SSLContext sslContext = SSLContexts.custom()
//              .useTLS()   //安全传输层协议（TLS）用于在两个通信应用程序之间提供保密性和数据完整性。该协议由两层组成： TLS 记录协议（TLS Record）和 TLS 握手协议（TLS Handshake）。
//              .useSSL()
//              .loadTrustMaterial(myTrustStore)
//              .build();
//      SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
		////////////////////////////////////////////////////////////////////


	}


	public static void main(String[] args) {
		String url = "https://api.orderlink.it/v1/user/token";
		String user="SHANGPIN";String pwd="12345678";
		String rs="";
		try {

			String kk=HttpUtils.get("http://www.acanfora.it/api_ecommerce_v2.aspx",false,240000);//.getData("https://api.orderlink.it/v1/user/token?username=SHANGPIN&password=12345678",false);System.out.println("content = " + kk);
		    System.out.println("kk = " + kk);
        } catch (Exception e) {
			e.printStackTrace();
		}
	}
}
