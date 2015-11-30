package com.shangpin.iog.linoricci.service;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.linoricci.dto.Item;
import com.shangpin.iog.service.ProductFetchService;

/**
 * Created by houkun on 2015/11/26.
 */
@Component("linoricci")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static String supplierId;
    private static String url;
    private static ResourceBundle bdl=null;
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        url = bdl.getString("url");
    }
    @Autowired
    private ProductFetchService productFetchService;

    /**
     * 得到产品信息并储存
     */
    public void fetchProductAndSave(){
    	Map<String,Item> spuMap= new HashMap<String,Item>();
    	Map<String,String> imgMap= new HashMap<String,String>();
        //获取产品信息
        logger.info("get product starting....");
    	String spuData = HttpUtil45.post(url+"GetAllItemsMarketplace",
    										new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10));
    	String skuData = HttpUtil45.post(url+"GetAllAvailabilityMarketplace",
    										new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10));
    	String imageData = HttpUtil45.post(url+"GetAllImageMarketplace",
    										new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10));
    	
    	
        logger.info("get product over");
        //映射数据并保存
        logger.info("save product into DB begin");
        //得到所有的spu信息
        String data = "";
        String[] spuStrings = spuData.split("\\r\\n");
		for (int i = 1; i < spuStrings.length; i++) {
			if (StringUtils.isNotBlank(spuStrings[i])) {
				if (i==1) {
				  data =  spuStrings[i].split("\\n")[1];
				}else {
				  data = spuStrings[i];
				}
				String[] spuArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
				SpuDTO spu = new SpuDTO();
				Item item = new Item();
				  try {
					   item.setColor(spuArr[10]);
					   item.setMarketPrice(spuArr[16]);
					   item.setSalePrice(spuArr[16]);
					   item.setSupplierPrice(spuArr[16]);
					   item.setDescription(spuArr[15]);
					   item.setSpuId(spuArr[0]);
					   spuMap.put(spuArr[0], item);

					   spu.setId(UUIDGenerator.getUUID());
		               spu.setSupplierId(supplierId);
		               spu.setSpuId(spuArr[0]);
		               spu.setBrandName(spuArr[2]);
		               spu.setCategoryName(spuArr[8]);
		               //spu.setSpuName(fields[0]);
		               spu.setSeasonId(spuArr[6]);
		               if (StringUtils.isNotBlank(spuArr[11])) {
		            	   spu.setMaterial(spuArr[11]);
		               }else {
		            	   spu.setMaterial(spuArr[15]);
		               }
		               spu.setCategoryGender(spuArr[5]);
		               spu.setProductOrigin(spuArr[40]);
		               productFetchService.saveSPU(spu);
		           } catch (ServiceException e) {
		               e.printStackTrace();
		           }
			}
		}
		//处理sku信息
		//处理图片信息
		String[] imageStrings = imageData.split("\\r\\n");
		for (int j = 1; j < imageStrings.length; j++) {
			if (StringUtils.isNotBlank(imageStrings[j])) {
				if (j==1) {
				  data =  imageStrings[j].split("\\n")[1];
				}else {
				  data = imageStrings[j];
				}
				
				String[] imageArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
				if (!imgMap.containsKey(imageArr[0])) {
					imgMap.put(imageArr[0], imageArr[1]);
				}else {
					imgMap.put(imageArr[0],imgMap.get(imageArr[0])+","+imageArr[1]);
				}
			}
		}
		String[] skuStrings = skuData.split("\\r\\n");
		for (int i = 1; i < skuStrings.length; i++) {
			if (StringUtils.isNotBlank(skuStrings[i])) {
				if (i==1) {
				  data =  skuStrings[i].split("\\n")[1];
				}else {
				  data = skuStrings[i];
				}
				String[] skuArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
				if (spuMap.containsKey(skuArr[0])) {
					Item item = spuMap.get(skuArr[0]);
					SkuDTO sku = new SkuDTO();
					sku.setId(UUIDGenerator.getUUID());
        			sku.setSupplierId(supplierId);
        			sku.setSpuId(skuArr[0]);
        			//sku.setSkuId(skuId);
        			sku.setProductSize(skuArr[1]);
        			sku.setMarketPrice(item.getMarketPrice());
        			sku.setSalePrice(item.getSalePrice());
        			sku.setSupplierPrice(item.getSupplierPrice());
        			sku.setColor(item.getColor());
        			sku.setProductDescription(item.getDescription());
        			sku.setSaleCurrency("EURO");
        			String stock = skuArr[2];
        			String barCode = skuArr[5];
        			sku.setStock(stock);
        			//skuid+barcode
        			sku.setSkuId(skuArr[0]+"-"+barCode);
        			sku.setBarcode(barCode);
        			sku.setProductCode(skuArr[0]);
        			// sku.setProductName(fields[14]);
        			try {
						productFetchService.saveSKU(sku);
						//处理图片
						String imgUrls = imgMap.get(skuArr[0]);
						if (StringUtils.isNotBlank(imgUrls)) {
							String[] imgUrlArr = imgUrls.split(",");
							for (String imgurl : imgUrlArr) {
								ProductPictureDTO dto = new ProductPictureDTO();
								dto.setPicUrl(imgurl);
								dto.setSupplierId(supplierId);
								dto.setId(UUIDGenerator.getUUID());
								dto.setSkuId(skuArr[0]+"-"+barCode);
								try {
									productFetchService.savePictureForMongo(dto);
								} catch (ServiceException e) {
									e.printStackTrace();
								}
							}
						}
						
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
			}
		}
        logger.info("save product into DB success");
    }

    public static void main(String[] args){
        new FetchProduct().fetchProductAndSave();
    }
}
