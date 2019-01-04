package com.shangpin.iog.${supplierName};
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.${supplierName}.schedule.AppContext;

/**
 * Created by ${createdby}
 */

public class StartUp {

	private static   Logger logger = LoggerFactory.getLogger(StartUp.class);
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
