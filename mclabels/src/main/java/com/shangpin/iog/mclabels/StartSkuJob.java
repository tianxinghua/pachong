package com.shangpin.iog.mclabels;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.mclabels.service.FetchProduct;

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
    	try {
    		//初始化spring
            loadSpringContext();
            log.info("--------spring初始化成功------");
            log.info("----拉取数据开始----");              
            System.out.println("-------fetch start---------");
            FetchProduct fetchProduct =(FetchProduct)factory.getBean("mclabels");
            fetchProduct.fetchProductAndSave();
            log.info("----拉取数据结束----");
            System.out.println("-------fetch end---------");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			System.exit(0);
		}
        
    }
    
}
