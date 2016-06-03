package com.shangpin.iog.magway.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

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
import com.shangpin.iog.magway.dto.BrandList;
import com.shangpin.iog.magway.dto.ColorList;
import com.shangpin.iog.magway.dto.Data;
import com.shangpin.iog.magway.dto.DataCat;
import com.shangpin.iog.magway.dto.DataGoodsInfo;
import com.shangpin.iog.magway.dto.DateSupplier;
import com.shangpin.iog.magway.dto.GoodsIdList;
import com.shangpin.iog.magway.dto.GoodsInfo;
import com.shangpin.iog.magway.dto.GoodsTypeList;
import com.shangpin.iog.magway.dto.SeasonList;
import com.shangpin.iog.magway.dto.SupplierList;
import com.shangpin.iog.magway.dto.Token;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.iog.magway.dto.GoodsCatgList;
import com.shangpin.iog.magway.dto.GoodsList;
import com.shangpin.iog.magway.dto.DataG;
import com.shangpin.iog.magway.dto.DataT;
import com.shangpin.iog.magway.dto.GoodsDetail;

@Component("magway2")
public class FetchProduct2 {

	private static Logger logger = Logger.getLogger("info");
	private static Logger error = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	public static int day;
	private static OutTimeConfig outTimeConf = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);
	
	private static String url_token = null;
	private static String url_getBrandList = null;
	private static String url_getColorList = null;
	private static String url_getSeasonList = null;
	private static String url_getGoodsTypeList = null;
	private static String url_getGoodsCatgList = null;
//	private static String url_getGoodsList = null;
//	private static String url_getGoodsDetail = null;
	private static String Authorization = null;
	private static String grant_type = null;
	
	private static String url_getSupplierList = null;
	private static String url_getGoodsInfo = null;
	private static String url_getGoodsIdList = null;
	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		
		url_token = bdl.getString("url_token");
		url_getBrandList = bdl.getString("url_getBrandList");
		url_getColorList = bdl.getString("url_getColorList");
		url_getSeasonList = bdl.getString("url_getSeasonList");
		url_getGoodsTypeList = bdl.getString("url_getGoodsTypeList");
		url_getGoodsCatgList = bdl.getString("url_getGoodsCatgList");
