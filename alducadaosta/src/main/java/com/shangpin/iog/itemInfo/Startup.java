package com.shangpin.iog.itemInfo;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.itemInfo.service.FetchProduct;

public class Startup {

	private static Logger logger = Logger.getLogger("info");
	private static ApplicationContext factory;
	private static void loadSpringContext()
	{
		factory = new AnnotationConfigApplicationContext(AppContext.class);
	}

	public static void main(String[] args)
	{
		//加载spring
		loadSpringContext();
		System.out.println("初始化Spring成功");
		logger.info("============初始化Spring成功===============");
		FetchProduct fetchProduct =(FetchProduct)factory.getBean("itemInfo");
		fetchProduct.fetchProductAndSave();
		logger.info("-------fetch end---------");
		System.out.println("-------fetch end---------");

	}

}
