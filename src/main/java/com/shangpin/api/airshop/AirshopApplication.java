package com.shangpin.api.airshop;

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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.common.utils.SpringContextUtil;
@Configuration
@EnableAutoConfiguration(exclude=WebMvcAutoConfiguration.class)
@ComponentScan(basePackages = "com.shangpin.api.airshop", 
excludeFilters = {
		@ComponentScan.Filter(
				type = FilterType.ANNOTATION, value = Controller.class) 
		})
@EnableConfigurationProperties
@ConfigurationProperties
public class AirshopApplication{
	// extends SpringBootServletInitializer
    public static void main(String[] args) {
    	
		SpringApplication.run(AirshopApplication.class, args);
	}
	/*@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AirshopApplication.class);
    }*/
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
	
	private List<HttpMessageConverter<?>> converters(){
		List<HttpMessageConverter<?>> converters = new ArrayList<>();
		converters.add(new StringHttpMessageConverter(Consts.UTF_8));
		MappingJackson2HttpMessageConverter jsonCv=new MappingJackson2HttpMessageConverter();
		ObjectMapper mapper = new ObjectMapper();
		//TODO 时间看情况定义格式
		mapper.setDateFormat(new SimpleDateFormat("yyyyMMddHHmmss"));
		mapper.setSerializationInclusion(Include.NON_NULL);
		jsonCv.setObjectMapper(mapper);
		converters.add(jsonCv);
		converters.add(new FormHttpMessageConverter());
		return converters;
	}

	@Bean
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
	
	@Bean
	public SpringContextUtil springContextUtil(){
		SpringContextUtil util = new SpringContextUtil();
		return util;
	}
}
