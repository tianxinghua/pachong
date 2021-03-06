package com.shangpin.iog.articoli;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.framework.ServiceException;
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
		logInfo.info("-----------------articoli fech start---------------------");
		FechProduct fech = (FechProduct) factory.getBean("antonacci");
		try {
			fech.sendAndSaveProduct();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		System.out.println("-------------Fech over-----------------");
		logInfo.info("--------------Fech over-----------------");
	}
}
