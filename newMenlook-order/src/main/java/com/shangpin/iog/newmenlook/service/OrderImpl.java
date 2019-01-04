package com.shangpin.iog.newmenlook.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.service.ProductSearchService;

@Component("newmenlook")
public class OrderImpl extends AbsOrderService {

	private static Logger logger = Logger.getLogger("error");
	private static Logger logInfo = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	private static String supplierNo = null;
	private static OutTimeConfig outTimeConf = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);
	
	static {
		if (null == bdl) {
			bdl = ResourceBundle.getBundle("conf");
			supplierId = bdl.getString("supplierId");
			supplierNo = bdl.getString("supplierNo");
		}
		
	}
	
	@Autowired
	ProductSearchService productSearchService;

	/**
	 * 下订单
	 */
	public void loopExecute() {
		this.checkoutOrderFromWMS(supplierNo, supplierId, true);
	}

	/**
	 * 确认支付
	 */
	public void confirmOrder() {
		this.confirmOrder(supplierId);
	}

	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {

		orderDTO.setStatus(OrderStatus.PLACED);
	}
	
	/**
	 * 构建下订单所需的参数
	 * @param orderDTO
	 * @return
	 */
	public String getBasketJsonParam(OrderDTO orderDTO){
		Map<String,Object> param = new HashMap<String,Object>();
		List<Map<String, Object>> product_items = new ArrayList<Map<String,Object>>();
		Map<String,Object> product_item = new HashMap<String,Object>();
		//拆分
		String detail = orderDTO.getDetail();
		String[] details = detail.split(",");
		int num = 0;
		String skuNo = null;
		for (int i = 0; i < details.length; i++) {
			num = Integer.parseInt(details[i].split(":")[1]);
			skuNo = details[i].split(":")[0];
		}
		String markPrice = "";
		try {
			Map tempmap = skuPriceService
					.getNewSkuPriceBySku(supplierId, skuNo);
			Map map = (Map) tempmap.get(supplierId);
			markPrice = (String) map.get(skuNo);
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		double totalPrice = 0;
		if (!"-1".equals(markPrice)) {
			String price = markPrice.split("\\|")[0];
			product_item.put("price", Double.parseDouble(price));
			totalPrice = (Double.parseDouble(price)) * num;
		} else {
			logger.info("产品sku= " + skuNo + " 没有市场价");
		}
		//产品信息		
		product_item.put("product_id", skuNo);
		product_item.put("quantity", num);		
		product_items.add(product_item);
		//支付方式
		List<Map<String, Object>> payment_instruments = new ArrayList<Map<String,Object>>();
		Map<String,Object> payment_instrument = new HashMap<String,Object>();
		payment_instrument.put("payment_method_id", "BML");
		payment_instruments.add(payment_instrument);
		//账目地址
		Map<String,Object> billing_address = new HashMap<>();
		billing_address.put("address1", "Piazza S. Freud n.1 20154 Milano - Italia");
		billing_address.put("company_name", "Genertec Italia S.r.l.");
		billing_address.put("city", "milan");
		billing_address.put("country_code", "20154");
		billing_address.put("first_name", "Filippo");
		billing_address.put("full_name", "Filippo Troina");
		billing_address.put("job_title", "warehouse manager");
		billing_address.put("last_name", "Troina");
		billing_address.put("phone", "0039 031 4149625");
		billing_address.put("post_box", "normal");
		billing_address.put("postal_code", "22075");
		billing_address.put("salutation", "Mr.");
		billing_address.put("second_name", "Troina");
		billing_address.put("state_code", "22075");
		billing_address.put("suffix", "no");
		billing_address.put("suite", "no");
		billing_address.put("title", "For Shangpin");
		//客户信息
		Map<String,Object> customer_info = new HashMap<>();
		customer_info.put("email", "liufang@shangpin.com");	
		//送货地址信息
		List<Map<String,Object>> shipments = new ArrayList<>();
		Map<String,Object> shipment = new HashMap<String,Object>();
		Map<String,Object> address = new HashMap<>();
		address.put("address1", "VIA G.LEOPARDI 27,22075 LURATE CACCIVIO (COMO)");
		address.put("company_name", "Genertec Italia S.r.l.");
		address.put("city", "Como");		
		address.put("country_code", "22075");
		address.put("first_name", "Filippo");
		address.put("full_name", "Filippo Troina");
		address.put("job_title", "warehouse manager");
		address.put("last_name", "Troina");
		address.put("phone", "0039 031 4149625");
		address.put("post_box", "normal");
		address.put("postal_code", "22075");
		address.put("salutation", "Mr.");
		address.put("second_name", "Troina");
		address.put("state_code", "22075");
		address.put("suffix", "no");
		address.put("suite", "no");
		address.put("title", "For Shangpin");
		shipment.put("shipping_address", address);
		//运输信息
		Map<String,Object> shipping_method = new HashMap<>();
		shipping_method.put("id", "MARKETPLACE_PLACEHOLDER");					
		shipment.put("shipping_method", shipping_method);
		shipments.add(shipment);
		
		param.put("shipments", shipments);
		param.put("product_total", totalPrice);
		param.put("order_total", totalPrice);
		param.put("product_items", product_items);
		param.put("billing_address", billing_address);
		param.put("customer_info", customer_info);
		param.put("payment_instruments", payment_instruments);
		
		return JSONObject.fromObject(param).toString();
	}
	
	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		
		CloseableHttpResponse resp = null;
		CloseableHttpResponse resp2 = null;
		try {

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
				//下单参数
				String basket_JValue = getBasketJsonParam(orderDTO);
				logInfo.info("basket_JValue==="+basket_JValue);
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
					Map<String,String> headerMapSM = new HashMap<String,String>();
					headerMapSM.put("Authorization", auth);
					headerMapSM.put("If-Match", basketETag);
					headerMapSM.put("Content-Type", "application/json");
					
					String basket_submit = "https://staging.menlook.com/dw/shop/v15_9/baskets/"+basket_id+"/submit?client_id=c8f0a7ef-dee7-4b94-8e5c-4ee108e61e26";
					logInfo.info("提交购物车URL==="+basket_submit); 										
					resp2 = HttpUtil45.operateData2("post", "", basket_submit, outTimeConf, null, "", headerMapSM, "", "");
					String gouwucheSubmit = EntityUtils.toString(resp2.getEntity(),"UTF-8");
					String gouwucheSubmitETag = resp2.getFirstHeader("ETag").getValue();
					logInfo.info("gouwucheSubmitETag==="+gouwucheSubmitETag);
					logInfo.info("gouwucheSubmit==="+gouwucheSubmit);
					//保存CustomerJWT、ETag和order_no 
					orderDTO.setSupplierOrderNo(JSONObject.fromObject(gouwucheSubmit).getString("order_no"));
					orderDTO.setStatus(OrderStatus.PAYED);
					orderDTO.setConsumerMsg(auth+"|"+gouwucheSubmitETag);
					orderDTO.setExcState("0");
				}
			}
			
		} catch (Exception e) {
			logger.error("e========"+e);
			logInfo.info("e========"+e);
			orderDTO.setExcDesc(e.getMessage());
			orderDTO.setExcState("1");
			orderDTO.setExcTime(new Date());
			e.printStackTrace();
		}finally{
			try {
				if(resp!=null)
					resp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(resp2!=null)
					resp2.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		
		deleteOrder.setStatus(OrderStatus.CANCELLED);
	}

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		deleteOrder.setStatus(OrderStatus.REFUNDED);
	}
	
	/**
	 * 计算取消订单的总金额
	 * @param deleteOrder
	 * @return
	 */
	public Double getAdjustmentAmount(ReturnOrderDTO deleteOrder){
		
		String detail = deleteOrder.getDetail();
		String[] details = detail.split(",");
		int num = 0;
		String skuNo = null;
		for (int i = 0; i < details.length; i++) {
			num = Integer.parseInt(details[i].split(":")[1]);
			skuNo = details[i].split(":")[0];
		}
		String markPrice = "";
		try {
			Map tempmap = skuPriceService
					.getNewSkuPriceBySku(supplierId, skuNo);
			Map map = (Map) tempmap.get(supplierId);
			markPrice = (String) map.get(skuNo);
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		double totalPrice = 0;
		if (!"-1".equals(markPrice)) {
			String price = markPrice.split("\\|")[0];
			totalPrice = (Double.parseDouble(price)) * num;
		} else {
			logger.info("产品sku= " + skuNo + " 没有市场价");
		}
		
		return totalPrice;
	}

	@Override
	public void handleEmail(OrderDTO orderDTO) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getSupplierSkuId(Map<String, String> skuMap)
			throws ServiceException {
		// TODO Auto-generated method stub

	}
	
//	private static ApplicationContext factory;
//	
//	private static void loadSpringContext()
//
//	{
//
//		factory = new AnnotationConfigApplicationContext(AppContext.class);
//	}
//	
//	public static void main(String[] args){
//		//loadSpringContext();
//		//OrderImpl order = (OrderImpl)factory.getBean("channeladvisorOrder");
//		OrderImpl order = new OrderImpl();
//		order.handleSupplierOrder(null);
//		
//		
//	}

}
