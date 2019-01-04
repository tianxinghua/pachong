package com.shangpin.iog.anatwine.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
/**
 * Created by sunny on 2015/6/2.
 */
public class ApennineHttpUtil {
//	 private static Products getProductList() {
//		 Map<String,String> map = new HashMap<String,String>();
//    	map.put("id","3");
//    	map.put("password","=eFf`khmbN:3Dfc");
//    	map.put("affiliate","shangpin");
//    	String json = HttpUtil45.post(url, map,new OutTimeConfig(1000*60*10,10*1000*60,10*1000*60));// new HTTPClient(Constant.URL_MARYLOU).fetchProductJson();
//    	System.out.println("商品"+json);
//    	logger.info(json);
//		Products products = null;
//		try {
//			products = ObjectXMLUtil.xml2Obj(Products.class, json);
//		} catch (JAXBException e) {
//			e.printStackTrace();
//		}
//		return products;
//	}
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
        System.out.println(responseJson);
//        System.out.println("==========================================================");

        return  responseJson;
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
  /*public static void main(String[] args) {
    	String url = "http://112.74.74.98:8082/api/GetProductPorperty?userName=spin&userPwd=spin112233&scode=COSEPIAF";
    	NameValuePair[] data = {
                new NameValuePair("scode", "")
        };
    	Map<String,String>map = new HashMap();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
    	map.put("ScodeAll", "TUSKBLKF");
    	map.put("UserName", "spin");
    	map.put("UserPwd", "spin112233");
    	try {
			//String kk = HttpUtils.post(url,map);
    		String kk = HttpUtils.get(url);
			System.out.println("商品属性"+kk);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
