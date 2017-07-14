package com.shangpin.ephub.product.business.rest.mail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.product.business.conf.mail.message.ShangpinMail;
import com.shangpin.ephub.product.business.conf.mail.sender.ShangpinMailSender;
import com.shangpin.ephub.product.business.rest.model.dto.BrandModelDto;
import com.shangpin.ephub.product.business.rest.model.result.BrandModelResult;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:HubBrandModelRuleController.java </p>
 * <p>Description: HUB品牌型号规则rest服务接口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月21日 下午3:52:56
 */
@RestController
@RequestMapping(value = "/shang-pin-mail-sender")
@Slf4j
public class ShangpinMailSenderController {
	/**
	 * 注入品牌型号规则业务逻辑实现实例
	 */
	@Autowired
	private ShangpinMailSender shangpinMailSender;
	/**
	 * 过时：请使用{@link #verifyWithCategory(BrandModelDto)}校验方式进行替代！
	 * 校验供应商品牌型号是否符合品牌方型号规则：只校验品牌不校验品类
	 * @param dto 数据传输对象
	 * @return 校验结果：包含是否校验通过以及校验之后的结果（校验通过的经过加工的品牌型号）
	 */
	@RequestMapping(value = "/send")
	public void send(@RequestBody ShangpinMail shangpinMail){
		log.info("发送邮件参数!", shangpinMail.toString());
		try {
			shangpinMailSender.sendShangpinMail(shangpinMail);
			log.info("发送邮件success!");
		} catch (Exception e) {
			log.info("发送邮件fail!");
		}
	}
}
