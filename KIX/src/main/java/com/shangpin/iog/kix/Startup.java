package com.shangpin.iog.kix;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.kix.service.FetchProduct;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
public class Startup {


    private static Logger log = Logger.getLogger("info");
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    public static void main(String[] args)
    {
        //加载spring
        log.info("----拉取kix数据开始----");
        loadSpringContext();
        log.info("----初始SPRING成功----");
        System.out.println("开始i");
        //拉取数据
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("kix");
        fetchProduct.fetchProductAndSave();
        log.info("----拉取kix数据完成----");


        System.out.println("-------fetch end---------");
    }
}
