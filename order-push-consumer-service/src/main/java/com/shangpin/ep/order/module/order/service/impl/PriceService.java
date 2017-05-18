package com.shangpin.ep.order.module.order.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.openapi.OpenApiProperties;
import com.shangpin.ep.order.enumeration.LogLeve;
import com.shangpin.ep.order.exception.ServiceException;
import com.shangpin.ep.order.module.order.bean.PriceQueryDTO;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

/**
 * Created by lizhongren on 2017/5/17.
 */
@Component
public class PriceService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    OpenApiProperties openApiProperties;

    OutTimeConfig outTimeConf =  new OutTimeConfig(1000*3,1000*60,1000*60);

    /**
     * 获取商品的采购价
     * @param supplierId
     * @param purchaseNo
     * @param skuNo：尚品SKUNO
     * @return
     * @throws Exception
     */
    public BigDecimal getPurchasePrice(String supplierId,String purchaseNo,  String skuNo)  throws Exception{
        String request = "",url="";
        request = "{\"OrderTime\":\"\",\"SkuNo\":\"" + skuNo +"\",\"SupplierId\":\""+supplierId+"\"}";
        url= openApiProperties.getOpenApi().getSupplyfindinfo();

        String result = this.getSupplyPrice(url,request);
        String purchasePrice = "10";  //默认给个值
        int priceStatus=0;

        //获取采购单明细
        if(result!=null){
            JSONObject json = JSONObject.parseObject(result);
            if("0".equals(json.getString("responseCode"))){

                JSONArray arr = json.getJSONArray("response");
                if(null!=arr&&arr.size()>0){
                    for(int i=0;i<arr.size();i++){
                        priceStatus  = arr.getJSONObject(i).getIntValue("PriceStatus");
                        if(1==priceStatus){
                            purchasePrice = arr.getJSONObject(i).getString("SupplyPrice");
                        }
                    }

                }else{

                    throw new Exception("采购单：" + purchaseNo+"获取采购价失败,返回内容==" + result );
                }
            }else{
                LogCommon.recordLog("采购单：" + purchaseNo+"获取采购价失败,返回内容==" + result, LogLeve.ERROR);
            }
        }
        return new BigDecimal(purchasePrice);

    }


    private String getSupplyPrice(String url,String queryJson){
        try {
            String result  =  HttpUtil45.operateData("post","json",
                    url,
                    outTimeConf,null,queryJson,null,"","");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
