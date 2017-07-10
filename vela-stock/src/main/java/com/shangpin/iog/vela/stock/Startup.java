package com.shangpin.iog.vela.stock;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.vela.stock.schedule.AppContext;

public class Startup {

    private static   Logger logger = LoggerFactory.getLogger(Startup.class);
    @SuppressWarnings("unused")
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
