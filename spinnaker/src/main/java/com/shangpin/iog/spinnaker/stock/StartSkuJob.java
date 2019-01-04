package com.shangpin.iog.spinnaker.stock;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.spinnaker.stock.service.FetchProduct;

/**
 * Created by Administrator on 2015/7/8.
 */

public class StartSkuJob {

    private static Logger log = Logger.getLogger("info");

    private static ApplicationContext factory;
    private static void loadSpringContext() {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    public static void main(String[] args) throws Exception {
        //加载spring
        log.info("----拉取spinnaker数据开始----");
        loadSpringContext();
        log.info("----初始SPRING成功----");
        System.out.println("开始拉取:");
        //拉取数据
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("spinnaker");
        try {
            fetchProduct.sendAndSaveProduct();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("----拉取spinnaker数据完成----");
        System.out.println("-------fetch end---------");
        System.exit(0);
    }

}
