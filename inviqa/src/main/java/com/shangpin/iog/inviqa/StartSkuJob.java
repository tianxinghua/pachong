package com.shangpin.iog.inviqa;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.inviqa.service.FetchProduct;

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
        log.info("----开始拉取数据--1--");
        System.out.println("-------fetch start---------");
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("inviqa");
        fetchProduct.fetchProductAndSave();
        log.info("----拉取结束----");
        System.out.println("-------fetch end---------");
        System.exit(0);
    }
    
}
