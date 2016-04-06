package com.shangpin.iog.ostore.stock.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


/**
 * Created by houkun on 2015/11/30.
 */
public class Startup {

    private static   Logger logger = LoggerFactory.getLogger("info");
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

	public static void main(String[] args)
    {
        //加载spring
    	System.out.println("start");
        loadSpringContext();
        logger.info(" schedule start  ");
    }
}
