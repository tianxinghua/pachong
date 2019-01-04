package com.shangpin.iog.newMenlook.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.SpuDTO;

public class Test {
	
	private static Logger logInfo  = Logger.getLogger("info");
	private static Logger logError = Logger.getLogger("error");
	private static Logger logMongoDB = Logger.getLogger("MongoDB");
	
	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static String product_lists_url = "";	
	private static String login_url = "";
	private static int day;
	private static OutTimeConfig outTimeConf = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);

//	public static void main(String[] args){
//		OutTimeConfig outTimeConf = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);
//		String basket_submit = "https://staging.menlook.com/dw/shop/v15_9/baskets/bcMv2iaaiOJ7EaaadqA5Y8WqaX/submit?client_id=c8f0a7ef-dee7-4b94-8e5c-4ee108e61e26";
//		Map<String,String> headerMapSM = new HashMap<String,String>();
//		headerMapSM.put("Authorization", "Bearer eyJfdiI6IjEiLCJhbGciOiJSUzI1NiIsInR5cCI6IkpXUyJ9.eyJfdiI6IjEiLCJleHAiOjE0NTIyNDEwMDMsImlhdCI6MTQ1MjIzOTIwMywiaXNzIjoiYzhmMGE3ZWYtZGVlNy00Yjk0LThlNWMtNGVlMTA4ZTYxZTI2Iiwic3ViIjoie1wiX3ZcIjpcIjFcIixcImN1c3RvbWVyX2luZm9cIjp7XCJjdXN0b21lcl9pZFwiOlwiYWRVMDNMaUhPdWJTb1VCblFhRjJONXc3OW1cIixcImd1ZXN0XCI6dHJ1ZX19In0.Yht7XhsI_uOv2XbL9bQeaDN3DQ0ruLzgEbmNLEIZWUZgDtzr4Yt2HAp4ZixpWgJq_4BcseAWC6mQNjSaAPvfoL9e0RFV1LofpxNWVLpzgPCnMz3HtM7QXjhPiuFlrYBHpJg-iPl7nyeh8eQFA0-fP6SgD-jHCK0w-D3f0Auxe10Af2AckG9lhNsLyP-bQgUi0qxfmngUIYvchv67gNhqSznF9PDTsUA2io_24ZGdU3ZdchJk38FgUVGsCjqataWfs_WeCaURacqkNkbrBaYnzZgFHZUmO9usDyANXQK_LUI2kOwfHHKwFJm35DlOEmEvlR_SixI0pETjR7tBAcOw6mDiGl_S6U1QzP3Fw2-RLlSc5l5wgfEF6QgVqrjko6qq9cKSWde5MJDIXbXQNbB38wCo-ZJ8BE9GVEBuJIadNRMlu5JGpBn6pzyoTxSyq9-IU79hjfQt4CzuL6D6-hrUf098is_34e6YpxBn2U5dou8");
//		headerMapSM.put("If-Match", "262858cd1bac31abd0faef2129925f5c04ba35bdf77d773009747871af421dcb");
//		headerMapSM.put("Content-Type", "application/json");
////		String gouwucheSubmit = HttpUtil45.postAuth(basket_submit, null, headerMapSM, outTimeConf, "", "");
////		System.out.println("gouwucheSubmit==="+gouwucheSubmit);
//		try {
//			String param = "{\"amount\": 2,\"payment_card\": {\"number\": \"4111111111111111\",\"security_code\": \"112\",\"holder\": \"Ed Smith\",\"card_type\": \"Visa\",\"expiration_month\": 2,\"expiration_year\": 2022},\"payment_method_id\": \"CREDIT_CARD.Visa\"}";
//			String payment = HttpUtil45.operateData("post", "json", "https://staging.menlook.com/dw/shop/v15_9/orders/bcMv2iaaiOJ7EaaadqA5Y8WqaX/payment_instruments?client_id=c8f0a7ef-dee7-4b94-8e5c-4ee108e61e26", outTimeConf, null, param, headerMapSM, "", "");
//			System.out.println(payment);
//			
//		} catch (ServiceException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	
//	static {
//		if (null == bdl)
//			bdl = ResourceBundle.getBundle("conf");
//		supplierId = bdl.getString("supplierId");
//		product_lists_url = bdl.getString("product_lists_url");
//		login_url = bdl.getString("login_url");
//		day = Integer.valueOf(bdl.getString("day"));
//	}

	public void fetchProductAndSave(){
		OutTimeConfig outTimeConf = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);
		CloseableHttpResponse resp = null;
		try {
			//登录
//			Map<String,String> map = new HashMap<String,String>();
//			map.put("username", "test-menlook@hotmail.fr");
//			map.put("password", "Meninvest2014");			
//			String jsonValue = JSONObject.fromObject(map).toString();
//			System.out.println(jsonValue);
//			String login = HttpUtil45.operateData("post","json",login_url,outTimeConf,null,jsonValue,"","");
//			System.out.println("login==="+login);
//			//产品list
//			String result = HttpUtil45.operateData("get", "", product_lists_url, outTimeConf, null, "", "", "");
//			System.out.println("result==="+result);
//			JSONArray array = JSONObject.fromObject(result).getJSONArray("data");
//			for(int i=0;i<array.size();i++){
//				String link = array.getJSONObject(i).getString("link");
//				String lists = HttpUtil45.get(link, outTimeConf, null);
//				System.out.println("lists==="+lists);
//				String item_link = JSONObject.fromObject(lists).getJSONObject("items_link").getString("link");
//				String item =  HttpUtil45.get(item_link, outTimeConf, null);
//				System.out.println("item==="+item);
//				if(JSONObject.fromObject(item).containsKey("data")){
//					JSONArray itemArr = JSONObject.fromObject(item).getJSONArray("data");
//					for(int k=0;k<itemArr.size();k++){
//						String details_link = itemArr.getJSONObject(k).getJSONObject("product_details_link").getString("link");
//						String detail = HttpUtil45.get(details_link, outTimeConf, null);
//						System.out.println("detail==="+detail);
//					}
//				}
//			}
			
//			String lists = HttpUtil45.get("https://staging.menlook.com/dw/shop/v15_9/product_lists/cdiGkiaaiOg6oaaadhU4bNqx3J?last_name=Meninvest&first_name=Test&client_id=c8f0a7ef-dee7-4b94-8e5c-4ee108e61e26&email=test-menlook%40hotmail.fr", outTimeConf, null);
//			System.out.println("lists==="+lists);
			
			//单个产品 OK
			//String ttt = HttpUtil45.get("https://staging.menlook.com/dw/shop/v15_9/products/215019?client_id=c8f0a7ef-dee7-4b94-8e5c-4ee108e61e26", outTimeConf, null);
			//System.out.println(ttt);
			//库存 OK
			//String sss = HttpUtil45.get("https://staging.menlook.com/dw/shop/v15_9/products/215019/availability?client_id=c8f0a7ef-dee7-4b94-8e5c-4ee108e61e26", outTimeConf, null);
			//System.out.println(sss);
			
			//注册 未激活
//			String url = "https://staging.menlook.com/dw/shop/v15_9/account/register?client_id=c8f0a7ef-dee7-4b94-8e5c-4ee108e61e26";
//			String jsonValue = "{\"credentials\":{\"username\":\"test-menlook@hotmail.fr\",\"password\":\"Meninvest2014\"},\"profile\":{\"email\":\"test-menlook@hotmail.fr\",\"first_name\":\"Test\",\"last_name\":\"Meninvest\",}}";
//			String register = HttpUtil45.operateData("post", "json", url, outTimeConf, null, jsonValue, "", "");
//			System.out.println(register);
			
			//认证
			String jsonValue = "{\"type\":\"guest\"}";
			String auth = HttpUtil45.getResponseHead("post", "json", "https://staging.menlook.com/dw/shop/v15_9/customers/auth?client_id=c8f0a7ef-dee7-4b94-8e5c-4ee108e61e26", outTimeConf, null, jsonValue,"Authorization", "", "");
			System.out.println("auth==="+auth);
			logInfo.info("auth==="+auth);
			if(StringUtils.isNotBlank(auth)){
				//创建购物车
				String basket_url = "https://staging.menlook.com/dw/shop/v15_9/baskets?client_id=c8f0a7ef-dee7-4b94-8e5c-4ee108e61e26";
				Map<String,String> headerMap = new HashMap<String,String>();
				headerMap.put("Authorization", auth);
				headerMap.put("Content-Type", "application/json");
				String basket_JValue = getBasketJsonParam(null);
				
//				String aaa = HttpUtil45.operateData("post", "json", basket_url, outTimeConf, null, basket_JValue, headerMap, "", "");
				
				resp = HttpUtil45.operateData2("post", "json", basket_url, outTimeConf, null, basket_JValue, headerMap, "", "");
				HttpEntity entity=resp.getEntity();
				String basket = EntityUtils.toString(entity,"UTF-8");
				EntityUtils.consume(entity);
				System.out.println("basket==="+basket);
				logInfo.info("basket==="+basket);
				String basketETag = resp.getFirstHeader("ETag").getValue();
				System.out.println("basketETag==="+basketETag);
				logInfo.info("basketETag==="+basketETag);
				if(StringUtils.isNotBlank(basket) && JSONObject.fromObject(basket).containsKey("basket_id") && StringUtils.isNotBlank("basketETag")){
					String basket_id = JSONObject.fromObject(basket).getString("basket_id");
//					//支持的支付方式
//					Map<String,String> paymentHeaderMap = new HashMap<String,String>();
//					paymentHeaderMap.put("Authorization", auth);
//					paymentHeaderMap.put("If-Match", basketETag);
//					String payment_methods = HttpUtil45.operateData("get", "", "https://staging.menlook.com/dw/shop/v15_9/baskets/"+basket_id+"/payment_methods?client_id=c8f0a7ef-dee7-4b94-8e5c-4ee108e61e26", outTimeConf, null, "", paymentHeaderMap, "", "");
//					System.out.println("payment_methods==="+payment_methods);
					//添加购物车
					
//					String basket_item = "https://staging.menlook.com/dw/shop/v15_9/baskets/"+basket_id+"/items?client_id=c8f0a7ef-dee7-4b94-8e5c-4ee108e61e26";
//					Map<String,String> headerMapBI = new HashMap<String,String>();
//					headerMapBI.put("Authorization", auth);
//					headerMapBI.put("If-Match", basketETag);
//					headerMapBI.put("Content-Type", "application/json");
//					Map<String,Object> mapBI = new HashMap<String,Object>();
//					mapBI.put("product_id", "205461");
//					mapBI.put("quantity", 1);
//					mapBI.put("price", 4.3);
//					String jsValueBI = JSONObject.fromObject(mapBI).toString();
//					String gouwuche = HttpUtil45.operateData("post", "json", basket_item, outTimeConf, null, jsValueBI, headerMapBI,"", "");
//					System.out.println("gouwuche==="+gouwuche);
//					logInfo.info("gouwuche==="+gouwuche);
					//提交购物车
					String basket_submit = "https://staging.menlook.com/dw/shop/v15_9/baskets/"+basket_id+"/submit?client_id=c8f0a7ef-dee7-4b94-8e5c-4ee108e61e26";
					Map<String,String> headerMapSM = new HashMap<String,String>();
					headerMapSM.put("Authorization", auth);
					headerMapSM.put("If-Match", basketETag);
					headerMapSM.put("Content-Type", "application/json");
					String gouwucheSubmit = HttpUtil45.postAuth(basket_submit, null, headerMapSM, outTimeConf, "", "");
					System.out.println("gouwucheSubmit==="+gouwucheSubmit);
					//保存CustomerJWT、ETag和order_no 
					
				}
				 
			}
			
			//支付订单
			
			
			
		
		
		} catch (Exception e) {
			logError.error(e);
			e.printStackTrace();
		}finally{
			try {
				if(resp!=null)
					resp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getBasketJsonParam(OrderDTO orderDTO){
		Map<String,Object> param = new HashMap<String,Object>();
		//产品信息
		List<Map<String, Object>> product_items = new ArrayList<Map<String,Object>>();
		Map<String,Object> product_item = new HashMap<String,Object>();
		product_item.put("product_id", "118060");
		product_item.put("quantity", 1);
		product_item.put("price", 45.5);
		product_items.add(product_item);
		//支付方式
		List<Map<String, Object>> payment_instruments = new ArrayList<Map<String,Object>>();
		Map<String,Object> payment_instrument = new HashMap<String,Object>();
		payment_instrument.put("payment_method_id", "BML");
		payment_instruments.add(payment_instrument);
		//账目地址
		Map<String,Object> billing_address = new HashMap<>();
		billing_address.put("address1", "北京市朝阳区三间房东路1号懋隆文化创意产业园A栋办公楼");
		billing_address.put("address2", "北京市朝阳区三间房东路1号懋隆文化创意产业园A栋办公楼");
		billing_address.put("company_name", "北京尚品百姿电子商务有限公司");
		billing_address.put("city", "北京");
		billing_address.put("country_code", "100024");
		//客户信息
		Map<String,Object> customer_info = new HashMap<>();
		customer_info.put("email", "buyer@shangpin.com");
		//订单地址信息
		List<Map<String,Object>> shipments = new ArrayList<>();
		Map<String,Object> shipment = new HashMap<String,Object>();
		Map<String,Object> address = new HashMap<>();
		address.put("address1", "中国北京朝阳000路000号000小区");
		address.put("city", "北京");
		address.put("company_name", "尚品");
		address.put("country_code", "100000");
		shipment.put("shipping_address", address);
		shipment.put("shipment_id", "me");
		shipments.add(shipment);
		
		param.put("product_total", 45.5);
		param.put("order_total", 45.5);
		param.put("product_items", product_items);
		param.put("billing_address", billing_address);
		param.put("customer_info", customer_info);
		param.put("payment_instruments", payment_instruments);
		param.put("shipments", shipments);
		
		return JSONObject.fromObject(param).toString();
	}
	
	
	public static void main(String[] args){
		try{
			
			String url = "http://staging.menlook.com/dw/shop/v15_9/products/M2045_1964_Beige?expand=availability,bundled_products,links,promotions,options,prices,variations,set_products&locale=fr&client_id=c8f0a7ef-dee7-4b94-8e5c-4ee108e61e26";
			String result = HttpUtil45.get(url, outTimeConf, null);
			JSONObject spuObject = JSONObject.fromObject(result);
			
			//spu
	        SpuDTO spu = new SpuDTO();
	        spu.setBrandName(spuObject.getString("brand")); 
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
}
