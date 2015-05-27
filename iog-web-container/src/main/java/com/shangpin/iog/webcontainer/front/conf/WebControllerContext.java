package com.shangpin.iog.webcontainer.front.conf;



import com.shangpin.iog.webcontainer.front.conf.interceptor.MySimpleMappingExceptionResolver;
import com.shangpin.iog.webcontainer.front.conf.interceptor.PageInterceptor;
import com.shangpin.iog.webcontainer.front.conf.interceptor.TokenInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.handler.SimpleServletHandlerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;


import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @description web应用上下文，用于扫描controller(view)层<br/>
 * 注册相关的viewResolver,exceptionResolver,multipartResolver,<br/>
 * il8n,intercepter,messageConverters等等
 * 
 * @author 陈小峰 <br/>
 *         2015年1月30日
 */
@Configuration
@EnableWebMvc
@ComponentScan(
		basePackages = "com.shangpin.iog.webcontainer.front.controller",
		useDefaultFilters = true, 
		includeFilters = {
				@ComponentScan.Filter(
						type=FilterType.ANNOTATION,
						value={Controller.class,RestController.class}),
})
public class WebControllerContext extends WebMvcConfigurationSupport {
	static Logger log = LoggerFactory.getLogger(WebControllerContext.class);
	




	public ViewResolver jspViewResolver() {
		log.info("-------------viewResolver:jspViewResolver");
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		viewResolver.setOrder(110);
		return viewResolver;	
	}
	@Bean
	public ViewResolver contentNegotiatingViewResolver(){
		log.info("-------------viewResolver:contentNegotiatingViewResolver-------");
		ContentNegotiatingViewResolver cnvr = new ContentNegotiatingViewResolver();
		cnvr.setContentNegotiationManager(mvcContentNegotiationManager());
		//设置可用viewResolver
		List<ViewResolver> viewResolvers=new ArrayList<>();
//		viewResolvers.add(httlViewResolver());
		//viewResolvers.add(thymeleafViewResolver());
		viewResolvers.add(jspViewResolver());
		viewResolvers.add(new BeanNameViewResolver());
		cnvr.setViewResolvers(viewResolvers);

		//设置默认view
		List<View> defaultViews=new ArrayList<>();
		MappingJackson2JsonView mj = new MappingJackson2JsonView();
		defaultViews.add(mj);
		cnvr.setDefaultViews(defaultViews);
		return cnvr;
	}
	
	@Override
	@Bean
	public ContentNegotiationManager mvcContentNegotiationManager() {
		log.info("-----------mvcContentNegotiationManager-------");
		ContentNegotiationManager x = super.mvcContentNegotiationManager();
		return x; 
	}
	
	@Bean  
	public MessageSource messageSource() {  
	    log.info("-------------MessageResource:MessageSource");  
	    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();  
	    messageSource.setBasename("messages.messages");  
	    return messageSource;  
	}
	/*
	 * 解决json的问题，不同浏览器不支持application/json这种类型，改为text/html方式就ok
	 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#configureMessageConverters(java.util.List)
	@Override
	protected void configureMessageConverters(
			List<HttpMessageConverter<?>> converters) {
		super.configureMessageConverters(converters);
		MappingJackson2HttpMessageConverter cvt = new MappingJackson2HttpMessageConverter();
		List<MediaType> supportedMediaTypes = new ArrayList<>();
		supportedMediaTypes.add(MediaType.TEXT_HTML);
		MediaType mt = new MediaType("text","html",Charset.forName("utf-8"));
		supportedMediaTypes.add(mt);
		cvt.setSupportedMediaTypes(supportedMediaTypes );
		converters.add(cvt);
	}
	 */
	@Bean  
    public HandlerAdapter servletHandlerAdapter(){  
        log.info("---------HandlerAdapter");  
        return new SimpleServletHandlerAdapter();  
    }
	
	@Bean  
    public LocaleChangeInterceptor localeChangeInterceptor(){  
        log.info("---------LocaleChangeInterceptor");  
        return new LocaleChangeInterceptor();  
    } 
	@Bean(name="localeResolver")  
    public CookieLocaleResolver cookieLocaleResolver(){  
        log.info("---------CookieLocaleResolver");  
        return new CookieLocaleResolver();  
    }
	
	@Bean  
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {  
        log.info("---------RequestMappingHandlerMapping");  
        return super.requestMappingHandlerMapping();  
    }
	/**
	 * 注册拦截器
	 */
	@Override  
    protected void addInterceptors(InterceptorRegistry registry) {  
        // TODO Auto-generated method stub  
        log.info("---------addInterceptors start");  
        registry.addInterceptor(localeChangeInterceptor());
        registry.addInterceptor(new PageInterceptor());
        registry.addInterceptor(new TokenInterceptor());
        log.info("addInterceptors end");  
    } 
	@Bean  
    public HandlerMapping resourceHandlerMapping() {  
        log.info("---------HandlerMapping");  
        return super.resourceHandlerMapping();  
    }
	/**
	 * 静态资源处理，可用nginx，apache等处理
	 */
	@Override  
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {  
        log.info("---------addResourceHandlers");  
        registry.addResourceHandler("/static/**","/assets/**").addResourceLocations("/static/","/assets/");  
    }
	


	/**
	 * 文件上传处理
	 * @return
	 */
	@Bean(name="multipartResolver")  
    public CommonsMultipartResolver commonsMultipartResolver(){  
        log.info("---------CommonsMultipartResolver");  
        return new CommonsMultipartResolver();  
    } 
	/**
	 * 自定义异常处理视图
	 * @return
	 */
	@Bean(name="exceptionResolver")
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver(){  
        log.info("-----------SimpleMappingExceptionResolver");  
        MySimpleMappingExceptionResolver er= new MySimpleMappingExceptionResolver();
        Properties em = new Properties();  
        em.setProperty("java.lang.RuntimeException", "500");  
        em.setProperty("java.lang.Exception", "500");  
        Properties statusCodes= new Properties();
        statusCodes.setProperty("404", "404");
        //Internal Server Error
        statusCodes.setProperty("500", "500");
        //Not Acceptable
        statusCodes.setProperty("404", "406");
        //Bad Request
        statusCodes.setProperty("404", "400");
        //Method Not Allowed
        statusCodes.setProperty("404", "405");
        //Unsupported Media Type
        statusCodes.setProperty("404", "415");

        er.setExceptionMappings(em);  
        er.setDefaultErrorView("500");  
		er.setStatusCodes(statusCodes);
        er.setExceptionAttribute("exception");  
        er.setDefaultStatusCode(500);
        return er;  
    } 
	@Bean  
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {  
        log.info("---------RequestMappingHandlerAdapter");  
        return super.requestMappingHandlerAdapter();  
    }

}
