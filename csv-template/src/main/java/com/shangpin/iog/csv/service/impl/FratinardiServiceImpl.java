package com.shangpin.iog.csv.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;

@Service
public class FratinardiServiceImpl implements ProductSaveService{
	private static ResourceBundle bdl = null;
	private static String supplierId;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
	}
	
	@Override
	public Map<String, Object> getProductMap(JSONArray list) {
		
		if(list==null){
			return null;
		}
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String, List<String>> imageMap = new HashMap<String, List<String>>();
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> ite = list.iterator();
		while(ite.hasNext()){
			JSONObject product =ite.next();
			SkuDTO sku = new SkuDTO();
			sku.setId(UUIDGenerator.getUUID());
			sku.setSkuId(product.getString("sku No"));
			sku.setSupplierId(supplierId);
			sku.setSpuId(splitSupplierSpuNo(product.getString("product No")));
			sku.setProductCode(splitSupplierSpuNo(product.getString("product No")));
			sku.setColor(product.getString("color"));
			sku.setMarketPrice(product.getString("market price"));
			sku.setStock(product.getString("qty"));
			sku.setBarcode(product.getString("Barcode"));
			sku.setProductSize(product.getString("size"));
			sku.setProductName(product.getString("name"));
			sku.setProductDescription(product.getString("description"));
			skuList.add(sku);
			
			String image1 = product.getString("image1");
			String image2 = product.getString("image2");
			String image3 = product.getString("image3");
			String image4 = product.getString("image4");
			List<String> images = new ArrayList<String>();
			images.add(image1);
			images.add(image2);
			images.add(image3);
			images.add(image4);
			imageMap.put(sku.getSkuId() + ";" + sku.getProductCode(), images);
			
			SpuDTO spu = new SpuDTO();
			spu.setId(UUIDGenerator.getUUID());
			spu.setSpuId(splitSupplierSpuNo(product.getString("product No")));
			spu.setSupplierId(supplierId);
			spu.setBrandName(product.getString("brand"));
			spu.setCategoryName(product.getString("category"));
			spu.setCategoryGender(product.getString("Gender"));
			spu.setMaterial(product.getString("materials"));
			String desc = product.getString("description");
			String origin = null;
			if(desc!=null&&desc.contains("Made in")){
				String [] arr = desc.split("<br>");
				for(String s :arr){
					if(s.contains("Made in")){
						origin = s.substring(s.indexOf("Made in")+7).trim();
						break;
					}
				}
			}
			spu.setProductOrigin(origin);
			spuList.add(spu);
		}
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;
	}
	private static String splitSupplierSpuNo(String spuNo) {
		if(spuNo!=null){
			String [] arr = spuNo.split("-");
			if(arr.length>0){
				spuNo = arr[0];
				if(spuNo!=null){
					String [] arr2 = spuNo.split("_");
					if(arr2.length>0){
						spuNo = arr2[0];
					}
				}
			}
		}
		return spuNo;
	}
}
