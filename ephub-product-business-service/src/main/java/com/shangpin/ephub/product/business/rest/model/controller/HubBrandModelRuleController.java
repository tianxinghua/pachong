package com.shangpin.ephub.product.business.rest.model.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shangpin.ephub.product.business.rest.model.dto.BrandModelDto;
import com.shangpin.ephub.product.business.rest.model.result.BrandModelResult;
import com.shangpin.ephub.product.business.rest.model.service.IHubBrandModelRuleService;

/**
 * <p>Title:HubBrandModelRuleController.java </p>
 * <p>Description: HUB品牌型号规则rest服务接口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月21日 下午3:52:56
 */
@Controller
@RequestMapping(value = "/hub-brand-model-rule")
public class HubBrandModelRuleController {
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
	public BrandModelResult verify(BrandModelDto dto){
		
		return null;
	}
}
