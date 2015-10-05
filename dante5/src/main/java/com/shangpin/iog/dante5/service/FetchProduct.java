package com.shangpin.iog.dante5.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dante5.dto.Item;
import com.shangpin.iog.dante5.dto.Rss;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
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
import java.util.ResourceBundle;

/**
 *
 */
@Component("dante5")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());
    private static Logger logMongo = Logger.getLogger("mongodb");
    @Autowired
    ProductFetchService productFetchService;

    private static ResourceBundle bdl=null;
    private static String supplierId;

    static {
        if(null==bdl)
            bdl= ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }

    public void fetchProductAndSave(String url) {
        try {
            Map<String, String> mongMap = new HashMap<>();
            OutTimeConfig timeConfig = OutTimeConfig.defaultOutTimeConfig();
            timeConfig.confRequestOutTime(600000);
            timeConfig.confSocketOutTime(600000);
            String result = HttpUtil45.get(url, timeConfig, null);
            HttpUtil45.closePool();

            System.out.println("result : " + result);

            if (result == null || "".equals(result)) {
                return;
            }

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

            int count = 0;
            for (Item item : rss.getChannel().getItem()) {
                if (item == null) {
                    continue;
                }
                System.out.println("count : " + ++count);

                String skuId = item.getId() + item.getSize(); //接口中g:id是spuId,对应不同尺码
                String spuId = item.getId();
                SkuDTO sku = new SkuDTO();

                //SKU 必填
                sku.setId(UUIDGenerator.getUUID());
                sku.setSupplierId(supplierId);
                sku.setSkuId(skuId);
                sku.setSpuId(spuId);
                sku.setMarketPrice(item.getPrice());
                sku.setColor(item.getColor());
                sku.setProductSize(item.getSize());
                sku.setStock(item.getAvailability());

                //SKU 选填
                sku.setProductName(item.getTitle());
                sku.setProductDescription(item.getDescription());
                sku.setProductCode(item.getMpn());

                try {
                    productFetchService.saveSKU(sku);
                } catch (ServiceException e) {
                    try {
                        if (e.getMessage().equals("数据插入失败键重复")) {
                            //更新价格和库存
                            productFetchService.updatePriceAndStock(sku);
                        } else {
                            e.printStackTrace();
                        }
                    } catch (ServiceException e1) {
                        e1.printStackTrace();
                    }
                }

                //保存图片
                if (item.getImageLinks() != null) {
                    for (String imageUrl : item.getImageLinks().getLinks()) {
                        if (imageUrl != null && !"".equals(imageUrl)) {
                            ProductPictureDTO dto = new ProductPictureDTO();
                            dto.setPicUrl(imageUrl);
                            dto.setSupplierId(supplierId);
                            dto.setId(UUIDGenerator.getUUID());
                            dto.setSkuId(skuId);
                            try {
                                productFetchService.savePictureForMongo(dto);
                            } catch (ServiceException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                //保存SPU
                SpuDTO spu = new SpuDTO();
                //SPU 必填
                spu.setId(UUIDGenerator.getUUID());
                spu.setSpuId(spuId);
                spu.setSupplierId(supplierId);
                spu.setCategoryName(item.getProductType());
                spu.setBrandName(item.getBrand());
                spu.setMaterial(item.getComposition());

                //SPU选填
                spu.setCategoryGender(item.getGender());

                try {
                    productFetchService.saveSPU(spu);
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
