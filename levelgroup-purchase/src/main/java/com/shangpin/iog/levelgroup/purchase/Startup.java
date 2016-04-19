package com.shangpin.iog.levelgroup.purchase;

import com.shangpin.iog.levelgroup.purchase.schedule.AppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by 董晶辉
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
    }
}
