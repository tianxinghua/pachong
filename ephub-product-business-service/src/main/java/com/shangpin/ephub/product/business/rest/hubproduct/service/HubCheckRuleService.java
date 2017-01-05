package com.shangpin.ephub.product.business.rest.hubproduct.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.product.business.model.dto.BrandModelDto;
import com.shangpin.ephub.client.product.business.model.gateway.HubBrandModelRuleGateWay;
import com.shangpin.ephub.client.product.business.model.result.BrandModelResult;
import com.shangpin.ephub.product.business.common.service.check.HubCheckService;
import com.shangpin.ephub.product.business.rest.hubproduct.dto.HubProductDto;
import com.shangpin.ephub.product.business.rest.hubproduct.manager.HubProductCheckManager;
import com.shangpin.ephub.product.business.rest.hubproduct.result.HubProductCheckResult;
import com.shangpin.ephub.product.business.rest.model.controller.HubBrandModelRuleController;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:HubCheckRuleService.java </p>
 * <p>Description: hua商品校验实现</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月23日 下午4:15:16
 */
@SuppressWarnings("unused")
@Service
@Slf4j
public class HubCheckRuleService {
	
	@Autowired
	private HubProductCheckManager hubProductCheckRuleManager;
	
	@Autowired
	HubCheckService hubCheckService;
	@Autowired
	HubBrandModelRuleGateWay hubBrandModelRuleGateWay;
	
	public HubProductCheckResult checkHubProduct(HubProductDto hubProduct){
		HubProductCheckResult result = new HubProductCheckResult();
		result.setPassing(true);
		StringBuffer str = new StringBuffer();
		//校验品牌
		if(hubProduct.getBrandNo()!=null){
			if(!hubCheckService.checkHubBrand(hubProduct.getBrandNo())){
				str.append("品牌编号不存在，") ;
				result.setPassing(false);
			}	
		}else{
			str.append("品牌编号为空，");
			result.setPassing(false);
		}
		
		//校验品类
		if(hubProduct.getCategoryNo()!=null){
			if(!hubCheckService.checkHubCategory(hubProduct.getCategoryNo())){
				str.append("品类编号有误") ;
				result.setPassing(false);
			}	
		}else{
			str.append("品类编号为空，");
			result.setPassing(false);
		}
		
		//校验颜色
		if(hubProduct.getHubColor()!=null){
			if(!hubCheckService.checkHubColor(hubProduct.getHubColor())){	
				str.append("颜色编号有误") ;
				result.setPassing(false);
			}
		}else{
			str.append("颜色为空，");
			result.setPassing(false);
		}
		
		//校验季节
		if(hubProduct.getSeason()!=null){
			if(!hubCheckService.checkHubSeason(hubProduct.getSeason())){
				str.append("季节编号有误") ;
				result.setPassing(false);
			}
		}else{
			str.append("季节为空，");
			result.setPassing(false);
		}
		
		//校验尺码
		if(hubProduct.getSkuSize()!=null){
			if(!hubCheckService.checkHubSize(hubProduct.getSkuSize())){
				str.append("尺码编号有误") ;
				result.setPassing(false);
			}	
		}else{
			str.append("尺码为空，");
			result.setPassing(false);
		}
		
		//校验性别
		if(hubProduct.getGender()!=null){
			if(!hubCheckService.checkHubGender(hubProduct.getGender())){
				str.append("性别编号有误") ;
				result.setPassing(false);
			}	
		}else{
			str.append("性别为空，");
			result.setPassing(false);
		}
		
		//货号
		BrandModelDto BrandModelDto = null;
		BrandModelResult brandModelResult= null;
		if(hubProduct.getSpuModel()!=null){
			BrandModelDto = new BrandModelDto();
			BrandModelDto.setBrandMode(hubProduct.getSpuModel());
			BrandModelDto.setHubBrandNo(hubProduct.getBrandNo());
			BrandModelDto.setHubCategoryNo(hubProduct.getCategoryNo());
			brandModelResult=  hubBrandModelRuleGateWay.verify(BrandModelDto);
		}else{
			str.append("spuModel为空");
			result.setPassing(false);
		}
			
		if(brandModelResult.isPassing()){
			if(result.isPassing()){
				result.setResult(brandModelResult.getBrandMode());
			}else{
				result.setResult(str.toString());
			}
		}else{
			str.append("spuModel："+hubProduct.getSpuModel()+"校验失败,校验结果："+brandModelResult.getBrandMode());
			result.setPassing(false);
			result.setResult(str.toString());
		}
		
		//校验产地
		return result;
	}
}
