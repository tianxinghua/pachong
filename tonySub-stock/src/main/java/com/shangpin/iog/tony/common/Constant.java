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
    public static String ITEMS_URL = baseUrl + "/ws/getInventory";// http://www.cs4b.eu/ws/getItemsList
    public static String CATEGORIES_INPUT = "{\"merchantId\":\""+ merchantId +"\",\"token\":\""+token+"\"}";
    public static String CATEGORIES_URL =baseUrl + "/ws/getCategories";
    public static String EVENTS_INPUT ="{\"merchantId\":\""+ merchantId +"\",\"token\":\""+token+"\"}";
            //"{\"merchantId\":\"55f707f6b49dbbe14ec6354d\",\"token\":\"d355cd8701b2ebc54d6c8811e03a3229\"}";
    public static String EVENTS_URL = baseUrl + "/ws/getEvents";

    /**
     * test
     * @param args
     */
    public static void main(String[] args) {


    }
}
