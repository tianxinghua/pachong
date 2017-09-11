package com.shangpin.ep.order.module.orderapiservice.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ep.order.common.HandleException;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.mail.message.ShangpinMail;
import com.shangpin.ep.order.conf.mail.sender.ShangpinMailSender;
import com.shangpin.ep.order.enumeration.ErrorStatus;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.sku.bean.HubSku;
import com.shangpin.ep.order.module.sku.service.impl.HubSkuService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title: YlatiServiceImpl</p>
 * <p>Description: ylati供应商邮件对接 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年8月9日 下午5:11:30
 *
 */
@Component("ylatiServiceImpl")
@Slf4j
public class YlatiServiceImpl implements IOrderService{
	
	private static String split = ";";
	
	@Autowired
    private LogCommon logCommon;    
	@Autowired
	private ShangpinMailSender shangpinMailSender;
	@Autowired
	private HubSkuService hubSkuService;
	@Autowired
    private HandleException handleException;

	@SuppressWarnings("static-access")
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		orderDTO.setLockStockTime(new Date());
		orderDTO.setPushStatus(PushStatus.NO_LOCK_API);
		orderDTO.setLogContent("------锁库结束-------");
		logCommon.loggerOrder(orderDTO, LogTypeStatus.LOCK_LOG);
	}

	@SuppressWarnings("static-access")
	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		try {
			HubSku sku = hubSkuService.getSku(orderDTO.getSupplierId(), orderDTO.getSupplierSkuNo());
			if(null != sku){
				StringBuffer buffer = new StringBuffer();
				buffer.append(orderDTO.getPurchaseNo()).append(split)
				.append(sku.getProductSize()).append(split).append(orderDTO.getSupplierSkuNo()).append(split)
				.append(sku.getProductCode()).append(split).append(sku.getBarcode()).append(split)
				.append(orderDTO.getQuantity());
				log.info("ylati推送订单参数："+buffer.toString()); 
				sendMail("order-shangpin",buffer.toString());
				orderDTO.setConfirmTime(new Date()); 
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED); 
				log.info("ylati推送成功。"); 
			}else{
				log.info("ylati根据供应商门户编号和供应商skuid查找SKU失败："+ orderDTO.getSupplierId()+" 供应商sku："+ orderDTO.getSupplierSkuNo());
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
				orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);							
				orderDTO.setDescription("ylati根据供应商门户编号和供应商skuid查找SKU失败");
			}
		} catch (Exception e) {
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			handleException.handleException(orderDTO,e);
			orderDTO.setLogContent("ylati推送订单异常========= "+e.getMessage());
			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		}
		
		
	}

	@Override
	public void handleCancelOrder(OrderDTO deleteOrder) {
		deleteOrder.setCancelTime(new Date()); 
		deleteOrder.setPushStatus(PushStatus.NO_LOCK_CANCELLED_API); 
	}

	@SuppressWarnings("static-access")
	@Override
	public void handleRefundlOrder(OrderDTO deleteOrder) {
		try {
			HubSku sku = hubSkuService.getSku(deleteOrder.getSupplierId(), deleteOrder.getSupplierSkuNo());
			if(null != sku){
				StringBuffer buffer = new StringBuffer();
				buffer.append(deleteOrder.getPurchaseNo()).append(split)
				.append(sku.getProductSize()).append(split).append(deleteOrder.getSupplierSkuNo()).append(split)
				.append(sku.getProductCode()).append(split).append(sku.getBarcode()).append(split)
				.append(deleteOrder.getQuantity());
				log.info("ylati退款单参数："+buffer.toString()); 
				sendMail("cancelled order-shangpin",buffer.toString());
				deleteOrder.setRefundTime(new Date());
				deleteOrder.setPushStatus(PushStatus.REFUNDED);
				log.info("ylati退款成功。"); 
			}else{
				deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
				deleteOrder.setErrorType(ErrorStatus.OTHER_ERROR);
				deleteOrder.setDescription("ylati根据供应商门户编号和供应商skuid查找SKU失败");
			}
		} catch (Exception e) {
			deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
			handleException.handleException(deleteOrder, e); 
			deleteOrder.setLogContent("ylati退款发生异常============"+e.getMessage());
			logCommon.loggerOrder(deleteOrder, LogTypeStatus.REFUNDED_LOG);		
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
		shangpinMail.setTo("sales@ylatifootwear.com");
		List<String> addTo = new ArrayList<>();
		addTo.add("press@ylatifootwear.com");
		addTo.add("vportogallo@genertecitalia.it");
//		addTo.add("lubaijiang@shangpin.com");
		addTo.add("steven.ding@shangpin.com");
		shangpinMail.setAddTo(addTo );
		shangpinMailSender.sendShangpinMail(shangpinMail);
	}

}
