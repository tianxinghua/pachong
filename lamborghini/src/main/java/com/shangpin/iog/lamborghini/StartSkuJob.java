package com.shangpin.iog.lamborghini;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.lamborghini.service.FetchProduct;

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
  //  	：http://qa.tmsservice.shangpin.com/LogisticDataService.svc/GetTask

//    		联调地址：LogisticDataService.svc/GetTask

    	Map m = new HashMap();
    	m.put("OpUser","OpUser");
    	m.put("SupplierOrderNoList","2016040707069");
    	String ss = HttpUtil45.post("http://qa.tmsservice.shangpin.com/LogisticDataService.svc/CreateTask", m,new OutTimeConfig(1000*60,1000*60,1000*60));
        //鍔犺浇spring
        loadSpringContext();
        log.info("--------spring初始化成功------");
        //鎷夊彇鏁版嵁
        log.info("----拉取数据开始----");              
        System.out.println("-------fetch start---------");
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("lamborghini");
        fetchProduct.fetchProductAndSave();
        log.info("----拉取数据结束----");
        System.out.println("-------fetch end---------");
        System.exit(0);
    }
    
}
