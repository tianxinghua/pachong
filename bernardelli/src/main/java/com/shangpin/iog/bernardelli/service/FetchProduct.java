package com.shangpin.iog.bernardelli.service;


import java.util.Arrays;
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

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.bernardelli.dto.Data;
import com.shangpin.iog.bernardelli.dto.Item;
import com.shangpin.iog.bernardelli.dto.Product;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

/**
 * Created by houkun on 2016/01/25.
 */
@Component("bernardelli")
public class FetchProduct {
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String url;
	private static String username;
	private static String password;
	public static int day;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("param");
		supplierId = bdl.getString("supplierId");
		url = bdl.getString("url");
		username = bdl.getString("username");
		password = bdl.getString("password");
		day = Integer.valueOf(bdl.getString("day"));
		
	}
	@Autowired
	ProductSearchService productSearchService;
	@Autowired
	private ProductFetchService productFetchService;
    public void fetchProductAndSave(){
        	Date startDate,endDate= new Date();
			startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
			//获取原有的SKU 仅仅包含价格和库存
			Map<String,SkuDTO> skuDTOMap = new HashedMap();
			try {
				skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10);
			System.out.println("请求数据");
			String data = HttpUtil45.postAuth(url, null, outTimeConf, username, password);
			Gson gson = new Gson();
			Data datas = gson.fromJson(data, Data.class);
			List<Product> productList = datas.getProduct();
			System.out.println("开始保存spu");
			
			for (Product product : productList) {
				SpuDTO spu = new SpuDTO();
				spu.setId(UUIDGenerator.getUUID());
				spu.setSupplierId(supplierId);
				spu.setSpuId(product.getProduct_id());
				spu.setBrandName(product.getBrand_id());
				spu.setCategoryName(product.getCategory());
				spu.setCategoryGender(product.getType());
				spu.setSeasonName(product.getSeason());
				spu.setMaterial(product.getProduct_material());
				//TODO 没有原产地
				//保存spu
				try {
					productFetchService.saveSPU(spu);
				} catch (ServiceException e) {
					e.printStackTrace();
					try {
						productFetchService.updateMaterial(spu);
					} catch (ServiceException e1) {
						e1.printStackTrace();
					}
				}
				//保存sku
				for (Item item : product.getItems().getItem()) {
					SkuDTO sku = new SkuDTO();
					sku.setId(UUIDGenerator.getUUID());
					sku.setSupplierId(supplierId);
					sku.setSpuId(spu.getSpuId());
					sku.setSkuId(item.getItem_id());
					sku.setProductName(product.getProduct_name());
					String size = item.getItem_size();
					if(size.indexOf("½")>0){
	                    size=size.replace("½","+");
	                }
					sku.setProductSize(size);
					sku.setBarcode(item.getBarcode());
					sku.setProductCode(product.getProduct_code());
					sku.setStock(item.getStock());
					sku.setColor(product.getProduct_colour());
					sku.setMarketPrice(product.getRetail_price());
					sku.setSaleCurrency("EURO");
					try {
						
						if(skuDTOMap.containsKey(sku.getSkuId())){
							skuDTOMap.remove(sku.getSkuId());
						}
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
		    				e1.printStackTrace();
		    			}			
					}
				}
				//保存图片
				productFetchService.savePicture(supplierId, spu.getSpuId(), null, Arrays.asList(product.getPictures()));
			}
        	//更新网站不再给信息的老数据
			for(Iterator<Map.Entry<String,SkuDTO>> itor = skuDTOMap.entrySet().iterator();itor.hasNext(); ){
				 Map.Entry<String,SkuDTO> entry =  itor.next();
				if(!"0".equals(entry.getValue().getStock())){//更新不为0的数据 使其库存为0
					entry.getValue().setStock("0");
					try {
						productFetchService.updatePriceAndStock(entry.getValue());
					} catch (ServiceException e) {
						e.printStackTrace();
					}
				}
			}
    }
    public static void main(String[] args) {
    	String url = "http://shop.bernardellistores.com/getProductsJSON/productsJSON.json";
    	Map<String, String> param = new HashMap();
		OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10);
		String userName = "bernardelli";
		String password = "BernB2B#572";
		String data = HttpUtil45.postAuth(url, param, outTimeConf, userName, password);
		Gson gson = new Gson();
		Data data2 = gson.fromJson(data, Data.class);
    	System.out.println(data.length());
    }
}
