package com.shangpin.iog.ebay;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.ebay.conf.EbayConf;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Created by huxia on 2015/7/2.
 */
public class FetchEbayAndSave extends Thread{

    private String store;
    private String   key;
    private FetchEbayProduct fetchProduct;
    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public FetchEbayProduct getFetchProduct() {
        return fetchProduct;
    }

    public void setFetchProduct(FetchEbayProduct fetchProduct) {
        this.fetchProduct = fetchProduct;
    }

    public FetchEbayAndSave() {
    }

    public FetchEbayAndSave( String store, String  key,FetchEbayProduct fetchProduct) {
        this.store = store;
        this.key = key;
        this.fetchProduct=fetchProduct;
    }


    @Override
    public void run() {


        /*for(String key:keys)
        {
            String[] storeName = storeBrand.get(key).split("`");

            for(String store:storeName) {
                */
         try{
                   //for (int i=4;i<101;i++) {
                        fetchProduct.fetchSpuAndSave(store.trim(), key.trim());
                   // }
                } catch (Exception e) {
                    e.printStackTrace();
                }
               // System.out.println("商店"+store+"商店");
          //  }
        //}

    }

}


