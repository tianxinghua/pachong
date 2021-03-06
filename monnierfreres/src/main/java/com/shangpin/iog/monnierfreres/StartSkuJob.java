package com.shangpin.iog.monnierfreres;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.monnierfreres.service.MonnierFrameFetchProduct;

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
    	
    	//加载spring
		loadSpringContext();
		log.info("----拉取数据开始----"); 
		MonnierFrameFetchProduct stockImp = (MonnierFrameFetchProduct) factory
				.getBean("monnierFrameFetchProduct");
		stockImp.sendAndSaveProduct();
		log.info("----拉取数据完成----");
		System.out.println("-------fetch end---------");
    }
    
}
