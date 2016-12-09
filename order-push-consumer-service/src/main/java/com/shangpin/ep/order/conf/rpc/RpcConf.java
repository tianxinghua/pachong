package com.shangpin.ep.order.conf.rpc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.Consts;
import org.apache.http.client.HttpClient;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * <p>Title:RpcConf.java </p>
 * <p>Description: 所有的RPC远程过程调用</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午3:33:29
 */
@Configuration
public class RpcConf {

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate rest = new RestTemplate();
		HttpComponentsClientHttpRequestFactory fac = new HttpComponentsClientHttpRequestFactory();
		fac.setHttpClient(httpClient());
		fac.setConnectTimeout(3000);
		fac.setReadTimeout(600000);
		fac.setConnectionRequestTimeout(600000);
		rest.setMessageConverters(converters());
		rest.setRequestFactory(fac);
		return rest;
	}
	public List<HttpMessageConverter<?>> converters(){
		List<HttpMessageConverter<?>> converters = new ArrayList<>();
		converters.add(new StringHttpMessageConverter(Consts.UTF_8));
		MappingJackson2HttpMessageConverter jsonCv=new MappingJackson2HttpMessageConverter();
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		mapper.setSerializationInclusion(Include.NON_NULL);
		jsonCv.setObjectMapper(mapper);
		converters.add(jsonCv);
		converters.add(new FormHttpMessageConverter());
		return converters;
	}
	public HttpClient httpClient() {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setDefaultMaxPerRoute(200);
		connectionManager.setMaxTotal(300);
		SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
		ConnectionConfig connectionConfig = ConnectionConfig.custom().setCharset(Consts.UTF_8).build();

		connectionManager.setDefaultConnectionConfig(connectionConfig);
		connectionManager.setDefaultSocketConfig(socketConfig);
		connectionManager.closeIdleConnections(30000, TimeUnit.SECONDS);
		return HttpClientBuilder.create().setConnectionManager(connectionManager).build();
	}
}
