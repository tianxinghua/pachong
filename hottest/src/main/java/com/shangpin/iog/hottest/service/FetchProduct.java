package com.shangpin.iog.hottest.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.hottest.dto.BaseProductLookup;
import com.shangpin.iog.hottest.dto.Item;
import com.shangpin.iog.hottest.dto.Products;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

@Component("hottest")
public class FetchProduct {

	private static Logger logger = Logger.getLogger("info");
	private static Logger error = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	public static int day;
	private static OutTimeConfig outTimeConf = new OutTimeConfig(1000 * 60*5,
			1000 * 60 * 5, 1000 * 60 * 5);
	private static String url = "";
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		url = bdl.getString("url");
	}

	public void fetchProductAndSave() {
		
		Map<String,String> headMap = new HashMap<>();
		headMap.put("Authorization", "RIX5NkHDIM25yUFZmDlVSdWEE7V3aSYv");
		int page = 1;
		int maxPage = 2;		
		while(page <=maxPage){
			try{
				
				String url = "http://www.pos123.us/api/v2/products/.json?limit=100&page="+page;
				logger.info(url);
				String result = HttpUtil45.get(url, outTimeConf, null, headMap, "", "");
				logger.info(result);
				JSONObject jsonObj = JSONObject.fromObject(result);
				JSONObject products = jsonObj.getJSONObject("products");
				for(int k=0;k<100;k++){
					if(products.containsKey(String.valueOf(k))){
						JSONObject product = products.getJSONObject(String.valueOf(k));//spu
						String style = product.getString("style");
						String color = product.getString("color");
						String colorDes = "";
						try{
							colorDes = product.getString("color_description");
						}catch(Exception e){
							e.printStackTrace();
						}
						//保存spu
						String spuId = style+color; 
						JSONArray items = product.getJSONArray("variations");
						for(int i=0;i<items.size();i++){
							try{
								JSONObject item = items.getJSONObject(i);//sku								
								//保存sku
								SkuDTO sku = new SkuDTO();
								sku.setId(UUIDGenerator.getUUID());
								sku.setSupplierId(supplierId);
								sku.setSpuId(spuId);
								sku.setSkuId(item.getString("upc"));
								sku.setProductCode(spuId);								
								sku.setColor(colorDes);
								sku.setSalePrice(product.getString("price"));
								sku.setProductName(item.getString("name"));
								sku.setProductSize(item.getString("size"));
								sku.setStock(item.getString("quantity"));
								sku.setBarcode(item.getString("upc"));
//								sku.setSaleCurrency("");
								
							}catch(Exception ex){
								error.error(ex); 
							}
						}
						try{
							SpuDTO spu = new SpuDTO();
							spu.setId(UUIDGenerator.getUUID());
							spu.setSupplierId(supplierId);
							spu.setSpuId(spuId);
							spu.setBrandName(product.getString("brand"));
							String gender = product.getString("gender");
							if(StringUtils.isBlank(gender)){
								gender = product.getString("age_group");
							}
							spu.setCategoryGender(gender);
							JSONArray cat = product.getJSONArray("categories");
							String category = "";
							for(int j=0;j<cat.size();j++){
								category +=cat.getString(j)+"|";
							}
							spu.setCategoryName(category);
//							spu.setMaterial("");
//							spu.setSeasonName("");
//							spu.setProductOrigin("");
							
							//保存图片
							JSONArray pics = product.getJSONArray("images");
							pics.add(product.getString("default_image"));
							List<String> list = new ArrayList<String>();
							for(int kk=0;kk<pics.size();kk++){
								list.add(pics.getString(kk));
							}
							
						}catch(Exception ex){
							error.error(ex); 
						}
					}else{
						break;
					}				
				}
				maxPage = products.getInt("pages");
				logger.info("page====="+page);
				logger.info("maxPage====="+maxPage);
				page = page+1;
			}catch(Exception ex){
				ex.printStackTrace();
				error.error(ex);
				if(page <=150){
					page --;
					maxPage = page +1;
				}				
			}
			
		}
		logger.info("抓取结束");
	}
	
}

