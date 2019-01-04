package com.shangpin.iog.${supplierName}.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.product.AbsSaveProduct;
import com.shangpin.iog.${supplierName}.dto.Item;
import com.shangpin.iog.${supplierName}.util.CSVUtil;
import com.shangpin.iog.${supplierName}.util.DownLoad;

/**
 * Created by ${createdby} on 2015/9/14.
 */

@Component("${supplierName}")
public class FetchProduct extends AbsSaveProduct{

	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String csvurl = null;
	private static String csvfilepath = null;
	private static char csvsep;
	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("${profileName}");
		supplierId = bdl.getString("supplierId");
		csvurl = bdl.getString("csvurl");
		csvfilepath = bdl.getString("csvfilepath");
		csvsep = (char)bdl.getObject("csvsep");		
	}
	
	public Map<String, Object> fetchProductAndSave() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String,List<String>> imageMap = new HashMap<String, List<String>>();
		
		try{
			
			//具体的业务
			DownLoad.downFromNet(csvurl, csvfilepath);
			File file = new File(csvfilepath);
			List<Item> items = CSVUtil.readCSV(file, Item.class, csvsep);
			logger.info("拉取的item数量========"+items.size()); 
			for(Item item : items){
				try {
					
					SkuDTO sku = new SkuDTO();
					sku.setId(UUIDGenerator.getUUID());
	                sku.setSupplierId(supplierId);
	                sku.setSkuId(item.getSkuId());
	                sku.setSpuId(item.getSpuId());
	                sku.setProductName(item.getProductName());
	                sku.setMarketPrice(item.getMarketPrice());
	                sku.setSalePrice(item.getSalePrice());
	                sku.setSupplierPrice(item.getSupplierPrice());
	                //sku.setBarcode();
	                sku.setProductCode(item.getProductCode());
	                sku.setColor(item.getColor());
	                sku.setProductDescription(item.getProductDescription());
	                //sku.setSaleCurrency();
	                sku.setProductSize(item.getProductSize());
	                sku.setStock(item.getStock());  
	                skuList.add(sku);
	                
	                SpuDTO spu = new SpuDTO();
	                spu.setId(UUIDGenerator.getUUID());
	                spu.setSupplierId(supplierId);
	                spu.setSpuId(sku.getSpuId());
	                spu.setCategoryGender(item.getCategoryGender());
	                spu.setCategoryName(item.getCategoryName());
	                spu.setBrandName(item.getBrandName());
	                spu.setSeasonName(item.getSeasonName());
	                spu.setMaterial(item.getMaterial());
	                spu.setProductOrigin(item.getProductOrigin());
	                spuList.add(spu);
	                /**
	                //图片自己处理
	    			List<String> list = new ArrayList<String>();
	    			imageMap.put(sku.getSkuId()+";"+sku.getProductCode()+" "+sku.getColor(), list);
	    			**/
	                
				} catch (Exception e) {
					loggerError.error(e); 
				}
			}
		
		}catch(Exception ex){
			loggerError.error(ex);
		}
		
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;		
	}	
	
}

