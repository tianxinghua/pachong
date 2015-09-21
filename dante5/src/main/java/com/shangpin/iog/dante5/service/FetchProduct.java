package com.shangpin.iog.dante5.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dante5.dto.Channel;
import com.shangpin.iog.dante5.dto.Item;
import com.shangpin.iog.dante5.dto.Rss;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;
import org.apache.commons.lang.StringUtils;
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

    public void fetchProductAndSave(String url){

        String supplierId = "2015091801508";
        try {
            Map<String,String> mongMap = new HashMap<>();
            OutTimeConfig timeConfig = OutTimeConfig.defaultOutTimeConfig();
            timeConfig.confRequestOutTime(600000);
            timeConfig.confSocketOutTime(600000);
            String result = HttpUtil45.get(url, timeConfig, null);

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
            System.out.println(rss);


            if (rss == null || rss.getChannel() == null) {
                return;
            }

            for (Item item : rss.getChannel().getItem()) {
                if(item == null){
                    continue;
                }

                String skuId = "";
                SkuDTO sku  = new SkuDTO();
                try {
                    sku.setId(UUIDGenerator.getUUID());
                    sku.setSupplierId(supplierId);

                    sku.setSpuId(item.getItemGroupId());
                    skuId = item.getId();
                    sku.setSkuId(skuId);
                    sku.setProductSize(item.getSize());
                    sku.setMarketPrice(item.getPrice());
                    sku.setSalePrice(item.getPrice());
                    sku.setSupplierPrice(item.getPrice());
                    sku.setColor(item.getColor());
                    sku.setProductDescription(item.getDescription());
                    sku.setStock(item.getAvailability());
//                    sku.setProductCode(product.getProducer_id());
                    productFetchService.saveSKU(sku);

//                    if(StringUtils.isNotBlank(item.getPicture())){
//                        String[] picArray = item.getPicture().split("\\|");
//
////                            List<String> picUrlList = Arrays.asList(picArray);
//                        for(String picUrl :picArray){
//                            ProductPictureDTO dto  = new ProductPictureDTO();
//                            dto.setPicUrl(picUrl);
//                            dto.setSupplierId(supplierId);
//                            dto.setId(UUIDGenerator.getUUID());
//                            dto.setSkuId(item.getItem_id());
//                            try {
////                                    productFetchService.savePicture(dto);
//                                productFetchService.savePictureForMongo(dto);
//                            } catch (ServiceException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//
//                    }

                } catch (ServiceException e) {
                    try {
                        if(e.getMessage().equals("数据插入失败键重复")){
                            //更新价格和库存
                            productFetchService.updatePriceAndStock(sku);
                        } else{
                            e.printStackTrace();
                        }

                    } catch (ServiceException e1) {
                        e1.printStackTrace();
                    }
                }

            }



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
