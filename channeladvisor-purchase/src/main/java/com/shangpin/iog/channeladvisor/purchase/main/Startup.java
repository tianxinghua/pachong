package com.shangpin.iog.channeladvisor.purchase.main;


import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.channeladvisor.purchase.schedule.AppContext;
import com.shangpin.iog.common.utils.DateTimeUtil;

/**
 * 
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
        //加载spring
        loadSpringContext();
        logger.info(" schedule start  ");
    }
}
