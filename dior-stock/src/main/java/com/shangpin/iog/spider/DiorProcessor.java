package com.shangpin.iog.spider;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;


public class DiorProcessor implements PageProcessor {

    Site site = Site.me().setSleepTime(0).setTimeOut(60000).setRetryTimes(3)
//            .addHeader(":authority","www.dior.com")
//            .addHeader(":method","GET")
//            .addHeader(":scheme","https")
//            .addHeader("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
//            .addHeader("accept-encoding","gzip, deflate, br")
//            .addHeader("accept-languag","zh-CN,zh;q=0.9")
//            .addHeader("cache-control","max-age=0")
//            .addHeader("referer","https://www.dior.com/couture/en_gb/womens-fashion/shoes")
            .addHeader("Cookie","userConnected=; siteaccess=en_gb; __olapicU=1536202118439; currentUniverse=womens-fashion; ak_bmsc=21F09081812DD5D7F67BC2B1968F1088601166F0076100005794975BAF6B6343~pl5Je4s/FXq0+ox6sGBHoN48EeQVva+zx2ShXAs2+X6BvmT/g51YVTk5z0TZ8hqOGPAHY8M2CNOL2PffxYrh6/aKspXYegAbjlpYHG4YctGiljCj0qlrI95jASMmpW5nES8fo9GP7n+4r7iB2Z98ueyFW6eAsOB97U5GChyN9av4RakCKLvpMjjI147rjxjYpBzIOBSi1/ve2BBK3f9pXDWzDAy5KJ7Njcdh8GUbgvhQk7YALZ1lDpCh0fvEK/uYjR; actionField=%2Fcouture%2Fen_gb%2Fwomens-fashion%2Fshoes; frontend=gsp0qrpvgteo4nlvct40nj3n01; bm_sv=942B3BBF239A6EDFFA1DB76B157AE4A0~N8Q9H9wJc9Vdl2HRZXIvmW+wLF7jiwUcvKG4hN8YILz35lCbTj++7VZaXtUKbh0WjmluNTp2YKaGO2/nkPZiic7YS1DRozpKvp0jpj5mG4F8DwD2RFjFS08TdXopfQzetOaanIM7WKitUX8sxbje5g==; advantages_opened=5; OptanonConsent=landingPath=NotLandingPage&datestamp=Tue+Sep+11+2018+18%3A09%3A52+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=3.6.24&AwaitingReconsent=false")
            .addHeader("user-agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36")
            ;


    @Override
    public Site getSite() {
        // TODO Auto-generated method stub
        return site;
    }

