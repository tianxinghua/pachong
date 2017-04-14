package com.shangpin.ep.order.module.orderapiservice.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.*;
import com.shangpin.ep.order.module.order.service.impl.OpenApiService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ep.order.common.HandleException;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.mail.message.ShangpinMail;
import com.shangpin.ep.order.conf.mail.sender.ShangpinMailSender;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.sku.bean.HubSku;
import com.shangpin.ep.order.module.sku.service.impl.HubSkuService;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

@Component("giglioServiceImpl")
@Slf4j
public class GiglioServiceImpl implements IOrderService{
	
	private static String split = ";";
	
	@Autowired
    private LogCommon logCommon;    
	@Autowired
	private ShangpinMailSender shangpinMailSender;
	@Autowired
	private HubSkuService hubSkuService;
	@Autowired
    private HandleException handleException;
	@Autowired
	SupplierProperties supplierProperties;
	@Autowired
	OpenApiService openApiService;

	private  String purchaseOrderUrl;
	private  String appKey;
	private  String appSe;


	@PostConstruct
	public void init(){

		appKey =  supplierProperties.getGiglio().getOpenApiKey();
		appSe =  supplierProperties.getGiglio().getOpenApiSecret();
		purchaseOrderUrl =  supplierProperties.getGiglio().getPurchaseOrderUrl();
	}

	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		orderDTO.setLockStockTime(new Date());
		orderDTO.setPushStatus(PushStatus.NO_LOCK_API);
		orderDTO.setLogContent("------锁库结束-------");
		logCommon.loggerOrder(orderDTO, LogTypeStatus.LOCK_LOG);
	}

	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		this.setOrderMsgAndSendMail(orderDTO);
		
		
	}

	@Override
	public void handleCancelOrder(OrderDTO deleteOrder) {
		deleteOrder.setCancelTime(new Date()); 
		deleteOrder.setPushStatus(PushStatus.NO_LOCK_CANCELLED_API); 
	}

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
				log.info("giglio退款单参数："+buffer.toString()); 
				sendMail("cancelled order-shangpin",buffer.toString());
				deleteOrder.setRefundTime(new Date());
				deleteOrder.setPushStatus(PushStatus.REFUNDED);
				log.info("giglio退款成功。"); 
			}else{
				deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
				deleteOrder.setErrorType(ErrorStatus.OTHER_ERROR);
				deleteOrder.setDescription("giglio根据供应商门户编号和供应商skuid查找SKU失败");
			}
		} catch (Exception e) {
			deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
			handleException.handleException(deleteOrder, e); 
			deleteOrder.setLogContent("giglio退款发生异常============"+e.getMessage());
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
		shangpinMail.setTo("giuseppe@giglio.com");
		List<String> addTo = new ArrayList<>();
		addTo.add("fabio@giglio.com");
		addTo.add("wangsaying@shangpin.com");
		addTo.add("lubaijiang@shangpin.com");
		shangpinMail.setAddTo(addTo );
		shangpinMailSender.sendShangpinMail(shangpinMail);
	}

	/**
	 * 支付后 第一次 直接推送 ，不包含PID  发送成功  抛异常 再次处理时查询 是否产生采购单 若有 再次发送邮件
	 * 若失败 只要有异常 就查询采购单是否存在
	 *
	 * @param orderDTO
	 */
	private void  setOrderMsgAndSendMail(OrderDTO orderDTO) {

		HubSku sku = hubSkuService.getSku(orderDTO.getSupplierId(), orderDTO.getSupplierSkuNo());
		try {
			if(null != sku){
				Date date = new Date();
				if(orderDTO.getOrderStatus()==OrderStatus.PAYED&&orderDTO.getPushStatus()==PushStatus.NO_LOCK_API){
					firstSendMail(orderDTO, sku, date);
				}else if(orderDTO.getOrderStatus()==OrderStatus.PAYED&&orderDTO.getPushStatus()==PushStatus.ORDER_CONFIRMED_ERROR){

					secondSendMail(orderDTO, sku);
				}


			}else{
				log.info("giglio根据供应商门户编号和供应商skuid查找SKU失败："+ orderDTO.getSupplierId()+" 供应商sku："+ orderDTO.getSupplierSkuNo());
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
				orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);
				orderDTO.setDescription("giglio根据供应商门户编号和供应商skuid查找SKU失败");
			}
		} catch (Exception e) {
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			handleException.handleException(orderDTO,e);
			orderDTO.setLogContent("giglio推送订单异常========= "+e.getMessage());

		}


	}

	private void secondSendMail(OrderDTO orderDTO, HubSku sku) throws Exception {
		//获取PID标记
		String pid = this.getPid(orderDTO.getPurchaseNo());
		StringBuffer buffer = new StringBuffer();
		buffer.append(orderDTO.getPurchaseNo()).append(split)
                .append(sku.getProductSize()).append(split).append(orderDTO.getSupplierSkuNo()).append(split)
                .append(sku.getProductCode()).append(split).append(sku.getBarcode()).append(split)
                .append(orderDTO.getQuantity()).append(";").append(pid);
		log.info("giglio推送订单参数："+buffer.toString());
		sendMail("order-shangpin",buffer.toString());
		orderDTO.setUpdateTime(new Date());
		if(StringUtils.isNotBlank(pid)){
            orderDTO.setConfirmTime(new Date());
            orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED);

        }else{
            log.info("giglio采购单号:"+ orderDTO.getPurchaseNo()+" 尚未生成");
            orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
            orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);
            orderDTO.setDescription("giglio采购单号:"+ orderDTO.getPurchaseNo()+" 尚未生成");
        }
	}

	private void firstSendMail(OrderDTO orderDTO, HubSku sku, Date date) throws Exception {
		StringBuffer buffer = new StringBuffer();
		buffer.append(orderDTO.getPurchaseNo()).append(split)
                .append(sku.getProductSize()).append(split).append(orderDTO.getSupplierSkuNo()).append(split)
                .append(sku.getProductCode()).append(split).append(sku.getBarcode()).append(split)
                .append(orderDTO.getQuantity());
		log.info("giglio推送订单参数："+buffer.toString());
		sendMail("order-shangpin",buffer.toString());

		orderDTO.setUpdateTime(date);

		//设置推送状态

		orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
		orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);
		log.info("giglio 无PID推送成功。");
		orderDTO.setDescription("giglio采购单号:"+ orderDTO.getPurchaseNo()+" 尚未生成");
		orderDTO.setLogContent("giglio采购单号:"+ orderDTO.getPurchaseNo()+" 尚未生成");
	}

	private String getPid(String purchaseNo){
		String result ="";
		return result;
	}

}
