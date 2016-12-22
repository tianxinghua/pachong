package com.shangpin.ephub.product.business.rest.checkHub.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.product.business.rest.model.dto.BrandModelDto;
import com.shangpin.ephub.product.business.rest.model.result.BrandModelResult;
import com.shangpin.ephub.product.business.rest.model.service.IHubBrandModelRuleService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:HubCheckRuleController.java </p>
 * <p>Description: HUB入库前校验规则rest服务接口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月22 下午3:52:56
 */
@RestController
@RequestMapping(value = "/hub-check")
@Slf4j
public class HubCheckRuleController {
	/**
	 * 注入品牌型号规则业务逻辑实现实例
	 */
	@Autowired
	private IHubBrandModelRuleService hubBrandModelRuleService;
	/**
	 * 校验供应商品牌型号是否符合品牌方型号规则
	 * @param dto 数据传输对象
	 * @return 校验结果：包含是否校验通过以及校验之后的结果（校验通过的经过加工的品牌型号）
	 */
	@RequestMapping(value = "/verify")
	public BrandModelResult verify(@RequestBody BrandModelDto dto){
		long start = System.currentTimeMillis();
		log.info(HubCheckRuleController.class.getName()+".verify接收到的参数为:{}， 系统即将开始进行品牌型号规则验证!", dto.toString());
		BrandModelResult result = new BrandModelResult();
		String brandModel = hubBrandModelRuleService.regexVerify(dto.getHubBrandNo(), dto.getHubCategoryNo(), dto.getBrandMode());
		if (StringUtils.isNotBlank(brandModel)) {
			result.setPassing(true);
			result.setBrandMode(brandModel);
		} else {
			String _brandModel = hubBrandModelRuleService.ruleVerify(dto.getHubBrandNo(), dto.getHubCategoryNo(), dto.getBrandMode());
			if (StringUtils.isBlank(_brandModel)) {
				result.setPassing(false);
			} else {
				result.setPassing(true);
				result.setBrandMode(_brandModel);
			}
		}
		log.info(HubCheckRuleController.class.getName()+".verify接收到的参数为:{}， 系统品牌型号规则验证结果为{}， 耗时{}milliseconds!", dto.toString(), result.toString(), System.currentTimeMillis() - start);
		return result;
	}
}