    @Override
    public void process(Page page) {
        // TODO Auto-generated method stub

        if (!page.getHtml().getDocument().select("title").text().equals("Dior")) {
            if (page.getUrl().regex("[0-9]+").match()) {

                try {
                    page.putField("gender",page.getHtml().getDocument().select("a[data-reactid=7]").text());
                    page.putField("brand","Dior");
                    page.putField("category",page.getHtml().getDocument().select("a[data-reactid=9]").text());
                    page.putField("SPU",page.getHtml().regex("Reference : (.+?)<").toString().trim());
                    page.putField("productModel",page.getResultItems().get("SPU"));
                    page.putField("season","");
                    page.putField("material","");
                    page.putField("color", StringUtils.substringBetween(page.getHtml().toString(),">Other colors : ","<"));
                    String sizesStr = StringUtils.substringBetween(page.getHtml().toString(),"\"sizes\":","findStoreLink");
                    page.putField("size", "");
                    String[] sizes = StringUtils.substringsBetween(sizesStr,"{\"label\":\"","\"");
                    if (sizes != null) {
                        page.putField("size", StringUtils.join(sizes, ","));
                    }
                    page.putField("proName",page.getHtml().getDocument().title().replaceAll(" - Dior",""));
                    page.putField("foreignPrice",page.getHtml().xpath("//span[@class='price']/text()").toString().replaceAll("£","")
                    .replaceAll(",",""));
                    page.putField("inlandPrice","");
                    page.putField("salePrice","");
                    String[] statuss = StringUtils.substringsBetween(sizesStr,"\"status\":\"","\"");
                    String qty = StringUtils.join(statuss, ",");
                    page.putField("qty", StringUtils.substringBetween(page.getHtml().toString(),"\"status\":\"","\"").replaceAll("AVAILABLE","1")
                            .replaceAll("OUTOFSTOCK","0"));
                    if (qty != null) {
                        page.putField("qty",qty.replaceAll("AVAILABLE","1").replaceAll("OUTOFSTOCK","0"));
                    }

                    page.putField("made","");
                    Elements eles = page.getHtml().getDocument().select("div[class=product-description-content]");
                    page.putField("desc",eles.get(0).text() + eles.get(1).text());
                    if (eles.size() == 2) {
                        page.putField("desc",eles.get(0).text());
                    }

                    List<String> imgs = page.getHtml().xpath("//div[@class='c-generic-media']/img/@src").all();
                    page.putField("pics", StringUtils.join(imgs.toArray(),"|"));
                    page.putField("detailLink",page.getUrl().toString());
                    page.putField("measurement", StringUtils.substringBetween(page.getHtml().toString(),"Dimensions:","<"));
                    page.putField("supplierId","");
                    page.putField("supplierNo","");
                    page.putField("supplierSkuNo","");


//                    List<String> list = page.getHtml().xpath("//div[@class='c-product-variations-swatches']/ul/li/a").links().all();
//                    if (list.size() != 0) {
//                        page.addTargetRequests(list);
//                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                //解析列表页将详情页链接放进待爬队列
                List<String> goodsList = page.getHtml().xpath("//div[@class='push-pic push-pic--border']/a").links().all();
//            for (int i = 0; i < goodsList.size(); i++) {
//                Request request = new Request();
//                request.setUrl(goodsList.get(i));
//                //Map<String,Object> map = new HashMap();
//                //map.put("type","");
//                //request.setExtras(map);
//                page.addTargetRequest(request);
//            }
                page.addTargetRequests(goodsList);
                page.setSkip(true);
            }
        }

    }
    public List<JSONObject> spider(String url) {

        List<JSONObject> goodsJsonList = new ArrayList<>();
        Spider.create(new DiorProcessor())
                //鞋列表
                //.addUrl("https://www.dior.com/couture/en_gb/womens-fashion/shoes")
                //女包列表
                //.addUrl("https://www.dior.com/couture/en_gb/womens-fashion/bags")
                //配饰
                //.addUrl("https://www.dior.com/couture/en_gb/womens-fashion/accessories")
                //女士配饰
                .addUrl(url)
                .addPipeline(new ConsolePipeline())
                .addPipeline(new Pipeline() {
                    @Override
                    public void process(ResultItems resultItems, Task task) {
                        String[] sizes = resultItems.get("size").toString().split(",");
                        String[] qtys = resultItems.get("qty").toString().split(",");
                        for (int i = 0; i < sizes.length; i++) {
                            GoodsEntity goods = new GoodsEntity();
                            goods.setGender(resultItems.get("gender"));
                            goods.setBrand(resultItems.get("brand"));
                            goods.setCategory(resultItems.get("category"));
                            goods.setSPU(resultItems.get("SPU"));
                            goods.setProductModel(resultItems.get("productModel"));
                            goods.setSeason(resultItems.get("season"));
                            goods.setMaterial(resultItems.get("material"));
                            goods.setColor(resultItems.get("color"));
                            goods.setSize(sizes[i]);
                            goods.setProName(resultItems.get("proName"));
                            goods.setForeignPrice(resultItems.get("foreignPrice"));
                            goods.setInlandPrice(resultItems.get("inlandPrice"));
                            goods.setSalePrice(resultItems.get("salePrice"));
                            goods.setQty(qtys[i]);
                            goods.setMade(resultItems.get("made"));
                            goods.setDesc(resultItems.get("desc"));
                            goods.setPics(resultItems.get("pics"));
                            goods.setDetailLink(resultItems.get("detailLink"));
                            goods.setMeasurement(resultItems.get("measurement"));
                            goods.setSupplierId(resultItems.get("supplierId"));
                            goods.setSupplierNo(resultItems.get("supplierNo"));
                            goods.setSupplierSkuNo(resultItems.get("supplierSkuNo"));
                            goods.setChannel(resultItems.get("channel"));
                            JSONObject goodsJson = (JSONObject) JSONObject.toJSON(goods);
                            goodsJsonList.add(goodsJson);
                        }

                    }
                })
                .thread(5)
                .run();

        return goodsJsonList;
    }
}
