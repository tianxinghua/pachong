package com.shangpin.iog.forzieri.stock;

import com.google.gson.Gson;
import com.shangpin.iog.forzieri.stock.dto.ResponseToken;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import org.apache.http.HttpResponse;


import javax.xml.ws.http.HTTPException;
import java.io.IOException;

public class GetToken {


    //用戶獲取 token的訪問位址
    public static final String GET_TOKEN_URL="http://open.vipgroup.com.hk/api/token?_method=PUT";
    /**
     * @param
     */

    public static String httpClientTest(){
        HttpClient httpClient=new HttpClient();
        PostMethod postMethod=new PostMethod(GET_TOKEN_URL);
        String response = "";
        String token = "";
        //HttpResponse response =null;
        try{
            NameValuePair uid=new NameValuePair("uid","087");
            NameValuePair username=new NameValuePair("username","shangpin");
            NameValuePair password=new NameValuePair("password","10122han9p1n2018");
            NameValuePair actiondate=new NameValuePair("actiondate","");
            postMethod.setRequestBody(new NameValuePair[]{uid,username,password,actiondate});
            int  statusCode=httpClient.executeMethod(postMethod);
            System.out.println("響應狀態"+statusCode);
            response=postMethod.getResponseBodyAsString();
            System.out.println("響應內容"+response);
        }catch(HTTPException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
        return response;
    }

    public static void main(String[] args) {
       String aa= httpClientTest();
        System.out.println(aa);
    }



}
