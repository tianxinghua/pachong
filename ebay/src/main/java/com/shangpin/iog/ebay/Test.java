package com.shangpin.iog.ebay;


import com.shangpin.iog.app.AppContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by huxia on 2015/6/30.
 */
public class Test {
    static final Log log = LogFactory.getLog(Test.class);
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    public static void main(String args[]) throws Exception {
        String id = "231359724482";
        String storeName = "inzara.store";
        log.info("----拉取ebay数据开始----");
        loadSpringContext();
        log.info("----初始SPRING成功----");
        //拉取数据
        FetchEbayProduct fetchProduct =(FetchEbayProduct)factory.getBean("ebay");
        //fetchProduct.FetchSkuAndSave(id);
        //fetchProduct.FetchSpuAndSave(storeName);

        log.info("----拉取ebay数据完成----");

    }
}
