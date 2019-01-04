package com.shangpin.api.airshop.config;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import javax.servlet.DispatcherType;
import javax.servlet.MultipartConfigElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.api.airshop.filter.LoginFiter;

/**
 * @description web应用上下文，用于扫描controller(view)层<br/>
 * 这个类可有可无<br/>
 * 注册相关的viewResolver,exceptionResolver,multipartResolver,<br/>
 * il8n,intercepter,messageConverters等等
 * 
 * @author 陈小峰 <br/>
 *         2015年1月30日
 */
@Configuration
@EnableWebMvc
@ComponentScan(
		basePackages = "com.shangpin.api.airshop", 
		includeFilters = {
				@ComponentScan.Filter(
				type=FilterType.ANNOTATION,
				value={Controller.class,RestController.class,ControllerAdvice.class})
		}
)
public class WebControllerContext extends WebMvcConfigurationSupport {
	static Logger log = LoggerFactory.getLogger(WebControllerContext.class);
	
	@Override
	@Bean
	public ContentNegotiationManager mvcContentNegotiationManager() {
		ContentNegotiationManager x = super.mvcContentNegotiationManager();
		return x; 
	}
	
	@Bean  
	public MessageSource messageSource() {  
	    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();  
	    messageSource.setBasename("messages.messages");  
	    return messageSource;  
	}
	/*
	 * 解决json的问题，不同浏览器不支持application/json这种类型，改为text/html方式就ok
	 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#configureMessageConverters(java.util.List)
	 */
	@Override
	protected void configureMessageConverters(
			List<HttpMessageConverter<?>> converters) {
		super.configureMessageConverters(converters);
		MappingJackson2HttpMessageConverter cvt = new MappingJackson2HttpMessageConverter();
		List<MediaType> supportedMediaTypes = new ArrayList<>();
		supportedMediaTypes.add(MediaType.APPLICATION_JSON);
		cvt.setSupportedMediaTypes(supportedMediaTypes );
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		mapper.setSerializationInclusion(Include.NON_NULL);
		
		cvt.setObjectMapper(mapper);
		
		converters.add(cvt);
	}
	/*@Bean  
    public HandlerAdapter servletHandlerAdapter(){  
        return new SimpleServletHandlerAdapter();  
    }*/
	
	
	@Bean(name="localeResolver")  
    public CookieLocaleResolver cookieLocaleResolver(){  
        return new CookieLocaleResolver();  
    }
	/*
	@Bean  
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {  
        return super.requestMappingHandlerMapping();  
    }*/
	
	@Bean  
    public HandlerMapping resourceHandlerMapping() {  
        return super.resourceHandlerMapping();  
    }
	/**
	 * 静态资源处理，可用nginx，apache等处理
	 */
	@Override  
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {  
        log.info("---------addResourceHandlers");  
        registry.addResourceHandler("/css/**","/js/**","/images/**","/favicon.ico","/monthlyReportTemplate.html","/waybill/printPID").addResourceLocations("classpath:/static/css/",
        		"classpath:/static/js/","classpath:/static/images/","classpath:/static/favicon.ico","classpath:/pdfTemplate/","classpath:/dhlTemplate/");  
    }
	
	/*@Bean  
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {  
        return super.requestMappingHandlerAdapter();  
    }*/
	/**
	 * 登录filter
	 * @return
	 */
	@Bean
	public FilterRegistrationBean myLoginFilter() {
	    FilterRegistrationBean registration = new FilterRegistrationBean();
	    registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 101);
	    registration.setFilter(new LoginFiter());
	    registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
	    //哪些uri需要进行filter处理
	    Collection<String> urls = new ArrayList<>();
	    urls.add("/*");
		registration.setUrlPatterns(urls);
	    return registration; 
	}
	@Bean  
    public MultipartConfigElement multipartConfigElement() {  
		MultipartConfigFactory factory = new MultipartConfigFactory();  
        factory.setMaxFileSize("10MB");  
        factory.setMaxRequestSize("10MB");  
        return factory.createMultipartConfig();  
    }  
}
