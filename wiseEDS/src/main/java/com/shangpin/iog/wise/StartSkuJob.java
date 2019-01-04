package com.shangpin.iog.wise;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.wise.service.FetchProduct;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by zhaogenchun on 2015/12/8.
 */
public class StartSkuJob {

    private static Logger log = Logger.getLogger("info");
    private static ApplicationContext factory;
    private static void loadSpringContext() {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    public static void main(String[] args){
        //鍔犺浇spring
        loadSpringContext();
        log.info("----spring初始化成功-");
        //鎷夊彇鏁版嵁
        log.info("----开始拉取数据----");              
        System.out.println("-------fetch start---------");
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("wise");
        fetchProduct.fetchProductAndSave();
        log.info("----结束拉取数据---");
        System.out.println("-------fetch end---------");
        System.exit(0);
    }
    
    
}
