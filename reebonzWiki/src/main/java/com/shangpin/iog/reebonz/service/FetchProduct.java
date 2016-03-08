package com.shangpin.iog.reebonz.service;

/**
 * Created by wang on 2015/9/21.
 */

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
import com.shangpin.iog.dto.EventProductDTO;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.reebonz.dto.Item;
import com.shangpin.iog.reebonz.dto.Items;
import com.shangpin.iog.reebonz.dto.ResponseObject;
import com.shangpin.iog.reebonz.util.MyJsonUtil;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

import net.sf.json.JSONObject;

import org.apache.commons.collections.map.HashedMap;
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
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static int rows;
	//以下参数做统计用，无实际意义
	private static int skuTotal=0;
	private static int skuSaveAndUpdateTotal=0;
	private static int sku=0;
	private static int skuSaveTotal=0 ;
	private static int updateTotal =0;
	private static int skuPassTotal =0;
	private static int allEventPassSkuTotal =0;
    @Autowired
   	ProductSearchService productSearchService;
	private static int day;
    
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		rows = Integer.parseInt(bdl.getString("rows"));
		day = Integer.valueOf(bdl.getString("day"));
	}
	private Map<String,SkuDTO> skuDTOMap = new HashedMap();
	/**
	 * fetch product and save into db
	 */
	public void fetchProductAndSave() {

		Date startDate,endDate= new Date();
		startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
		//获取原有的SKU 仅仅包含价格和库存
		
		try {
			skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		
		// 第一步：获取活动信息
		List<Item> eventList = MyJsonUtil.getReebonzEventJson();
		int i=0;
		if(eventList!=null){
			for (Item item : eventList) {
				logger.info("--------------活动"+(++i)+"---------------------");
				// 第二步：根据活动获取商品信息
				// 获取商品总数量
				String productNum = getProductNum(item.getEvent_id());
				// rows代表每次请求的数据行数，默认10
				if(productNum!=null){
					for (int start = 0; start < Integer.parseInt(productNum); start += rows) {
						List<Item> eventSpuList = MyJsonUtil
								.getReebonzSpuJsonByEventId(item.getEvent_id(), start,
										rows);
						// 保存入库
						messMappingAndSave(eventSpuList);
					}
				}
				logger.info("---拉取活动" + item.getEvent_id() + "下的sku总数为：" + sku);
//				System.out.println("---拉取活动" + item.getEvent_id() + "下的sku总数为：" + sku);
				skuTotal +=sku;
				sku=0;
				logger.info("-----sku保存总数："+skuSaveTotal);
				logger.info("-----sku更新总数："+updateTotal);
				logger.info("-----sku去重总数："+skuPassTotal);
				skuSaveAndUpdateTotal+=skuSaveTotal+=updateTotal;
				allEventPassSkuTotal+=skuPassTotal;
				skuSaveTotal =0;
				updateTotal =0;
				skuPassTotal =0;
			}
			skuSaveTotal =0;
			updateTotal =0;
			logger.info("reebonz供应商拉取的所有活动总数："+i);
			logger.info("reebonz供应商拉取所有活动下的商品总数："+skuTotal);
			logger.info("reebonz总共更新和保存的sku总数："+skuSaveAndUpdateTotal);
			logger.info("reebonz总共去重过滤掉总数："+allEventPassSkuTotal);
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

	/**
	 * message mapping and save into DB
	 */
	private void messMappingAndSave(List<Item> array) {
		
	

		if(array!=null){
			for (Item item : array) {
				boolean flag = false;
				boolean f = false;
				EventProductDTO eventDTO = null;
				// 判断sku是否已存在,若存在再判断是否此活动已结束，如果已结束则入库，若未结束则跳过不保存
				try {
					eventDTO = eventProductService.checkEventSku(supplierId,
							item.getSku());
					if (eventDTO != null) {
	 					if (!item.getEvent_id().equals(eventDTO.getEventId())) {
							// 新的活动,判断是否旧活动已到期
							SimpleDateFormat sf = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							Date endDate = sf.parse(getString(eventDTO.getEndDate()));
							boolean before = endDate.before(new Date());
							if (before) {
								// 旧活动已结束
								flag = true;
							}else{
								// 旧活动未结束
								f=true;
							}
						} else {
							// 已存在的活动更新产品
							flag = false;
						}
					}else{
						//新产品，且未参加过任何活动
						flag=true;
					}
				} catch (ServiceException e2) {
					e2.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}

				// 把新活动保存入库到EVENT_PRODUCT表中
				if (flag) {
					EventProductDTO event = new EventProductDTO();
					try {
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
				}
				if(flag){
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
						if (item.getMaterial() != null) {
							for (int i = 0; i < item.getMaterial().length; i++) {
								if (i == 0) {
									materialTemp.append(item.getMaterial()[i]);
								} else {
									materialTemp.append("," + item.getMaterial()[i]);
								}
							}
						}
						spu.setMaterial(materialTemp.toString());
						StringBuffer tempGender = new StringBuffer();
						if (item.getGender() != null) {
							for (int i = 0; i < item.getGender().length; i++) {
								if (i == 0) {
									tempGender.append(item.getGender()[i]);
								} else {
									tempGender.append("," + item.getGender()[i]);
								}
							}
						}
						spu.setCategoryGender(tempGender.toString());
						productFetchService.saveSPU(spu);
						
					} catch (Exception e) {
						try {
							productFetchService.updateMaterial(spu);
						} catch (ServiceException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

				}
				
				
				if (StringUtils.isNotBlank(item.getImages()[0])) {
					String[] picArray = item.getImages();
					productFetchService.savePicture(supplierId, item.getSku(), null, Arrays.asList(picArray));
				}
				//
				// 第三步：根据skuId与eventId获取商品的库存跟尺码
				
				List<Item> skuScokeList = MyJsonUtil.getSkuScokeJson(
						item.getEvent_id(), item.getSku());
				if (skuScokeList != null) {
					sku +=skuScokeList.size();
					for (Item stock : skuScokeList) {
						SkuDTO sku = new SkuDTO();
						try {
							sku.setId(UUIDGenerator.getUUID());
							sku.setSupplierId(supplierId);
							sku.setSpuId(item.getSku());
							String proSize = stock.getOption_name();
							if ("no-size".equals(proSize)) {
								sku.setProductSize("A");
								sku.setSkuId(item.getSku() + "|A");
							} else {
								sku.setProductSize(proSize);
								sku.setSkuId(item.getSku() + "|"
										+ stock.getOption_code());
							}

							sku.setStock(stock.getTotal_stock_qty());
							sku.setSalePrice(item.getFinal_selling_price());
							sku.setMarketPrice(item.getRetail_price());
							String discount = item.getPartner_discount();
							if (Double.parseDouble(discount) == 1) {
								sku.setSupplierPrice(item.getFinal_selling_price());
							} else {
								double price = Double.parseDouble(item
										.getFinal_selling_price())
										* (1 - Double.parseDouble(discount));
								BigDecimal   b   =   new   BigDecimal(price);  
								double   f1   =   b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();  
								sku.setSupplierPrice(String.valueOf(f1));
							}

							if (item.getColor() != null) {
								if (item.getColor().length > 0) {
									sku.setColor(item.getColor()[0]);
								}
							}
							sku.setProductName(item.getTitle());
							sku.setProductDescription(item.getDescription()+","+item.getMeasurement());
							sku.setProductCode(item.getSku());
							sku.setSaleCurrency(item.getCurrency());
							sku.setEventStartDate(item.getEvent_start_date());
							sku.setEventEndDate(item.getEvent_end_date());
							if(skuDTOMap.containsKey(sku.getSkuId())){
								skuDTOMap.remove(sku.getSkuId());
    						}
							//新产品入库，旧产品只更新价格库存
							if(flag){
								skuSaveTotal+=1;
								productFetchService.saveSKU(sku);
								
							}else{
								if(!f){
									productFetchService.updatePriceAndStock(sku);
									updateTotal+=1;
								}else{
									skuPassTotal +=1;
								}
							
//								System.out.println("------更新库存以及价格success："+stock.getTotal_stock_qty()+":"+item.getFinal_selling_price());
								
							}
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
					}
				}
			}
		}
	}

	private String getProductNum(String eventId) {
		String spuJson = MyJsonUtil.getProductNum(eventId);
		if("{\"error\":\"发生异常错误\"}".equals(spuJson)){
			logger.info("网络连接："+spuJson);
			return null;
		}else{
			ResponseObject obj = new Gson().fromJson(spuJson,
					ResponseObject.class);
			if(obj!=null){
				Object o = obj.getResponse();
				JSONObject jsonObject = JSONObject.fromObject(o);
				Items eventSpuList = new Gson().fromJson(jsonObject.toString(),
						Items.class);
				if(eventSpuList!=null){
					return eventSpuList.getNumFound();
				}else{
					return null;
				}
				
			}else{
				return null;
			}
		}
	}

	private static String getString(String ts) throws ParseException {
		ts = ts.replace("Z", " UTC");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		Date dt = sdf.parse(ts);
		TimeZone tz = sdf.getTimeZone();
		Calendar c = sdf.getCalendar();
		StringBuffer result = new StringBuffer();
		result.append(c.get(Calendar.YEAR));
		result.append("-");
		result.append((c.get(Calendar.MONTH) + 1));
		result.append("-");
		result.append(c.get(Calendar.DAY_OF_MONTH));
		result.append(" ");
		result.append(c.get(Calendar.HOUR_OF_DAY));
		result.append(":");
		result.append(c.get(Calendar.MINUTE));
		result.append(":");
		result.append(c.get(Calendar.SECOND));
		return result.toString();
	}
}
