package com.shangpin.iog.monnierfreres;

import java.text.SimpleDateFormat;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.monnierfreres.service.FetchProduct;
import com.shangpin.iog.monnierfreres.service.MonnierFrameFetchProduct;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by zhaogenchun on 2015/9/25.
 */
public class StartSkuJob {

//    private static Logger log = Logger.getLogger("info");
//
//    private static ApplicationContext factory;
//    private static void loadSpringContext() {
//        factory = new AnnotationConfigApplicationContext(AppContext.class);
//    }
//
//    public static void main(String[] args){
////        //加载spring
////        loadSpringContext();
////        log.info("----初始SPRING成功--1--");
////        //拉取数据
////        log.info("----拉取数据开始----");              
////        System.out.println("-------fetch start---------");
////        FetchProduct fetchProduct =(FetchProduct)factory.getBean("monnierfreres");
////        fetchProduct.fetchProductAndSave();
////        log.info("----拉取数据完成----");
////        System.out.println("-------fetch end---------");
////        System.exit(0);
//    	//加载spring
//		loadSpringContext();
//		log.info("----初始SPRING成功--1--");
//		log.info("----拉取数据开始----"); 
//		MonnierFrameFetchProduct stockImp = (MonnierFrameFetchProduct) factory
//				.getBean("monnierFrameFetchProduct");
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		System.out.println("-------fetch start---------");
//		stockImp.handleData("sku", supplierId, day, picpath);
//		log.info("----拉取数据完成----");
//		System.out.println("-------fetch end---------");
//    }
    
}
