package com.shangpin.iog.lungolivigno.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

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
import com.shangpin.iog.lungolivigno.dto.LoginDTO;
import com.shangpin.iog.lungolivigno.dto.Pagination;
import com.shangpin.iog.lungolivigno.dto.RequestAttributeDTO;
import com.shangpin.iog.lungolivigno.dto.RequestProductsDTO;
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
	private static String url_getAttributes = null;
	private static String user_name = null;
	private static String user_password = null;
	private static String url_getPriceList = null;
	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		url_login = bdl.getString("url_login");
		url_getProducts = bdl.getString("url_getProducts");
		url_getAttributes = bdl.getString("url_getAttributes");
		user_name = bdl.getString("user_name");
		user_password = bdl.getString("user_password");
		url_getPriceList = bdl.getString("url_getPriceList");
	}
	
	public Map<String, Object> fetchProductAndSave() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String,List<String>> imageMap = new HashMap<String, List<String>>();
		
		try{
			//具体的业务
			
			//skuList.add(sku);
			//spuList.add(spu);
			/**
			List<String> list = new ArrayList<String>();
			imageMap.put(sku.getSkuId()+";"+sku.getProductCode()+" "+sku.getColor(), list);
			**/
			System.out.println("-------------开始登陆----------------");
			OutTimeConfig outTimeConf = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);
			User user = new User();
			user.setUserName(user_name);
			user.setPassword(user_password);			
			String jsonValue = new Gson().toJson(user); 			
			String result = HttpUtil45.operateData("post", "json", url_login, outTimeConf, null, jsonValue, "", "");
			logger.info("login result==="+result);
			int i = 0;
			while(HttpUtil45.errorResult.equals(result) && i<100){
				result = HttpUtil45.operateData("post", "json", url_login, outTimeConf, null, jsonValue, "", "");
				i++;
			}
			if(!HttpUtil45.errorResult.equals(result)){
				logger.info("登录了第 "+i+"次登录成功");
				System.out.println("登录了第 "+i+"次登录成功"); 
				LoginDTO LoginDTO = new Gson().fromJson(result, LoginDTO.class);
				String sessionId = LoginDTO.getResult();
				String url = url_getProducts+sessionId;
				System.out.println(url);
				Pagination pagination = new Pagination();
				pagination.setCount(100000);
				pagination.setOffset(1);
				RequestProductsDTO requestProductsDTO = new RequestProductsDTO();
				requestProductsDTO.setFromDate("20160501");
				requestProductsDTO.setPagination(pagination);
				requestProductsDTO.setPriceList("01");
				requestProductsDTO.setWithStock("true");
				String jsonValuePro = new Gson().toJson(requestProductsDTO); 
				logger.info(jsonValuePro); 
				String productsResult = HttpUtil45.operateData("post", "json", url, outTimeConf, null, jsonValuePro, "", "");
				System.out.println(productsResult); 
				logger.info("=================获取数据结束==============");
				System.out.println("=================获取数据结束==============");
				ResponseProductsDTO responseProductsDTO = new Gson().fromJson(productsResult, ResponseProductsDTO.class);
				logger.info("=================转化对象结束==============");
				System.out.println("=================转化对象结束==============");
				for(Result  resultDTO :responseProductsDTO.getResult()){
					try {
						SpuDTO spu = new SpuDTO();
						spu.setId(UUIDGenerator.getUUID());
						spu.setSupplierId(supplierId);
						spu.setSpuId(resultDTO.getSku());
						spu.setCategoryGender(resultDTO.getAttributes().get(2).getValue());
						spu.setCategoryName(resultDTO.getAttributes().get(8).getValue().trim());
						spu.setBrandName(resultDTO.getAttributes().get(1).getValue());
						spu.setSeasonName(resultDTO.getAttributes().get(4).getValue());
						String material = resultDTO.getAttributes().get(10).getValue().trim();
						spu.setMaterial(StringUtils.isNotBlank(material)? material:resultDTO.getAttributes().get(10).getCode().trim());
						String origin = resultDTO.getAttributes().get(6).getValue();
						spu.setProductOrigin(StringUtils.isNotBlank(origin)? origin:resultDTO.getAttributes().get(6).getCode());
						spuList.add(spu);
						
						for(Sizes sizes : resultDTO.getSizes()){
							try {
								
								SkuDTO sku = new SkuDTO();
								sku.setId(UUIDGenerator.getUUID());
		                        sku.setSupplierId(supplierId);
		                        sku.setSpuId(spu.getSpuId());
		                        sku.setSkuId(resultDTO.getSku()+"-"+sizes.getLabel());
		                        sku.setProductName(resultDTO.getName());
		                        sku.setMarketPrice("");
		                        sku.setSalePrice("");
		                        sku.setSupplierPrice(""+sizes.getPrice()); 
		                        sku.setProductCode(resultDTO.getAttributes().get(5).getCode());
		                        sku.setColor(resultDTO.getAttributes().get(9).getValue().trim());
		                        sku.setProductSize(sizes.getLabel());
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
	
	public static void main(String[] args) {
		FetchProduct f = new FetchProduct();
		f.fetchProductAndSave();
	}
	
}

