package com.shangpin.iog.common.utils.httpclient;

import java.io.*;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
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
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.framework.ServiceException;

/**
 * http 请求基于httpclient4.5
 * @description
 * @author 陈小峰 <br/>
 *         2015年7月11日
 */
public class HttpUtil45 {
	static final Logger logger = LoggerFactory.getLogger(HttpUtil45.class);
	static volatile PoolingHttpClientConnectionManager connManager=null; 
	static volatile boolean poolShutDown=false;
	private static SSLConnectionSocketFactory socketFactory=null;
	public static String errorResult = "{\"error\":\"发生异常错误\"}";
	/**
	 * 请求需要认证的url
	 * @param url url
	 * @param param 请求参数 nullable
	 * @param outTimeConf 超时时间设置，nullable
	 * @param userName 认证用户名
	 * @param password 认证密码
	 * @return 请求结果的字符串
	 * @see OutTimeConfig 超时设置
	 */
	public static String postAuth(String url,Map<String,String> param,OutTimeConfig outTimeConf,String userName,String password){
		String result=null;
		try {
			HttpClientContext localContext = getAuthContext(url,userName,password);
			result=post(url,param,outTimeConf,localContext);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
		return result;
	}
	
	/**
	 * 无参数post请求
	 * @param url 请求url
	 * @param outTimeConf 超时时间设置 nullable
	 * @return 请求结果数据
	 * @see OutTimeConfig 超时设置
	 */
	public static String post(String url,OutTimeConfig outTimeConf){
		return post(url,null,outTimeConf,getPlainContext(url));
	}
	/**
	 * 带参数post请求
	 * @param url 请求url
	 * @param param 请求参数
	 * @param outTimeConf 超时时间设置 nullable
	 * @return 请求结果数据
	 * @see OutTimeConfig 超时设置
	 */
	public static String post(String url,Map<String,String> param,OutTimeConfig outTimeConf){		
		return post(url,param,outTimeConf,getPlainContext(url));
	}
	/**
	 * get请求
	 * @param url 请求url
	 * @param outTimeConf 请求超时时间设置 nullable
	 * @param param 请求参数 nullable
	 * @return 请求结果，若由异常为null
	 * @see OutTimeConfig 超时设置
	 */
	public static String get(String url,OutTimeConfig outTimeConf,Map<String,String> param){
        return getResult(url, outTimeConf, param,null);
	}

    private static String getResult(String url, OutTimeConfig outTimeConf, Map<String, String> param,HttpClientContext localContext) {
        String urlStr=paramGetUrl(url, param);
        HttpGet get = new HttpGet(urlStr);
        String result=null;
        CloseableHttpResponse resp=null;
        try {
            CloseableHttpClient ht=null;
            ht=getHttpClient(null);
            if(null==localContext) localContext = getPlainContext(url);
            localContext.setRequestConfig(defaultRequestConfig(outTimeConf));
            resp=ht.execute(get,localContext);
            HttpEntity entity=resp.getEntity();
            result= EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        }catch(Exception e){
            e.printStackTrace();
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


    /**
     * get请求
     * @param url 请求url
     * @param outTimeConf 请求超时时间设置 nullable
     * @param param 请求参数 nullable
     * @param username 认证用户
     * @param password 认证密码
     * @return 请求结果，若由异常为null
     * @see OutTimeConfig 超时设置
     */
    public static String get(String url,OutTimeConfig outTimeConf,Map<String,String> param,String username,String password){
        HttpClientContext localContext =null;
        if(StringUtils.isNotBlank(username)){
            localContext = getAuthContext(url, username, password);
        }else{
            localContext = getPlainContext(url);
        }

        return getResult(url, outTimeConf, param,localContext);
    }

	/**
	 * 获取流 分解每行数据
	 * @param url      请求地址
	 * @param outTimeConf    超时时间设置
	 * @param param          请求参数
	 * @param username      用户名
	 * @param password       密码
	 * @return      字符串内容列表
	 */
	public static List<String> getContentListByInputSteam(String url,OutTimeConfig outTimeConf,Map<String,String> param,String username,String password){
		HttpClientContext localContext =null;
		List<String> contentList = new ArrayList<>();
		if(StringUtils.isNotBlank(username)){
			localContext = getAuthContext(url, username, password);
		}else{
			localContext = getPlainContext(url);
		}

		String urlStr=paramGetUrl(url, param);
		HttpGet get = new HttpGet(urlStr);
		String result=null;
		CloseableHttpResponse resp=null;
		try {
			CloseableHttpClient ht=null;
			ht=getHttpClient(null);
			if(null==localContext) localContext = getPlainContext(url);
			localContext.setRequestConfig(defaultRequestConfig(outTimeConf));
			resp=ht.execute(get,localContext);
			HttpEntity entity=resp.getEntity();
			final InputStream instream = entity.getContent();
			if (instream == null) {
				return contentList;
			}
			try {
				Args.check(entity.getContentLength() <= Integer.MAX_VALUE,
						"HTTP entity too large to be buffered in memory");
				int i = (int)entity.getContentLength();
				if (i < 0) {
					i = 4096;
				}
				Charset charset = null;
				Charset defaultCharset = Consts.UTF_8;
				try {
					final ContentType contentType = ContentType.get(entity);
					if (contentType != null) {
						charset = contentType.getCharset();
					}
				} catch (final UnsupportedCharsetException ex) {
					if (defaultCharset == null) {
						throw new UnsupportedEncodingException(ex.getMessage());
					}
				}
				if (charset == null) {
					charset = defaultCharset;
				}
				if (charset == null) {
					charset = HTTP.DEF_CONTENT_CHARSET;
				}
				final BufferedReader reader = new BufferedReader(new InputStreamReader(instream, charset));
			    String value="";
				int count = 0;
			    while((value = reader.readLine()) != null) {
					contentList.add(value);
					count++;
					System.out.println("ddgg"+count);
				}
			} finally {
				if (instream != null) {
					instream.close();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				if(resp!=null)
					resp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return contentList;
	}

	/**
	 * 获取图片
	 * @param url 请求地址
	 * @param path  保存路径
	 * @param outTimeConf  超时时间对象
	 * @param param    请求参数
	 */
	public static void getPicture(String url,String path ,OutTimeConfig outTimeConf,Map<String,String> param){
		String urlStr=paramGetUrl(url, param);
		HttpGet get = new HttpGet(urlStr);
		String result=null;
		CloseableHttpResponse resp=null;
		BufferedInputStream bis =null;
		BufferedOutputStream bos =null;
		FileOutputStream fos = null;


		try {
			CloseableHttpClient ht=null;
			ht=getHttpClient(null);//HttpClients.createDefault();
			HttpClientContext localContext = getPlainContext(url);//HttpClientContext.create();
			localContext.setRequestConfig(defaultRequestConfig(outTimeConf));
			resp=ht.execute(get);
			HttpEntity entity=resp.getEntity();
//			InputStream instream = entity.getContent();
			File filePath = new File(path);
			if(!filePath.exists()){
				filePath.mkdirs();
			}
			String file ="";
			if(url.indexOf(".JPG")>0){
				file = url.substring(0,url.indexOf(".JPG")+4);
				file = file.substring(file.lastIndexOf("/")+1)+".jpg";
			}else if(url.indexOf(".jpg")>0){
				file = url.substring(0,url.indexOf(".JPG")+4);
				file = file.substring(file.lastIndexOf("/")+1)+".jpg";
			}else if(url.indexOf("?")>0){
                file = url.substring(0,url.indexOf("?"));
				file = file.substring(file.lastIndexOf("/")+1)+".jpg";
			}
			file = path+ "/" +file;
			File writeFile = new File(file);
			if(!writeFile.exists()){
				try {
					writeFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

				 fos = new FileOutputStream(writeFile,true);
              //加入缓冲流，为了提高速度，可以把与buffer有管的语句删了，看看速度
//				 bis = new BufferedInputStream(instream);
				 bos = new BufferedOutputStream(fos);
//				byte b [] = new byte[1024];
//				while(bis.read(b)!=-1){
//					bis.read(b);
//					bos.write(b);
//					bos.flush();
//				}
			entity.writeTo(bos);




		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				if(resp!=null)
					resp.close();
				if(null!=bos) bos.close();
				if(null!=bis) bis.close();
				if(null!=fos) fos.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 关闭连接池
	 */
	public static void closePool(){
		poolShutDown=true;
		if(null!=connManager) connManager.close();
	}
	private static String post(String url,Map<String,String> param,OutTimeConfig outTimeConf
			,HttpClientContext localContext ){
		HttpPost post=new HttpPost(url);
		if(param!=null){
			Iterable<? extends NameValuePair> nvs = map2NameValuePair(param);
			post.setEntity(new UrlEncodedFormEntity(nvs, Charset
					.forName("UTF-8")));			
		}
		String result=null;
		CloseableHttpResponse resp=null;
		try {
			CloseableHttpClient ht=null;
			ht=getHttpClient(url);
			localContext.setRequestConfig(defaultRequestConfig(outTimeConf));
			resp=ht.execute(post, localContext);
			HttpEntity entity=resp.getEntity();
			result=EntityUtils.toString(entity);
			EntityUtils.consume(entity);
		}catch(Exception e){
			logger.error("请求url：{}错误{}",url,e.getMessage());
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
	
	private static RequestConfig defaultRequestConfig(OutTimeConfig outTimeConf){
		OutTimeConfig outCnf=outTimeConf;
		if(outTimeConf==null) outCnf=OutTimeConfig.defaultOutTimeConfig();
		return RequestConfig.custom()
		.setConnectionRequestTimeout(outCnf.getConnectOutTime())
		.setConnectTimeout(outCnf.getConnectOutTime())
		.setSocketTimeout(outCnf.getSocketOutTime())
		.build();
	}
	/**
	 * 获取http请求的client<br/>连接池方式<br/>如果client不用了方可关闭，否则再次获取会出现异常
	 * @param url 请求的url nullable
	 * @return  httpClient
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 */
	private static CloseableHttpClient getHttpClient(String url) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException{
		CloseableHttpClient httpclient = HttpClients.custom()
				//.setDefaultCredentialsProvider(createCredentials(url))
				.setSSLSocketFactory(getSslConnectionSocketFactory())
				//TODO 设置连接池
				.setConnectionManager(getPoolingConnectionManager(url))
				.setRetryHandler(getRetryHandler())
				.build();
		//httpclient =HttpClients.custom().build();
		return httpclient;
	}
	/**
	 * 重试次数handler
	 * @return
	 */
	private static HttpRequestRetryHandler getRetryHandler() {
		HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {
		    public boolean retryRequest(
		            IOException exception,
		            int executionCount,
		            HttpContext context) {
		        if (executionCount >= 5) {
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
		            return false;
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
		return myRetryHandler;
	}

	/**
	 * 获取连接池，管理,注册了http,https的信任所有连接<br/>
	 * 默认策略，最大连接数200，每个路由默认连接数3
	 * @param url  链接地址 nullable
	 * @return httpClient连接池
	 * @throws KeyStoreException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	private static PoolingHttpClientConnectionManager getPoolingConnectionManager(String url) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException{
		if(connManager ==null || poolShutDown){
			//TODO 注册socket连接策略，http,https认证方式
			connManager = new PoolingHttpClientConnectionManager(getDefaultRegistry());
			connManager.setMaxTotal(200);//设置最大连接数200
			connManager.setDefaultMaxPerRoute(3);//设置每个路由默认连接数
			/*SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(socketOutTime).build();
			connManager.setDefaultSocketConfig(socketConfig);*/
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
	 * 请求url需要认证的上下文
	 * @param url 请求url
	 * @param userName 用户名
	 * @param password 密码
	 * @return
	 */
	private static HttpClientContext getAuthContext(String url,String userName,String password){
		HttpHost target = url2Host(url);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(
				new AuthScope(target.getHostName(), target.getPort()),
				new UsernamePasswordCredentials(userName, password));
		AuthCache authCache = new BasicAuthCache();
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(target, basicAuth);
		HttpClientContext localContext = HttpClientContext.create();
		localContext.setAuthCache(authCache);
		localContext.setCredentialsProvider(credsProvider);
		return localContext;
	}
	/**
	 * 普通的无需认证的请求上下文
	 * @param url 请求url nullable
	 * @return
	 */
	private static HttpClientContext getPlainContext(String url){
		/*HttpHost target = url2Host(url);
		AuthCache authCache = new BasicAuthCache();
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(target, basicAuth);
		localContext.setAuthCache(authCache);*/
		HttpClientContext localContext = HttpClientContext.create();
		return localContext;
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
	 * @param url
	 * @return
	 */
	private static HttpHost url2Host(String url) {
		HttpHost target = new HttpHost(getHost(url), getPort(url),
				url.substring(0, url.indexOf(":")));
		return target;
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
	
	/**
	 * 根据url获取主机
	 * 
	 * @param url
	 * @return
	 */
	private static String getHost(String url) {
		int s = url.indexOf("://");
		int e = url.indexOf("/", s + 3);
		String host = e > 0 ? url.substring(s + 3, e) : url.substring(s + 3);
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




	
	/*@Test
	public void testAuthPost() {
		String userName = "SHANGPIN";
		String password = "12345678";
		String AUTH = "https://api.orderlink.it/v1/user/token";
		String rs=postAuth(AUTH, null, null, userName, password);
		System.out.println(rs);
	}
	@Test
	public void testGet(){
		Map<String,String> param = new HashMap<String, String>();
		param.put("page", "1");param.put("limit", "20");
		String token="6c9ade4c5fea79a5c0b060c67b55f4a2a59316dff3a18f047990484b8cc74d8c6ecddbbbb03139211f017ee9ea983f908ae5a46cf087294ccfdb46a78107fd01ec280b64e9fd7ccfd09864f93a4c46b181ffead68b644604efe5cc5f903a9954";
		param.put("access_token", token);
		String productId=null,recordId=null;
		if(productId!=null) param.put("product_id", productId);
		if(recordId!=null) param.put("id", recordId);
		
		String[] x={"151001LCX000007-P31","151400NCX000003-NERO","151481ASC000001-2310C",
				"151481DPL000003-00100","151481DCW000008-2720C"
		};
		String url="https://api.orderlink.it/v1/stocks";
		for (String record : x) {
			try{
				param.put("id", record);
				String url1=paramGetUrl(url, param);
				String rrs=get(url1, null,null);
				System.out.println(rrs);
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
	}*/
}
