package com.shangpin.ephub.price.consumer.util;

import com.alibaba.fastjson.JSONObject;
import com.shangpin.ephub.price.consumer.service.dto.PriceParamDTO;
import lombok.Value;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Configuration
@ConfigurationProperties(prefix = "marketprice")
public class HttpClientUtil {

    private String priceurl;

    public static String sendRequest(String url){
        StringBuffer temp=new StringBuffer();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost=new HttpPost(url);
     //   HttpGet httpGet = new HttpGet(url);
        StringBuffer json = new StringBuffer();
     //   List<ItemInfo> a = new ArrayList<ItemInfo>();
        PriceParamDTO priceParamDTO = new PriceParamDTO();
        priceParamDTO.setSkuNo("1982329290-200845227238");
        priceParamDTO.setChannelName("cccc");
        priceParamDTO.setMarketPrice(new BigDecimal(666));
        priceParamDTO.setSpecialMarketPrice("777");
        priceParamDTO.setSopUserNo(2018081701906L);

        List<PriceParamDTO> list = new ArrayList<PriceParamDTO>();
        list.add(priceParamDTO);
        HttpEntity httpEntity = null;
      //  FileOutputStream fos = null;
        try{
            StringEntity stringEntity = new StringEntity(JSONObject.toJSONString(list));

            stringEntity.setContentType("application/json; charset=utf-8");
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
          //  a = JsonUtil.getListFromJsonFile(fileName);
        }catch (Exception e){
            System.out.println(e.getMessage());
            //logger.info(e.getMessage());
        }finally{
         /*   try {
                fos.close();
                EntityUtils.consume(httpEntity);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }*/
        }
        return temp.toString();
    }

    public static void main(String[] args) {
     String re=   HttpClientUtil.sendRequest("http://qa.sopoutapi.shangpin.com/Product/ModifyProductMarketPrice");
        System.out.println(re);
    }


}
