package com.shangpin.iog.reebonz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.reebonz.service.FetchProduct;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by zhaogenchun on 2015/9/25.
 */
public class StartSkuJob {

    private static Logger log = Logger.getLogger("info");

    private static ApplicationContext factory;
    private static void loadSpringContext() {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    public static void main(String[] args){
        //鍔犺浇spring
        loadSpringContext();
        log.info("----鍒濆SPRING鎴愬姛--1--");
        //鎷夊彇鏁版嵁
        log.info("----鎷夊彇鏁版嵁寮�濮�----");              
        System.out.println("-------fetch start---------");
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("reebonzWiki");
        fetchProduct.fetchProductAndSave();
        log.info("----鎷夊彇鏁版嵁瀹屾垚----");
        System.out.println("-------fetch end---------");
        System.exit(0);
    }
    
}
