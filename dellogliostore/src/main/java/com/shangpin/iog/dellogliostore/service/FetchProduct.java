package com.shangpin.iog.dellogliostore.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
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
import com.shangpin.iog.service.ProductSearchService;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

/**
 *
 */
@Component("dellogliostore")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());
    private static Logger logMongo = Logger.getLogger("mongodb");
    @Autowired
    ProductFetchService productFetchService;
    @Autowired
    ProductSearchService productSearchService;

    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static int day;

    static {
        if(null==bdl)
            bdl= ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        day = Integer.valueOf(bdl.getString("day"));
    }

    public void fetchProductAndSave(String url) {

        try {
        	Date startDate,endDate= new Date();
            startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
            Map<String,SkuDTO> skuDTOMap = new HashedMap();
            try {
                skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
            Map<String, String> mongMap = new HashMap<>();
            OutTimeConfig timeConfig = new OutTimeConfig(1000*60*20, 1000*60*20,1000*60*20);
//            timeConfig.confRequestOutTime(600000);
//            timeConfig.confSocketOutTime(600000);
            String result = HttpUtil45.get(url, timeConfig, null);
            HttpUtil45.closePool();

            System.out.println("result : " + result);

            //Remove BOM from String
            if (result != null && !"".equals(result)) {
                result = result.replace("\uFEFF", "");
            }

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
                	try {
                        productFetchService.updateMaterial(spu);
                    } catch (ServiceException e1) {
                        e1.printStackTrace();
                    }
                }

                SkuItems skuItems = spuItem.getSkuItems();
                if (skuItems == null || skuItems.getSkuItems() == null) {
                    continue;
                }

                for (SkuItem skuItem : skuItems.getSkuItems()) {

                    //库存为0不进行入库
                    if (skuItem.getStock() == null || "".equals(skuItem.getStock().trim()) || "0".equals(skuItem.getStock().trim())) {
                        continue;
                    }

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

                    //SKU 选填
                    sku.setProductCode(spuItem.getProductCode());

                    try {
                    	if(skuDTOMap.containsKey(sku.getSkuId())){
                            skuDTOMap.remove(sku.getSkuId());
                        }
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
          //更新网站不再给信息的老数据
			for(Iterator<Map.Entry<String,SkuDTO>> itor = skuDTOMap.entrySet().iterator();itor.hasNext(); ){
				 Map.Entry<String,SkuDTO> entry =  itor.next();
				if(!"0".equals(entry.getValue().getStock())){//更新不为0的数据 使其库存为0
					entry.getValue().setStock("0");
					try {
						productFetchService.updatePriceAndStock(entry.getValue());
					} catch (ServiceException e) {
						e.printStackTrace();
					}
				}
			}

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
