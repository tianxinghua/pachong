package com.shangpin.iog.opticalscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.opticalscribe.service.FetchProduct;
import com.shangpin.iog.opticalscribe.service.WangFetchProduct;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
        loadSpringContext();
        //鎷夊彇鏁版嵁
        System.out.println("-------fetch start---------");
        WangFetchProduct fetchProduct =(WangFetchProduct)factory.getBean("opticalscribe");
        try {
			fetchProduct.getUrlList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("-------fetch end---------");
        System.exit(0);
    }
    
}