package com.shangpin.iog.productmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.productweb.Startup;

public class StartUp {
	   private static   Logger logger = LoggerFactory.getLogger(Startup.class);
       private static ApplicationContext factory;
       private static void loadSpringContext()
       {
           factory = new AnnotationConfigApplicationContext(AppContext.class);
       }
       public static ApplicationContext getApplicationContext(){
    	   return factory;
       }
    public static void main(String[] args) throws  Exception{
    	loadSpringContext();
        logger.info(" schedule start  ");
        StartTask  startTask = (StartTask) factory.getBean("startTask");
        startTask.startTask();
        Thread.currentThread().join();
    }
} 
