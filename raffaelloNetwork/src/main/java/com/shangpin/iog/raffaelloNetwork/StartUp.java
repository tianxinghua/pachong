package com.shangpin.iog.raffaelloNetwork;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.raffaelloNetwork.Service.FetchProduct;

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
		System.out.println("-------------加载  Spring 成功------------");
		logInfo.info("-------------start Spring successful------------");
		FetchProduct fech = (FetchProduct) factory.getBean("raffaelloNetwork");
		fech.fetchAndSave();
		System.out.println("------------ Fech over-----------------");
		logInfo.info("------------ Fech End-----------------");
	}
}
