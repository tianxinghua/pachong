package com.shangpin.iog.stockMontiHK;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.stockMontiHK.service.StockMontiService;

/**
 * Created by zhaogenchun on 2015/9/25.
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
        log.info("--------spring初始化成功------");
        //鎷夊彇鏁版嵁
        log.info("----拉取数据开始----");              
        System.out.println("-------fetch start---------");
        StockMontiService fetchProduct =(StockMontiService)factory.getBean("stockMontiService");
        fetchProduct.findSupplier();
        log.info("----拉取数据结束----");
        System.out.println("-------fetch end---------");
        System.exit(0);
    }
    
}
