package com.shangpin.iog.export.client;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.export.service.ExportService;

public class Client {

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
		
		ExportService exportService = (ExportService)factory.getBean("exportService");
		exportService.writeFile();
		loggerInfo.info("结束");
		System.exit(0); 
		
				
	}
}
