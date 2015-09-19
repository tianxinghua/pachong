package com.shangpin.iog.tony.service;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.product.dao.SkuMapper;
import com.shangpin.iog.tony.dto.CommonParam;
import com.shangpin.iog.tony.dto.ItemListParm;
import com.shangpin.iog.tony.dto.SkuParam;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by loyalty on 15/9/19.
 */
public class TonyService {

    public static void main(String[] args){

//        Map<String,String> map = new HashMap<>();
//
//        map.put("merchantId","55f707f6b49dbbe14ec6354d");
//        map.put("token","d355cd8701b2ebc54d6c8811e03a3229");
//
//        SkuParam param = new SkuParam();
//        param.setMerchantId("55f707f6b49dbbe14ec6354d");
//        param.setToken("d355cd8701b2ebc54d6c8811e03a3229");
//        param.setSku("M4004573_001-42");
//        Gson gson = new Gson();
//        String json = gson.toJson(param,SkuParam.class);
//
//        ItemListParm itemListParm = new ItemListParm();
//
//        itemListParm.setMerchantId("55f707f6b49dbbe14ec6354d");
//        itemListParm.setToken("d355cd8701b2ebc54d6c8811e03a3229");
//        itemListParm.setAp("55f70819b49dbbdc4ec6354d");
//        String itemList = gson.toJson(itemListParm,ItemListParm.class);
//
//
//        String kk = null;
//        try {
//            kk = HttpUtil45.operateData("post", "json", "http://www.cs4b.eu/ws/getItemList", null, map, itemList, "", "");
//        } catch (ServiceException e) {
//            e.printStackTrace();
//        }
        String kk=null;
        System.out.println("kk = " + null + "123");

    }
}
