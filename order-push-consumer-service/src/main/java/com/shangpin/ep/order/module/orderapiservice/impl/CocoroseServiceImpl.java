package com.shangpin.ep.order.module.orderapiservice.impl;

import com.shangpin.ep.order.conf.mail.message.ShangpinMail;
import com.shangpin.ep.order.conf.mail.sender.ShangpinMailSender;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.sku.bean.HubSku;
import com.shangpin.ep.order.module.sku.service.impl.HubSkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * <p>Description: 供应商订单以发送邮件的方式发送给对方</p>

 *
 */
@Component("cocoroseMailService")
@Slf4j
public class CocoroseServiceImpl implements IOrderService {
	
	@Autowired
	private ShangpinMailSender shangpinMailSender;
	@Autowired
	private HubSkuService hubSkuService;


	private String emailContent = "Dear Sir or Madam of COCOROSE,\n" +
			"We are pleased to inform you that you have a new SHANGPIN order of ______.\n" +
			"Please check your stock status in time and remember that your order will be available within 60 hours from the order happened time.\n" +
			"Please make sure all of our orders will be well arrange and ship out in time.\n" +
			"Thank you for your support.\n" +
			"\n" +
			"Best regards\n" +
			"www.shangpin.com";
	


	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {

	}

	/**
	 * 给供应商发送邮件
	 * @param orderDTO
	 */
	public void handleConfirmOrder(OrderDTO orderDTO) {
		try {

			String messageText =emailContent.replaceAll("______",orderDTO.getPurchaseNo());
			log.info("cocorose  推送内容 ："+messageText);
			sendMail("order-shangpin",messageText);
			log.info("cocorose  推送成功。");

		} catch (Exception e) {
			handleConfirmError(orderDTO);
			log.error("cocorose  推送订单异常========= "+e.getMessage());

		}
		
	}

	@Override
	public void handleCancelOrder(OrderDTO deleteOrder) {

	}

	public void handleRefundlOrder(OrderDTO deleteOrder) {

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
		shangpinMail.setTo("studio@cocoroselondon.com ");
		List<String> addTo = new ArrayList<>();
		addTo.add("gareth@cocoroselondon.com");
		addTo.add("steven.ding@shangpin.com ");
//		addTo.add("lizhongren@shangpin.com");


		shangpinMail.setAddTo(addTo );
		shangpinMailSender.sendShangpinMail(shangpinMail);
	}
	
	private void handleConfirmError(OrderDTO orderDTO){
		try {
			String message = "采购单号："+orderDTO.getPurchaseNo();
			String subject = "cocorose 邮件发送失败的采购单";
			sendMailToShangpin(subject,message);
		} catch (Exception e) {
			log.error("cocorose 邮件发送时 发生异常============"+e.getMessage());
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

		shangpinMailSender.sendShangpinMail(shangpinMail);
	}

}
