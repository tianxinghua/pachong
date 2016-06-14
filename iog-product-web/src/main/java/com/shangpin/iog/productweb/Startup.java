package com.shangpin.iog.productweb;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.productweb.schedule.StartTaskService;
import com.shangpin.iog.productweb.schedule.service.impl.StartTaskServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by Administrator on 2015/9/28.
 */
public class Startup {
	   private static   Logger logger = LoggerFactory.getLogger(Startup.class);
       private static ApplicationContext factory;
       private static void loadSpringContext()
       {
           factory = new AnnotationConfigApplicationContext(AppContext.class);
       }

    public static void main(String[] args) throws  Exception{
    	loadSpringContext();
        logger.info(" schedule start  ");
        StartTaskService  service = (StartTaskServiceImpl) factory.getBean("startTaskServiceImpl");
        service.startTask();
        Thread.currentThread().join();
    }
}
