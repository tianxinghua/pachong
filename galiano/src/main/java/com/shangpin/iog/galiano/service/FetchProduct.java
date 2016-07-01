package com.shangpin.iog.galiano.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.galiano.dto.Item;
import com.shangpin.iog.galiano.dto.Items;
import com.shangpin.iog.galiano.dto.Product;
import com.shangpin.iog.galiano.dto.Products;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

import org.apache.commons.collections.map.HashedMap;
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
@Component("galiano")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static String supplierId;
    private static String url;
	public static int day;
    private static ResourceBundle bdl=null;
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        url = bdl.getString("url");
        day = Integer.valueOf(bdl.getString("day"));
    }
    @Autowired
	ProductSearchService productSearchService;
    @Autowired
    ProductFetchService productFetchService;

    public void fetchProductAndSave(String url){

        String supplierId = "2015070301312";
        try {
            Map<String,String> mongMap = new HashMap<>();

            OutTimeConfig timeConfig = new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30);
            String result = HttpUtil45.get(url,timeConfig,null);
            mongMap.put("supplierId",supplierId);
            mongMap.put("supplierName","galiano");
            mongMap.put("result",result) ;
            logMongo.info(mongMap);
            Products products= ObjectXMLUtil.xml2Obj(Products.class, result);
            List<Product> productList = products.getProducts();
            
            Date startDate,endDate= new Date();
			startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
            Map<String,SkuDTO> skuDTOMap = new HashedMap();
			try {
				skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			
            for(Product product:productList){
                SpuDTO spu = new SpuDTO();


                Items items = product.getItems();
                if(null==items){//无SKU
                    continue;
                }

                List<Item> itemList = items.getItems();
                if(null==itemList) continue;
                String skuId="",size="";
                for(Item item:itemList){
                    SkuDTO sku  = new SkuDTO();
                    try {
                        sku.setId(UUIDGenerator.getUUID());
                        sku.setSupplierId(supplierId);
                        sku.setSpuId(product.getProductId());
                        skuId = item.getItem_id();
                        if(StringUtils.isNotBlank(skuId)&&skuId.indexOf("½")>0){
                            skuId = skuId.replace("½","+");
                        }
                        sku.setSkuId(skuId);
                        size = item.getItem_size();
                        if(StringUtils.isNotBlank(size)&&size.indexOf("½")>0){
                            size = size.replace("½","+");
                        }
                        sku.setProductSize(size);
                        sku.setMarketPrice(item.getMarket_price());
                        sku.setSalePrice(item.getSell_price());
                        sku.setSupplierPrice(item.getSupply_price());

                        sku.setColor(item.getColor());
                        sku.setProductDescription(item.getDescription());
                        sku.setStock(item.getStock());
                        sku.setProductCode(product.getProducer_id());
                        
                        if(skuDTOMap.containsKey(sku.getSkuId())){
    						skuDTOMap.remove(sku.getSkuId());
    					}
                        productFetchService.saveSKU(sku);

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
//                                    productFetchService.savePictureForMongo(dto);
//                                } catch (ServiceException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//
//                        }

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
                    
                    if(StringUtils.isNotBlank(item.getPicture())){
                        String[] picArray = item.getPicture().split("\\|");

                        List<String> picUrlList = Arrays.asList(picArray);
                        productFetchService.savePicture(supplierId, null, skuId, picUrlList);
                    }
                }

                try {
                    spu.setId(UUIDGenerator.getUUID());
                    spu.setSupplierId(supplierId);
                    spu.setSpuId(product.getProductId());
                    spu.setBrandName(product.getProduct_brand());
                    spu.setCategoryName(product.getCategory());
                    spu.setSpuName(product.getProduct_name());
                    String seasonId = product.getSeason_name().trim();
                    String season = "";
                    if(seasonId.equals("161")){
                    	season = "161 Primave estate 2016";
                    }else if (seasonId.equals("51")){
                    	season = "51 Autunno inverno 2015";
                    }else if (seasonId.equals("141")){
                    	season = "141 Primavera estate 2014";
                    }else if (seasonId.equals("151")){
                    	season = "151 Primavera estate 2015";
                    }else if(seasonId.equals("41")){
                    	season = "41 Autunno inverrno 2014";
                    }else if(seasonId.equals("31")){
                    	season = "31 Autunno inverrno 2013";
                    }else if(seasonId.equals("21")){
                    	season = "21 Autunno inverrno 2012";
                    }else if(seasonId.equals("131")){
                    	season = "131 Primavera estate 2013";
                    }
                    spu.setSeasonId(StringUtils.isNotBlank(season)? season:seasonId);
                    spu.setMaterial(StringUtils.isBlank(product.getProduct_material())?product.getDescription():product.getProduct_material());
                    spu.setCategoryGender(product.getGender());
                    productFetchService.saveSPU(spu);
                } catch (ServiceException e) {
                	try {
						productFetchService.updateMaterial(spu);
					} catch (ServiceException e1) {
						e1.printStackTrace();
					}
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


        } catch (JAXBException e) {
            e.printStackTrace();
        }finally {
            HttpUtil45.closePool();
        }

    }


}
