package com.shangpin.iog.reebonz.service;

/**
 * Created by wang on 2015/9/21.
 */

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.TimeZone;

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
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static int rows;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		rows = Integer.parseInt(bdl.getString("rows"));
	}

	/**
	 * fetch product and save into db
	 */
	public void fetchProductAndSave() {

		// 第一步：获取活动信息
		logger.info("拉取活动数据开始");
		List<Item> eventList = MyJsonUtil.getReebonzEventJson();
		if(eventList!=null){
			logger.info("拉取活动数据结束");
			for (Item item : eventList) {
				// 第二步：根据活动获取商品信息
				// 获取商品总数量
				String productNum = getProductNum(item.getEvent_id());
				logger.info("获得活动" + item.getEvent_id() + "下的商品总数为：" + productNum);
				// rows代表每次请求的数据行数，默认10
				if(productNum!=null){
					for (int start = 0; start < Integer.parseInt(productNum); start += rows) {
						List<Item> eventSpuList = MyJsonUtil
								.getReebonzSpuJsonByEventId(item.getEvent_id(), start,
										rows);
						logger.info("已拉取活动" + item.getEvent_id() + "下的商品总数为：" + start
								+ rows);
						// 保存入库
						messMappingAndSave(eventSpuList);
					}
					logger.info("活动" + item.getEvent_id() + "下的所有商品拉取完成");
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

						if (StringUtils.isNotBlank(item.getImages()[0])) {
							String[] picArray = item.getImages();
							for (String picUrl : picArray) {
								ProductPictureDTO dto = new ProductPictureDTO();
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

				}
				
				//
				// 第三步：根据skuId与eventId获取商品的库存跟尺码
				
				List<Item> skuScokeList = MyJsonUtil.getSkuScokeJson(
						item.getEvent_id(), item.getSku());
				if (skuScokeList != null) {
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
								sku.setSupplierPrice(String.valueOf(price));
							}

							if (item.getColor() != null) {
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
							//新产品入库，旧产品只更新价格库存
							if(flag){
								productFetchService.saveSKU(sku);
							}else{
								System.out.println("------更新库存以及价格success："+stock.getTotal_stock_qty()+":"+item.getFinal_selling_price());
								productFetchService.updatePriceAndStock(sku);
							}
						} catch (ServiceException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	private String getProductNum(String eventId) {
		String spuJson = MyJsonUtil.getProductNum(eventId);
		if (spuJson != null) {
			ResponseObject obj = new Gson().fromJson(spuJson,
					ResponseObject.class);
			Object o = obj.getResponse();
			JSONObject jsonObject = JSONObject.fromObject(o);
			Items eventSpuList = new Gson().fromJson(jsonObject.toString(),
					Items.class);
			return eventSpuList.getNumFound();
		} else {
			return null;
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
