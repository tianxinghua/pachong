package com.shangpin.iog.bernardelli;

import com.shangpin.iog.bernardelli.service.FetchProductData;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;

/**
 * Created by houkun on 2016.01.25.
 */
public class Startup {
    private static Logger log = Logger.getLogger("info");
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    public static void main(String[] args) {

        // 加载spring
        log.info("----拉取monnalisa数据开始----");
        loadSpringContext();
        log.info("----初始SPRING成功----");
        // 拉取数据
        FetchProductData fetchProduct = (FetchProductData) factory.getBean("gg");
        fetchProduct.fetchProductAndSave();
        log.info("----拉取monnalisa数据完成----");
        System.out.println("-------fetch end---------");

    }
    }


