package com.shangpin.iog.tony;



import com.shangpin.iog.tony.schedule.AppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;



/**
 * Created by sunny on 2015/8/5.
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
