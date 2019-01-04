package com.shang.iog.facade.dubbo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Startup
{

   static  Logger logger = LoggerFactory.getLogger(Startup.class);

	private static ApplicationContext factory;
	private static void loadSpringContext()
	{

        factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	
	public static void main(String[] args)
	{
        logger.info(" dubbo init start");
		loadSpringContext();
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info(" dubbo init end");
	}

}
