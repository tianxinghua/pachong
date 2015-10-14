package com.shangpin.iog.reebonz.service;

/**
 * Created by wang on 2015/9/21.
 */

import java.io.File;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.EventProductDTO;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.onsite.base.constance.Constant;
import com.shangpin.iog.reebonz.dto.Item;
import com.shangpin.iog.reebonz.dto.Items;
import com.shangpin.iog.reebonz.dto.ResponseObject;
import com.shangpin.iog.reebonz.util.MyJsonUtil;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by 赵根春 on 2015/9/25.
 */
@Component("reebonzWiki")
public class FetchProduct {

	@Autowired
	ProductFetchService productFetchService;
	
	@Autowired
	EventProductService eventProductService;
    private static Logger logger = Logger.getLogger("info");
    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static int rows;
    private static String eventUrl=null;
    private static String productUrl=null;
    private static String stockUrl=null;

    static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        rows = Integer.parseInt(bdl.getString("rows"));
        eventUrl = bdl.getString("eventUrl");
        productUrl = bdl.getString("productUrl");
        stockUrl = bdl.getString("stockUrl");
    }
    
	/**
	 * fetch product and save into db
	 */
	public void fetchProductAndSave() {
		
		// 第一步：获取活动信息
		logger.info("拉取活动数据开始");
		List<Item> eventList = MyJsonUtil.getReebonzEventJson(eventUrl);
		for (Item item : eventList) {
			// 第二步：根据活动获取商品信息
			logger.info("拉取活动"+item.getEvent_id()+"下的商品总数");
			//获取商品总数量
			String productNum = getProductNum(productUrl,item
					.getEvent_id());
			logger.info("拉取活动"+item.getEvent_id()+"下的商品数据");
			//rows代表每次请求的数据行数，默认10
			for(int start=0;start<Integer.parseInt(productNum);start+=rows){
				List<Item> eventSpuList = MyJsonUtil.getReebonzSpuJsonByEventId(productUrl,item
						.getEvent_id(),start,rows);
				//保存入库
				messMappingAndSave(eventSpuList);
			}
		}

	}

	/**
	 * message mapping and save into DB
	 */
	private void messMappingAndSave(List<Item> array) {

			for (Item item : array) {
				
				//把新活动保存入库到EVENT_PRODUCT表中
				EventProductDTO event = new EventProductDTO();
				try{
					event.setEventId(item.getEvent_id());
					event.setSkuId(item.getSku());
					event.setSupplierId(supplierId);
					event.setStartDate(item.getEvent_start_date());
					event.setEndDate(item.getEvent_end_date());
					eventProductService.saveEventProduct(event);
				} catch (ServiceException e) {
					if (e.getMessage().equals("活动数据重复,插入失败键")) {
						System.out.println("数据插入失败键重复");
					} else {
						e.printStackTrace();
					}
				}
				
				
				SpuDTO spu = new SpuDTO();
				try {
					spu.setId(UUIDGenerator.getUUID());
					spu.setSupplierId(supplierId);
					spu.setSpuId(item.getSku());
					spu.setCategoryName(item.getProduct_category_name());
					spu.setSubCategoryName(item.getProduct_category_name());
					spu.setBrandName(item.getBrand_name());
					spu.setSpuName(item.getTitle());
					StringBuffer materialTemp = new StringBuffer();
					if(item.getMaterial()!=null){
						for(int i=0;i<item.getMaterial().length;i++){
							if(i==0){
								materialTemp.append(item.getMaterial()[i]);
							}else{
								materialTemp.append(","+item.getMaterial()[i]);
							}
						}
					}
					spu.setMaterial(materialTemp.toString());
					StringBuffer tempGender = new StringBuffer();
					if(item.getGender()!=null){
						for(int i=0;i<item.getGender().length;i++){
							if(i==0){
								tempGender.append(item.getGender ()[i]);
							}else{
								tempGender.append(","+item.getGender()[i]);
							}
						}
					}
					spu.setCategoryGender(tempGender.toString());
					productFetchService.saveSPU(spu);
					
					
					 if(StringUtils.isNotBlank(item.getImages()[0])){
		                    String[] picArray = item.getImages();
		                    for(String picUrl :picArray){
		                        ProductPictureDTO dto  = new ProductPictureDTO();
		                        dto.setPicUrl(picUrl);
		                        dto.setSupplierId(supplierId);
		                        dto.setId(UUIDGenerator.getUUID());
		                        dto.setSpuId(item.getSku());
		                        try {
		                            productFetchService.savePictureForMongo(dto);
		                            System.out.println("图片保存success");
		                        } catch (ServiceException e) {
		                            e.printStackTrace();
		                        }
		                    }
		                }
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				//
				// 第三步：根据skuId与eventId获取商品的库存跟尺码
				List<Item> skuScokeList = MyJsonUtil.getSkuScokeJson(stockUrl,item.getEvent_id(),
						item.getSku());
				if(skuScokeList!=null){
					for (Item stock : skuScokeList) {
						
							SkuDTO sku = new SkuDTO();
							try {
								sku.setId(UUIDGenerator.getUUID());
								sku.setSupplierId(supplierId);
								sku.setSpuId(item.getSku());
								String proSize = stock.getOption_name();
								if("no-size".equals(proSize)){
									sku.setProductSize("A");
									sku.setSkuId(item.getSku() + "|A");
								}else{
									sku.setProductSize(proSize);
									sku.setSkuId(item.getSku() + "|"
											+ stock.getOption_code());
								}
								
								sku.setStock(stock.getTotal_stock_qty());
								sku.setSalePrice(item.getFinal_selling_price());
								sku.setMarketPrice(item.getRetail_price());
								if(item.getColor()!=null){
									if (item.getColor().length > 0) {
										sku.setColor(item.getColor()[0]);
									}
								}
								sku.setProductName(item.getTitle());
								sku.setProductDescription(item.getDescription());
								sku.setProductCode(item.getSku());
								sku.setSaleCurrency(item.getCurrency());
								sku.setEventStartDate(item.getEvent_start_date());
								sku.setEventEndDate(item.getEvent_end_date());
								productFetchService.saveSKU(sku);
								
							} catch (ServiceException e) {
								try {
									if (e.getMessage().equals("数据插入失败键重复")) {
										// update
										productFetchService.updatePriceAndStock(sku);
									} else {
										e.printStackTrace();
									}
								} catch (ServiceException e1) {
									e1.printStackTrace();
								}
							}
						}
					}
				}
				
	}
	
	private String getProductNum(String productUrl,String eventId) {
		String spuJson = MyJsonUtil.getProductNum(productUrl,eventId);
		if(spuJson!=null){
			ResponseObject obj = new Gson().fromJson(spuJson, ResponseObject.class);
			Object o = obj.getResponse();
			JSONObject jsonObject = JSONObject.fromObject(o); 
			Items eventSpuList = new Gson().fromJson(jsonObject.toString(), Items.class);
			return eventSpuList.getNumFound();
		}else{
			return null;
		}
	}
}
