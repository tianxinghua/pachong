package com.shangpin.iog.spider;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.*;


public class BalenciagaProcessor implements PageProcessor {


    Site site = Site.me().setSleepTime(0).setTimeOut(60000).setRetryTimes(3)
            .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/13.10586")
            ;


    @Override
    public Site getSite() {
        // TODO Auto-generated method stub
        return site;
    }

    static String type = "";

    @Override
    public void process(Page page) {
        // TODO Auto-generated method stub
        System.out.println("*****************");

        if (page.getUrl().toString().contains(".html")) {
            try {
                String gender = "women";
                if (type.contains("男")) {
                    gender = "men";
                }
                page.putField("gender", gender);
                page.putField("brand", "Balenciaga");
                page.putField("category", type);
                String SPU = StringUtils.substringBetween(page.getHtml().toString(),"data-ytos-code10=\"","\"");
                page.putField("SPU", SPU);
                page.putField("productModel", SPU);
                page.putField("season", "");
                page.putField("material", page.getHtml().getDocument().select("p[class=attributesUpdater MainMaterial ] ")
                        .select("span[class=value]").text());
                page.putField("color", page.getHtml().getDocument().select("span[class=accordion-itemvariants-value]").text());
                //抽取size
                String qtyHtml = Jsoup.connect("https://www.balenciaga.com/yTos/api/Plugins/ItemPluginApi/GetCombinationsAsync/?siteCode=BALENCIAGA_FR&code10="+SPU)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/13.10586")
                        .ignoreContentType(true)
                        .get().body().html();
                JSONObject qtyJSON = JSONObject.parseObject(qtyHtml);
                String sizeJSONArrayStr = qtyJSON.getJSONArray("Sizes").toString();
                String[] sizeArray = StringUtils.substringsBetween(sizeJSONArrayStr,"Description\":\"","\"");

                JSONArray colorJSONArray = qtyJSON.getJSONArray("Colors");
                for (int i = 0; i < colorJSONArray.size(); i++) {
                    if (colorJSONArray.getJSONObject(i).getString("Code10").equals(SPU)) {
                        page.putField("color", colorJSONArray.getJSONObject(i).getString("Description"));
                    }
                }

                page.putField("size", StringUtils.join(sizeArray, ","));
                page.putField("proName", page.getHtml().getDocument().select("span[class=value]").get(4).text());
                page.putField("foreignPrice", page.getHtml().getDocument().select("span[class=value]").get(3).text().replaceAll(" ",""));
                page.putField("inlandPrice", "");
                page.putField("salePrice", "");
                //抽取库存

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
                page.putField("qty", StringUtils.join(qtyList,","));
                page.putField("made", "");
                page.putField("desc", page.getHtml().getDocument().select("div[class=item-description]").text());

                List<String> imgs = page.getHtml().xpath("//ul[@class='alternativeImages']/li/img/@src").all();
                page.putField("pics", StringUtils.join(imgs, "|"));
                page.putField("detailLink", page.getUrl().toString());
                page.putField("measurement", "");
                page.putField("supplierId", "2018090402046");
                page.putField("supplierNo", "S0000987");
                page.putField("supplierSkuNo", "");



            } catch (Exception e){
                e.printStackTrace();
            }

        } else {
            page.addTargetRequests(page.getHtml().xpath("//a[@class='item-display-image-container item-link']").links().all());
            System.out.println(page.getTargetRequests().size());
            page.setSkip(true);
        }



    }

//    public List<GoodsEntity> updateInventorySpider(String url) {
//        SetGoods setGoods = new SetGoods();
//        List<GoodsEntity> goodsList = new ArrayList<>();
//        Spider spider = new Spider(new BalenciagaProcessor());
//        spider.addUrl(url);
//        //spider.thread(1);
//        spider.addPipeline(new Pipeline() {
//            @Override
//            public void process(ResultItems resultItems, Task task) {
//                String[] sizes = resultItems.get("size").toString().split(",");
//                String[] qtys = resultItems.get("qty").toString().split(",");
//                for (int i = 0; i < sizes.length; i++) {
//                    GoodsEntity goods = setGoods.setgoods(resultItems, sizes, qtys, i);
//                    goodsList.add(goods);
//                }
//            }
//        });
//        spider.run();
//        //return goodsList;
//    }
}