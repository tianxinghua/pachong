package com.shangpin.iog.facade.dubbo.service;

/**
 * Created by huxia on 2015/9/23.
 */
public class Test {
    public static void main(String args[]){
        String skuImgUrl ="http://1.jpg";
        String[] skuImageUrlArray = skuImgUrl.split("\\|\\|");
        for(String sku:skuImageUrlArray){
             System.out.println(sku);
        }
    }


}
