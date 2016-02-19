package com.shangpin.iog.rosi;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;


public class StartUp {

	private static Logger log = Logger.getLogger("info");

    private static ApplicationContext factory;
    
	private static void loadSpringContext()

	{

        factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	
	public static void main(String[] args)
	{

        //加载spring
		loadSpringContext();
		System.out.println("-------start Spring successful------");	
		log.info("-------start Spring successful------");
		FechProduct fetchProduct = (FechProduct)factory.getBean("rosi");
		fetchProduct.fechAndSave();
		System.out.println("----------------fetch end------------------");
		log.info("----------------fetch end------------------");

	}
}
