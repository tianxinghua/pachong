package com.shangpin.iog.ostore.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by loyalty on 15/6/8.
 */
@Component("ostore")
public class FetchProduct {
    //final Logger logger = Logger.getLogger(this.getClass());
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static String grabStockUrl = "";
    private static int day;
    static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        grabStockUrl = bdl.getString("grabStockUrl");
        day = Integer.valueOf(bdl.getString("day"));
    }
    
    @Autowired
	ProductSearchService productSearchService;
    @Autowired
    private ProductFetchService productFetchService;



    public void fetchProductAndSave(String url){

        //String supplierId = "2015081401431";
        Date startDate,endDate= new Date();
		startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
		//获取原有的SKU 仅仅包含价格和库存
		Map<String,SkuDTO> skuDTOMap = new HashedMap();
		try {
			skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
        
        try {
            Map<String,String> mongMap = new HashMap<>();
            OutTimeConfig timeConfig = OutTimeConfig.defaultOutTimeConfig();
            timeConfig.confRequestOutTime(10*60*1000);
            timeConfig.confConnectOutTime(10*60*1000);
            timeConfig.confSocketOutTime(10*60*1000);
            List<String> resultList = HttpUtil45.getContentListByInputSteam(url, timeConfig, null, null, null);
            HttpUtil45.closePool();
            StringBuffer buffer =new StringBuffer();
            for(String content:resultList){
                buffer.append(content).append("|||");
            }
            mongMap.put("supplierId",supplierId);
            mongMap.put("supplierName","acanfora");
            mongMap.put("result", buffer.toString()) ;
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
                    	try {
    						productFetchService.updateMaterial(spu);
    					} catch (ServiceException e1) {
    						e1.printStackTrace();
    					}
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
                            sku.setProductSize(size.replace("½","+"));
                            sku.setMarketPrice(contentArray[10]);
                            sku.setColor(contentArray[3]);
                            sku.setProductDescription(contentArray[5]);
                            sku.setStock(stock);

//                            sku.setProductCode(product.getProducer_id());
                            
                            if(skuDTOMap.containsKey(sku.getSkuId())){
        						skuDTOMap.remove(sku.getSkuId());
        					}

                            productFetchService.saveSKU(sku);


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

                        //保存图片
                        String[] picArray = contentArray[12].split(",");
                        List<String> picLit = Arrays.asList(picArray);
                        productFetchService.savePicture(supplierId,null,skuId,picLit);

//                        for(String picUrl :picArray){
//                            ProductPictureDTO dto  = new ProductPictureDTO();
//                            dto.setPicUrl(picUrl);
//                            dto.setSupplierId(supplierId);
//                            dto.setId(UUIDGenerator.getUUID());
//                            dto.setSkuId(skuId);
//                            try {
//                                productFetchService.savePictureForMongo(dto);
//                            } catch (ServiceException e) {
//                                e.printStackTrace();
//                            }
//                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