//		url_getGoodsList = bdl.getString("url_getGoodsList");
//		url_getGoodsDetail = bdl.getString("url_getGoodsDetail");
		Authorization = bdl.getString("Authorization");
		grant_type = bdl.getString("grant_type");
		
		url_getSupplierList = bdl.getString("url_getSupplierList");
		url_getGoodsInfo = bdl.getString("url_getGoodsInfo");
		url_getGoodsIdList = bdl.getString("url_getGoodsIdList");
	}
	@Autowired
	private ProductFetchService productFetchService;
	@Autowired
	private ProductSearchService productSearchService;
	
	public void fetchProductAndSave() {
		Date startDate,endDate= new Date();
		startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
		//获取原有的SKU 仅仅包含价格和库存
		Map<String,SkuDTO> skuDTOMap = new HashMap<String,SkuDTO>();
		try {
			skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		try{
		
			Token token= getToken();
			String access_token = token.getAccess_token();
			if(StringUtils.isNotBlank(access_token)){
				//brand
				Map<String,String> headMap = new HashMap<String,String>();
				headMap.put("Authorization", token.getToken_type()+" "+access_token);
				//suppliers
//				String suppliers = HttpUtil45.get(url_getSupplierList, outTimeConf, null, headMap, "", "");
//				SupplierList supplierList = new Gson().fromJson(suppliers, SupplierList.class);	
				SupplierList supplierList = new SupplierList();
				List<DateSupplier> supplierL = new ArrayList<DateSupplier>();
				DateSupplier dateSupplier0 = new DateSupplier();
				dateSupplier0.setID("6");
				dateSupplier0.setName("JULIAN FASHION SRL");				
				DateSupplier dateSupplier1 = new DateSupplier();
				dateSupplier1.setID("38");
				dateSupplier1.setName("ACANFORA S.R.L.");
				supplierL.add(dateSupplier1);
				supplierL.add(dateSupplier0);
				supplierList.setData(supplierL); 
				String brandStr = HttpUtil45.get(url_getBrandList, outTimeConf, null, headMap, "", "");
				logger.info("brandStr===="+brandStr); 
				BrandList brandList = new Gson().fromJson(brandStr, BrandList.class);
				//color
				String colorStr = HttpUtil45.get(url_getColorList, outTimeConf, null, headMap, "", "");
				logger.info("colorStr===="+colorStr); 
				ColorList colorList = new Gson().fromJson(colorStr, ColorList.class);
				//season
				String seasonStr = HttpUtil45.get(url_getSeasonList, outTimeConf, null, headMap, "", "");
				logger.info("seasonStr===="+seasonStr); 
				SeasonList seasonList = new Gson().fromJson(seasonStr, SeasonList.class);
				//goods type
				String goodstypeStr = HttpUtil45.get(url_getGoodsTypeList, outTimeConf, null, headMap, "", "");
				logger.info("goodstypeStr===="+goodstypeStr); 
				GoodsTypeList goodsTypeList = new Gson().fromJson(goodstypeStr, GoodsTypeList.class);
				//goods catg
				String goodscatgStr = HttpUtil45.get(url_getGoodsCatgList, outTimeConf, null, headMap, "", "");
				logger.info("goodscatgStr===="+goodscatgStr); 
				GoodsCatgList goodsCatgList = new Gson().fromJson(goodscatgStr, GoodsCatgList.class);
				//goods list
//				String goodslistStr = HttpUtil45.get(url_getGoodsList, outTimeConf, null, headMap, "", "");
//				logger.info("goodslistStr===="+goodslistStr); 
//				GoodsList goodsList = new Gson().fromJson(goodslistStr, GoodsList.class);
//				if(null != supplierList && null != supplierList.getData() && supplierList.getData().size()>0
//						&& null != brandList && null!= brandList && null != brandList.getData() && brandList.getData().size()>0
//						&& null != goodsCatgList && null != goodsCatgList.getData() && goodsCatgList.getData().size()>0){
					for(DateSupplier dateSupplier:supplierList.getData()){
//						for(Data brandDate : brandList.getData()){
//							for(DataCat dataCat : goodsCatgList.getData()){
								try {
									
//									Map<String, String> paramMap = new HashMap<String,String>();
//									paramMap.put("supplierId", dateSupplier.getID());
//									paramMap.put("brandId", brandDate.getID());
//									paramMap.put("categoryId", dataCat.getCategoryID());
//									System.out.println(headMap.toString()); 
									String goodsIdUrl = url_getGoodsIdList+"?supplierId="+dateSupplier.getID();
									String goodsIds = HttpUtil45.get(goodsIdUrl, outTimeConf, null, headMap, "", "");
									logger.info(dateSupplier.getName()+"   "+goodsIds); 
									GoodsIdList goodsIdList = new Gson().fromJson(goodsIds, GoodsIdList.class);
									if(null != goodsIdList && null != goodsIdList.getData() && goodsIdList.getData().size()>0){
										for(Integer goodsId :goodsIdList.getData()){
											try {
												String infoUrl = url_getGoodsInfo+goodsId;
												String goodsInfo = HttpUtil45.post(infoUrl, null, headMap, outTimeConf);
												GoodsInfo data = new Gson().fromJson(goodsInfo, GoodsInfo.class);
												for(com.shangpin.iog.magway.dto.Stocks stock:data.getData().getStocks()){
													try{
														//sku===============================
														SkuDTO sku = new SkuDTO();
														sku.setId(UUIDGenerator.getUUID());
														sku.setSupplierId(supplierId);
														sku.setSkuId(data.getData().getID()+"-"+stock.getSize()); 
														sku.setSpuId(data.getData().getID());
														sku.setProductName(data.getData().getName());
														sku.setMarketPrice(data.getData().getPriceEuro());
														sku.setSalePrice(data.getData().getYourPriceEuro());
														sku.setSaleCurrency("Euro"); 
														sku.setProductCode(data.getData().getCode());
														String color = "";
														for(com.shangpin.iog.magway.dto.DataC dataC:colorList.getData()){
															if(dataC.getId().equals(data.getData().getColorCode())){
																color = dataC.getName();
																break;
															}
														}
														if(StringUtils.isBlank(color)){
															color = data.getData().getColor();
														}
														sku.setColor(color);
														sku.setProductSize(stock.getSize());
														sku.setStock(stock.getQty());
														sku.setProductDescription(dateSupplier.getName() +" "+ dateSupplier.getID()); 
														if(skuDTOMap.containsKey(sku.getSkuId())){
															skuDTOMap.remove(sku.getSkuId());
														}
														try {
															productFetchService.saveSKU(sku);
														} catch (ServiceException e) {
															e.printStackTrace();
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
														
														//spu=======================
														SpuDTO spu = new SpuDTO();
														spu.setId(UUIDGenerator.getUUID());
														spu.setSupplierId(supplierId);
														spu.setSpuId(data.getData().getID());
														String categoryGender = "";
														for(DataT datat:goodsTypeList.getData()){
															if(data.getData().getTypeId().equals(datat.getID())){
																categoryGender = datat.getName();
																break;
															}
														}
														spu.setCategoryGender(categoryGender);
														spu.setCategoryId(data.getData().getCategoryId());	
														String cat = "";
														for(DataCat dateCat:goodsCatgList.getData()){
															if(dateCat.getCategoryID().equals(data.getData().getCategoryId())){
																cat = dateCat.getName();
																break;
															}
														}
														spu.setCategoryName(cat);
														spu.setSeasonId(data.getData().getSeason());
														String seasonName = "";
														for(com.shangpin.iog.magway.dto.DataS dataS:seasonList.getData()){
															if(dataS.getSeasonCode().equals(data.getData().getSeason())){
																seasonName = dataS.getName();
																break;
															}
														}
														spu.setSeasonName(seasonName);
														spu.setBrandId(data.getData().getBrandId());
														String brandName = "";
														for(Data dataB :brandList.getData()){
															if(dataB.getID().equals(data.getData().getBrandId())){
																brandName = dataB.getName();
																break;
															}
														}
														spu.setBrandName(brandName);
														spu.setMaterial(data.getData().getComposition());
														spu.setProductOrigin(data.getData().getMadeIn());
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
														
														//图片================================
														List<String> picList = new ArrayList<String>();
														for(com.shangpin.iog.magway.dto.Pictures pic:data.getData().getPictures()){
															picList.add(pic.getPictureUrl());
														}
														try{
															productFetchService.savePicture(supplierId, spu.getSpuId(), null, picList);
														}catch(Exception ex){
															ex.printStackTrace();
														}
														
													}catch(Exception ex){
														ex.printStackTrace();
														error.error(ex); 
													}
													
												}
												
											} catch (Exception e) {
												error.error(e); 
											}
											
										}
									}
									
								} catch (Exception e) {
									error.error(e); 
								}
								
							//}
						//}
					}
				//}
				
			}
		
		}catch(Exception e){
			e.printStackTrace();
			error.error(e); 
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
		
		logger.info("抓取结束");
	}
	
	public Token getToken(){
		Token token = null;
		try{
			
			Map<String,String> param = new HashMap<String,String>();
			param.put("grant_type", grant_type);
			Map<String,String> headerMap = new HashMap<String,String>();
			headerMap.put("Authorization", Authorization);
			String result = HttpUtil45.post(url_token, param, headerMap, outTimeConf);
			logger.info("Token===="+result);
			System.out.println(result);
			Gson gson = new Gson();
			token = gson.fromJson(result, Token.class);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return token;
		
	}
	
	
	
	
	
	
}

