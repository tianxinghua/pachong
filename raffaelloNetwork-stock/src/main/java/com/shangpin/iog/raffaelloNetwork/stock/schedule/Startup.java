package com.shangpin.iog.raffaelloNetwork.stock.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.common.utils.logger.LoggerUtil;


/**
 * Created by houkun on 2015/11/30.
 */
public class Startup {

    private static   Logger logger = LoggerFactory.getLogger(Startup.class);
    private static LoggerUtil logError = LoggerUtil.getLogger("error");
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

	public static void main(String[] args)
    {
        //加载spring
    	System.out.println("start");
    	try {
    		loadSpringContext();
		} catch (Exception e) {
			e.printStackTrace();
			logError.error(e);
			
		}
        
        logger.info(" schedule start  ");
    }
}
