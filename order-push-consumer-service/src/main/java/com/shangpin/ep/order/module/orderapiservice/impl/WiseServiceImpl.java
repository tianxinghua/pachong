package com.shangpin.ep.order.module.orderapiservice.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ep.order.conf.mail.message.ShangpinMail;
import com.shangpin.ep.order.conf.mail.sender.ShangpinMailSender;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
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
@Component("wiseMailService")
@Slf4j
public class WiseServiceImpl{
	
	@Autowired
	private ShangpinMailSender shangpinMailSender;
	@Autowired
	private HubSkuService hubSkuService;
	
	/**
	 * 判断状态，如果推送api成功则给供应商发送邮件，否则给尚品发送邮件
	 * @param orderDTO
	 */
	public void pushConfirmOrder(OrderDTO orderDTO){
		try {
			if(PushStatus.ORDER_CONFIRMED.equals(orderDTO.getPushStatus())){
				handleConfirmOrder(orderDTO);
			}else{
				handleConfirmError(orderDTO);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e); 
		}
	}

	/**
	 * 给供应商发送邮件
	 * @param orderDTO
	 */
	private void handleConfirmOrder(OrderDTO orderDTO) {
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
				log.info("wise推送成功。"); 
			}else{
				log.info("wise根据供应商门户编号和供应商skuid查找SKU失败："+ orderDTO.getSupplierId()+" 供应商sku"+ orderDTO.getSupplierSkuNo());
			}
		} catch (Exception e) {
			log.error("wise推送订单异常========= "+e.getMessage());
		}
		
	}

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
		shangpinMail.setTo("martina@wiseboutique.com");
		List<String> addTo = new ArrayList<>();
		addTo.add("francesca.fiorani@wiseboutique.com");
		addTo.add("marketplace@wiseboutique.com");
//		addTo.add("andrea.venturini@wiseboutique.com");
//		addTo.add("wangsaying@shangpin.com");
//		addTo.add("lubaijiang@shangpin.com");
//		addTo.add("steven.ding@shangpin.com");
		shangpinMail.setAddTo(addTo );
		shangpinMailSender.sendShangpinMail(shangpinMail);
	}
	
	private void handleConfirmError(OrderDTO orderDTO){
		try {
			String message = "采购单号："+orderDTO.getPurchaseNo();
			String subject = "Wise推送失败的采购单";
			sendMailToShangpin(subject,message);
		} catch (Exception e) {
			log.error("wise发送推送失败采购单邮件时发生异常============"+e.getMessage());
		}
	}
	/**
	 * 发送邮件
	 * @param subject 邮件主题
	 * @param text 邮件内容
	 * @throws Exception
	 */
	private void sendMailToShangpin(String subject,String text) throws Exception {
		ShangpinMail shangpinMail = new ShangpinMail();
		shangpinMail.setFrom("chengxu@shangpin.com");
		shangpinMail.setSubject(subject);
		shangpinMail.setText(text);
		shangpinMail.setTo("lizhongren@shangpin.com");
		List<String> addTo = new ArrayList<>();
		addTo.add("lubaijiang@shangpin.com");
//		addTo.add("steven.ding@shangpin.com");
		shangpinMail.setAddTo(addTo );
		shangpinMailSender.sendShangpinMail(shangpinMail);
	}

}
