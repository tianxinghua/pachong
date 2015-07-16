package com.shangpin.iog.ebay;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.ebay.conf.EbayInit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by huxia on 2015/7/10.
 */
public class FetchEbayPool {
    private static ApplicationContext factory;
    static final Log log = LogFactory.getLog(FetchEbayAndSave.class);
    private static void loadSpringContext() {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    public static void main(String args[]) throws Exception{
        loadSpringContext();
        //FetchEbayProduct fetchProduct=new FetchEbayProduct();
       FetchEbayProduct fetchProduct = (FetchEbayProduct) factory.getBean("ebay");
        Map<String,String> storeBrand= EbayInit.getStoreBrand("store-brand");
        Set<String> keys=storeBrand.keySet();
        log.info("----拉取ebay数据开始----");
        System.out.println("----拉取ebay数据开始----");
        //TODO 此处请在主线程中初始化，只初始化一遍就行

        System.out.println("----初始SPRING成功----");
        log.info("----初始SPRING成功----");
        //拉取数据
        ExecutorService pool= Executors.newCachedThreadPool();
        FetchEbayAndSave fetch=null;
        for(String key:keys)
        {
            String[] storeName = storeBrand.get(key).split("`");
            for(String store:storeName) {

                 fetch = new FetchEbayAndSave(store, key, fetchProduct);

                pool.execute(fetch);
            }
        }
       /*
        FetchEbayAndSave fetch2=new FetchEbayAndSave("","CK");
        FetchEbayAndSave fetch3=new FetchEbayAndSave("","CK");
        FetchEbayAndSave fetch4=new FetchEbayAndSave("","CK");*/

        //pool.execute(fetch1);
        /*
        pool.execute(fetch2);
        pool.execute(fetch3);
        pool.execute(fetch4);*/
        System.out.println("jmdkjdk");
        log.info("----拉取ebay数据完成----");
    }
}
