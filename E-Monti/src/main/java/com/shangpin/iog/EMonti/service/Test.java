package com.shangpin.iog.EMonti.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.EMonti.axisclient.MagentoService;
import com.shangpin.iog.EMonti.axisclient.MagentoServiceLocator;
import com.shangpin.iog.EMonti.axisclient.PortType;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;

public class Test {

	public static void main(String[] args) {
		try {
			MagentoService magentoService = new MagentoServiceLocator();		
			PortType portType =magentoService.getPort();
			String login = portType.login("soapws", "soap22WS!@#");
//			HashMap<String,String>[] attribute_setMap = (HashMap<String,String>[])portType.call(login, "product_attribute_set.list",null);
			
//			Map categoryMp = new HashMap();
//			categoryMp.put("sessionId", login);
//			
//			HashMap[] category_attributeMapArray = (HashMap[])portType.call(login,"catalog_category_attribute.list",categoryMp);
//			
//			for(HashMap map : category_attributeMapArray ){
//				System.out.println("catory " + map.toString());
//				if(null==map.get("attribute_id")) continue;
//				Map attributMap = new HashMap();
//				attributMap.put("sessionId", login);
//				attributMap.put("attributeId",map.get("code"));
//				attributMap.put("storeView","code");
//				Object oj = portType.call(login,"catalog_category_attribute.options",attributMap);
//			}
//			
			
			Map attributMap1 = new HashMap();
			attributMap1.put("storeView","");
			attributMap1.put("identifierType", "sku");
			attributMap1.put("product","111580");
			
			try {
				HashMap[] oj = (HashMap[])portType.call(login,"catalog_product_attribute_media.list",attributMap1);
				System.out.println(oj); 
			} catch (Exception e) {
//				System.out.println((String)map.get("code") + map.get("attribute_id") + "  not find"); 
				// TODO: handle exception
				
				e.printStackTrace();
			}
			
			
			
			Map<String,Object> paramatt = new HashMap<String,Object>();
			paramatt.put("setId", "4");				
			HashMap[] product_attribute = (HashMap[])portType.call(login, "product_attribute.list",paramatt);
			List filters = new ArrayList<>();
			for(HashMap map : product_attribute){
				if(null==map.get("attribute_id")) continue;
				if("92".equals(map.get("attribute_id"))||"150".equals(map.get("attribute_id"))){
					 // 92 color; 150 materiale
				}else{
					continue;
				}
				
				filters.add(map.get("attribute_id"));
				
				Map attributMap = new HashMap();
				attributMap.put("sessionId", login);
				attributMap.put("attributeId",map.get("attribute_id"));
				attributMap.put("storeView","");
				try {
					Object oj = portType.call(login,"catalog_product_attribute.options",attributMap);
					System.out.println(oj); 
				} catch (Exception e) {
//					System.out.println((String)map.get("code") + map.get("attribute_id") + "  not find"); 
					// TODO: handle exception
					
					e.printStackTrace();
				}
				
			}
							
			@SuppressWarnings("rawtypes")
			HashMap[] objects = (HashMap[])portType.call(login, "catalog_product.list",null);
			System.out.println(objects.length); 
			for(Map map : objects){	
				try {
					System.out.println("product_id==="+map.get("product_id").toString()); 
					Map<String,Object> param = new HashMap<String,Object>();
//						param.put("sessionId", login);
					param.put("product",map.get("product_id").toString());
					param.put("storeView", "");
					param.put("attributes", filters.toArray());
					param.put("productIdentifierType", "ID");
					HashMap product = (HashMap)portType.call(login, "catalog_product.info", param);
					
					//获取价格和库存
//					String skuStock = "";
//					Map stockMap = new HashMap();
//					String[] aaa = new String[1];
//					aaa[0] = map.get("sku").toString();
//					stockMap.put("productIds", aaa);
//					HashMap[] oo = (HashMap[])portType.call(login, "cataloginventory_stock_item.list", stockMap);
//					for(HashMap stock : oo){
//						skuStock  = stock.get("qty").toString();
//					}
					
					//获取品类
//					String gender = "";
//					String[] category_ids = (String[])product.get("category_ids");
//					String category = "";
//					if(null != category_ids && category_ids.length>0){
//						for(String catid :category_ids){
//							Map categoryMap = new HashMap();
//							categoryMap.put("categoryId", catid);
//							HashMap categoryObj = (HashMap)portType.call(login,"catalog_category.info",categoryMap);
//							if(!category.equals(categoryObj.get("name").toString())){
//								category = category + " "+categoryObj.get("name").toString();
//							}
//							if("men".equals(categoryObj.get("name").toString().toLowerCase()) || "women".equals(categoryObj.get("name").toString().toLowerCase())){
//								gender = categoryObj.get("name").toString().toLowerCase();
//							}
//						}
//						
//					}
//					String[] colors = new String[1];
//					colors[0] = product.get("color").toString();
//					Map attributMap = new HashMap();
//					attributMap.put("sessionId", login);
//					attributMap.put("attributeId",product.get("color").toString());
//					attributMap.put("storeView","");
//					Object oj = portType.call(login,"product_attribute.options",attributMap);
//					System.out.println(oj); 
//					sku.setColor(product.get("color").toString());
//					sku.setProductDescription(product.get("description").toString());
//					sku.setProductSize(product.get("dimension").toString());///不用翻译
//					
//					spu.setBrandName(product.get("aw_shopbybrand_brand").toString());
//					spu.setMaterial(product.get("materiale").toString());
//					spu.setProductOrigin(product.get("manufacturer").toString());
					
					
				} catch (Exception e) {
					e.printStackTrace(); 
					
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
