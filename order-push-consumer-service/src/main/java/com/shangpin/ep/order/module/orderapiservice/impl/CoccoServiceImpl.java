package com.shangpin.ep.order.module.orderapiservice.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.mail.message.ShangpinMail;
import com.shangpin.ep.order.conf.mail.sender.ShangpinMailSender;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title: WiseServiceImpl</p>
 * <p>Description: wise供应商订单以发送邮件的方式发送给对方</p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月22日 上午10:15:54
 *
 */
@Component("coccolebimbiOrderImpl")
@Slf4j
public class CoccoServiceImpl implements IOrderService{

	@Autowired
    LogCommon logCommon;  
	@Autowired
	private ShangpinMailSender shangpinMailSender;

	@SuppressWarnings("static-access")
	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
				try {
					sendMail(orderDTO.getPurchaseNo(),orderDTO.getSupplierSkuNo(),orderDTO.getPurchasePriceDetail());
					orderDTO.setConfirmTime(new Date());
					orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED);
					orderDTO.setLogContent("------推送结束-------");
					logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
				} catch (Exception e) {
					e.printStackTrace();
				}
	}

	public void handleRefundlOrder(OrderDTO deleteOrder) {
		deleteOrder.setRefundTime(new Date());
		deleteOrder.setPushStatus(PushStatus.REFUNDED);
		deleteOrder.setLogContent("------退款结束-------");
		logCommon.loggerOrder(deleteOrder, LogTypeStatus.REFUNDED_LOG);
	}
	/**
	 * 发送邮件
	 * @param subject 邮件主题
	 * @param text 邮件内容
	 * @throws Exception
	 */
	private void sendMail(String cgd,String sku,String price) throws Exception {
		
		 String messageText =
 				"KEY: "+sku+"<br>"+
 				"QUANTITY: 1 <br>"+
 				"CUSTOMER NAME: "+"<br>"+
 				"CUSTOMER SHIPPING ADDRESS: "+"<br>"+
 				"CUSTOMER BILLING ADDRESS: "+"<br>"+
 				"TOTAL PAYED FOR THE ORDER: "+price+"<br>"+
 				"Shangpin OrderNo: "+cgd+"<br>";
   	
		ShangpinMail shangpinMail = new ShangpinMail();
		shangpinMail.setFrom("chengxu@shangpin.com");
		shangpinMail.setSubject("shangpin-order-"+cgd);
		shangpinMail.setText(messageText);
		shangpinMail.setTo("zhaogenchun@shangpin.com");
		List<String> addTo = new ArrayList<>();
		addTo.add("cesare.m@coccolebimbi.com");
		addTo.add("sabino.m@coccolebimbi.com");
		addTo.add("gio.p@coccolebimbi.com");
		addTo.add("winnie.liu@shangpin.com");
		shangpinMail.setAddTo(addTo);
		shangpinMailSender.sendShangpinMail(shangpinMail);
	}

	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		orderDTO.setLockStockTime(new Date());
		orderDTO.setPushStatus(PushStatus.NO_LOCK_API);
		orderDTO.setLogContent("------锁库结束-------");
		logCommon.loggerOrder(orderDTO, LogTypeStatus.LOCK_LOG);		
	}


	@Override
	public void handleCancelOrder(OrderDTO deleteOrder) {
		deleteOrder.setCancelTime(new Date());
		deleteOrder.setPushStatus(PushStatus.NO_LOCK_CANCELLED_API);
		deleteOrder.setLogContent("------取消结束-------");
		logCommon.loggerOrder(deleteOrder, LogTypeStatus.LOCK_LOG);		
	}

}
