package com.shangpin.iog.common.utils.httpclient;

import java.io.*;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
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

import com.shangpin.framework.ServiceMessageException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
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
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.Args;
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
	private static CloseableHttpClient httpClient=null;
	static{
		init();
	}
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
	 * 请求需要认证的带参数,带请求头参数url
	 * @param url url
	 * @param param 请求参数 nullable
	 * @param headerMap  请求头参数
	 * @param outTimeConf 超时时间设置，nullable
	 * @param userName 认证用户名
	 * @param password 认证密码
	 * @return 请求结果的字符串
	 * @see OutTimeConfig 超时设置
	 */
	public static String postAuth(String url,Map<String,String> param,Map<String,String> headerMap,OutTimeConfig outTimeConf,String userName,String password){
		String result=null;
		try {
			HttpClientContext localContext = getAuthContext(url,userName,password);
			result=post(url,param,headerMap,outTimeConf,localContext);
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
	 * get请求
	 * @param url 请求url
	 * @param outTimeConf 请求超时时间设置 nullable
	 * @param param 请求参数 nullable
	 * @param headMap  header 请求参数 设置header
	 * @param username 认证用户 如果没有 请不要填写
	 * @param password 认证密码
	 * @return 请求结果，若由异常为null
	 * @see OutTimeConfig 超时设置
	 */
	public static String get(String url,OutTimeConfig outTimeConf,Map<String,String> param,Map<String,String> headMap ,String username,String password){
		HttpClientContext localContext =null;
		if(StringUtils.isNotBlank(username)){
			localContext = getAuthContext(url, username, password);
		}else{
			localContext = getPlainContext(url);
		}

		return getResult(url, outTimeConf, param,headMap,localContext);
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
	 * 带参数,带请求头参数post请求
	 * @param url 请求url
	 * @param param 请求参数
	 * @param headerMap  请求头参数
	 * @param outTimeConf 超时时间设置 nullable
	 * @return 请求结果数据
	 * @see OutTimeConfig 超时设置
	 */
	public static String post(String url,Map<String,String> param,Map<String,String> headerMap,OutTimeConfig outTimeConf){
		return post(url,param,headerMap,outTimeConf,getPlainContext(url));
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
		return getResult(url, outTimeConf, param,null, null);
	}

	/**
	 * 操作数据
	 * @param operatorType 操作类型  get put patch delete
	 * @param transParaType 传递参数类型  json  无值 代表非JSON
	 * @param url
	 * @param outTimeConf
	 * @param param     参数（非json类型时传入 ，json时 不做处理)
	 * @param jsonValue   json类型时 需要传入的参数
	 * @param username  如果需要验证 需要填写 无值或为空 则认为不需要验证
	 * @param password
	 * @return
	 */
	public static  String operateData(String operatorType,String transParaType ,String url,OutTimeConfig outTimeConf,Map<String,String> param,String jsonValue ,String username,String password) throws ServiceException{
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
		    throw new ServiceMessageException("无此操作方法");
		}


		return getResultWithStatusCode(request, outTimeConf, null,localContext);

	}
	
	/**
	 * 操作数据
	 * @param operatorType 操作类型  get put patch delete
	 * @param transParaType 传递参数类型  json  无值 代表非JSON
	 * @param url
	 * @param outTimeConf
	 * @param param     参数（非json类型时传入 ，json时 不做处理)
	 * @param jsonValue   json类型时 需要传入的参数
	 * @param headerMap 头信息
	 * @param username  如果需要验证 需要填写 无值或为空 则认为不需要验证
	 * @param password
	 * @return
	 */
	public static  String operateData(String operatorType,String transParaType ,String url,OutTimeConfig outTimeConf,Map<String,String> param,String jsonValue ,Map<String,String> headerMap,String username,String password) throws ServiceException{
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

			return getResultWithStatusCode(post, outTimeConf, headerMap, localContext);


		}else if("put".equals(operatorType.toLowerCase())){

			HttpPut putMothod = new HttpPut(url);
			setTransParam(transParaType, param, jsonValue, putMothod);


			return getResultWithStatusCode(putMothod, outTimeConf, headerMap, localContext);


		}else if("patch".equals(operatorType.toLowerCase())){

			HttpPatch patch  = new HttpPatch(url);

			setTransParam(transParaType, param, jsonValue, patch);


			return getResultWithStatusCode(patch, outTimeConf, headerMap,localContext);


		}else if("delete".equals(operatorType.toLowerCase())){
			String urlStr=paramGetUrl(url, param);
            request = new HttpDelete(urlStr);
		}else{
		    throw new ServiceMessageException("无此操作方法");
		}


		return getResultWithStatusCode(request, outTimeConf, headerMap,localContext);

	}
	
	/**
	 * 操作数据
	 * @param operatorType 操作类型  get put patch delete
	 * @param transParaType 传递参数类型  json  无值 代表非JSON
	 * @param url
	 * @param outTimeConf
	 * @param param     参数（非json类型时传入 ，json时 不做处理)
	 * @param jsonValue   json类型时 需要传入的参数
	 * @param headerMap 头信息
	 * @param username  如果需要验证 需要填写 无值或为空 则认为不需要验证
	 * @param password
	 * @return
	 */
	public static  CloseableHttpResponse operateData2(String operatorType,String transParaType ,String url,OutTimeConfig outTimeConf,Map<String,String> param,String jsonValue ,Map<String,String> headerMap,String username,String password) throws ServiceException{
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

			return getResponseWithStatusCode(post, outTimeConf, headerMap, localContext);


		}else if("put".equals(operatorType.toLowerCase())){

			HttpPut putMothod = new HttpPut(url);
			setTransParam(transParaType, param, jsonValue, putMothod);


			return getResponseWithStatusCode(putMothod, outTimeConf, headerMap, localContext);


		}else if("patch".equals(operatorType.toLowerCase())){

			HttpPatch patch  = new HttpPatch(url);

			setTransParam(transParaType, param, jsonValue, patch);


			return getResponseWithStatusCode(patch, outTimeConf, headerMap,localContext);


		}else if("delete".equals(operatorType.toLowerCase())){
			String urlStr=paramGetUrl(url, param);
            request = new HttpDelete(urlStr);
		}else{
		    throw new ServiceMessageException("无此操作方法");
		}


		return getResponseWithStatusCode(request, outTimeConf, headerMap,localContext);

	}
	
	/**
	 * 获取返回的头信息
	 * @param operatorType 操作类型  get put patch delete
	 * @param transParaType 传递参数类型  json  无值 代表非JSON
	 * @param url
	 * @param outTimeConf
	 * @param param     参数（非json类型时传入 ，json时 不做处理)
	 * @param jsonValue   json类型时 需要传入的参数
	 * @param name  请求想要得到的头信息的name
	 * @param username  如果需要验证 需要填写 无值或为空 则认为不需要验证
	 * @param password
	 * @return
	 */
	public static  String getResponseHead(String operatorType,String transParaType ,String url,OutTimeConfig outTimeConf,Map<String,String> param,String jsonValue ,String name,String username,String password) throws ServiceException{
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

			return getAuthorizationWithStatusCode(post, outTimeConf, null, localContext,name);


		}else if("put".equals(operatorType.toLowerCase())){

			HttpPut putMothod = new HttpPut(url);
			setTransParam(transParaType, param, jsonValue, putMothod);


			return getAuthorizationWithStatusCode(putMothod, outTimeConf, null, localContext,name);


		}else if("patch".equals(operatorType.toLowerCase())){

			HttpPatch patch  = new HttpPatch(url);

			setTransParam(transParaType, param, jsonValue, patch);


			return getAuthorizationWithStatusCode(patch, outTimeConf, null,localContext,name);


		}else if("delete".equals(operatorType.toLowerCase())){
			String urlStr=paramGetUrl(url, param);
            request = new HttpDelete(urlStr);
		}else{
		    throw new ServiceMessageException("无此操作方法");
		}


		return getAuthorizationWithStatusCode(request, outTimeConf, null,localContext,name);

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

		}else{
            if(param!=null){
                Iterable<? extends NameValuePair> nvs = map2NameValuePair(param);
				method.setEntity(new UrlEncodedFormEntity(nvs, Charset
                        .forName("UTF-8")));
            }

        }
	}




	private static String getResult(String url, OutTimeConfig outTimeConf, Map<String, String> param,Map<String,String> headMap,HttpClientContext localContext) {
		String urlStr=paramGetUrl(url, param);
		HttpGet get = new HttpGet(urlStr);

		setHeader(headMap, get);

		String result=null;
		CloseableHttpResponse resp=null;
		try {
			if(null==localContext) localContext = getPlainContext(url);
			localContext.setRequestConfig(defaultRequestConfig(outTimeConf));

			resp=httpClient.execute(get,localContext);

			HttpEntity entity=resp.getEntity();
			result= EntityUtils.toString(entity, "UTF-8");
			EntityUtils.consume(entity);
		}catch(Exception e){
			logger.error("--------------httpError:"+e.getMessage());
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
	 * 设置header
	 * @param headMap
	 * @param request
	 */
	private static void  setHeader(Map<String,String> headMap,HttpUriRequest request ){
		if(null!=headMap&&headMap.size()>0){

			for(Iterator<Map.Entry<String,String>> itor = headMap.entrySet().iterator();itor.hasNext();){
				Map.Entry<String,String> entry = itor.next();
				request.setHeader(entry.getKey(),entry.getValue());
			}
		}

	}


	private static String getResultWithStatusCode(HttpUriRequest request, OutTimeConfig outTimeConf,Map<String,String> headMap, HttpClientContext localContext) throws ServiceException{

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
		}catch(ServiceException e){
			logger.error("响应码非200,响应码为 : "+e.getMessage());
			throw e;

		}catch(Exception e){
			logger.error("--------------httpError:"+e.getMessage());
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
	
	private static CloseableHttpResponse getResponseWithStatusCode(HttpUriRequest request, OutTimeConfig outTimeConf,Map<String,String> headMap, HttpClientContext localContext) throws ServiceException{

		CloseableHttpResponse resp=null;
		try {
			setHeader(headMap,request);
			localContext.setRequestConfig(defaultRequestConfig(outTimeConf));

			resp=httpClient.execute(request,localContext);

			getResponseStatus(resp);

		}catch(ServiceException e){
			logger.error("响应码非200,响应码为 : "+e.getMessage());
			throw e;

		}catch(Exception e){
			logger.error("--------------httpError:"+e.getMessage());
		}
		return resp;
	}
	
	private static String getAuthorizationWithStatusCode(HttpUriRequest request, OutTimeConfig outTimeConf,Map<String,String> headMap, HttpClientContext localContext,String name) throws ServiceException{

		String result=null;
		CloseableHttpResponse resp=null;
		try {
			setHeader(headMap,request);
			localContext.setRequestConfig(defaultRequestConfig(outTimeConf));

			resp=httpClient.execute(request,localContext);

			getResponseStatus(resp);
			result = resp.getFirstHeader(name).getValue();
		}catch(ServiceException e){
			logger.error("响应码非200,响应码为 : "+e.getMessage());
			throw e;

		}catch(Exception e){
			logger.error("--------------httpError:"+e.getMessage());
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


	private static String getResult(HttpUriRequest request, OutTimeConfig outTimeConf,Map<String,String> headMap, HttpClientContext localContext) {

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
			logger.error("--------------httpError:"+e.getMessage());
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


     private static int getResponseStatus(CloseableHttpResponse resp) throws ServiceException{
		 int stateCode = resp.getStatusLine().getStatusCode();
		 logger.info("链接返回状态码：" + stateCode);
		 System.out.println("返回状态码：" + stateCode);
		 if(200==stateCode||201==stateCode||202==stateCode){

		 }else{
			 if(401==stateCode){
				 HttpEntity entity=resp.getEntity();
			     try {
			    	  logger.error(EntityUtils.toString(entity,"UTF-8"));
				 }catch (Exception e) {
					
					e.printStackTrace();
				 } 
			 }
			 throw new ServiceMessageException("状态码:"+stateCode);
		 }
		 return stateCode;
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

		return getResult(url, outTimeConf, param,null,localContext);
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
		CloseableHttpResponse resp=null;
		try {
			if(null==localContext) localContext = getPlainContext(url);
			localContext.setRequestConfig(defaultRequestConfig(outTimeConf));
			resp=httpClient.execute(get,localContext);
			getResponseStatus(resp);
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
		return getPostResult(url, outTimeConf, localContext, post);
	}

	private static String getPostResult(String url, OutTimeConfig outTimeConf, HttpClientContext localContext, HttpPost post) {
		String result=null;
		CloseableHttpResponse resp=null;
		try {
			localContext.setRequestConfig(defaultRequestConfig(outTimeConf));
			resp=httpClient.execute(post, localContext);
			getResponseStatus(resp);
			HttpEntity entity=resp.getEntity();

			result= EntityUtils.toString(entity,"UTF-8");
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


	private static String post(String url,Map<String,String> param,Map<String,String> headerParam,OutTimeConfig outTimeConf
			,HttpClientContext localContext ){
		HttpPost post=new HttpPost(url);
		if(param!=null){
			Iterable<? extends NameValuePair> nvs = map2NameValuePair(param);
			post.setEntity(new UrlEncodedFormEntity(nvs, Charset
					.forName("UTF-8")));
		}

	    setHeader(headerParam,post);


		return getPostResult(url, outTimeConf, localContext, post);
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

	public static void init(){
		try {
			connManager=getPoolingConnectionManager(null);
			httpClient=getHttpClient(connManager);
		} catch (KeyManagementException | NoSuchAlgorithmException
				| KeyStoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取http请求的client<br/>连接池方式<br/>如果client不用了方可关闭，否则再次获取会出现异常
	 * @param connManager2 请求的url nullable
	 * @return  httpClient
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 */
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


	/**
	 * 获取图片
	 * @param url 请求地址
	 * @param fileName  保存的文件名称
	 * @param outTimeConf  超时时间对象
	 * @param param    请求参数
	 */
	public static void getPicture(String url,String fileName ,OutTimeConfig outTimeConf,Map<String,String> param){
		String urlStr=paramGetUrl(url, param);
		HttpGet get = new HttpGet(urlStr);
		String result=null;
		CloseableHttpResponse resp=null;
		BufferedInputStream bis =null;
		BufferedOutputStream bos =null;
		FileOutputStream fos = null;


		String realPath = HttpUtil45.class.getClassLoader().getResource("").getFile();

		try {
			realPath= URLDecoder.decode(realPath, "utf-8")+"downimage";
			File filePath = new File(realPath);
			if(!filePath.exists()){
				filePath.mkdirs();
			}
			System.err.println("============================================================="+realPath);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}


		try {
			CloseableHttpClient ht=null;
			ht=getHttpClient(null);//HttpClients.createDefault();
			HttpClientContext localContext = getPlainContext(url);//HttpClientContext.create();
			localContext.setRequestConfig(defaultRequestConfig(outTimeConf));
			resp=ht.execute(get);
			HttpEntity entity=resp.getEntity();
//			InputStream instream = entity.getContent();

			String file ="";
//			if(url.indexOf(".JPG")>0){
//				file = url.substring(0,url.indexOf(".JPG")+4);
//				file = file.substring(file.lastIndexOf("/")+1)+".jpg";
//			}else if(url.indexOf(".jpg")>0){
//				file = url.substring(0,url.indexOf(".JPG")+4);
//				file = file.substring(file.lastIndexOf("/")+1)+".jpg";
//			}else if(url.indexOf("?")>0){
//				file = url.substring(0,url.indexOf("?"));
//				file = file.substring(file.lastIndexOf("/")+1)+".jpg";
//			}
			file = realPath+ "/" +fileName;
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


	public  static void main(String[] args){

		HttpGet request = new HttpGet("https://api.channeladvisor.com/oauth2/token?client_id=qwmmx12wu7ug39a97uter3dz29jbij3j&" +
				"grant_type=soap&scope=inventory&developer_key=537c99a8-e3d6-4788-9296-029420540832&password=ChannelAdvisor15&account_id=12018111");
		String auth = "qwmmx12wu7ug39a97uter3dz29jbij3j" + ":" + "TqMSdN6-LkCFA0n7g7DWuQ";
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);
		request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);

		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = null;
		try {
			response = client.execute(request);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int statusCode = response.getStatusLine().getStatusCode();
		System.out.println("status = " + statusCode);

	}
}
