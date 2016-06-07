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
//        System.out.println(responseJson);
//        System.out.println("==========================================================");

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

}
