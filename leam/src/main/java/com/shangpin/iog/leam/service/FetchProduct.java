package com.shangpin.iog.leam.service;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.leam.dto.SkuDTO;
import com.shangpin.iog.service.ProductFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunny on 2015/8/18.
 */
@Component("leam")
public class FetchProduct {
    @Autowired
    ProductFetchService productFetchService;
    String supplierId = "";//供应商ID
    String skuUrl="";//请求sku地址
    public void fetchProductAndSave(String url){

    }
    private static List<SkuDTO> getSkus(String skusUrl){
        List<SkuDTO>list = new ArrayList<>();
        String result="";
        String token="";
        try {
            token=getToken("");
            Map<String, String> param = new HashMap<>();
            OutTimeConfig outTimeConf = new OutTimeConfig();
            result= HttpUtil45.get(skusUrl, outTimeConf, param, "", "");
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
    private static String getToken(String tokenUrl){
        Map<String, String> param = new HashMap<>();
        OutTimeConfig outTimeConf = new OutTimeConfig();
        param.put("user","");
        param.put("password","");
        String result = HttpUtil45.post(tokenUrl,param,outTimeConf);
        return result;
    }
}
