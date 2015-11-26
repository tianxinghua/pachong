package com.shangpin.iog.channeladvisor.purchase.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.channeladvisor.purchase.dto.Item;
import com.shangpin.iog.channeladvisor.purchase.dto.Order;
import com.shangpin.iog.channeladvisor.purchase.dto.Promotion;
import com.shangpin.iog.channeladvisor.purchase.utils.UtilOfChannel;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;

@Configuration("order")
public class OrderImpl extends AbsOrderService {

	private static Logger logger = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	private static String supplierNo = null;
	private static String createOrderUrl = null;
	private static String updateOrderUrl = null;
	private static String access_token = "";
	private static OutTimeConfig timeConfig = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);
	static {
		if (null == bdl) {
			bdl = ResourceBundle.getBundle("conf");
			supplierId = bdl.getString("supplierId");
			supplierNo = bdl.getString("supplierNo");
			createOrderUrl = bdl.getString("createOrderUrl");
			updateOrderUrl = bdl.getString("updateOrderUrl");
		}
		access_token = UtilOfChannel.getNewToken(timeConfig);
	}

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

	// public void updateEvent() {
	//
	// }

	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {

		try{
			String jsonValue = getRequestParam(orderDTO);//下订单所需要的参数
			System.out.println("param==="+jsonValue);
						
			createOrderUrl = createOrderUrl+"?access_token="+access_token;
			String excDesc = "";
			String result = "";
			String sku_stock = "";
			String id = "";
			String stock = "";
			try{
				//判断库存
				sku_stock = orderDTO.getDetail().split(",")[0];
				id = sku_stock.split(":")[0];
				stock = sku_stock.split(":")[1];
				String content = HttpUtil45.get("https://api.channeladvisor.com/v1/Products("+id+")?access_token="+access_token, timeConfig, null);
				String realStock = JSONObject.fromObject(content).getString("TotalAvailableQuantity");
				if(Integer.parseInt(stock) <= Integer.parseInt(realStock)){
					result = HttpUtil45.operateData("post", "json",createOrderUrl, timeConfig, null, jsonValue, "", "");
				}else{
					result = HttpUtil45.errorResult;
					excDesc = "库存不足：订单产品数量为"+stock+"实际库存为"+realStock;
				}
				
			}catch(ServiceException ex){
				logger.error(ex.getMessage());
				
				//access_token过期，刷新access_token
				if(ex.getMessage().equals("状态码:401")){
					access_token = UtilOfChannel.getNewToken(timeConfig);//获取新的访问令牌
					createOrderUrl = createOrderUrl+"?access_token="+access_token;
					try{
						String content = HttpUtil45.get("https://api.channeladvisor.com/v1/Products("+id+")?access_token="+access_token, timeConfig, null);
						String realStock = JSONObject.fromObject(content).getString("TotalAvailableQuantity");
						if(Integer.parseInt(stock) <= Integer.parseInt(realStock)){
							result = HttpUtil45.operateData("post", "json",createOrderUrl, timeConfig, null, jsonValue, "", "");
						}else{
							result = HttpUtil45.errorResult;
							excDesc = "库存不足：订单产品数量为"+stock+"实际库存为"+realStock;
						}
					
					}catch(ServiceException e){//下单失败，发生异常
						logger.error("下单失败===="+e.getMessage());
						result = HttpUtil45.errorResult;
						excDesc = e.getMessage();
					}
				}else{//下单失败，发生异常
					result = HttpUtil45.errorResult;
					excDesc = ex.getMessage();
				}
			}
			
			System.out.println("result==="+result);
			if(StringUtils.isNotBlank(result) && !result.equals(HttpUtil45.errorResult)){
				JSONObject json = JSONObject.fromObject(result);
				orderDTO.setSupplierId(json.getString("ID"));
				orderDTO.setStatus(OrderStatus.PLACED);
			}else{
				//发生异常
				orderDTO.setExcDesc("下单失败==="+excDesc);
				orderDTO.setExcState("1");
				orderDTO.setExcTime(new Date());
			}
			
			
		}catch(Exception ex){
			orderDTO.setExcDesc(ex.getMessage());
			orderDTO.setExcState("1");
			orderDTO.setExcTime(new Date());
			logger.error(ex.getMessage());
			ex.printStackTrace();
		}
		
	}

	/**
	 * 构造下订单所需要的参数
	 * @param orderDTO
	 * @return
	 */
	public String getRequestParam(OrderDTO orderDTO) {

		Map<String, Object> param = new HashMap<String, Object>();

		String detail = orderDTO.getDetail();
		String[] details = detail.split(",");
		int num = 0;
		String skuNo = null;
		for (int i = 0; i < details.length; i++) {
			num = Integer.parseInt(details[i].split(":")[1]);
			skuNo = details[i].split(":")[0];
		}
		String markPrice = "1";
//		try {
//			Map tempmap = skuPriceService
//					.getNewSkuPriceBySku(supplierId, skuNo);
//			Map map = (Map) tempmap.get(supplierId);
//			markPrice = (String) map.get(skuNo);
//		} catch (ServiceException e) {
//			logger.error(e.getMessage());
//			e.printStackTrace();
//		}
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		Map<String,Object> itemMap = new HashMap<String,Object>();
		itemMap.put("Sku",skuNo);
		itemMap.put("Quantity",num);
		double totalPrice = 0;
		if (!"-1".equals(markPrice)) {
			String price = markPrice.split("\\|")[0];
			itemMap.put("UnitPrice", Double.parseDouble(price));
			totalPrice = (Double.parseDouble(price)) * num;
		} else {
			logger.info("产品sku= " + skuNo + " 没有市场价");
		}
		items.add(itemMap);
		param.put("ProfileID", 12018111);
		param.put("SiteID", 587);
		param.put("SiteName", "shangpin");
		param.put("BuyerEmailAddress", "buyer@example.com");
		param.put("CreatedDateUtc", "2015-11-24T14:33:00.1Z");//(new SimpleDateFormat("yyyy-MM-dd HH:mm")).format(new Date())
		param.put("TotalPrice", totalPrice);//String.valueOf(totalPrice)
		param.put("Items", JSONArray.fromObject(items));

		return JSONObject.fromObject(param).toString();
	}

	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		//Retrieve details about a single order
		//GET https://api.channeladvisor.com/v1/Orders(123456)
		try{
			
			String newStoken = UtilOfChannel.getNewToken(timeConfig);//获取新的访问令牌
			updateOrderUrl = updateOrderUrl+"("+orderDTO.getSupplierOrderNo()+")?access_token="+newStoken;
			String result = HttpUtil45.get(updateOrderUrl, timeConfig, null);
			System.out.println("result=="+result);
			if(StringUtils.isNotBlank(result) && !result.equals(HttpUtil45.errorResult)){
				JSONObject json = JSONObject.fromObject(result);
				switch (json.getString("PaymentStatus")) {
				case "NotYetSubmitted":
					
					break;
				case "Cleared":
					
					break;
				case "Submitted":
					orderDTO.setStatus(OrderStatus.CONFIRMED);
					break;
				case "Failed":
					
					break;
				case "Deposited":
					orderDTO.setStatus(OrderStatus.WAITPLACED);
					break;
				}
			}else{//异常
				
			}
			
		}catch(Exception ex){
			orderDTO.setExcDesc(ex.getMessage());
			orderDTO.setExcState("1");
			ex.printStackTrace();
		}
		
	}

	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		//Mark an order as not exported
		//https://api.channeladvisor.com/v1/Orders(123456)/Adjust
		try{
			
			OutTimeConfig outTimeConf = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);
			String newStoken = UtilOfChannel.getNewToken(outTimeConf);//获取新的访问令牌
			Map<String,String> param = new HashMap<String,String>();
			param.put("Reason", "BuyerCancelled");
			param.put("PreventSiteProcessing", "True");
			param.put("SellerAdjustmentID", deleteOrder.getSpOrderId());
			param.put("AdjustmentAmount", "0");
			
			System.out.println(JSONObject.fromObject(param));
			
			String url = "https://api.channeladvisor.com/v1/Orders("+deleteOrder.getSupplierOrderNo()+")/Adjust?access_token="+newStoken;
			String rStr = HttpUtil45.post(url, param, outTimeConf);
			System.out.println(rStr);
			if( !rStr.equals(HttpUtil45.errorResult) && StringUtils.isNotBlank(rStr)){
				
			}else{
				//发生异常
			}
			
		}catch(Exception ex){
			logger.error(ex);
			ex.printStackTrace();
		}
		

	}

	/**
	 * 计算取消订单的总金额
	 * @param deleteOrder
	 * @return
	 */
	public String getAdjustmentAmount(ReturnOrderDTO deleteOrder){
		
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
		
		return String.valueOf(totalPrice);
	}
	
	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		// TODO Auto-generated method stub

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
	
	public static void main(String[] args){
		OrderImpl order = new OrderImpl();
		OrderDTO oo = new OrderDTO();
		oo.setDetail("277562:5");
		//String str = JSONObject.fromObject(order.getRequestParam(oo)).toString();
		//System.out.println(str);
		order.handleSupplierOrder(oo);
		
//		ReturnOrderDTO deleteOrder = new ReturnOrderDTO();
//		deleteOrder.setSpOrderId("123abc");
//		deleteOrder.setSupplierOrderNo("123456");
//		order.handleCancelOrder(deleteOrder);
//		OrderDTO orderDTO = new OrderDTO();
//		orderDTO.setSupplierOrderNo("159669");
//		order.handleConfirmOrder(orderDTO);
		
	}

}
