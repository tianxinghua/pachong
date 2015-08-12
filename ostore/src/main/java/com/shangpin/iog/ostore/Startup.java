package com.shangpin.iog.ostore;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.ostore.service.FetchProduct;
import org.apache.log4j.Logger;
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
        loadSpringContext();
        log.info("----��ʼSPRING�ɹ�----");
        //��ȡ����
        log.info("----��ȡostore���ݿ�ʼ----");

        FetchProduct fetchProduct = (FetchProduct)factory.getBean("ostore");
        fetchProduct.fetchProductAndSave("http://b2b.officinastore.com/shangpin.asp");

        log.info("----��ȡostore�������----");
        System.out.println("-------fetch end---------");
        System.exit(0);

    }
}
