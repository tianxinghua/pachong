package com.shangpin.iog.lungolivigno.service;

import java.io.IOException;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
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
		url_login = bdl.getString("url_login");
		url_getProducts = bdl.getString("url_getProducts");
//		url_getAttributes = bdl.getString("url_getAttributes");
		user_name = bdl.getString("user_name");
		user_password = bdl.getString("user_password");
		url_getPriceList = bdl.getString("url_getPriceList");
		url_getPrice = bdl.getString("url_getPrice");
	}
	
	private static Gson gson =  new Gson();
	private static OutTimeConfig outTimeConf = new OutTimeConfig(1000*5, 1000*60 * 60, 1000*60 * 60);
	
	public Map<String, Object> fetchProductAndSave() {
		
		try{
			//具体的业务
			//获取图片
			Map<String,List<String>> picMap= new HashMap<>();
			try {
				FTPUtils ftpUtils = new FTPUtils("Shangpin","Shangpin17!","88.149.230.95",24);
				picMap =ftpUtils.listPicUrl();
			} catch (IOException e) {
				e.printStackTrace();
			}
			while(picMap.size()<1){
				Thread.sleep(1000*60*3);
				try {
					FTPUtils ftpUtils = new FTPUtils("Shangpin","Shangpin17!","88.149.230.95",24);
					picMap =ftpUtils.listPicUrl();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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
				requestProductsDTO.setFromDate("20170101");
				requestProductsDTO.setPriceList(italyPriceList);
				requestProductsDTO.setWithStock(true);
				int offset = 1;



				while(true){
					//获取产品
					sessionId = login();
					Pagination pagination = new Pagination();
					pagination.setCount(10);
					pagination.setOffset(offset);
					requestProductsDTO.setPagination(pagination);
					String jsonValuePro = gson.toJson(requestProductsDTO); 
					String url = url_getProducts+sessionId;
					System.out.println(url);
					System.out.println("request parameter:"+ jsonValuePro);
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
								/**
								 * 单独的接口获取供价
								 */
								RequestPriceDTO requestPriceDTO =  new RequestPriceDTO();
								Pagination pagination1 = new Pagination();
								pagination1.setCount(10);
								pagination1.setOffset(1); 
								List<String> skus = new ArrayList<String>();
								skus.add(resultDTO.getSku());
								requestPriceDTO.setSku(skus);
								requestPriceDTO.setPriceList(shangpinPriceList); 
								requestPriceDTO.setPagination(pagination1);
								String priceParam = gson.toJson(requestPriceDTO);
								logger.info(resultDTO.getSku()+"请求价格接口GetPrice开始。");
								String priceJson = getDataOfInterface(url_getPrice+sessionId, priceParam);
								ResponseGetPrice responseGetPrice = null;
								if(StringUtils.isNotBlank(priceJson)){
									responseGetPrice = gson.fromJson(priceJson, ResponseGetPrice.class);
								}else{
									logger.info(resultDTO.getSku()+"获取价格失败。");
								}
								/**
								 * 设置供价
								 */
								List<Sizes> lists = new ArrayList<Sizes>();
								for(Sizes sizes : resultDTO.getSizes()){
									if(null != responseGetPrice && responseGetPrice.isStatus()){
			                        	for(Sizes sizePrice :responseGetPrice.getResult().get(0).getSizes()){
				                        	if(sizes.getSizeIndex() == sizePrice.getSizeIndex()){
				                        		sizes.setSupplierPrice(sizePrice.getPrice()); 
				                        		break;
				                        	}
				                        }
			                        }
									lists.add(sizes);
								}
								resultDTO.setSizes(lists);
								//添加图片
                                if(picMap.containsKey(resultDTO.getSku())){
                                	resultDTO.setPicUrls(picMap.get(resultDTO.getSku()));
								}


								
								supp.setData(gson.toJson(resultDTO));
								pushMessage(null);
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						offset = offset + 10 ;
					}else{
						break;
					}
				}
			}else{
				logger.info("##################登录失败##################################");
			}
		
		}catch(Exception ex){
			loggerError.error(ex);
		}
		return null;		
	}
	
	/**
	 * 请求接口
	 * @param url
	 * @param jsonParam
	 * @return
	 */
	private String getDataOfInterface(String url,String jsonParam){
		logger.info("请求的url："+url);
		logger.info("请求的参数："+jsonParam);
		System.out.println("request parameter:" + jsonParam);
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

