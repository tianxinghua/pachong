package com.shangpin.iog.gilt.service;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.service.ProductFetchService;
import com.sun.org.apache.xerces.internal.impl.dv.xs.DateDV;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunny on 2015/8/5.
 */
@Component("gilt")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    ProductFetchService productFetchService;
    String supplierId = "";//
    String skusUrl="https://api-sandbox.gilt.com/global/skus";
    public void fetchProductAndSave(String url){

    }
    public static void main(String[] args) {
        String url = "https://api-sandbox.gilt.com/global/skus";
        /*NameValuePair[] data = {
                new NameValuePair("scode", "")
        };
        Map<String,String>map = new HashMap();
        map.put("ScodeAll", "TUSKBLKF");
        map.put("UserName", "spin");
        map.put("UserPwd", "spin112233");*/
        try {
            //String kk = HttpUtils.post(url,map);
        Map<String,String> param = new HashMap<>();
        param.put("limit","20");
        param.put("offset","0");
        param.put("since","");
        param.put("sku_ids","");
        OutTimeConfig outTimeConf = new OutTimeConfig();
        String result=HttpUtil45.get(url, outTimeConf, null,"fb8ea6839b486dba8c5cabb374c03d9d","");
        //String result = HttpUtil45.postAuth(url, null, outTimeConf, "fb8ea6839b486dba8c5cabb374c03d9d","");
        System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
