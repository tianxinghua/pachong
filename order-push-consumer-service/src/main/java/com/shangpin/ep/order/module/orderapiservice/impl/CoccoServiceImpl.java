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
import com.shangpin.ep.order.module.sku.bean.HubSku;
import com.shangpin.ep.order.module.sku.service.impl.HubSkuService;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title: WiseServiceImpl</p>
 * <p>Description: wise供应商订单以发送邮件的方式发送给对方</p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月22日 上午10:15:54
 *
 */
@Component("coccoMailService")
@Slf4j
public class CoccoServiceImpl implements IOrderService{

	@Autowired
    LogCommon logCommon;  
	@Autowired
	private ShangpinMailSender shangpinMailSender;
	@Autowired
	private HubSkuService hubSkuService;

	@SuppressWarnings("static-access")
	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		String messageText ="Shangpin OrderNo: "+orderDTO.getPurchaseNo()+"<br>"+
				"KEY: "+orderDTO.getSupplierSkuNo()+"<br>"+
				"QUANTITY: 1 <br>"+
				"CUSTOMER NAME: "+"<br>"+
				"CUSTOMER SHIPPING ADDRESS: "+"<br>"+
				"CUSTOMER BILLING ADDRESS: "+"<br>"+
				"TOTAL PAYED FOR THE ORDER: "+"<br>";
				log.info("wise推送订单参数："+messageText); 
				try {
					sendMail("coccolebimbi-order-shangpin",messageText);
				} catch (Exception e) {
					e.printStackTrace();
				}
				log.info("wise推送成功。"); 
	}
	
	

	public void handleRefundlOrder(OrderDTO deleteOrder) {
		try {
			HubSku sku = hubSkuService.getSku(deleteOrder.getSupplierId(), deleteOrder.getSupplierSkuNo());
			if(null != sku){
				String messageText ="Shangpin OrderNo: "+deleteOrder.getPurchaseNo()+"<br>"+
						"KEY: "+deleteOrder.getSupplierSkuNo()+"<br>"+
						"QUANTITY: 1 <br>"+
						"CUSTOMER NAME: "+"<br>"+
						"CUSTOMER SHIPPING ADDRESS: "+"<br>"+
						"CUSTOMER BILLING ADDRESS: "+"<br>"+
						"Status: cancelled";
				log.info("wise退款单参数："+messageText); 
				sendMail("wise-cancelled order-shangpin",messageText);
				log.info("wise退款成功。"); 
			}else{
				log.error("wise根据供应商门户编号和供应商skuid查找SKU失败");
			}
		} catch (Exception e) {
			log.error("wise发送退款邮件发生异常============"+e.getMessage());
		}
	}
	
	/**
	 * 发送邮件
	 * @param subject 邮件主题
	 * @param text 邮件内容
	 * @throws Exception
	 */
	private void sendMail(String subject,String text) throws Exception {
		ShangpinMail shangpinMail = new ShangpinMail();
		shangpinMail.setFrom("chengxu@shangpin.com");
		shangpinMail.setSubject(subject);
		shangpinMail.setText(text);
		shangpinMail.setTo("zhaogenchun@shangpin.com");
		List<String> addTo = new ArrayList<>();
//		addTo.add("lubaijiang@shangpin.com");
		addTo.add("zhaogenchun@shangpin.com");
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
