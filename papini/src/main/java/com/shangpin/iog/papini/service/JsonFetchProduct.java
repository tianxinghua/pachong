package com.shangpin.iog.papini.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.papini.jsonDTO.Item;
import com.shangpin.iog.papini.jsonDTO.Product;
import com.shangpin.iog.papini.jsonDTO.Products;
import com.shangpin.product.AbsSaveProduct;
 
@Component("jsonFetchProduct")
public class JsonFetchProduct extends AbsSaveProduct{

	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	private static String jsonUrl = null;
	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		jsonUrl = bdl.getString("jsonUrl");
	}
	
	@Override
	public Map<String, Object> fetchProductAndSave() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String,List<String>> imageMap = new HashMap<String, List<String>>();
		try {
			String jsonResult = HttpUtil45.get(jsonUrl, new OutTimeConfig(1000*60*10, 1000*60*30, 1000*60*30), null);
			Products products = new Gson().fromJson(jsonResult, Products.class);
			if(null != products){
				logger.info("拉取的spu总数======="+products.getProduct().size());
				for(Product product :products.getProduct()){
					try {
						SpuDTO spu = new SpuDTO();
						spu.setId(UUIDGenerator.getUUID());
						spu.setSupplierId(supplierId);
						spu.setSpuId(product.getProduct_id());
						spu.setBrandName(product.getProducer_id());
						spu.setCategoryGender(product.getType());
						spu.setCategoryName(product.getCategory());
						spu.setMaterial(product.getProduct_Material());
						spu.setSeasonName(product.getSeason());						
						spu.setProductOrigin(product.getProduct_MadeIn());
						spuList.add(spu);
						logger.info("拉取的sku总数==========="+product.getItems().getItem().size()); 
						for(Item item : product.getItems().getItem()){
							try {
								SkuDTO sku = new SkuDTO();
								sku.setId(UUIDGenerator.getUUID());
								sku.setSupplierId(supplierId);
								sku.setSpuId(spu.getSpuId());//								
								sku.setSkuId(item.getItem_id());
								sku.setBarcode(item.getBarcode()); 
								sku.setProductCode(product.getProducer_id()+"-"+item.getColor());
								sku.setColor(item.getColor());								
								sku.setMarketPrice("");
								sku.setSalePrice("");
								sku.setSupplierPrice(product.getSupply_price());								
								sku.setProductSize(item.getItem_size());								
								sku.setStock(item.getStock());
								sku.setSaleCurrency("EURO");
								skuList.add(sku);
								//图片
								imageMap.put(sku.getSkuId()+";"+sku.getProductCode(), item.getPictures());
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}else{
				loggerError.error("拉取数据为空，Products为空"); 
			}
			
			
			
		} catch (Exception e) {
			loggerError.error(e.toString()); 
		}
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;	
	}

}
