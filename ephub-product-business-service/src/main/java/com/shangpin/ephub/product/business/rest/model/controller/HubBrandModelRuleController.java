package com.shangpin.ephub.product.business.rest.model.controller;

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
 * <p>Title:HubBrandModelRuleController.java </p>
 * <p>Description: HUB品牌型号规则rest服务接口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月21日 下午3:52:56
 */
@RestController
@RequestMapping(value = "/hub-brand-model-rule")
@Slf4j
public class HubBrandModelRuleController {
	/**
	 * 注入品牌型号规则业务逻辑实现实例
	 */
	@Autowired
	private IHubBrandModelRuleService hubBrandModelRuleService;
	/**
	 * 过时：请使用{@link #verifyWithCategory(BrandModelDto)}校验方式进行替代！
	 * 校验供应商品牌型号是否符合品牌方型号规则：只校验品牌不校验品类
	 * @param dto 数据传输对象
	 * @return 校验结果：包含是否校验通过以及校验之后的结果（校验通过的经过加工的品牌型号）
	 */
	@Deprecated
	@RequestMapping(value = "/verify")
	public BrandModelResult verify(@RequestBody BrandModelDto dto){
		long start = System.currentTimeMillis();
		log.info("品牌校验规则（仅仅校验品牌不校验品类）服务接收到的参数为:{}， 系统即将开始进行品牌型号规则验证!", dto.toString());
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
		log.info("品牌校验规则（仅仅校验品牌不校验品类）服务接收到的参数为:{}， 系统品牌型号规则验证结果为{}， 耗时{}milliseconds!", dto.toString(), result.toString(), System.currentTimeMillis() - start);
		return result;
	}
	/**
	 * 校验供应商品牌型号是否符合品牌方型号规则：校验品牌并且校验品类
	 * @param dto 数据传输对象
	 * @return 校验结果：包含是否校验通过以及校验之后的结果（校验通过的经过加工的品牌型号）
	 */
	@RequestMapping(value = "/verify-with-category")
	public BrandModelResult verifyWithCategory(@RequestBody BrandModelDto dto){
		long start = System.currentTimeMillis();
		log.info("品牌校验规则（校验品牌并且校验品类）服务接收到的参数为:{}， 系统即将开始进行品牌型号规则验证!", dto.toString());
		BrandModelResult result = new BrandModelResult();
		//临时解决方案，眼镜货号不校验
		if(checkHubCategoryIsYanJing(dto,result)){
			return result;
		}
		String brandModel = hubBrandModelRuleService.regexVerifyWithCategory(dto.getHubBrandNo(), dto.getHubCategoryNo(), dto.getBrandMode());
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
		log.info("品牌校验规则（校验品牌并且校验品类）服务接收到的参数为:{}， 系统品牌型号规则验证结果为{}， 耗时{}milliseconds!", dto.toString(), result.toString(), System.currentTimeMillis() - start);
		return result;
	}


	@RequestMapping(value = "/replace-symbol")
	public String  replaceSymbol(@RequestBody BrandModelDto dto){

		return  hubBrandModelRuleService.replaceSymbol(dto.getHubBrandNo(), dto.getHubCategoryNo(), dto.getBrandMode()," ");

	}

	private boolean checkHubCategoryIsYanJing(BrandModelDto dto,BrandModelResult result) {
		if(dto!=null&&dto.getHubCategoryNo()!=null&&dto.getHubCategoryNo().startsWith("A13")){
			if(dto.getBrandMode()!=null){
				result.setPassing(true);	
			}else{
				result.setPassing(false);
			}
			result.setBrandMode(dto.getBrandMode());
			return true;
		}
		return false;
	}
}
