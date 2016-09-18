package com.shangpin.iog.attributemapping;

import com.shangpin.iog.app.AppContext;

import com.shangpin.iog.attributemapping.service.BrandMappingService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class StartUp {

	private static org.apache.log4j.Logger loggerInfo = org.apache.log4j.Logger
			.getLogger("info");

	private static ApplicationContext factory;
	private static void loadSpringContext()

	{

        factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	public static void main(String[] args) {
		
		loadSpringContext();
		loggerInfo.info("初始化成功，开始同步");
		BrandMappingService service = (BrandMappingService)factory.getBean("brandMappingService");
		service.setBrandMapping();

				
	}
}
