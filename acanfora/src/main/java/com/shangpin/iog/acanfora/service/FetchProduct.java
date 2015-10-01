package com.shangpin.iog.acanfora.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.acanfora.dto.Item;
import com.shangpin.iog.acanfora.dto.Items;
import com.shangpin.iog.acanfora.dto.Product;
import com.shangpin.iog.acanfora.dto.Products;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.util.*;

/**
 * Created by loyalty on 15/6/8.
 */
@Component("acanfora")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());
    private static Logger logMongo = Logger.getLogger("mongodb");
    @Autowired
    ProductFetchService productFetchService;

    public void fetchProductAndSave(String url){

        String supplierId = "2015050800242";
        try {
            Map<String,String> mongMap = new HashMap<>();
            OutTimeConfig timeConfig = OutTimeConfig.defaultOutTimeConfig();
            timeConfig.confRequestOutTime(600000);
            timeConfig.confSocketOutTime(600000);
            String result = HttpUtil45.get(url, timeConfig, null);
            HttpUtil45.closePool();
            mongMap.put("supplierId",supplierId);
            mongMap.put("supplierName","acanfora");
            mongMap.put("result",result) ;
            logMongo.info(mongMap);
            Products products= ObjectXMLUtil.xml2Obj(Products.class, result);
            List<Product> productList = products.getProducts();
            for(Product product:productList){
                SpuDTO spu = new SpuDTO();
                System.out.println(product);

                Items items = product.getItems();
                if(null==items){//无SKU
                    continue;
                }

                List<Item> itemList = items.getItems();
                if(null==itemList) continue;
                String skuId = "";
                for(Item item:itemList){
//                    SkuDTO sku  = new SkuDTO();
//                    try {
//                        sku.setId(UUIDGenerator.getUUID());
//                        sku.setSupplierId(supplierId);
//
//                        sku.setSpuId(product.getProductId());
//                        skuId = item.getItem_id();
//                        if(skuId.indexOf("½")>0){
//                            skuId = skuId.replace("½","+");
//                        }
//                        sku.setSkuId(skuId);
//                        sku.setProductSize(item.getItem_size());
//                        sku.setMarketPrice(item.getMarket_price());
//                        sku.setSalePrice(item.getSell_price());
//                        sku.setSupplierPrice(item.getSupply_price());
//                        sku.setColor(item.getColor());
//                        sku.setProductDescription(item.getDescription());
//                        sku.setStock(item.getStock());
//                        sku.setProductCode(product.getProducer_id());
//                        productFetchService.saveSKU(sku);
//
//                        if(StringUtils.isNotBlank(item.getPicture())){
//                            String[] picArray = item.getPicture().split("\\|");
//
////                            List<String> picUrlList = Arrays.asList(picArray);
//                            for(String picUrl :picArray){
//                                ProductPictureDTO dto  = new ProductPictureDTO();
//                                dto.setPicUrl(picUrl);
//                                dto.setSupplierId(supplierId);
//                                dto.setId(UUIDGenerator.getUUID());
//                                dto.setSkuId(item.getItem_id());
//                                try {
////                                    productFetchService.savePicture(dto);
//                                    productFetchService.savePictureForMongo(dto);
//                                } catch (ServiceException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//
//                        }
//
//                    } catch (ServiceException e) {
//                        try {
//                            if(e.getMessage().equals("数据插入失败键重复")){
//                                //更新价格和库存
//                                productFetchService.updatePriceAndStock(sku);
//                            } else{
//                                e.printStackTrace();
//                            }
//
//                        } catch (ServiceException e1) {
//                            e1.printStackTrace();
//                        }
//                    }
                }

//                try {
//                    spu.setId(UUIDGenerator.getUUID());
//                    spu.setSupplierId(supplierId);
//                    spu.setSpuId(product.getProductId());
//                    spu.setBrandName(product.getProduct_brand());
//                    spu.setCategoryName(product.getCategory());
//                    spu.setSpuName(product.getProduct_name());
//                    spu.setSeasonId(product.getSeason_code());
//                    spu.setMaterial(product.getProduct_material());
//                    productFetchService.saveSPU(spu);
//                } catch (ServiceException e) {
//                    e.printStackTrace();
//                }


            }


        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

}
