package com.shangpin.iog.ebay;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by huxia on 2015/7/10.
 */
public class FetchEbayPool {
    public static void main(String args[]) throws Exception{

        ExecutorService pool= Executors.newCachedThreadPool();
        FetchEbayAndSave fetch=new FetchEbayAndSave("Jockey-Direct-Closeouts","Jockey");
        //FetchEbayAndSave fetch1=new FetchEbayAndSave("Victorias-Outlet","Victoria'sSecret");
       /*
        FetchEbayAndSave fetch2=new FetchEbayAndSave("","CK");
        FetchEbayAndSave fetch3=new FetchEbayAndSave("","CK");
        FetchEbayAndSave fetch4=new FetchEbayAndSave("","CK");*/
        pool.execute(fetch);
        //pool.execute(fetch1);
        /*
        pool.execute(fetch2);
        pool.execute(fetch3);
        pool.execute(fetch4);*/

    }
}
