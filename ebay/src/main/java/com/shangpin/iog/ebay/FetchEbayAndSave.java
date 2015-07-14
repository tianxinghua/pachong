package com.shangpin.iog.ebay;

import com.shangpin.iog.app.AppContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ResourceBundle;

/**
 * Created by huxia on 2015/7/2.
 */
public class FetchEbayAndSave extends Thread{

    private String storeName;
    private String keyWords;

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public String getStorName() {
        return storeName;
    }

    public void setStorName(String storName) {
        this.storeName = storName;
    }

    public FetchEbayAndSave() {
    }

    public FetchEbayAndSave(String storName, String keyWords) {
        this.storeName = storName;
        this.keyWords = keyWords;
    }


    static final Log log = LogFactory.getLog(FetchEbayAndSave.class);
    private static ApplicationContext factory;

    private static void loadSpringContext() {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    @Override
    public void run() {

        log.info("----拉取ebay数据开始----");
        System.out.println("----拉取ebay数据开始----");
        loadSpringContext();
        System.out.println("----初始SPRING成功----");
        log.info("----初始SPRING成功----");
        //拉取数据
        FetchEbayProduct fetchProduct = (FetchEbayProduct) factory.getBean("ebay");
        try {
            fetchProduct.fetchSpuAndSave(storeName.trim(), keyWords.trim());
        } catch (Exception e) {
        }
        System.out.println("jmdkjdk");
        log.info("----拉取ebay数据完成----");
    }


   /* public static void main(String args[]) throws Exception {
        //String id = "281082188156";
        //ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("store-brand");
        //String[] storeName = RESOURCE_BUNDLE.getString("JessicaSimpson").split("`");
        //String storeName = "mlg_fashion";
        String keywords = "TommyHilfiger";
        log.info("----拉取ebay数据开始----");
        System.out.println("----拉取ebay数据开始----");
        loadSpringContext();
        System.out.println("----初始SPRING成功----");
        log.info("----初始SPRING成功----");
        //拉取数据
        FetchEbayProduct fetchProduct =(FetchEbayProduct)factory.getBean("ebay");
        //fetchProduct.fetchSkuAndSave(id);
        *//*for(String store:storeName) {
            fetchProduct.fetchSpuAndSave(store,keywords);
            System.out.println("商店"+store+"商店");
        }*//*
        fetchProduct.fetchSpuAndSave("DYK-Group-for-Hilfiger",keywords.trim());
//      GrabWithTradAndShoppingApi egProduct = (GrabWithTradAndShoppingApi)factory.getBean(GrabWithTradAndShoppingApi.class);
//      egProduct.FetchAndSave();
        System.out.println("jmdkjdk");
        log.info("----拉取ebay数据完成----");
    }*/
}


