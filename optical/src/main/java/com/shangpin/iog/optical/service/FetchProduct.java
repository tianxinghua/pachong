package com.shangpin.iog.optical.service;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.product.AbsSaveProduct;

/**
 * Created by usr on 2015/9/14.
 */

@Component("optical")
public class FetchProduct extends AbsSaveProduct{

	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String url = null;
	private static String filepath = null;
	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");	
		url = bdl.getString("excel.url");
		filepath = bdl.getString("excel.filepath");
	}
	  
	public String getValue(String value){
		
		if(StringUtils.isNotBlank(value)){
			return value;
		}else{
			return null;
		}
		
		
	}
	public Map<String, Object> fetchProductAndSave() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String,List<String>> imageMap = new HashMap<String, List<String>>();
		
		try{
			String xml = HttpUtil45.get("http://www.opticalscribe.com/spfiles/spfilexls.xml",new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30), null);
			List<List<String>> list = Utils.getListProduct(xml);
			for(List<String> item : list){
				try {
					
					SpuDTO spu = new SpuDTO();
					SkuDTO sku = new SkuDTO();
					
					sku.setId(UUIDGenerator.getUUID());
					sku.setSupplierId(supplierId);
					sku.setSkuId(getValue(item.get(4)));
					sku.setSpuId(getValue(item.get(3)));
					sku.setProductName(getValue(item.get(6)));
					sku.setMarketPrice(getValue(item.get(16)));
					sku.setSalePrice("");
					sku.setSupplierPrice(getValue(item.get(17)));
					sku.setBarcode(getValue(item.get(4))+"-"+getValue(item.get(8)));
					sku.setProductCode(getValue(item.get(3)));
					sku.setColor(getValue(item.get(7)));
					sku.setProductDescription(getValue(item.get(14)));
					sku.setSaleCurrency(getValue(item.get(18)));
					sku.setProductSize(getValue(item.get(8)));
					sku.setStock(getValue(item.get(15)));
					skuList.add(sku);
//					System.out.println(sku.getSkuId());
					spu.setId(UUIDGenerator.getUUID()); 
					spu.setSupplierId(supplierId);
					spu.setSpuId(sku.getSpuId());
					spu.setCategoryGender(getValue(item.get(5)));
					spu.setCategoryName(getValue(item.get(1)));
					spu.setBrandName(getValue(item.get(2)));
					spu.setSeasonName(getValue(item.get(19)));
					spu.setMaterial(getValue(item.get(9)));
					spu.setProductOrigin(getValue(item.get(10)));
					spuList.add(spu);
//					
					List<String> list1 = new ArrayList<String>();
					for(int i=13;i<=17;i++){
						if(item.size()==i){
							break;
						}
						String url = getValue(item.get(i));
						if(url!=null){
							list1.add(url);	
						}
						
					}
				
					imageMap.put(spu.getSpuId()+";"+sku.getProductCode(), list1);
					
				} catch (Exception e) {
					e.printStackTrace();
					loggerError.error(e); 
				}
				
				
			}
			
			//skuList.add(sku);
			//spuList.add(spu);
			/**
			List<String> list = new ArrayList<String>();
			imageMap.put(sku.getSkuId()+";"+sku.getProductCode()+" "+sku.getColor(), list);
			**/
			System.out.println("skuList========="+skuList.size());
			System.out.println("spuList========="+spuList.size());
			System.out.println("imageMap========"+imageMap.size()); 
		
		}catch(Exception ex){
			ex.printStackTrace();
			loggerError.error(ex);
		}
		
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;		
	}	
	
	public static void main(String[] args) {
		FetchProduct f = new FetchProduct();
		f.fetchProductAndSave();
	}
	
}

