package com.shangpin.ephub.price.consumer.util;

import com.alibaba.fastjson.JSONObject;
import com.shangpin.ephub.price.consumer.service.dto.PriceParamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Getter
@Setter
@ConfigurationProperties(prefix = "marketprice")
@Component
public class HttpClientUtil {

    private  String priceurl;

    public  String sendRequest(String param){
        StringBuffer temp=new StringBuffer();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost=new HttpPost(priceurl);
     //   HttpGet httpGet = new HttpGet(url);
        StringBuffer json = new StringBuffer();

        HttpEntity httpEntity = null;
      //  FileOutputStream fos = null;
        try{
            StringEntity stringEntity = new StringEntity(param,"utf-8");

            stringEntity.setContentType("application/json");
            stringEntity.setContentEncoding("UTF-8");
            httpPost.setEntity(stringEntity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            httpEntity = response.getEntity();

            InputStream is = new BufferedInputStream(new DataInputStream(httpEntity.getContent()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));


            String lineMessage;
            while ((lineMessage = reader.readLine()) != null) {
                temp.append(lineMessage);
                System.out.println(lineMessage);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            log.info(e.getMessage());
        }finally{
           try {
                EntityUtils.consume(httpEntity);
            }catch (Exception e){
                System.out.println(e.getMessage());
               log.info(e.getMessage());
            }
        }
        return temp.toString();
    }



}
