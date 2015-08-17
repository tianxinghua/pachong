package com.shangpin.iog.ostore.service;

import com.shangpin.framework.ServiceException;

import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by loyalty on 15/6/8.
 */
@Component("ostore")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());
    private static Logger logMongo = Logger.getLogger("mongodb");
    @Autowired
    private ProductFetchService productFetchService;

    public void fetchProductAndSave(String url){

        String supplierId = "2015081401431";
        try {
            Map<String,String> mongMap = new HashMap<>();
            OutTimeConfig timeConfig = OutTimeConfig.defaultOutTimeConfig();
            timeConfig.confRequestOutTime(360000);
            timeConfig.confConnectOutTime(36000);
            timeConfig.confSocketOutTime(360000);
            List<String> resultList = HttpUtil45.getContentListByInputSteam(url, timeConfig, null, null, null);
            HttpUtil45.closePool();
            mongMap.put("supplierId",supplierId);
            mongMap.put("supplierName","acanfora");
            mongMap.put("result",resultList.toString()) ;
            try {
                logMongo.info(mongMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            int i=0;
            String stock="",size ="";
            String skuId = "";
            for(String content:resultList){
                if(i==0){
                    i++;
                    continue;
                }
                i++;
                //SKU;Brand;ModelName;Color;ColorFilter;Description;Materials;Sex;Category;Season;Price;Discount;Images;SizesFormat;Sizes
               // 0 ;  1   ;  2      ;3    ;   4       ;    5      ;6        ;7  ;  8     ;  9   ;10   ; 11     ;  12  ;  13       ; 14
                String[] contentArray = content.split(";");
                if(null==contentArray||contentArray.length<15) continue;
                try {
                    SpuDTO spu = new SpuDTO();
                    spu.setId(UUIDGenerator.getUUID());
                    spu.setSupplierId(supplierId);
                    spu.setSpuId(contentArray[0]);
                    spu.setBrandName(contentArray[1]);
                    spu.setCategoryName(contentArray[8]);
                    spu.setSpuName(contentArray[2]);
                    spu.setSeasonId(contentArray[9]);
                    spu.setMaterial(contentArray[6]);
                    spu.setCategoryGender(contentArray[7]);
                    System.out.println(spu.getCategoryGender());
                    try{
                        productFetchService.saveSPU(spu);
                    }catch (ServiceException e){
                        e.printStackTrace();
                    }


                    String[] sizeArray = contentArray[14].split(",");

                    for(String sizeAndStock:sizeArray){
                        if(sizeAndStock.contains("(")&&sizeAndStock.length()>1) {
                            size = sizeAndStock.substring(0, sizeAndStock.indexOf("("));
                            stock = sizeAndStock.substring(sizeAndStock.indexOf("(")+1, sizeAndStock.length() - 1);
                            //System.out.println("库存"+stock);
                        }
                        SkuDTO sku  = new SkuDTO();
                        try {
                            sku.setId(UUIDGenerator.getUUID());
                            sku.setSupplierId(supplierId);

                            sku.setSpuId(contentArray[0]);
                            skuId = contentArray[0] + "-"+size;
                            if(skuId.indexOf("½")>0){
                                skuId = skuId.replace("½","+");
                            }
                            sku.setSkuId(skuId);
                            sku.setProductSize(size);
                            sku.setMarketPrice(contentArray[10]);
                            sku.setColor(contentArray[3]);
                            sku.setProductDescription(contentArray[5]);
                            sku.setStock(stock);

//                            sku.setProductCode(product.getProducer_id());
                            if(Integer.valueOf(stock)!=0){
                                productFetchService.saveSKU(sku);
                            }else {
                                continue;
                            }

                            String[] picArray = contentArray[12].split(",");

                            for(String picUrl :picArray){
                                ProductPictureDTO dto  = new ProductPictureDTO();
                                dto.setPicUrl(picUrl);
                                dto.setSupplierId(supplierId);
                                dto.setId(UUIDGenerator.getUUID());
                                dto.setSkuId(skuId);
                                try {
//                                    productFetchService.savePicture(dto);
                                    productFetchService.savePictureForMongo(dto);
                                } catch (ServiceException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            try {
                                if(e.getMessage().equals("数据插入失败键重复")){
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
