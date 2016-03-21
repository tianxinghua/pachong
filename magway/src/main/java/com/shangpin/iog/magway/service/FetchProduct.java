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
import com.shangpin.iog.magway.dto.GoodsTypeList;
import com.shangpin.iog.magway.dto.SeasonList;
import com.shangpin.iog.magway.dto.Token;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.iog.magway.dto.GoodsCatgList;
import com.shangpin.iog.magway.dto.GoodsList;
import com.shangpin.iog.magway.dto.DataG;
import com.shangpin.iog.magway.dto.DataT;
import com.shangpin.iog.magway.dto.GoodsDetail;

@Component("magway")
public class FetchProduct {

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
	private static String url_getGoodsList = null;
	private static String url_getGoodsDetail = null;
	private static String Authorization = null;
	private static String grant_type = null;
	
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
		url_getGoodsList = bdl.getString("url_getGoodsList");
		url_getGoodsDetail = bdl.getString("url_getGoodsDetail");
		Authorization = bdl.getString("Authorization");
		grant_type = bdl.getString("grant_type");
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
				String goodslistStr = HttpUtil45.get(url_getGoodsList, outTimeConf, null, headMap, "", "");
				logger.info("goodslistStr===="+goodslistStr); 
				GoodsList goodsList = new Gson().fromJson(goodslistStr, GoodsList.class);
				for(DataG data:goodsList.getData()){
					try{
						
						SpuDTO spu = new SpuDTO();
						spu.setId(UUIDGenerator.getUUID());
						spu.setSupplierId(supplierId);
						spu.setSpuId(data.getID());
						String categoryGender = "";
						for(DataT datat:goodsTypeList.getData()){
							if(data.getTypeId().equals(datat.getID())){
								categoryGender = datat.getName();
								break;
							}
						}
						spu.setCategoryGender(categoryGender);
						spu.setCategoryId(data.getCategoryId());
						String categoryName = "";
						for(com.shangpin.iog.magway.dto.DataCat dataCat:goodsCatgList.getData()){
							if(data.getCategoryId().equals(dataCat.getCategoryID())){
								categoryName = dataCat.getName();
								break;
							}
						}
						spu.setCategoryName(categoryName);
						spu.setSeasonId(data.getSeason());
						String seasonName = "";
						for(com.shangpin.iog.magway.dto.DataS dataS:seasonList.getData()){
							if(dataS.getSeasonCode().equals(data.getSeason())){
								seasonName = dataS.getName();
								break;
							}
						}
						spu.setSeasonName(seasonName);
						spu.setBrandId(data.getBrandId());
						String brandName = "";
						for(com.shangpin.iog.magway.dto.Data brand:brandList.getData()){
							if(data.getBrandId().equals(brand.getID())){
								brandName = brand.getName();
								break;
							}
						}
						spu.setBrandName(brandName);
						
						String detailUri = url_getGoodsDetail+data.getID();
						String goodsDetailStr = HttpUtil45.get(detailUri, outTimeConf, null, headMap, "", "");				
						GoodsDetail goodsDetail = new Gson().fromJson(goodsDetailStr, GoodsDetail.class);
						try{
							spu.setMaterial(goodsDetail.getData().getComposition());
						}catch(Exception ex){
							error.error(ex);
							error.error(goodsDetail.getData().getID());
						}					
						spu.setProductOrigin(goodsDetail.getData().getMadeIn());
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
						
						List<String> picList = new ArrayList<String>();
						for(com.shangpin.iog.magway.dto.Pictures pic:goodsDetail.getData().getPictures()){
							picList.add(pic.getPictureUrl());
						}
						try{
							productFetchService.savePicture(supplierId, spu.getSpuId(), null, picList);
						}catch(Exception ex){
							ex.printStackTrace();
						}					
						
						for(com.shangpin.iog.magway.dto.Stocks stock:goodsDetail.getData().getStocks()){
							try{
								
								SkuDTO sku = new SkuDTO();
								sku.setId(UUIDGenerator.getUUID());
								sku.setSupplierId(supplierId);
								sku.setSkuId(data.getID()+"-"+stock.getSize()); 
								sku.setSpuId(data.getID());
								sku.setProductName(data.getName());
								sku.setMarketPrice(data.getPriceCN());
								sku.setSalePrice(data.getYourPriceCN());
								sku.setSaleCurrency("China"); 
								sku.setProductCode(data.getCode());
								String color = "";
								for(com.shangpin.iog.magway.dto.DataC dataC:colorList.getData()){
									if(goodsDetail.getData().getColor().equals(dataC.getId())){
										color = dataC.getName();
										break;
									}
								}
								if(StringUtils.isBlank(color)){
									color = goodsDetail.getData().getColor();
								}
								sku.setColor(color);
								sku.setProductSize(stock.getSize());
								sku.setStock(stock.getQty()); 
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
								
							}catch(Exception ex){
								ex.printStackTrace();
								error.error(ex); 
							}
							
						}
						
					}catch(Exception e){
						e.printStackTrace();
						error.error(e); 
					}
					
					
				}
				
				
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

