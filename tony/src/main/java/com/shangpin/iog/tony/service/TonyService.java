package com.shangpin.iog.tony.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.product.dao.SkuMapper;
import com.shangpin.iog.tony.dto.*;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpRequest;
import sun.net.www.http.HttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by loyalty on 15/9/19.
 */
public class TonyService {

    public void getCategories(){
        String json = "{\"merchantId\":\"55f707f6b49dbbe14ec6354d\",\"token\":\"d355cd8701b2ebc54d6c8811e03a3229\"}";
        try {
            String rtnData = HttpUtil45.operateData("post", "json", "http://www.cloudstore.srl/ws/getCategories", null, null, json, "", "");
            System.out.println("rtnData=="+rtnData);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    public void getEvents(){
        String json = "{\"merchantId\":\"55f707f6b49dbbe14ec6354d\",\"token\":\"d355cd8701b2ebc54d6c8811e03a3229\"}";
        try {
            String rtnData = HttpUtil45.operateData("post", "json", "http://www.cloudstore.srl/ws/getEvents", null, null, json, "", "");
            System.out.println("rtnData=="+rtnData);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
    public void getItem(){
        String json = "{\"sku\":\"geColor\",\"merchantId\":\"55f707f6b49dbbe14ec6354d\",\"token\":\"d355cd8701b2ebc54d6c8811e03a3229\"}";
        try {
            String rtnData = HttpUtil45.operateData("post", "json", "http://www.cloudstore.srl/ws/getItem", null, null, json, "", "");
            System.out.println("rtnData=="+rtnData);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
    public void createOrder(){
        String json = "{\"merchantId\":\"55f707f6b49dbbe14ec6354d\"," +
                "\"token\":\"d355cd8701b2ebc54d6c8811e03a3229\"," +
                "\"shopOrderId\":\"aaa\"," +
                "\"status\":\"CONFIRMED\"," +
                "\"statusDate\":\"2015/01/31 09:01:00\"," +
                "\"orderDate\":\"2015/01/31 09:01:00\"," +
                "\"items\":[{ \"sku\":\"123\" , \"qty\":1 ," + "\"price\":1 ,\"cur\":135 }]," +
                "\"orderTotalPrice\":555.00," +
                "\"shippingInfo\":{ \"fees\":1 , " +
                "\"address\":{\"firstname\":\"2\", \"lastname\":\"2\"," + " \"companyname\":\"2\"," +
                " \"street\":\"2\", \"hn\":\"2\", \"zip\":\"2\", \"city\":\"2\"," + " \"province\":\"2\" ," +
                "\"state\":\"2\" }}," +
                "\"billingInfo\":{ \"total\":1.00 ," +
                "\"paymentMethod\":1 ," +
                "\"address\":{ \"firstname\":\"2\", \"lastname\":\"2\"," + " \"companyname\":\"2\", " +
                "\"street\":\"2\", \"hn\":\"2\", \"zip\":\"2\", \"city\":\"2\", " +
                "\"province\":\"2\" ,\"state\":\"2\" }}}";
        System.out.println("request json == "+json);
        try {
            String rtnData = HttpUtil45.operateData("post", "json", "http://www.cloudstore.srl/ws/createOrder", null, null, json, "", "");
            System.out.println("rtnData=="+rtnData);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
    public void updateOrderStatus(){
        String json = "{\"merchantId\":\"55f707f6b49dbbe14ec6354d\"," +
                "\"token\":\"d355cd8701b2ebc54d6c8811e03a3229\"," +
                "\"shopOrderId\":\"aaa\"," +
                "\"status\":\"OTHER\"," +
                "\"statusDate\":\"2015/01/31 09:01:00\"}";
        System.out.println("request json == "+json);
        try {
            String rtnData = HttpUtil45.operateData("post", "json", "http://www.cloudstore.srl/ws/updateOrderStatus", null, null, json, "", "");
            System.out.println("rtnData=="+rtnData);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        new TonyService().getEvents();
        //new TonyService().getItem();
       // new TonyService().createOrder();
       // new TonyService().updateOrderStatus();
/*

        //String test = "{\"ap\":\"55547b77b49dbb81156e71df\",\"merchantId\":\"55f707f6b49dbbe14ec6354d\",\"token\":\"d355cd8701b2ebc54d6c8811e03a3229\"}";
        String test = "{\"merchantId\":\"55f707f6b49dbbe14ec6354d\",\"token\":\"d355cd8701b2ebc54d6c8811e03a3229\"}";
        Map<String,String> map = new HashMap<>();
        map.put("merchantId","55f707f6b49dbbe14ec6354d");
        map.put("token","d355cd8701b2ebc54d6c8811e03a3229");
        map.put("ap","55547b77b49dbb81156e71df");

        SkuParam param = new SkuParam();
        param.setMerchantId("55f707f6b49dbbe14ec6354d");
        param.setToken("d355cd8701b2ebc54d6c8811e03a3229");
        param.setSku("M4004573_001-42");
        Gson gson = new Gson();
        String json = gson.toJson(param,SkuParam.class);
        System.out.println("---------------------------------");
        System.out.println(json);

        ItemListParm itemListParm = new ItemListParm();

        itemListParm.setMerchantId("55f707f6b49dbbe14ec6354d");
        itemListParm.setToken("d355cd8701b2ebc54d6c8811e03a3229");
        itemListParm.setAp("55547b77b49dbb81156e71df");
        String itemList = gson.toJson(itemListParm,ItemListParm.class);
        //System.out.println(itemList);


        String kk = null;
        try {
            //kk = HttpUtil45.operateData("post", "json", "http://www.cs4b.eu/ws/getItemsList", null, map, test, "", "");
            kk = HttpUtil45.operateData("post", "json", "http://www.cs4b.eu/ws/getCategories", null, map, test, "", "");
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //System.out.println("kk23332-- = " + kk + "123");

        String k2 = kk.substring(kk.indexOf("["), kk.length() - 2).replaceAll("\\$", "");
        System.out.println("---------66------------");
        System.out.println(k2);
        ItemsList itemsList = null;
        Items[] array = null;

        array=new Gson().fromJson(k2,new TypeToken<Items[]>() {}.getType());

        System.out.println(array.length);


        Items items = array[0];

        System.out.println("age "+items.getAge());
        System.out.println("image "+items.getImages().length);
        System.out.println("cat-id " + items.getCat_id().toString());
        System.out.println("size " + items.get_id().toString());
*/


    }


}

