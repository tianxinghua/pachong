package com.shangpin.iog.efashion;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.efashion.service.FetchProduct;

/**
 * Created by zhaogenchun on 2015/9/25.
 */
public class StartSkuJob {

    private static Logger log = Logger.getLogger("info");

    private static ApplicationContext factory;
    private static void loadSpringContext() {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    private static void readLine(String content){
    	File file = new File("C://cc.json");
    	FileWriter fwriter = null;
    	   try {
    	    fwriter = new FileWriter(file);
    	    fwriter.write(content);
    	   } catch (Exception ex) {
    	    ex.printStackTrace();
    	   } finally {
    	    try {
    	     fwriter.flush();
    	     fwriter.close();
    	    } catch (Exception ex) {
    	     ex.printStackTrace();
    	    }
    	   }
    }
    public static void main(String[] args) throws IOException{
    	
        loadSpringContext();
        log.info("--------spring初始化成功------");
        //鎷夊彇鏁版嵁
        log.info("----拉取数据开始----");              
        System.out.println("-------fetch start---------");
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("efashion");
        fetchProduct.sendAndSaveProduct();
        log.info("----拉取数据结束----");
        System.out.println("-------fetch end---------");
        System.exit(0);
    }
    
}
