package com.shangpin.iog.railSoAtelier;
import java.util.HashMap;
import java.util.Map;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.railSoAtelier.dto.Attributes;
import com.shangpin.iog.railSoAtelier.service.FetchProduct;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by wangyuzhi on 2015/9/11.
 */
public class StartSkuJob {

    private static Logger log = Logger.getLogger("info");

    private static ApplicationContext factory;
    private static void loadSpringContext() {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    public static void main(String[] args) throws Exception {
    	
        //加载spring
        loadSpringContext();
        log.info("----初始SPRING成功--1--");
        //拉取数据
        log.info("----拉取marylou数据开始----");
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("railSoAtelier");
        fetchProduct.fetchProductAndSave();
        log.info("----拉取marylou数据完成----");
        System.out.println("-------fetch end---------");
        System.exit(0);
    }

}
