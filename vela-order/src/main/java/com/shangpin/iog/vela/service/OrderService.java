package com.shangpin.iog.vela.service;


import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.shangpin.iog.common.utils.SendMail;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ProductOfSpecDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.service.ProductSpecSearchService;
import com.shangpin.iog.vela.dto.Parameters;
import com.shangpin.iog.vela.dto.Parameters2;
import com.shangpin.iog.vela.dto.ResponseObject;



@Component
public class OrderService extends AbsOrderService {

	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	private static String supplierNo = null;
	private static String setOrderUrl,queryOrderUrl,cancelUrl;
	private static String dBContext = null;
	// private static String purchase_no = null;
	// private static String order_no = null;
	// private static String barcode = null;
	private static String key = null;
	private static Map<String, String> param = null;
	
	@Autowired
	ProductSpecSearchService productSpecSearchService;

	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");

	private OutTimeConfig defaultConfig = new OutTimeConfig(1000 * 2, 1000 * 60*5, 1000 * 60*5);
	private static String smtpHost = null;
	private static String from = null;
	private static String fromUserPassword = null;
	private static String to = null;
	private static String subject = null;
	private static String messageText = null;
	private static String messageType = null;
	static {
		if (null == bdl) {
			bdl = ResourceBundle.getBundle("param");
		}
		supplierId = bdl.getString("supplierId");
		supplierNo = bdl.getString("supplierNo");
		setOrderUrl = bdl.getString("setOrderUrl");
		queryOrderUrl = bdl.getString("queryOrderUrl");
		cancelUrl = bdl.getString("cancelUrl");
		dBContext = bdl.getString("dBContext");
		key = bdl.getString("key");



		smtpHost  = bdl.getString("smtpHost");
		from = bdl.getString("from");
		fromUserPassword = bdl.getString("fromUserPassword");
		to = bdl.getString("to");
		subject = bdl.getString("subject");
		messageText = bdl.getString("messageText");
		messageType = bdl.getString("messageType");
	}

	// 下单处理
	public void startWMS() {
		this.checkoutOrderFromWMS(supplierNo, supplierId, true);
	}

	// 订单确认处理
	public void confirmOrder() {
		this.confirmOrder(supplierId);

	}

	/**
	 * 锁库存
	 */
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		orderDTO.setStatus(OrderStatus.PLACED);

	}

	/**
	 * 推送订单
	 */
	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		orderDTO.setExcState("0");
		createOrder(orderDTO);

	}

	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		deleteOrder.setStatus(OrderStatus.CANCELLED);
	}

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		deleteOrder.setExcDesc("0");
		//deleteOrder.setStatus(OrderStatus.REFUNDED);
