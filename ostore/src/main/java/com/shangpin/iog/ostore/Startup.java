package com.shangpin.iog.ostore;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.ostore.service.FetchProduct;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by huxia on 2015/8/11.
 */
public class Startup {
    private static Logger log = Logger.getLogger("info");

    private static ApplicationContext factory;
    private static void loadSpringContext() {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    public static void main(String[] args) throws Exception{
        //加载spring

        loadSpringContext();
        log.info("----初始SPRING成功----");
        //拉取数据
        log.info("----拉取ostore数据开始----");
        try {
            FetchProduct fetchProduct = (FetchProduct)factory.getBean("ostore");
            fetchProduct.fetchProductAndSave("http://b2b.officinastore.com/Scambio/Atelier/Shangpin/shangpin.csv");
        } catch (BeansException e) {
            e.printStackTrace();
        }

        log.info("----拉取ostore数据完成----");
        System.out.println("-------fetch end---------");
        System.exit(0);

    }
}
