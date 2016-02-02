package com.shangpin.iog.monti.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.monti.dto.Parameters2;
import com.shangpin.iog.monti.dto.ResponseObject;

import net.sf.json.JSONArray;

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
	private static String ordQty = null;
	private static String key = null;
	private static Map<String, String> param = null;

	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");

	private OutTimeConfig defaultConfig = new OutTimeConfig(1000 * 2, 1000 * 60*5, 1000 * 60*5);

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
		createOrder(OrderStatus.CONFIRMED, orderDTO);

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

	private void createOrder(String status, OrderDTO orderDTO) {

		// 获取订单信息
		Parameters2 order = getOrder(status, orderDTO);
		Gson gson = new Gson();

		String json = gson.toJson(order, Parameters2.class);
		System.out.println("request json == " + json);
		String rtnData = null;
		logger.info("推送的数据：" + json);
		System.out.println("rtnData==" + json);
		try {
			 Map<String, String> map =new HashMap<String, String>();
			 String[] barcode = orderDTO.getDetail().split(":");
			 map.put("DBContext", dBContext);
			 map.put("purchase_no", orderDTO.getSpPurchaseNo());
			 map.put("order_no", orderDTO.getSupplierOrderNo());
			 map.put("barcode", barcode[0]);
			 //map.put("barcode", "2004238900028");
			 map.put("ordQty", barcode[1]);
			 map.put("key", key);
			 map.put("sellPrice", order.getSellPrice());
			 rtnData = HttpUtil45.get(setOrderUrl, defaultConfig , map);
			//rtnData = HttpUtil45.operateData("get", "json", url, null, null, json, "", "");
			// {"error":"发生异常错误"}
			logger.info("推送" + status + "订单返回结果==+==" + rtnData);
			System.out.println("推送订单返回结果==+==" + rtnData);
			if (HttpUtil45.errorResult.equals(rtnData)) {
				orderDTO.setExcState("1");
				orderDTO.setExcDesc(rtnData);
				return;
			}
			//logger.info("Response ：" + rtnData + ", shopOrderId:" + order.getBarcode());

			ResponseObject responseObject = gson.fromJson(rtnData, ResponseObject.class);
			if ("ko".equals(responseObject.getStatus())) {
				orderDTO.setExcState("1");
				orderDTO.setExcDesc(responseObject.getMessage().toString());
			} else if (OrderStatus.PLACED.equals(status)) {
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
			 map.put("order_no", deleteOrder.getSupplierOrderNo());
			 map.put("key", key);
			 rtnData2 = HttpUtil45.get(queryOrderUrl, defaultConfig , map);
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
				 map.put("order_no", deleteOrder.getSupplierOrderNo());
				 map.put("key", key);
				 rtnData = HttpUtil45.get(cancelUrl, defaultConfig , map);
				logger.info("推送" + status + "退单返回结果==+==" + rtnData);
				System.out.println("推送退单返回结果==+==" + rtnData);
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
		}
		
		
	}

	private Parameters2 getOrder(String status, OrderDTO orderDTO) {

		String detail = orderDTO.getDetail();
		String[] details = detail.split(":");
		// logger.info("detail数据格式:"+detail);

		Parameters2 order = new Parameters2();
		order.setDBContext(dBContext);
		order.setPurchase_no(orderDTO.getSpPurchaseNo());
		order.setOrder_no(orderDTO.getSpOrderId());
//		order.setBarcode(details[0]);
//		order.setOrdQty(ordQty);
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

}