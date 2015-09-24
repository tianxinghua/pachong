package com.shangpin.iog.dante5.service;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dante5.dto.Rss;
import com.shangpin.iog.service.ProductFetchService;
import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Component("dante5")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());
    private static Logger logMongo = Logger.getLogger("mongodb");
    @Autowired
    ProductFetchService productFetchService;

    public void fetchProductAndSave(String url) {

        String supplierId = "2015092401436";
        try {
            Map<String, String> mongMap = new HashMap<>();
            OutTimeConfig timeConfig = OutTimeConfig.defaultOutTimeConfig();
            timeConfig.confRequestOutTime(600000);
            timeConfig.confSocketOutTime(600000);
            String result = HttpUtil45.get(url, timeConfig, null);

            System.out.println(result);

            XmlParserCreator parserCreator = new XmlParserCreator() {
                @Override
                public XmlPullParser createParser() {
                    try {
                        return XmlPullParserFactory.newInstance().newPullParser();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            };

            GsonXml gsonXml = new GsonXmlBuilder()
                    .setSameNameLists(true)
                    .setXmlParserCreator(parserCreator)
                    .create();

            Rss rss = gsonXml.fromXml(result, Rss.class);

            if (rss == null || rss.getChannel() == null) {
                return;
            }

            System.out.println(rss);

//            for (Item item : rss.getChannel().getItem()) {
//                if (item == null) {
//                    continue;
//                }
//                System.out.println("item : " + item);
//
//                String skuId = "";
//                SkuDTO sku = new SkuDTO();
//
//                //SKU 必填
//                sku.setId(UUIDGenerator.getUUID());
//                sku.setSupplierId(supplierId);
//                skuId = item.getId();
//                sku.setSkuId(skuId);
//                sku.setSpuId(skuId);
//                sku.setMarketPrice(item.getPrice());
//                sku.setColor(item.getColor());
//                sku.setProductSize(item.getSize());
//                sku.setStock(item.getAvailability());
//
//                //SKU 选填
//                sku.setProductName(item.getTitle());
//                sku.setProductDescription(item.getDescription());
//
//                try {
//                    productFetchService.saveSKU(sku);
//                } catch (ServiceException e) {
//                    try {
//                        if (e.getMessage().equals("数据插入失败键重复")) {
//                            //更新价格和库存
//                            productFetchService.updatePriceAndStock(sku);
//                        } else {
//                            e.printStackTrace();
//                        }
//                    } catch (ServiceException e1) {
//                        e1.printStackTrace();
//                    }
//                }
//
//                //保存图片
//                if (item.getImageLinks() != null) {
//                    for (ImageLinks image : item.getImageLinks()) {
//                        if (image.getLink() != null && !"".equals(image.getLink())) {
//                            ProductPictureDTO dto = new ProductPictureDTO();
//                            dto.setPicUrl(image.getLink());
//                            dto.setSupplierId(supplierId);
//                            dto.setId(UUIDGenerator.getUUID());
//                            dto.setSkuId(sku.getSkuId());
//                            try {
//                                productFetchService.savePictureForMongo(dto);
//                            } catch (ServiceException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
//
//                //保存SPU
//                SpuDTO spu = new SpuDTO();
//                //SPU 必填
//                spu.setId(UUIDGenerator.getUUID());
//                spu.setSpuId(item.getId());
//                spu.setSupplierId(supplierId);
//                spu.setCategoryName(item.getProductType());
//                spu.setBrandName(item.getBrand());
//                spu.setMaterial(item.getComposition());
//
//                try {
//                    productFetchService.saveSPU(spu);
//                } catch (ServiceException e) {
//                    e.printStackTrace();
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
