package com.shangpin.iog.dellogliostore.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dellogliostore.dto.Feed;
import com.shangpin.iog.dellogliostore.dto.SkuItem;
import com.shangpin.iog.dellogliostore.dto.SkuItems;
import com.shangpin.iog.dellogliostore.dto.SpuItem;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Component("dellogliostore")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());
    private static Logger logMongo = Logger.getLogger("mongodb");
    @Autowired
    ProductFetchService productFetchService;

    public void fetchProductAndSave(String url) {

//        String supplierId = "2015092501047"; //测试
        String supplierId = "2015092401530"; //正式
        try {
            Map<String, String> mongMap = new HashMap<>();
            OutTimeConfig timeConfig = OutTimeConfig.defaultOutTimeConfig();
            timeConfig.confRequestOutTime(600000);
            timeConfig.confSocketOutTime(600000);
            String result = HttpUtil45.get(url, timeConfig, null);
            HttpUtil45.closePool();

            Feed feed = ObjectXMLUtil.xml2Obj(Feed.class, result);
            System.out.println(feed);

            if (feed == null || feed.getSpuItems() == null) {
                return;
            }

            int count = 0;
            for (SpuItem spuItem : feed.getSpuItems().getItems()) {
                if (spuItem == null) {
                    continue;
                }
                System.out.println("count : " + ++count);
                System.out.println("spuItem : " + spuItem);

                String spuId = spuItem.getSpuId();

                //保存SPU
                SpuDTO spu = new SpuDTO();
                //SPU 必填
                spu.setId(UUIDGenerator.getUUID());
                spu.setSpuId(spuId);
                spu.setSupplierId(supplierId);
                spu.setCategoryName(spuItem.getCategory());
                spu.setBrandName(spuItem.getBrand());
                spu.setMaterial(spuItem.getMaterial());

                //SPU 选填
                spu.setSpuName(spuItem.getName());
                spu.setCategoryGender(spuItem.getSex());


                try {
                    productFetchService.saveSPU(spu);
                } catch (ServiceException e) {
                    e.printStackTrace();
                }

                SkuItems skuItems = spuItem.getSkuItems();
                if (skuItems == null || skuItems.getSkuItems() == null) {
                    continue;
                }

                for (SkuItem skuItem : skuItems.getSkuItems()) {
                    String skuId = spuId + skuItem.getSize();
                    SkuDTO sku = new SkuDTO();

                    //SKU 必填
                    sku.setId(UUIDGenerator.getUUID());
                    sku.setSupplierId(supplierId);
                    sku.setSkuId(skuId);
                    sku.setSpuId(spuId);
                    sku.setMarketPrice(spuItem.getPrice());
                    sku.setColor(spuItem.getName());
                    sku.setProductSize(skuItem.getSize());
                    sku.setStock(skuItem.getStock());
                    sku.setProductDescription(spuItem.getDescription());

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
                }

                //没有图片, BD自己传
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
