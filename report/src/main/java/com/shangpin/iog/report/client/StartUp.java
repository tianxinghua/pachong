package com.shangpin.iog.report.client;

import java.util.ResourceBundle;

import com.shangpin.iog.report.service.ReportService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;


public class StartUp {

	private static org.apache.log4j.Logger loggerInfo = org.apache.log4j.Logger
			.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String suppliers = null;
	private static String isHK = null;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		suppliers = bdl.getString("suppliers");	

	}
	private static ApplicationContext factory;
	private static void loadSpringContext()

	{

        factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	public static void main(String[] args) {
		loadSpringContext();
		loggerInfo.info("初始化成功，开始同步");

		ReportService reportService = (ReportService)factory.getBean("reportService");

		reportService.mail();
		
		loggerInfo.info("===========同步完成========"); 
	}
}
