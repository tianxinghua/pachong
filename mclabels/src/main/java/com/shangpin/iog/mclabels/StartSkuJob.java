package com.shangpin.iog.mclabels;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.mclabels.service.FetchProduct;

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
    	
        //鍔犺浇spring
        loadSpringContext();
        log.info("--------spring初始化成功------");
        //鎷夊彇鏁版嵁
        log.info("----拉取数据开始----");              
        System.out.println("-------fetch start---------");
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("efashion");
        fetchProduct.save();
        log.info("----拉取数据结束----");
        System.out.println("-------fetch end---------");
        System.exit(0);
    }
    
}
