package com.shangpin.iog.DellaHK.purchase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.DellaHK.purchase.schedule.AppContext;


public class StartUp {

	private static   Logger logger = LoggerFactory.getLogger(StartUp.class);
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

 public static void main(String[] args) throws  Exception{
 	loadSpringContext();
     logger.info(" schedule start  ");
 }
}
