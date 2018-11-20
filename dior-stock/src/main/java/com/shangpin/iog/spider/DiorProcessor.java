package com.shangpin.iog.spider;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class DiorProcessor {

    public List<GoodsEntity> updateInventorySpider(String url) throws IOException {

        List<GoodsEntity> goodsList = new ArrayList<>();
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/13.10586")
                .get();

        List<String> sizeList = new ArrayList<>();
        sizeList.add("U");
        String[] sizes = null;
        if (url.contains("fr_fr")) {
            sizes = StringUtils.substringsBetween(doc.html(),"\"Taille : ","\"");
        } else if (url.contains("en_gb")) {
            sizes = StringUtils.substringsBetween(doc.html(),"\"Size : ","\"");
        }

        if (sizes != null) {
            sizeList.clear();
            Set<String> sizeSet = new HashSet<>();
            for (int i = 0; i < sizes.length; i++) {
                if (sizeSet.add(sizes[i])) {
                    sizeList.add(sizes[i]);
                }
            }
        }


        List<String> qtyList = new ArrayList<>();
        String[] qtys = StringUtils.substringsBetween(doc.html(),"status\":\"","\"");
        if (sizeList.size() == 1 && sizeList.get(0).equals("U")) {
            if (qtys[0].contains("AVAILABLE")) {
                qtyList.add("5");
            } else {
                qtyList.add("0");
            }

        } else {
            for (int i = 0; i < sizeList.size(); i++) {
                if (qtys[i].equals("AVAILABLE")) {
                    qtyList.add("5");
                } else {
                    qtyList.add("0");
                }
            }
        }


        String price = StringUtils.substringBetween(doc.html(),"\"price\":",",");


        for (int i = 0; i < sizeList.size(); i++) {
            GoodsEntity goodsEntity = new GoodsEntity();
            goodsEntity.setSize(sizeList.get(i));
            goodsEntity.setQty(qtyList.get(i));
            goodsEntity.setForeignPrice(price);
            goodsList.add(goodsEntity);
        }
        return goodsList;
    }
}
