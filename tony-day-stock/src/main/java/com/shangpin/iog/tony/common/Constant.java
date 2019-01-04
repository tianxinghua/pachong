package com.shangpin.iog.tony.common;

import java.util.ResourceBundle;

/**
 * Created by wangyuzhi on 2015/9/11.
 */
public class Constant {
    public static ResourceBundle bundle = ResourceBundle.getBundle("conf");
    //供应商ID
    public static String SUPPLIER_ID = bundle.getString("supplierId");
    public static String merchantId =   bundle.getString("merchantId");
    public static String token =   bundle.getString("token");
    public static String baseUrl =   bundle.getString("baseUrl");
    //public static String SUPPLIER_ID = "2015101501608";
    public static String TEXT_JSON = "text/json";
    public static String APPLICATION_JSON = "application/json";
    public static String ITEMS_INPUT = "{\"merchantId\":\""+ merchantId +"\",\"token\":\""+token+"\"}";
    public static String ITEMS_URL =baseUrl+ "/ws/getInventory";//http://sandbox.cs4b.eu/ws/getInventory
    /**
     * test
     * @param args
     */
    public static void main(String[] args) {


    }
}
