package com.shangpin.iog.linoricci.order;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.service.ProductSearchService;

public class LinoricciOrderServiceImpl extends AbsOrderService{
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	private static String url = null;
	static {
		if(null==bdl){
			bdl=ResourceBundle.getBundle("conf");
		}
		supplierId = bdl.getString("supplierId");
		url = bdl.getString("url");
	}
	@Autowired
	ProductSearchService productSearchservice;
	
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("ID_ORDER_WEB", orderDTO.getSpOrderId());
		//TODO 参数设置？
		param.put("ID_CLIENTE_WEB", "");
		param.put("DESTINATIONROW1", "");
		param.put("DESTINATIONROW2", "");
		param.put("DESTINATIONROW3", "");
		String skuId = orderDTO.getDetail().split(",")[0].split(":")[0];
		String barcode = skuId.split("-")[1];
		param.put("BARCODE", barcode);
		String qty = orderDTO.getDetail().split(",")[0].split(":")[1];
		param.put("QTY", qty);
		ProductDTO productForOrder = null;
		try {
		    productForOrder = productSearchservice.findProductForOrder(supplierId, skuId);
		} catch (ServiceException e) {
			logger.info("查找商品出错");
			e.printStackTrace();
		}
		String price = productForOrder.getSupplierPrice();
		param.put("PRICE", price);
		String returnData = HttpUtil45.post(url+"NewOrder", param, new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10));
		
		if (returnData.contains("OK")) {
			 orderDTO.setExcState("0");
			 orderDTO.setSupplierOrderNo(orderDTO.getSpOrderId());//商品的订单Id
			 orderDTO.setStatus(OrderStatus.PLACED);
		} else {
			//推送订单失败
			orderDTO.setExcDesc("订单失败，库存不足");
			sendMail(orderDTO);
		}
	}

	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		try {
			String returnData = setStatusOrder(orderDTO, "1");
			if (returnData.contains("OK")) {
				orderDTO.setExcState("0");
				orderDTO.setStatus(OrderStatus.CONFIRMED);
			} else {
				//确认订单失败
				orderDTO.setExcDesc("确认订单失败");
				//处理采购异常
				handlePurchaseOrderExc(orderDTO);
			}
		} catch (Exception e) {
			orderDTO.setExcDesc("网络原因付款失败"+e.getMessage());
			orderDTO.setExcState("1");
			e.printStackTrace();
		}
	}

	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		try {
			if(deleteOrder.getSupplierOrderNo()==null){ 
				deleteOrder.setExcState("0");
				deleteOrder.setStatus(OrderStatus.CANCELLED);
			}else{
				//TODO  暂时使用orderAmendment
				String returnData = orderAmendment(deleteOrder);
				if (returnData.contains("OK")) {
					deleteOrder.setExcState("0");
					deleteOrder.setStatus(OrderStatus.CANCELLED);
				} else {
					//取消订单失败
					logger.info("取消订单失败");
					deleteOrder.setExcDesc("取消订单失败");
					deleteOrder.setStatus(OrderStatus.CANCELLED);
					deleteOrder.setExcState("0");
				}
			}
		} catch (Exception e) {
			logger.info("取消订单失败");
			deleteOrder.setExcDesc(e.getMessage());
			deleteOrder.setExcState("1");
			e.printStackTrace();
		}
	}

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		
	}

	@Override
	public void handleEmail(OrderDTO orderDTO) {
		
	}

	@Override
	public void getSupplierSkuId(Map<String, String> skuMap)
			throws ServiceException {
		// TODO Auto-generated method stub
		
	}
	//采购异常处理
	private void handlePurchaseOrderExc(OrderDTO orderDTO) {
		String result = setPurchaseOrderExc(orderDTO);
		if("-1".equals(result)){
			orderDTO.setStatus(OrderStatus.NOHANDLE);
		}else if("1".equals(result)){
			orderDTO.setStatus(OrderStatus.PURCHASE_EXP_SUCCESS);
		}else if("0".equals(result)){
			orderDTO.setStatus(OrderStatus.PURCHASE_EXP_ERROR);
		}
		orderDTO.setExcState("0");
	}
	private void sendMail(OrderDTO orderDTO) {
		
		try{
			long tim = 60l;
			//判断有异常的订单如果处理超过两小时，依然没有解决，则把状态置为不处理，并发邮件
			if(DateTimeUtil.getTimeDifference(orderDTO.getCreateTime(),new Date())/(tim*1000*60)>0){ 
				
				String result = setPurchaseOrderExc(orderDTO);
				if("-1".equals(result)){
					orderDTO.setStatus(OrderStatus.NOHANDLE);
				}else if("1".equals(result)){
					orderDTO.setStatus(OrderStatus.PURCHASE_EXP_SUCCESS);
				}else if("0".equals(result)){
					orderDTO.setStatus(OrderStatus.PURCHASE_EXP_ERROR);
				}else{
					orderDTO.setStatus(OrderStatus.NOHANDLE);
				}
				//超过一天 不需要在做处理 订单状态改为其它状体
				orderDTO.setExcState("0");
			}else{
				orderDTO.setExcState("1");
			}
		}catch(Exception x){
			logger.info("订单超时" + x.getMessage());
		}
	}
	//TODO 暂时当做付款确认，功能确定后修改
	public String setStatusOrder(OrderDTO orderDTO,String status){
		Map<String, String> param = new HashMap<String, String>();
		param.put("CODICE", orderDTO.getSupplierOrderNo());
		param.put("ID_CLIENTE", "");
		param.put("ID_STATUS", status);
		String returnData = HttpUtil45.post(url+"OrderAmendment", param, new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10));
		return returnData;
	}
	//TODO 暂时当取消订单使用，功能明确后修改
	public String orderAmendment(ReturnOrderDTO deleteOrder){
		Map<String, String> param = new HashMap<String, String>();
		param.put("ID_ORDER_WEB", deleteOrder.getSupplierOrderNo());
		//TODO 参数设置？
		param.put("ID_CLIENTE_WEB", "");
		String skuId = deleteOrder.getDetail().split(",")[0].split(":")[0];
		String barcode = skuId.split("-")[1];
		param.put("BARCODE", barcode);
		//TODO 取消订单设置成0
		String qty = deleteOrder.getDetail().split(",")[0].split(":")[1];
		param.put("QTY", qty);
		String returnData = HttpUtil45.post(url+"OrderAmendment", param, new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10));
		return returnData;
	}
	
	
}
