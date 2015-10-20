package com.shangpin.iog.tony.common;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2015/9/21.
 */
public class MyJsonClient {
    /**
     * get tony data
     * */
    public String getTonyJson(){
        String input = "{\"merchantId\":\"55f707f6b49dbbe14ec6354d\",\"token\":\"d355cd8701b2ebc54d6c8811e03a3229\"}";
        String json = null;
        try {
            json = HttpUtil45.operateData("post", "json", "http://www.cs4b.eu/ws/getItemsList", new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10), null, input, "", "");
        } catch (ServiceException e) {
            e.printStackTrace();
        } finally {
            HttpUtil45.closePool();
        }

        try {
            File newTextFile = new File("E:/tonyITEMS.xml");
            if (!newTextFile.exists())
                newTextFile.createNewFile();
            FileWriter fw;
            fw = new FileWriter(newTextFile);
            fw.write(json);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(json);
        return  json.substring(json.indexOf("["), json.length() - 2).replaceAll("\\$", "");
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 通过JSON方式发送HTTP请求
     * */
    public String httpPostOfJson(String entityJson,String url) {
        //建立请求实体
        StringEntity se = getStringEntity(entityJson);
        //定义http请求并设值
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(HTTP.CONTENT_TYPE, Constant.APPLICATION_JSON);
        httpPost.setEntity(se);
        //建立http客户端并发送请求
        CloseableHttpResponse response =  myExecute(httpPost);
        //System.out.println(response.toString());
        //返回响应JSON串
        String responseJson = getResponseAsString(response);
       // System.out.println(responseJson);
        System.out.println("==========================================================");

        return  responseJson;
    }
    /**
     * 获取请求实体
     * */
    private StringEntity getStringEntity(String entityJson){
        StringEntity se = null;
        try {
            se = new StringEntity(entityJson);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        se.setContentType(Constant.TEXT_JSON);
        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, Constant.APPLICATION_JSON));
        return se;
    }
    /**
     * 执行请求并返回结果
     * */
    private CloseableHttpResponse myExecute(HttpPost httpPost){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("-----------------------------");
        System.out.println("返回码："+response.getStatusLine().getStatusCode());
        return response;
    }
    /**
     * 将结果转化为字符串
     * */
    private String getResponseAsString(CloseableHttpResponse response){
        InputStream is = null;
        try {
            is = response.getEntity().getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * get tony data
     * */
    public String getTonyCategoriesJson(){
        String input = "{\"merchantId\":\"55f707f6b49dbbe14ec6354d\",\"token\":\"d355cd8701b2ebc54d6c8811e03a3229\"}";
        String json = null;
        try {
            json = HttpUtil45.operateData("post", "json", "http://www.cs4b.eu/ws/getCategories",  new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10), null, input, "", "");
        } catch (ServiceException e) {
            e.printStackTrace();
        } finally {
            HttpUtil45.closePool();
        }
        System.out.println(json);
        return json;
    }
    /**
     *get stock by getEvents
     * */
    public String getEvents(){
        String json = "{\"merchantId\":\"55f707f6b49dbbe14ec6354d\",\"token\":\"d355cd8701b2ebc54d6c8811e03a3229\"}";
        try {
            String rtnData = HttpUtil45.operateData("post", "json", "http://www.cs4b.eu/ws/getEvents",  new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10), null, json, "", "");
            System.out.println("rtnData=="+rtnData);
        } catch (ServiceException e) {
            e.printStackTrace();
        } finally {
            HttpUtil45.closePool();
        }
        return json;
    }
    /**
     *test
     * */
    public static void main(String[] args){
/*        String input = "{\"merchantId\":\"55f707f6b49dbbe14ec6354d\",\"token\":\"d355cd8701b2ebc54d6c8811e03a3229\"}";
        String url = "http://www.cs4b.eu/ws/getCategories";*/
/*            new MyJsonClient().getJsonByHttp();
            new MyJsonClient().getJsonByHttpCategories();*/
/*        String s1 = new MyJsonClient().httpPostOfJson(Constant.ITEMS_INPUT, Constant.ITEMS_URL);
        String s2 = new MyJsonClient().httpPostOfJson(Constant.CATEGORIES_INPUT, Constant.CATEGORIES_URL);
        System.out.println(s1);
        System.out.println("===================================");
        System.out.println(s2);*/

        new MyJsonClient().getEvents();
        //new MyJsonClient().getTonyJson();
        //MyJsonClient.getTonyCategoriesJson();
    }
}
