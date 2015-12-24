package com.shangpin.iog.aspesi;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.aspesi.service.FetchProduct;

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
        log.info("----拉取aspesi数据开始----");
        loadSpringContext();
        log.info("----初始SPRING成功----");
        //拉取数据
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("aspesi");
        fetchProduct.fetchProductAndSave();
        log.info("----拉取aspesi数据完成----");


        System.out.println("-------fetch end---------");
    }
}
