package com.shangpin.iog.lungolivigno.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.lungolivigno.dto.LoginDTO;
import com.shangpin.iog.lungolivigno.dto.Pagination;
import com.shangpin.iog.lungolivigno.dto.PriceList;
import com.shangpin.iog.lungolivigno.dto.RequestPriceDTO;
import com.shangpin.iog.lungolivigno.dto.RequestProductsDTO;
import com.shangpin.iog.lungolivigno.dto.ResponseGetPrice;
import com.shangpin.iog.lungolivigno.dto.ResponseProductsDTO;
import com.shangpin.iog.lungolivigno.dto.Result;
import com.shangpin.iog.lungolivigno.dto.Sizes;
import com.shangpin.iog.lungolivigno.dto.User;
import com.shangpin.product.AbsSaveProduct;

/**
 * Created by lubaijiang on 2015/9/14.
 */

@Component("lungolivigno")
public class FetchProduct extends AbsSaveProduct{

	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	
	private static String url_login = null;
	private static String url_getProducts = null;
//	private static String url_getAttributes = null;
	private static String user_name = null;
	private static String user_password = null;
	private static String url_getPriceList = null;
	private static String url_getPrice = null;
	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		url_login = bdl.getString("url_login");
		url_getProducts = bdl.getString("url_getProducts");
//		url_getAttributes = bdl.getString("url_getAttributes");
		user_name = bdl.getString("user_name");
		user_password = bdl.getString("user_password");
		url_getPriceList = bdl.getString("url_getPriceList");
		url_getPrice = bdl.getString("url_getPrice");
	}
	
	private static Gson gson =  new Gson();
	private static OutTimeConfig outTimeConf = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);
	/**
	 * 请求接口
	 * @param url
	 * @param jsonParam
	 * @return
	 */
	private String getDataOfInterface(String url,String jsonParam){
		logger.info("请求的url："+url);
		logger.info("请求的参数："+jsonParam); 
		String data = "";
		int i = 0;
		while(i<20){
			try {
				if(StringUtils.isBlank(jsonParam)){
					data = HttpUtil45.operateData("post", "", url, outTimeConf, null, "", "", "");
				}else{
					data = HttpUtil45.operateData("post", "json", url, outTimeConf, null, jsonParam, "", "");
				}
				return data;
			} catch (Exception e) {
				i ++ ;
				loggerError.error("请求接口"+url+"第"+i+"次异常："+e.getMessage(),e); 
			}
		}
		loggerError.error("请求接口"+url+"最终失败。"); 
		return data;
	}
	
	public Map<String, Object> fetchProductAndSave() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String,List<String>> imageMap = new HashMap<String, List<String>>();
		
		try{
			//具体的业务
			String sessionId = login();
			if(StringUtils.isNotBlank(sessionId)){
				//获取价格
				logger.info("===========开始获取priceList=========");
				String priceListJson = getDataOfInterface(url_getPriceList+sessionId, "");
				System.out.println(priceListJson); 
				PriceList priceList = gson.fromJson(priceListJson, PriceList.class);
				String italyPriceList = priceList.getResult().get(0).getCode();
				String shangpinPriceList = priceList.getResult().get(1).getCode();
				RequestProductsDTO requestProductsDTO = new RequestProductsDTO();
				requestProductsDTO.setFromDate("20160101");
				requestProductsDTO.setPriceList(italyPriceList);
				requestProductsDTO.setWithStock(true);
				int offset = 1;
				while(true){
					//获取产品
					sessionId = login();
					Pagination pagination = new Pagination();
					pagination.setCount(100);
					pagination.setOffset(offset);
					requestProductsDTO.setPagination(pagination);
					String jsonValuePro = gson.toJson(requestProductsDTO); 
					String url = url_getProducts+sessionId;
					System.out.println(url);
					logger.info("开始获取第"+offset+"页产品。"); 
					String productsResult = getDataOfInterface(url,jsonValuePro);
					System.out.println(productsResult);
					if(StringUtils.isBlank(productsResult)){
						logger.info("第"+offset+"页产品获取失败。结束拉取。");
						return null;
					}
					ResponseProductsDTO responseProductsDTO = gson.fromJson(productsResult, ResponseProductsDTO.class);
					if(null != responseProductsDTO.getResult() && responseProductsDTO.getResult().size() >0){
						for(Result  resultDTO :responseProductsDTO.getResult()){
							try {
								sessionId = login();
								SpuDTO spu = new SpuDTO();
								spu.setId(UUIDGenerator.getUUID());
								spu.setSupplierId(supplierId);
								spu.setSpuId(resultDTO.getSku());
								spu.setCategoryGender(resultDTO.getAttributes().get(2).getValue());
								spu.setCategoryName(resultDTO.getAttributes().get(8).getValue().trim());
								spu.setBrandName(resultDTO.getAttributes().get(1).getValue());
								spu.setSeasonName(resultDTO.getAttributes().get(3).getValue()+resultDTO.getAttributes().get(4).getValue());
								String material = resultDTO.getAttributes().get(10).getValue().trim();
								spu.setMaterial(StringUtils.isNotBlank(material)? material:resultDTO.getAttributes().get(10).getCode().trim());
								String origin = resultDTO.getAttributes().get(6).getValue();
								spu.setProductOrigin(StringUtils.isNotBlank(origin)? origin:resultDTO.getAttributes().get(6).getCode());
								spuList.add(spu);
								
								//请求获取价格
								RequestPriceDTO requestPriceDTO =  new RequestPriceDTO();
								Pagination pagination1 = new Pagination();
								pagination1.setCount(100);
								pagination1.setOffset(1); 
								List<String> skus = new ArrayList<String>();
								skus.add(resultDTO.getSku());
								requestPriceDTO.setSku(skus);
								requestPriceDTO.setPriceList(shangpinPriceList); 
								requestPriceDTO.setPagination(pagination1);
								String priceParam = gson.toJson(requestPriceDTO);
								logger.info(spu.getSpuId()+"请求价格接口GetPrice开始。");
								String priceJson = getDataOfInterface(url_getPrice+sessionId, priceParam);
								ResponseGetPrice responseGetPrice = null;
								if(StringUtils.isNotBlank(priceJson)){
									responseGetPrice = gson.fromJson(priceJson, ResponseGetPrice.class);
								}else{
									logger.info(spu.getSpuId()+"获取价格失败。");
								}
								for(Sizes sizes : resultDTO.getSizes()){
									try {
										String size = sizes.getLabel();
										if(StringUtils.isNotBlank(sizes.getLabel()) && sizes.getLabel().contains("½")){
											size = size.replaceAll("½", "+");
										}
										
										SkuDTO sku = new SkuDTO();
										sku.setId(UUIDGenerator.getUUID());
				                        sku.setSupplierId(supplierId);
				                        sku.setSpuId(spu.getSpuId());
				                        sku.setSkuId(resultDTO.getSku()+"-"+sizes.getSizeIndex());
				                        sku.setProductName(resultDTO.getName());
				                        sku.setMarketPrice(sizes.getPrice());
				                        sku.setSalePrice("");
				                        String supplierPrice = "";
				                        if(null != responseGetPrice && responseGetPrice.isStatus()){
				                        	for(Sizes sizePrice :responseGetPrice.getResult().get(0).getSizes()){
					                        	if(sizes.getSizeIndex() == sizePrice.getSizeIndex()){
					                        		supplierPrice = sizePrice.getPrice();
					                        		break;
					                        	}
					                        }
				                        }
				                        sku.setSupplierPrice(supplierPrice); 
				                        sku.setProductCode(resultDTO.getAttributes().get(5).getCode());
				                        sku.setColor(resultDTO.getAttributes().get(9).getValue().trim());
				                        sku.setProductSize(size);
				                        sku.setStock(""+sizes.getQty());
				                        skuList.add(sku);
				                        
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						offset = offset + 100 ;
					}else{
						break;
					}
				}
//				String urlAttr = url_getAttributes+sessionId;
//				RequestAttributeDTO requestAttributeDTO = new RequestAttributeDTO();
//				requestAttributeDTO.setOffset(1);
//				requestAttributeDTO.setCount(10000);
//				String requestAttributeStr = new Gson().toJson(requestAttributeDTO);
//				String responseAttriStr = HttpUtil45.operateData("post", "json", urlAttr, outTimeConf, null, requestAttributeStr, "", "");
//				System.out.println(responseAttriStr); 
				
			}else{
				logger.info("##################登录失败##################################");
			}
//			String urlStock = url_getStock+sessionId;
//			System.out.println(urlStock);
//			String productsResult = HttpUtil45.post(urlStock, outTimeConf);
//			System.out.println(productsResult); 
		
		}catch(Exception ex){
			loggerError.error(ex);
		}
		
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;		
	}

	private String login(){
		try{
			System.out.println("-------------开始登陆----------------");
			OutTimeConfig outTimeConf = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);
			User user = new User();
			user.setUserName(user_name);
			user.setPassword(user_password);			
			String jsonValue = new Gson().toJson(user); 			
			String result = HttpUtil45.operateData("post", "json", url_login, outTimeConf, null, jsonValue, "", "");
			logger.info("login result==="+result);
			int i = 0;
			while(HttpUtil45.errorResult.equals(result) && i<10){
				result = HttpUtil45.operateData("post", "json", url_login, outTimeConf, null, jsonValue, "", "");
				i++;
			}
			if(!HttpUtil45.errorResult.equals(result)){
				logger.info("登录了第 "+i+"次登录成功");
				System.out.println("登录了第 "+i+"次登录成功"); 
				LoginDTO LoginDTO = new Gson().fromJson(result, LoginDTO.class);
				return LoginDTO.getResult();
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("登录异常："+e.getMessage(),e); 
		}
		return null;
	}
	
	public static void main(String[] args) {
		FetchProduct f = new FetchProduct();
		f.fetchProductAndSave();
	}
	
}

