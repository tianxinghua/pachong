package com.shangpin.iog.facade.dubbo.service;

import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.service.ProductSearchService;

/**
 * Created by huxia on 2015/9/23.
 */
public class Test {
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    public static void main(String args[]){
//        String skuImgUrl ="http://1.jpg";
//        String[] skuImageUrlArray = skuImgUrl.split("\\|\\|");
//        for(String sku:skuImageUrlArray){
//             System.out.println(sku);
//        }
        loadSpringContext();
        ProductSearchService productSearchService = (ProductSearchService)factory.getBean("productSearchServiceImpl");
    	String supplier = "20150619013001122";
		Date startDate = new Date(10000000);
		Date endDate = new Date();
		try {
			String skuId = productSearchService.exportSkuId(supplier, startDate, endDate);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
    }


}
