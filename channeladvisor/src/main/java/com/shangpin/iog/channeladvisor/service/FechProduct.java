package com.shangpin.iog.channeladvisor.service;

import java.util.ResourceBundle;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
import com.shangpin.iog.service.ProductFetchService;

@Component("channeladvisor")
public class FechProduct {

	private static Logger logInfo  = Logger.getLogger("info");
	private static Logger logError = Logger.getLogger("error");
	private static Logger logMongoDB = Logger.getLogger("MongoDB");
	
	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static String access_token = "";
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		access_token = bdl.getString("conf.access.token");
	}

	@Autowired
	public ProductFetchService productFetchService;

	public void fetchProductAndSave() {

		String url = "https://api.channeladvisor.com/v1/Products?access_token="+access_token;
		OutTimeConfig timeConfig = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);

		try{
			
			//方式1
			String content = HttpUtil45.get(url, timeConfig, null);
			//logInfo.info(content);
			//System.out.println(content);
			JSONObject jsonObj = JSONObject.fromObject(content);
			JSONArray array = jsonObj.getJSONArray("value");
			for(int i=0;i<array.size();i++){
				JSONObject obj = array.getJSONObject(i);
				
				String categoryName = obj.getString("Classification");
				String brandName = obj.getString("Brand");
				String categoryGender = "";
				String material = "";
				
				String skuId = obj.getString("Sku");
				String stock = obj.getString("TotalAvailableQuantity");
				String color = "";
				String productName = "";
				String productCode = "";
				String productSize = "";
				
				String id = obj.getString("ID");
				String attributeUrl = "https://api.channeladvisor.com/v1/Products("+id+")/Attributes?access_token="+access_token;
				String attributes = HttpUtil45.get(attributeUrl, timeConfig, null);
				JSONObject attrObj = JSONObject.fromObject(attributes);
				JSONArray attrArr = attrObj.getJSONArray("value");
				
				for(int j=0;j<attrArr.size();j++){
					JSONObject attr= attrArr.getJSONObject(j);
					switch (attr.getString("Name")) {
					case "Color":
						color = attr.getString("Value");
						break;
					case "ebay title":
						productName = attr.getString("Value");
						break;
					case "Size":
						productSize = attr.getString("Value");
						break;				
					case "Gender":
						categoryGender = attr.getString("Value");
						break;
					case "Material":
						material = attr.getString("Value");
						break;
					case "UPC":
						productCode = attr.getString("Value");
					default:
						break;
					}
				}
				//图片
				String picUrl = "https://api.channeladvisor.com/v1/Products("+id+")/Images?access_token="+access_token;
				String pics = HttpUtil45.get(picUrl, timeConfig, null);
				JSONArray picArr = JSONObject.fromObject(pics).getJSONArray("value");
				for(int k=0;k<picArr.size();k++){
					ProductPictureDTO dto = new ProductPictureDTO();
					dto.setId(UUIDGenerator.getUUID());
					dto.setPicUrl(picArr.getJSONObject(k).getString("Url"));
					dto.setSkuId(skuId);
					dto.setSpuId(id);
					dto.setSupplierId(supplierId);
					try {
	                    productFetchService.savePictureForMongo(dto);
	                } catch (ServiceException e) {
	                    e.printStackTrace();
	                }
				}
				
				//入库sku
				SkuDTO sku = new SkuDTO();
	            sku.setId(UUIDGenerator.getUUID());
	            sku.setSupplierId(supplierId);
	            sku.setSkuId(skuId);
	            sku.setSpuId(id);
	            sku.setColor(color);
	            sku.setProductCode(productCode);
	            sku.setProductName(productName);
	            sku.setProductSize(productSize);
	            sku.setStock(stock);
	            sku.setSupplierId(supplierId);
	            sku.setSupplierPrice("");
	            sku.setSaleCurrency("USD");
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
	                	logError.error(e1.getMessage());
	                    e1.printStackTrace();
	                }
	            }
	            
	            //入库spu
	            SpuDTO spu = new SpuDTO();
	            spu.setId(UUIDGenerator.getUUID());
	            spu.setSpuId(id);
	            spu.setSupplierId(supplierId);
	            spu.setBrandName(brandName);
	            spu.setCategoryGender(categoryGender);
	            spu.setCategoryName(categoryName);
	            spu.setMaterial(material);
	            try {
	                productFetchService.saveSPU(spu);
	            } catch (ServiceException e) {
	            	logError.error(e.getMessage());
	                e.printStackTrace();
	            }
	            
			}
			
		}catch(Exception ex){
			logError.error(ex.getMessage());
			ex.printStackTrace();
		}
		

	}
}
