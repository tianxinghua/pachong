package com.shangpin.iog.glamest.server;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;

public class StartUp {

private static Logger logInfo  = Logger.getLogger("info");
	
	private static ApplicationContext factory;

	private static void loadSpringContext()

	{

		factory = new AnnotationConfigApplicationContext(AppContext.class);
	}

	public static void main(String[] args) {
		//加载spring
		loadSpringContext();
		System.out.println("初始化Spring成功");
		logInfo.info("menlook数据拉取开始---------------------");
		FechProduct fech = (FechProduct) factory.getBean("glamest");
		fech.fechAndSave();
		System.out.println("------------拉取完成 Fech over-----------------");
		logInfo.info("------------拉取完成 Fech over-----------------");
	}
}
