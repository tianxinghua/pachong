package com.shangpin.iog.opticalscribe;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.opticalscribe.service.CanadagooseFR;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhaogenchun on 2015/9/25.
 */
public class StartSkuJobCanadagoose {

    private static Logger logger = Logger.getLogger("info");

    private static ApplicationContext factory;
    private static void loadSpringContext() {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    public static void main(String[] args){
        loadSpringContext();

        //鎷夊彇鏁版嵁
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long start = System.currentTimeMillis();
        System.out.println("-------it - fetch start:"+sim.format(new Date())+"---------");
        logger.info("-------it- fetch start:"+sim.format(new Date())+"---------");
       // GucciProductIT fetchProduct =(GucciProductIT)factory.getBean("gucciProductIT");
        CanadagooseFR canadagoose=(CanadagooseFR)factory.getBean("canadagooseFR");
        try {
            canadagoose.getUrlList();
		} catch (Exception e) {
			e.printStackTrace();
		}
        System.out.println("-------fetch end:"+sim.format(new Date())+"---------");
        System.out.println("总用时："+(System.currentTimeMillis()-start)/1000*60);
        logger.info("-------fetch end:"+sim.format(new Date())+"---------");
        logger.info("总用时："+(System.currentTimeMillis()-start)/1000*60);
        System.exit(0);
    }
    
}
