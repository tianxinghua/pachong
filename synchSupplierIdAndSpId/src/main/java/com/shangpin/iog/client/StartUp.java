package com.shangpin.iog.client;

import java.util.ResourceBundle;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.service.OpenapiService;
import com.shangpin.iog.service.SopService;

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
		isHK = bdl.getString("isHK");
	}
	private static ApplicationContext factory;
	private static void loadSpringContext()

	{

        factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	public static void main(String[] args) {
		try {
			loadSpringContext();
			loggerInfo.info("初始化成功，开始同步");
			if("1".equals(isHK)){
				SopService sopService = (SopService)factory.getBean("sopService");
				sopService.dotheJob(suppliers); 
			}else{
				OpenapiService openapiService = (OpenapiService)factory.getBean("openapiService");
				openapiService.dotheJob(suppliers);
			}
			loggerInfo.info("===========同步完成========"); 
		} catch (Exception e) {
			e.printStackTrace();
		}		
		System.exit(0); 
	}
}
