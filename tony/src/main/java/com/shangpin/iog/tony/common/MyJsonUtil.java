package com.shangpin.iog.tony.common;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;

/**
 * Created by Administrator on 2015/9/21.
 */
public class MyJsonUtil {
    /**
     * get tony data
     * */
    public static String getTonyJson(){
        String input = "{\"merchantId\":\"55f707f6b49dbbe14ec6354d\",\"token\":\"d355cd8701b2ebc54d6c8811e03a3229\"}";
        String json = null;
        try {
            json = HttpUtil45.operateData("post", "json", "http://www.cs4b.eu/ws/getItemsList", null, null, input, "", "");
        } catch (ServiceException e) {
            e.printStackTrace();
        }
       // System.out.println(json);
        return  json.substring(json.indexOf("["), json.length() - 2).replaceAll("\\$", "");
    }
    /**
     * get tony data
     * */
    public static String getTonyCategoriesJson(){
        String input = "{\"merchantId\":\"55f707f6b49dbbe14ec6354d\",\"token\":\"d355cd8701b2ebc54d6c8811e03a3229\"}";
        String json = null;
        try {
            json = HttpUtil45.operateData("post", "json", "http://www.cs4b.eu/ws/getCategories", null, null, input, "", "");
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        System.out.println(json);
        return json;
    }
    /**
     *get stock by getEvents
     * */
    public static String getEvents(){
        String json = "{\"merchantId\":\"55f707f6b49dbbe14ec6354d\",\"token\":\"d355cd8701b2ebc54d6c8811e03a3229\"}";
        try {
            String rtnData = HttpUtil45.operateData("post", "json", "http://www.cs4b.eu/ws/getEvents", null, null, json, "", "");
            System.out.println("rtnData=="+rtnData);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return json;
    }
    /**
     *test
     * */
    public static void main(String[] args){
        MyJsonUtil.getTonyJson();
        //MyJsonUtil.getTonyCategoriesJson();
    }
}
