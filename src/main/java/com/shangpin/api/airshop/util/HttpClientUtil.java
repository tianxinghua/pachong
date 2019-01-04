package com.shangpin.api.airshop.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


/**http请求公用方法
 * @author qinyingchun
 */
@Component
public class HttpClientUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	private static RestTemplate restTemplate;
	@Autowired
	public HttpClientUtil(RestTemplate restTemplate){
		HttpClientUtil.restTemplate = restTemplate;
	}
	
	/**
	 * get 请求
	 * @param url 请求url rest请求格式
	 * @param params 请求参数
	 * @return
	 */
	public static String doGet(String url){
		logger.info("HttpClient request url:" + url);
		String str = restTemplate.getForObject(url, String.class);
		logger.info("HttpClient response data :" + str);
		return str;
	}
	
	/**
	 * post 请求<br/>
	 * <b>注意:</b>以json格式发送请求，请求头contentType是{@link  MediaType#APPLICATION_JSON}
	 * @param url 请求url
	 * @param param 请求参数键值对，最终转换成json格式
	 * @return
	 */
	public static String doPost(String url,Map<String,String> param){
		ResponseEntity<String> entity = restTemplate.postForEntity(url, param, String.class);
		HttpStatus statusCode = entity.getStatusCode();
		if(HttpStatus.OK.equals(statusCode)){
			logger.info("HttpClient request url:" + url + "|params:" + param.toString());
			return entity.getBody();
		}else{
			 logger.error("HttpClient,error status code:" + statusCode + "|request url:" + url + "|params:" + param.toString());
		}
		return null;
	}
	/**
	 * 发送键值对post请求，请求头为：{@link MediaType#APPLICATION_FORM_URLENCODED}
	 * <br/>
	 * @param url 请的url
	 * @param param 请求的键值对参数
	 * @return 对方返回的字符串
	 */
	public static String postKVPair(String url,Map<String,String> param){
		MultiValueMap<String, String> param1 = new LinkedMultiValueMap<>(param.size());
		param1.setAll(param);
		ResponseEntity<String> entity = restTemplate.postForEntity(url, param1, String.class);
		HttpStatus statusCode = entity.getStatusCode();
		if(HttpStatus.OK.equals(statusCode)){
			logger.info("HttpClient request url:" + url + "|params:" + param.toString());
			return entity.getBody();
		}else{
			 logger.error("HttpClient,error status code:" + statusCode + "|request url:" + url + "|params:" + param.toString());
		}
		return null;
	}
	/**
	 * post 请求，发送键值对post请求，请求头为：{@link MediaType#APPLICATION_FORM_URLENCODED}
	 * @param url 请求url
	 * @param content 请求内容，接收方通过请求流获取到数据
	 * @return
	 */
	public static String doPost(String url,String content){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<String> entity  = new HttpEntity<String>("=" +content.toString(), headers);
		ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
		HttpStatus statusCode = response.getStatusCode();
		if(HttpStatus.OK.equals(statusCode)){
			logger.info("HttpClient request url:" + url + "|params:" + content.toString());
			return response.getBody();
		}else{
			logger.error("HttpClient,error status code:" + statusCode + "|request url:" + url + "|params:" + content.toString());
		}
		return null;
	}
	
	/**
	 * post 请求，发送键值对post请求，请求头为：{@link MediaType#APPLICATION_FORM_URLENCODED}
	 * @param url 请求url
	 * @param content 请求内容，接收方通过请求流获取到数据
	 * @return
	 */
	public static String doPostForJson(String url,String content){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<String> entity  = new HttpEntity<String>(content.toString(), headers);
		ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
		HttpStatus statusCode = response.getStatusCode();
		if(HttpStatus.OK.equals(statusCode)){
			logger.info("HttpClient request url:" + url + "|params:" + content.toString());
			return response.getBody();
		}else{
			logger.error("HttpClient,error status code:" + statusCode + "|request url:" + url + "|params:" + content.toString());
		}
		return null;
	}
}
