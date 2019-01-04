package com.shangpin.iog.mario;

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
		System.out.println("-------------start Spring successful------------");
		logInfo.info("-------------start Spring successful------------");
		FetchProduct fech = (FetchProduct) factory.getBean("mario");
		fech.fetchAndSave();
		System.out.println("------------ Fech over-----------------");
		logInfo.info("------------ Fech over-----------------");
	}
}
