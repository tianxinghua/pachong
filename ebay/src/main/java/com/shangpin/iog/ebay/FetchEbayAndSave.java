package com.shangpin.iog.ebay;


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

        try {
            fetchProduct.fetchSpuAndSave(store.trim(), key.trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


