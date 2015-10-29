package com.shangpin.iog.havok;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.havok.service.FetchProduct;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by huxia on 2015/10/15.
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
        log.info("----初始SPRING成功----");
        //拉取数据
        log.info("----拉取articoli数据开始----");

        FetchProduct fetchProduct = (FetchProduct) factory.getBean("havok");
        fetchProduct.fetchAndSave();
        log.info("----拉取articoli数据完成----");
        System.out.println("-------fetch end---------");
        System.exit(0);
    }
}
