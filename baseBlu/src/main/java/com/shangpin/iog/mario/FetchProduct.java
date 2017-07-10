package com.shangpin.iog.mario;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.mario.dto.Item;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.product.AbsSaveProduct;

@Component("baseBlu")
public class FetchProduct extends AbsSaveProduct{

	private static Logger logInfo = Logger.getLogger("info");
	
	private static ResourceBundle bdl = null;
	private static String supplierId = "";	
	private static String uri;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("param");
		supplierId = bdl.getString("supplierId");
		uri = bdl.getString("uri");
	}
	
	@Override
	public Map<String, Object> fetchProductAndSave() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String,List<String>> imageMap = new HashMap<String, List<String>>();
		try {
			List<Item> items = CVSUtil.readCSV(uri, Item.class, ',');
			if(items.size()>0){
				logInfo.info("------------------一共有"+items.size()+"条数据----------------"); 
				for(Item item : items){
					try {
						//保存spu
						SpuDTO spu = new SpuDTO();
						spu.setId(UUIDGenerator.getUUID());
						spu.setSupplierId(supplierId);
						spu.setSpuId(item.getSku_brand()+item.getColor());
						spu.setCategoryGender(item.getGender());
						spu.setCategoryName(item.getCategory());
						spu.setSubCategoryName(item.getSub_category());
						spu.setSeasonName(item.getSeason());
						spu.setBrandName(item.getBrand());
						spu.setMaterial(item.getMaterial());
						spu.setProductOrigin(item.getMade_in()); 
						spuList.add(spu);
						//保存sku
						String color_code = item.getCod_colore_brand();
						if(color_code.length() == 1){
							color_code = "00"+color_code;
						}else if(color_code.length() == 2){
							color_code = "0"+color_code;
						}
						String size_stock_barcode = item.getSize_stock_barcode();
						String[] strs = size_stock_barcode.split("\\|");
						for(int i=0;i<strs.length;i++){
							if(i % 3 == 0){
								SkuDTO sku = new SkuDTO();
								sku.setId(UUIDGenerator.getUUID());
								sku.setSupplierId(supplierId);	
								sku.setSpuId(item.getSku_brand()+item.getColor());
								sku.setProductSize(strs[i]); 
								sku.setStock(strs[i+1]);
								sku.setSkuId(strs[i+2]);//skuid设置为barcode
								sku.setBarcode(strs[i+2]);
								sku.setProductCode(item.getSku_brand()+"  "+color_code);
								sku.setColor(item.getColor());
								sku.setMarketPrice(item.getRtl_price());
								sku.setSupplierPrice(item.getSupply_price());
								sku.setSalePrice("");
								sku.setProductDescription(CVSUtil.standard(item.getLong_description()));
								sku.setProductName(CVSUtil.standard(item.getShort_description()));
								skuList.add(sku);
							}
						}
						//保存图片
						String urls = item.getUrl_pictures();
						if(StringUtils.isNotBlank(urls)){
							List<String> picLists = new ArrayList<String>();
							for(String url : urls.split("\\|")){
								picLists.add(url);
							}
							imageMap.put(spu.getSpuId()+";"+spu.getSpuId(), picLists);
						}
						
					} catch (Exception e) {
						e.printStackTrace();
						logInfo.error(e.toString());
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logInfo.error(e.toString());
		}
		

		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;		
	}
	
}
