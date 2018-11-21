package com.shangpin.iog.spider;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.IOException;
import java.util.*;


public class BalenciagaProcessor{


    public List<GoodsEntity> updateInventorySpider(String url) throws IOException {

        List<GoodsEntity> goodsList = new ArrayList<>();
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/13.10586")
                .get();
        String SPU = StringUtils.substringBetween(doc.html(),"data-ytos-code10=\"","\"");
        String price = doc.select("span[class=value]").get(3).text().replaceAll(" ","");
        String qtyHtml = Jsoup.connect("https://www.balenciaga.com/yTos/api/Plugins/ItemPluginApi/GetCombinationsAsync/?siteCode=BALENCIAGA_FR&code10="+SPU)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/13.10586")
                .ignoreContentType(true)
                .get().body().html();
        JSONObject qtyJSON = JSONObject.parseObject(qtyHtml);
        String sizeJSONArrayStr = qtyJSON.getJSONArray("Sizes").toString();
        String[] sizeArray = StringUtils.substringsBetween(sizeJSONArrayStr,"Description\":\"","\"");
        String size = StringUtils.join(sizeArray, ",");
        JSONArray qtyJSONArray = qtyJSON.getJSONArray("SizesByCode10");
        String qtyStr = "";
        for (int i = 0; i < qtyJSONArray.size(); i++) {
            if (qtyJSONArray.getJSONObject(i).getString("Code10").equals(SPU)) {
                qtyStr = qtyJSONArray.getJSONObject(i).getJSONArray("Sizes").toString();
                break;
            }
        }
        List<String> qtyList = new ArrayList<>();
        for (int i = 0; i < sizeArray.length; i++) {
            if (qtyStr.contains(sizeArray[i])) {
                qtyList.add("5");
            } else {
                qtyList.add("0");
            }
        }
        String qty = StringUtils.join(qtyList,",");


        String[] sizes = size.split(",");
        String[] qtys = qty.split(",");
        for (int i = 0; i < sizes.length; i++) {
            GoodsEntity goodsEntity = new GoodsEntity();
            goodsEntity.setSize(sizes[i]);
            goodsEntity.setQty(qtys[i]);
            goodsEntity.setForeignPrice(price);
            goodsList.add(goodsEntity);
        }
        return goodsList;
    }
}