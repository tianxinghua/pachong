package com.shangpin.iog.theClucher;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;

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
        loadSpringContext();
        log.info("--------spring初始化成功------");
        log.info("----拉取数据开始----");   
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("theClucher");
        fetchProduct.getProductList();
        log.info("----拉取数据结束----");
        System.exit(0);
    }
    
}