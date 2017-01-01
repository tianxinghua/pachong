package com.shangpin.ephub.product.business.rest.hubproduct.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.product.business.common.service.check.HubCheckService;
import com.shangpin.ephub.product.business.rest.hubproduct.dto.HubProductDto;
import com.shangpin.ephub.product.business.rest.hubproduct.manager.HubProductCheckManager;

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
	
	public String checkHubProduct(HubProductDto hubProduct){
		
		StringBuffer str = new StringBuffer();
		//校验品牌
		if(hubProduct.getBrandNo()!=null){
			if(!hubCheckService.checkHubBrand(hubProduct.getBrandNo())){
				str.append("品牌编号不存在，") ;
			}	
		}
		
		//校验品类
		if(hubProduct.getCategoryNo()!=null){
			if(!hubCheckService.checkHubCategory(hubProduct.getCategoryNo())){
				str.append("品类编号有误") ;
			}	
		}
		
		//校验颜色
		if(hubProduct.getHubColor()!=null){
			if(!hubCheckService.checkHubColor(hubProduct.getHubColor())){	
				str.append("颜色编号有误") ;
			}
		}
		
		//校验季节
		if(hubProduct.getSeason()!=null){
			if(!hubCheckService.checkHubSeason(hubProduct.getSeason(),hubProduct.getMarketTime())){
				str.append("季节编号有误") ;
			}
		}
		
		//校验尺码
		if(hubProduct.getSkuSize()!=null){
			if(!hubCheckService.checkHubSize(hubProduct.getSkuSize())){
				str.append("尺码编号有误") ;
			}	
		}
		
		//校验性别
		if(hubProduct.getGender()!=null){
			if(!hubCheckService.checkHubGender(hubProduct.getGender())){
				str.append("性别编号有误") ;
			}	
		}
		//校验产地
		return str.toString();
	}
}
