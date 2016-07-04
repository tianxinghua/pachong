package com.shangpin.iog.optical.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.optical.dto.Item;
import com.shangpin.iog.optical.excel.ReadExcel;
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
	
	public Map<String, Object> fetchProductAndSave() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String,List<String>> imageMap = new HashMap<String, List<String>>();
		
		try{
			//具体的业务
			ReadExcel.downLoadFile(url, filepath);
			List<Item> items = ReadExcel.readExcel(Item.class, filepath);
			for(Item item : items){
				try {
					
					SpuDTO spu = new SpuDTO();
					SkuDTO sku = new SkuDTO();
					
					sku.setId(UUIDGenerator.getUUID());
					sku.setSupplierId(supplierId);
					sku.setSkuId(item.getSkuNo()+"-"+item.getProductSize());
					sku.setSpuId(item.getSkuNo());
					sku.setProductName(item.getProductName());
					sku.setMarketPrice(item.getMarkerPrice());
					sku.setSalePrice("");
					sku.setSupplierPrice(item.getSupplierPrice());
					sku.setBarcode(item.getSkuNo()+"-"+item.getProductSize());
					sku.setProductCode(item.getProductModel());
					sku.setColor(item.getProductColor());
					sku.setProductDescription(item.getProductDescription());
					sku.setSaleCurrency(item.getCurrency());
					sku.setProductSize(item.getProductSize());
					sku.setStock(item.getStock());
					skuList.add(sku);
					System.out.println(sku.getSkuId());
					spu.setId(UUIDGenerator.getUUID()); 
					spu.setSupplierId(supplierId);
					spu.setSpuId(sku.getSpuId());
					spu.setCategoryGender(item.getGender());
					spu.setCategoryName(item.getCategoryName());
					spu.setBrandName(item.getBrandName());
					spu.setSeasonName(item.getSeason());
					spu.setMaterial(item.getMaterial());
					spu.setProductOrigin(item.getProductOrigin());
					spuList.add(spu);
					
					List<String> list = new ArrayList<String>();
					if(StringUtils.isNotBlank(item.getProductUrl1())){
						list.add(item.getProductUrl1());
					}
					if(StringUtils.isNotBlank(item.getProductUrl2())){
						list.add(item.getProductUrl2());
					}
					if(StringUtils.isNotBlank(item.getProductUrl3())){
						list.add(item.getProductUrl3());
					}
					imageMap.put(spu.getSpuId()+";"+sku.getProductCode(), list);
					
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

