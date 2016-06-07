package com.shangpin.iog.giglio.order.service;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.common.utils.SendMail;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.service.SkuPriceService;

@Component
public class OrderImpl extends AbsOrderService {

	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	private static String supplierNo = null;

	private static String smtpHost = null;
	private static String from = null;
	private static String fromUserPassword = null;
	private static String to = null;
	private static String subject = null;
	private static String messageType = null;
	@Autowired
	SkuPriceService skuPriceService;

	@Autowired
	com.shangpin.iog.service.OrderService productOrderService;

	private static Map<String, String> map = new HashMap<String, String>();
	static {
		if (null == bdl) {
			bdl = ResourceBundle.getBundle("conf");
		}
		supplierId = bdl.getString("supplierId");
		supplierNo = bdl.getString("supplierNo");
		fromUserPassword = bdl.getString("fromUserPassword");
		from = bdl.getString("from");
		to = bdl.getString("to");
		subject = bdl.getString("subject");
		messageType = bdl.getString("messageType");
		smtpHost = bdl.getString("smtpHost");
	}

	public void loopExecute() {
		this.checkoutOrderFromWMS(supplierNo, supplierId, true);
	}

	public void confirmOrder() {
		this.confirmOrder(supplierId);

	}

	/**
	 * 锁库存
	 */
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		orderDTO.setExcState("0");
		orderDTO.setStatus(OrderStatus.PLACED);
	}

	/**
	 * 推送订单
	 */
	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		orderDTO.setExcState("0");
		orderDTO.setStatus(OrderStatus.CONFIRMED);
		sendMail(orderDTO);
	}

	/**
	 * 解除库存锁
	 */
	@Override
	public void handleCancelOrder(final ReturnOrderDTO deleteOrder) {
		deleteOrder.setExcState("0");
		// 超过一天 不需要在做处理 订单状态改为其它状体
		deleteOrder.setStatus(OrderStatus.CANCELLED);
	}

	/**
	 * 获取真正的供货商编号
	 *
	 * @param skuMap
	 *            key skuNo ,value supplerSkuNo
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public void getSupplierSkuId(Map<String, String> skuMap)
			throws ServiceException {

	}

	/*
	 * detail数据格式： skuId:数量,skuId:数量
	 */

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		// TODO Auto-generated method stub
		deleteOrder.setExcState("0");
		deleteOrder.setStatus(OrderStatus.REFUNDED);
	}

	@Override
	public void handleEmail(OrderDTO orderDTO) {
	}
	
	private void sendMail(OrderDTO orderDTO){
		
		try {
			String detail = orderDTO.getDetail();
			String string = detail.split(",")[0];
			String messageText = string.split(":")[0]+" "+string.split(":")[1]+" "+orderDTO.getSpPurchaseNo();
			SendMail.sendMessage(smtpHost, from, fromUserPassword, to, subject, messageText , messageType);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setDetail("asdf:1,");
		orderDTO.setSpPurchaseNo("12312313");
		new OrderImpl().handleConfirmOrder(orderDTO );
	}
}
