package com.shangpin.iog.efashion.service;

/**
 * Created by wang on 2015/9/21.
 */

import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.EventProductDTO;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.efashion.dao.Image;
import com.shangpin.iog.efashion.dao.Material;
import com.shangpin.iog.efashion.dao.Result;
import com.shangpin.iog.efashion.dao.ReturnObject;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.iog.efashion.dao.Item;

import net.sf.json.JSONObject;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by 赵根春 on 2015/9/25.
 */
@Component("efashion")
public class FetchProduct {

	@Autowired
	ProductFetchService productFetchService;
	@Autowired
	private ProductSearchService productSearchService;
	@Autowired
	EventProductService eventProductService;
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String url;
	public static int day;
	public static int max;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		max = Integer.valueOf(bdl.getString("max"));
		url = bdl.getString("url");
	}
	static int i=0;
	static int j=0;
	public  void getProductList(int index){
		String json = HttpUtil45
				.get(url+"&limit="+max+"&offset="+index,
						new OutTimeConfig(1000 * 60, 1000 * 60, 1000 * 60),
						null);
		try{
			ReturnObject obj = new Gson().fromJson(json, ReturnObject.class);
			// 第一步：获取活动信息
			if(obj!=null){
				Result result = obj.getResults();
				List<Item> item = result.getItems();
				if(!item.isEmpty()){
				
					i++;
					System.out.println("------------------------第"+i+"页---------------------------");
					
					messMappingAndSave(item);
					j = j+item.size();
					System.out.println("商品数量："+item.size());
					getProductList(max*i+1);
				}
			}
		}catch(Exception e){
			i++;
			getProductList(max*i+1);
		}
		
		
	}
	/**
	 * fetch product and save into db
	 */
	Map<String,SkuDTO> skuDTOMap = new HashedMap();
	public void fetchProductAndSave() {
		Date startDate,endDate= new Date();
		startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
		
		try {
			skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		// 第一步：获取活动信息
		getProductList(1);
		System.out.println("总的商品数量："+j);
		logger.info("总的商品数量："+j);
		System.out.println("--拉取数据end--");
		logger.info("--拉取数据end--");
//		messMappingAndSave();
	
		//获取原有的SKU 仅仅包含价格和库存
		
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
	/**
	 * message mapping and save into DB
	 */
	private  void messMappingAndSave(List<Item> item1) {
		if(item1!=null){
			
			
			for (Item item : item1) {
				SpuDTO spu = new SpuDTO();
				try {
					spu.setId(UUIDGenerator.getUUID());
					spu.setSupplierId(supplierId);
					spu.setSpuId(item.getProduct_id());
					spu.setCategoryName(item.getFirst_category());
					spu.setBrandName(item.getBrand());
					spu.setSpuName(item.getItem_intro());
					List<Material> list = item.getTechnical_info();
					if(list!=null&&!list.isEmpty()){
						StringBuffer str = new StringBuffer();
						for(Material m:list){
							str.append(",").append(m.getPercentage()).append(m.getName());
						}
						spu.setMaterial(str.substring(1));
					}
					spu.setProductOrigin(item.getMade_in());
					spu.setSeasonName(item.getSeason_year()+item.getSeason_reference());
					spu.setCategoryGender(item.getGender());
					productFetchService.saveSPU(spu);
				} catch (Exception e) {
					try {
						productFetchService.updateMaterial(spu);
					} catch (ServiceException e1) {
						e1.printStackTrace();
					}
				}
				
				String skuId = null;
				SkuDTO sku = new SkuDTO();
				try {
					sku.setId(UUIDGenerator.getUUID());
					sku.setSupplierId(supplierId);
					sku.setSpuId(item.getProduct_id());
					String size = null;
					size = item.getSize();
					if(size==null){
						size = "A";
					}
					skuId = item.getProduct_id()+"-"+size;
					sku.setSkuId(skuId);
					sku.setProductSize(size);
					sku.setStock(item.getQuantity());
					sku.setProductCode(item.getProduct_reference()+" "+item.getColor_reference());
					sku.setMarketPrice(item.getRetail_price());
					sku.setSupplierPrice(item.getPrice());
					sku.setColor(item.getColor());
					sku.setProductName(item.getItem_intro());
					sku.setProductDescription(item.getItem_description());
					sku.setSaleCurrency(item.getCurrency());
					sku.setMemo(item.getProduct_reference()+"|"+item.getColor_reference()+"|"+item.getSize());
					if(skuDTOMap.containsKey(sku.getSkuId())){
						skuDTOMap.remove(sku.getSkuId());
					}
					
					productFetchService.saveSKU(sku);
					
				} catch (ServiceException e) {
					if (e.getMessage().equals("数据插入失败键重复")) {
						try {
							productFetchService.updatePriceAndStock(sku);
						} catch (ServiceException e1) {
							e1.printStackTrace();
						}
					} else {
						e.printStackTrace();
					}

				}
				
				String [] picArray = item.getItem_images().getFull();
				productFetchService.savePicture(supplierId, item.getProduct_id(), null,Arrays.asList(picArray));
		
		}
		System.out.println("--保存end.....--");
		logger.info("--保存end.....--");
	}
	}
}
