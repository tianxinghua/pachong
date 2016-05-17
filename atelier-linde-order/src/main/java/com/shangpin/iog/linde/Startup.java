package com.shangpin.iog.linde;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.linde.schedule.AppContext;

/**
 * Created by zhaogenchun on 2015/8/5.
 */
public class Startup {

    private static   Logger logger = LoggerFactory.getLogger(Startup.class);
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    public static void main(String[] args)
    {
        loadSpringContext();
        logger.info(" schedule start  ");
    }
}
