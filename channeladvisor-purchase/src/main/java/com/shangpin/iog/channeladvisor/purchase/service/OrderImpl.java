package com.shangpin.iog.channeladvisor.purchase.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.channeladvisor.purchase.utils.UtilOfChannel;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.service.ProductSearchService;

@Component("channeladvisorOrder")
public class OrderImpl extends AbsOrderService {

	private static Logger logger = Logger.getLogger("error");
	private static Logger logInfo = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	private static String supplierNo = null;
	private static String createOrderUrl = "https://api.channeladvisor.com/v1/Orders";
	private static OutTimeConfig timeConfig = new OutTimeConfig(1000*5*60, 1000*60 * 5, 1000*60 * 5);
	private static String access_token = "";
	private static String nostock = "nostock";
	static {
		if (null == bdl) {
			bdl = ResourceBundle.getBundle("conf");
			supplierId = bdl.getString("supplierId");
			supplierNo = bdl.getString("supplierNo");
			access_token = UtilOfChannel.getNewToken(timeConfig);
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

	// public void updateEvent() {
	//
	// }

	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {

		try{
			
			String jsonValue = getRequestParam(orderDTO);//下订单所需要的参数
//			System.out.println("param==="+jsonValue);
			logInfo.info("param==="+jsonValue);
			String excDesc = "";
			String result = "";
			String id = "";
			String stock = "";
			try{
				if(!access_token.equals(UtilOfChannel.ERROR)){
					
					String orderUrl = createOrderUrl+"?access_token="+access_token;//access_token;
					logInfo.info("createOrderUrl===="+orderUrl);
					//判断库存
					String sku_stock = orderDTO.getDetail().split(",")[0];
					String sku = sku_stock.split(":")[0];
					id = productSearchService.findProductForOrder(supplierId,sku).getSpuId();
					logInfo.info("id======"+id);
					stock = sku_stock.split(":")[1];
					String content = HttpUtil45.operateData("get", "", "https://api.channeladvisor.com/v1/Products("+id+")?access_token="+access_token, timeConfig, null, "", "", "");
					logInfo.info("请求库存===="+content);
					String realStock = JSONObject.fromObject(content).getString("TotalAvailableQuantity");
					logInfo.info("realStock======"+realStock);
					if(Integer.parseInt(stock) <= Integer.parseInt(realStock)){
						result = HttpUtil45.operateData("post", "json",orderUrl, timeConfig, null, jsonValue, "", "");
					}else{
						result = nostock;
						excDesc = "库存不足：订单产品数量为"+stock+"实际库存为"+realStock;
					}
					
				}else{
					result = HttpUtil45.errorResult;
					excDesc = "access_token 无效！！！";
				}
				
				
			}catch(ServiceException ex){
				logger.error("ex=="+ex);
				logInfo.info("ex=="+ex);
				//access_token过期
				if(ex.getMessage().equals("状态码:401")){
					access_token = UtilOfChannel.getNewToken(timeConfig);
					handleSupplierOrder(orderDTO);
					return;
				}else{
					result = HttpUtil45.errorResult;
					excDesc = ex.getMessage();
				}
				
			}
			//根据返回信息设置订单状态
//			System.out.println("result==="+result);
			logInfo.info("result==="+result);
			if(StringUtils.isNotBlank(result) && !result.equals(HttpUtil45.errorResult) && !result.equals(nostock)){
				JSONObject json = JSONObject.fromObject(result);
				orderDTO.setSupplierOrderNo(json.getString("ID"));
				orderDTO.setStatus(OrderStatus.PLACED);
				orderDTO.setExcState("0");
			}else if(result.equals(nostock)){
				//发生异常
				orderDTO.setExcDesc("下单失败==="+excDesc);
				orderDTO.setExcState("1");
				orderDTO.setExcTime(new Date());
				//采购异常处理
				doOrderExc(orderDTO);
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
	 * 采购异常处理
	 * @param orderDTO
	 */
	public void doOrderExc(OrderDTO orderDTO){
		String reResult = setPurchaseOrderExc(orderDTO);
		if("-1".equals(reResult)){
			orderDTO.setStatus(OrderStatus.NOHANDLE);
		}else if("1".equals(reResult)){
			orderDTO.setStatus(OrderStatus.PURCHASE_EXP_SUCCESS);
		}else if("0".equals(reResult)){
			orderDTO.setStatus(OrderStatus.PURCHASE_EXP_ERROR);
		}
		orderDTO.setExcState("0");
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
		param.put("SiteName", "Shangpin.com");
		param.put("BuyerEmailAddress", "sunxiaowen@shangpin.com");
		param.put("CreatedDateUtc", UtilOfChannel.getUTCTime());//
		param.put("TotalPrice", totalPrice);//
		param.put("Items", JSONArray.fromObject(items));
		param.put("SellerOrderID", orderDTO.getSpOrderId());
		param.put("ShippingFirstName", "TRANS_WORLD");
		param.put("ShippingLastName", "COURIER");
		param.put("ShippingAddressLine1", "145-02_156th_Street");
		param.put("ShippingAddressLine2", "SARA_SPW");
		param.put("ShippingCity", "Jamaica");
		param.put("ShippingPostalCode", "11434");
		param.put("ShippingCity", "NY");
		param.put("ShippingCountry", "US");

		return JSONObject.fromObject(param).toString();
	}

	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		//Retrieve details about a single order
		//POST https://api.channeladvisor.com/v1/Fulfillments
		try{
			
			String result = "";
			String excDesc = "";//异常信息
			
			if(!access_token.equals(UtilOfChannel.ERROR)){
				String orderId = orderDTO.getSupplierOrderNo();
				if(StringUtils.isNotBlank(orderId) && !"null".equals(orderId)){
					String url = "https://api.channeladvisor.com/v1/Orders("+orderId+")?access_token="+access_token;
					Map<String,String> param = new HashMap<>();
					param.put("CheckoutStatus", "Completed");
					param.put("PaymentStatus", "Cleared");
					param.put("CheckoutDateUtc", UtilOfChannel.getUTCTime());
					param.put("PaymentDateUtc", UtilOfChannel.getUTCTime());
//					param.put("ShippingFirstName", "TRANS WORLD");
//					param.put("ShippingLastName", "COURIER");
//					param.put("ShippingAddressLine1", "145-02 156th Street");
//					param.put("ShippingAddressLine2", "SARA SPW");
//					param.put("ShippingCity", "Jamaica");
//					param.put("ShippingPostalCode", "11434");
//					param.put("ShippingCity", "NY");
//					param.put("ShippingCountry", "US");
					String jsonValue = JSONObject.fromObject(param).toString();
					logInfo.info("param==="+jsonValue);
					logInfo.info("url==="+url);
					try{
						
						HttpUtil45.operateData("put", "json", url, timeConfig, null, jsonValue, "", "");
						
					}catch(ServiceException e){
						logger.error("e=="+e.getMessage());
						logInfo.info("e=="+e.getMessage());
						
						if(e.getMessage().equals("状态码:401")){//access_token过期
							access_token = UtilOfChannel.getNewToken(timeConfig);
							handleConfirmOrder(orderDTO);
							return;
						}else if(e.getMessage().equals("状态码:204")){//支付成功
							
							result = UtilOfChannel.SUCCESSFUL;
							
						}else{//其他情况支付失败
							result = HttpUtil45.errorResult;
							excDesc = e.getMessage();
						}	
						
					}
				}else{
					result = HttpUtil45.errorResult;
					excDesc = "未获得对方订单id";
				}				
				
			}else{
				result = HttpUtil45.errorResult;
				excDesc = "access_token 无效！！！";
			}
			
			//根据返回信息设置订单状态
			System.out.println("result=="+result);
			logInfo.info("result==="+result);
			if(result.equals(UtilOfChannel.SUCCESSFUL)){
				
				orderDTO.setStatus(OrderStatus.CONFIRMED);
				orderDTO.setExcState("0");
				
			}else{
				orderDTO.setExcDesc("支付失败=="+excDesc);
				orderDTO.setExcState("1");
				orderDTO.setExcTime(new Date());
				//采购异常处理
//				doOrderExc(orderDTO);
			}
			
		}catch(Exception ex){
			logger.error(ex);
			orderDTO.setExcDesc(ex.getMessage());
			orderDTO.setExcState("1");
			orderDTO.setExcTime(new Date());
			ex.printStackTrace();
			//采购异常处理
//			doOrderExc(orderDTO);
		}
		
	}

	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		//Mark an order as not exported
		//https://api.channeladvisor.com/v1/Orders(123456)/Adjust
		String rStr = "";
		String excDesc = "";
		try{	
			
			if(!access_token.equals(UtilOfChannel.ERROR)){
				Map<String,Object> param = new HashMap<String,Object>();
				param.put("Reason", "BuyerCancelled");
				param.put("PreventSiteProcessing", "True");
				param.put("SellerAdjustmentID", deleteOrder.getSpOrderId());
				param.put("AdjustmentAmount", getAdjustmentAmount(deleteOrder));
				String jsonValue = JSONObject.fromObject(param).toString();	
				System.out.println("jsonValue==="+jsonValue);
				logInfo.info("param==="+jsonValue);
				
				String url = "https://api.channeladvisor.com/v1/Orders("+deleteOrder.getSupplierOrderNo()+")/Adjust?access_token="+access_token;
				try{
					
					HttpUtil45.operateData("post", "json", url, timeConfig, null, jsonValue, "", "");
				
				}catch(ServiceException e){
					logger.error("e=="+e);
					if(e.getMessage().equals("状态码:401")){//access_token过期
						access_token = UtilOfChannel.getNewToken(timeConfig);
						handleCancelOrder(deleteOrder);
						return;
					}else if(e.getMessage().equals("状态码:204")){//取消成功
						
						rStr = UtilOfChannel.SUCCESSFUL;
						
					}else{
						rStr = HttpUtil45.errorResult;
						excDesc = e.getMessage();
					}	
					
				}
			}else{
				rStr = HttpUtil45.errorResult;
				excDesc = "access_token 无效！！！";
			}
			
			//根据返回结果设置退单的状态
			System.out.println("result=="+rStr);
			logInfo.info("result==="+rStr);
			if( rStr.equals(UtilOfChannel.SUCCESSFUL)){
				//取消成功
				deleteOrder.setExcState("0");
				deleteOrder.setStatus(OrderStatus.CANCELLED);				
			}else{
				//取消失败
				deleteOrder.setExcDesc("订单已被供货商取消或者发生异常 "+excDesc);
				deleteOrder.setExcState("1");
				deleteOrder.setExcTime(new Date());
			}
			
		}catch(Exception ex){
			deleteOrder.setExcDesc(ex.getMessage());
			deleteOrder.setExcState("1");
			deleteOrder.setExcTime(new Date());
			logger.error(ex);
			ex.printStackTrace();
		}
		

	}

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		
		String result = "";
		String excDesc = "";
		
		try{
			
			if(!access_token.equals(UtilOfChannel.ERROR)){
				Map<String,Object> param = new HashMap<String,Object>();
				param.put("PreventSiteProcessing", "True");
				param.put("SellerAdjustmentID", deleteOrder.getSpOrderId());
				param.put("AdjustmentAmount", getAdjustmentAmount(deleteOrder));
				String jsonValue = JSONObject.fromObject(param).toString();	
				System.out.println("jsonValue==="+jsonValue);
				logInfo.info("param==="+jsonValue);
				String url = "https://api.channeladvisor.com/v1/Orders("+deleteOrder.getSupplierOrderNo()+")/Adjust?access_token="+access_token;
				try{
					
					HttpUtil45.operateData("post", "json", url, timeConfig, null, jsonValue, "", "");
				
				}catch(ServiceException e){
					logger.error("e=="+e);
					//access_token过期
					if(e.getMessage().equals("状态码:401")){
						access_token = UtilOfChannel.getNewToken(timeConfig);
						handleRefundlOrder(deleteOrder);
						return;
					}else if(e.getMessage().equals("状态码:204")){
						
						result = UtilOfChannel.SUCCESSFUL;
						
					}else{
						result = HttpUtil45.errorResult;
						excDesc = e.getMessage();
					}				
					
				}
			}else{
				result = HttpUtil45.errorResult;
				excDesc = "access_token 无效！！！";
			}
			
			//根据返回结果设置退单的状态
			System.out.println("result=="+result);
			logInfo.info("result==="+result);
			if( result.equals(UtilOfChannel.SUCCESSFUL)){
				//取消成功
				deleteOrder.setExcState("0");
				deleteOrder.setStatus(OrderStatus.REFUNDED);				
			}else{
				//取消失败
				deleteOrder.setExcDesc("订单已被供货商取消或者发生异常 "+excDesc);
				deleteOrder.setExcState("1");
				deleteOrder.setExcTime(new Date());
			}
			
		}catch(Exception ex){
			deleteOrder.setExcDesc(ex.getMessage());
			deleteOrder.setExcState("1");
			deleteOrder.setExcTime(new Date());
			logger.error(ex);
			ex.printStackTrace();
		}
		
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
//		loadSpringContext();
//		OrderImpl order = (OrderImpl)factory.getBean("channeladvisorOrder");
//		OrderDTO oo = new OrderDTO();
//		oo.setDetail("NY-15051:1");
//		//
//		oo.setSpOrderId("201512101209");		
//		
//		order.handleSupplierOrder(oo);
//		
////		oo.setSupplierOrderNo("165303");
////		order.handleConfirmOrder(oo);
//		
////		ReturnOrderDTO deleteOrder = new ReturnOrderDTO();
////		deleteOrder.setDetail("NY-25957:1");
////		deleteOrder.setSpOrderId("201512101209");
////		deleteOrder.setSupplierOrderNo("178470");
////		order.handleCancelOrder(deleteOrder);
//		
////		order.handleRefundlOrder(deleteOrder);
//		
//	}

}