//		if("HO".equals(deleteOrder.getStatus())){
//			
//		}
		refundlOrder(OrderStatus.REFUNDED,deleteOrder);

	}

	@Override
	public void handleEmail(OrderDTO orderDTO) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getSupplierSkuId(Map<String, String> skuMap) throws ServiceException {
		// TODO Auto-generated method stub

	}

	private void createOrder( OrderDTO orderDTO) {

		// 获取订单信息
		Parameters order = getOrder( orderDTO);
		Gson gson = new Gson();

		String json = gson.toJson(order, Parameters.class);
		System.out.println("request json == " + json);
		String rtnData = null;
		logger.info("推送的数据：" + json);
		System.out.println("rtnData==" + json);
		try {
			 Map<String, String> map =new HashMap<String, String>();
			 map.put("DBContext", dBContext);
			 map.put("purchase_no", orderDTO.getSpPurchaseNo());
			 map.put("order_no", orderDTO.getSpOrderId());
			 map.put("barcode", order.getBarcode());
			 //map.put("barcode", "2004238900028");
			 map.put("ordQty", order.getOrdQty());
			 map.put("key", key);
			 map.put("sellPrice", order.getSellPrice());
			 rtnData = HttpUtil45.get(setOrderUrl, defaultConfig , map);
			//rtnData = HttpUtil45.operateData("get", "json", url, null, null, json, "", "");
			// {"error":"发生异常错误"}
			logger.info("推送订单返回结果==+==" + rtnData);
			System.out.println("推送订单返回结果==+==" + rtnData);
			if (HttpUtil45.errorResult.equals(rtnData)) {
				orderDTO.setExcState("1");
				orderDTO.setExcDesc(rtnData);
				return;
			}
			//logger.info("Response ：" + rtnData + ", shopOrderId:" + order.getBarcode());

			ResponseObject responseObject = gson.fromJson(rtnData, ResponseObject.class);
			if(null==responseObject.getStatus()){
				orderDTO.setExcState("1");
				orderDTO.setExcDesc(responseObject.getMessage());
			}else if ("ko".equals(responseObject.getStatus().toLowerCase())) {
            	orderDTO.setExcDesc(responseObject.getMessage());
//				if("Error !! Quantity Not Available.".equals(responseObject.getMessage())){   //无库存
//					orderDTO.setExcState("0");
//                 	this.setPurchaseExc(orderDTO);
//
//				}else if("Error !! Barcode Not Exist.".equals(responseObject.getMessage())){
//					orderDTO.setExcState("0");
//					this.setPurchaseExc(orderDTO);
//				}else{
//					orderDTO.setExcState("1");
//					orderDTO.setExcDesc(responseObject.getMessage().toString());
//				}
				if("0".equals(String.valueOf(responseObject.getId_b2b_order()))||"-1".equals(String.valueOf(responseObject.getId_b2b_order()))){   //无库存
				    orderDTO.setExcState("0");
					this.setPurchaseExc(orderDTO);

				}else{
					orderDTO.setExcState("1");
					orderDTO.setExcDesc(responseObject.getMessage().toString());
				}

			} else {
				orderDTO.setStatus(OrderStatus.CONFIRMED);
				orderDTO.setSupplierOrderNo(String.valueOf(responseObject.getId_b2b_order()));
			}
		} catch (Exception e) {
			// loggerError.error("Failed Response ：" + e.getMessage() + ",
			// shopOrderId:"+order.getBarcode());
			orderDTO.setExcState("1");
			orderDTO.setExcDesc(e.getMessage());
		}
	}
	
	private void refundlOrder(String status, ReturnOrderDTO deleteOrder) {
		//查询状态
		String rtnData2 = null;
		try{
			Map<String, String> map =new HashMap<String, String>();
			 map.put("DBContext", dBContext);
			 map.put("purchase_no", deleteOrder.getSpPurchaseNo());
			 map.put("order_no", deleteOrder.getSpOrderId());
			 map.put("key", key);
			 rtnData2 = HttpUtil45.get(queryOrderUrl, defaultConfig , map);
			logger.info("查询订单状态返回值:" + rtnData2);
			System.out.println("查询订单状态返回值:" + rtnData2);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		// 获取退单信息
		Gson gson = new Gson();
		String rtnData = null;

		ResponseObject response = gson.fromJson(rtnData2, ResponseObject.class);
		if("HO".equals(response.getStatus())){
			try {
				 Map<String, String> map =new HashMap<String, String>();
				 map.put("DBContext", dBContext);
				 map.put("purchase_no", deleteOrder.getSpPurchaseNo());
				 map.put("order_no", deleteOrder.getSpOrderId());
				 map.put("key", key);
				 rtnData = HttpUtil45.get(cancelUrl, defaultConfig , map);
				logger.info("推送采购单：" + deleteOrder.getSpPurchaseNo() + "退单返回结果==" + rtnData);
				System.out.println("推送采购单："+ deleteOrder.getSpPurchaseNo() +"退单返回结果==" + rtnData);
				if (HttpUtil45.errorResult.equals(rtnData)) {
					deleteOrder.setExcState("1");
					deleteOrder.setExcDesc(rtnData);
					return;
				}

				ResponseObject responseObject = gson.fromJson(rtnData, ResponseObject.class);
				if ("OK".equals(responseObject.getStatus())) {
					deleteOrder.setExcState("0");
					//deleteOrder.setExcDesc(responseObject.getMessage().toString());
				} else {
					deleteOrder.setStatus("1");
					deleteOrder.setExcDesc(responseObject.getMessage().toString());
					//deleteOrder.setSupplierOrderNo(String.valueOf(responseObject.getId_b2b_order()));
				}
			} catch (Exception e) {
				deleteOrder.setExcState("1");
				deleteOrder.setExcDesc(e.getMessage());
			}
		}else{
			loggerError.error("采购单"+deleteOrder.getSpPurchaseNo()+"用户退款,供货商状态已变，无法通知。");
			deleteOrder.setExcDesc("采购单"+deleteOrder.getSpPurchaseNo()+"用户退款,供货商状态已变，无法通知。");
		}
		
		
	}

	private Parameters getOrder( OrderDTO orderDTO) {

		String detail = orderDTO.getDetail();
		String[] details = detail.split(":");
		// logger.info("detail数据格式:"+detail);

		Parameters order = new Parameters();
		order.setDBContext(dBContext);
		order.setPurchase_no(orderDTO.getSpPurchaseNo());
		order.setOrder_no(orderDTO.getSpOrderId());
		
		String skuId = details[0];
		ProductOfSpecDTO pro = null;
		try {
			pro = productSpecSearchService.findProductBySupplierIdAndSkuId(supplierId, skuId);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		order.setBarcode(pro.getBarCode());
		order.setOrdQty(details[1]);
		order.setKey(key);
//		String sPurchasePrice = StringUtils.isBlank(orderDTO.getPurchasePriceDetail())?"0":orderDTO.getPurchasePriceDetail();
//     	BigDecimal purchasePrice = new BigDecimal(sPurchasePrice).divide(new BigDecimal(1.05),2,BigDecimal.ROUND_HALF_UP);
//		order.setSellPrice(purchasePrice.toString());
		order.setSellPrice("0");




//		try {
//			Map tempmap = skuPriceService.getNewSkuPriceBySku(supplierId, details[0]);
//			Map map = (Map) tempmap.get(supplierId);
//			markPrice = (String) map.get(details[0]);
//			if (!"-1".equals(markPrice)) {
//				String price = markPrice.split("\\|")[1];
//				if (!"-1".equals(price)) {
//					order.setSellPrice(price);
//				} else {
//					order.setSellPrice(orderDTO.getPurchasePriceDetail());
//				}
//
//			} else {
//				order.setSellPrice(orderDTO.getPurchasePriceDetail());
//			}
//		} catch (ServiceException e) {
//			order.setSellPrice(orderDTO.getPurchasePriceDetail());
//			System.out.println("sku" + details[0] + "没有供货价");
//			logger.info("异常错误：" + e.getMessage());
//		}
		return order;
	}
	
	private Parameters2 getRefundlOrder(String status, ReturnOrderDTO deleteOrder) {
		Parameters2 refundlOrder = new Parameters2();
		refundlOrder.setDBContext(dBContext);
		refundlOrder.setPurchase_no(deleteOrder.getSpPurchaseNo());
		refundlOrder.setOrder_no(deleteOrder.getSpOrderId());
		refundlOrder.setKey(key);
		
		return refundlOrder;
	}


	private void setPurchaseExc(final OrderDTO orderDTO){
		String result = setPurchaseOrderExc(orderDTO);
		if("-1".equals(result)){
			orderDTO.setStatus(OrderStatus.NOHANDLE);
		}else if("1".equals(result)){
			orderDTO.setStatus(OrderStatus.PURCHASE_EXP_SUCCESS);
		}else if("0".equals(result)){
			orderDTO.setStatus(OrderStatus.PURCHASE_EXP_ERROR);
		}

		Thread t = new Thread(	 new Runnable() {
			@Override
			public void run() {
				try {
					SendMail.sendMessage(smtpHost, from, fromUserPassword, to, subject,"spinnaker 采购单"+orderDTO.getSpPurchaseNo()+"已弃单.状态是:"+orderDTO.getStatus(), messageType);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t.start();

	}

}
