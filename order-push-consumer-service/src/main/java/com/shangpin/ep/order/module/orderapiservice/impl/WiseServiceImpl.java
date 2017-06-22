package com.shangpin.ep.order.module.orderapiservice.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

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
 * <p>Title: WiseServiceImpl</p>
 * <p>Description: wise供应商订单以发送邮件的方式发送给对方，已弃用 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月22日 上午10:15:54
 *
 */
@Deprecated
@Slf4j
public class WiseServiceImpl implements IOrderService {
	
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
				//采购单号 尺码  skuId 货号  barcode 数量    
				String messageText ="Shangpin OrderNo: "+orderDTO.getPurchaseNo()+"<br>"+
									"ProductSize: "+sku.getProductSize()+"<br>"+
									"SpuId-SkuId: "+orderDTO.getSupplierSkuNo()+"<br>"+
									"StyleCode-ColorCode: "+(null != sku.getProductCode()? sku.getProductCode():"")+"<br>"+
									"Barcode: "+sku.getBarcode()+"<br>"+
									"Qty: "+orderDTO.getQuantity()+"<br>"+
									"Status: confirmed";
				log.info("wise推送订单参数："+messageText); 
				sendMail("wise-order-shangpin",messageText);
				orderDTO.setConfirmTime(new Date()); 
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED); 
				log.info("wise推送成功。"); 
			}else{
				log.info("wise根据供应商门户编号和供应商skuid查找SKU失败："+ orderDTO.getSupplierId()+" 供应商sku"+ orderDTO.getSupplierSkuNo());
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
				orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);							
				orderDTO.setDescription("wise根据供应商门户编号和供应商skuid查找SKU失败");
			}
		} catch (Exception e) {
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			handleException.handleException(orderDTO,e);
			orderDTO.setLogContent("wise推送订单异常========= "+e.getMessage());
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
				String messageText ="Shangpin OrderNo: "+deleteOrder.getPurchaseNo()+"<br>"+
						"ProductSize: "+sku.getProductSize()+"<br>"+
						"SpuId-SkuId: "+deleteOrder.getSupplierSkuNo()+"<br>"+
						"StyleCode-ColorCode: "+(null != sku.getProductCode()? sku.getProductCode():"")+"<br>"+
						"Barcode: "+sku.getBarcode()+"<br>"+
						"Qty: "+deleteOrder.getQuantity()+"<br>"+
						"Status: cancelled";
				log.info("wise退款单参数："+messageText); 
				sendMail("wise-cancelled order-shangpin",messageText);
				deleteOrder.setRefundTime(new Date());
				deleteOrder.setPushStatus(PushStatus.REFUNDED);
				log.info("wise退款成功。"); 
			}else{
				deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
				deleteOrder.setErrorType(ErrorStatus.OTHER_ERROR);
				deleteOrder.setDescription("wise根据供应商门户编号和供应商skuid查找SKU失败");
			}
		} catch (Exception e) {
			deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
			handleException.handleException(deleteOrder, e); 
			deleteOrder.setLogContent("wise退款发生异常============"+e.getMessage());
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
		shangpinMail.setTo("martina@wiseboutique.com");
		List<String> addTo = new ArrayList<>();
		addTo.add("francesca.fiorani@wiseboutique.com");
		addTo.add("andrea.venturini@wiseboutique.com");
		addTo.add("wangsaying@shangpin.com");
//		addTo.add("lubaijiang@shangpin.com");
		addTo.add("steven.ding@shangpin.com");
		shangpinMail.setAddTo(addTo );
		shangpinMailSender.sendShangpinMail(shangpinMail);
	}

}
