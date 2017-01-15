package com.shangpin.ephub.product.business.rest.hubproduct.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.product.business.model.dto.BrandModelDto;
import com.shangpin.ephub.client.product.business.model.gateway.HubBrandModelRuleGateWay;
import com.shangpin.ephub.client.product.business.model.result.BrandModelResult;
import com.shangpin.ephub.client.util.RegexUtil;
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
			if(!hubCheckService.getBrand(hubProduct.getBrandNo())){
				str.append("品牌编号"+hubProduct.getBrandNo()+"不存在,") ;
				result.setPassing(false);
			}	
		}else{
			str.append("品牌编号为空，");
			result.setPassing(false);
		}
		
		//校验品类
		if(hubProduct.getCategoryNo()!=null){
			if(!hubCheckService.getCategoryName(hubProduct.getCategoryNo())){
				str.append("品类编号"+hubProduct.getCategoryNo()+"不存在,") ;
				result.setPassing(false);
			}	
		}else{
			str.append("品类编号为空，");
			result.setPassing(false);
		}
		
		//校验颜色
		if(hubProduct.getHubColor()!=null){
			if(!hubCheckService.checkHubColor(hubProduct.getHubColor())){	
				str.append("颜色"+hubProduct.getHubColor()+"不存在,") ;
				result.setPassing(false);
			}
		}else{
			str.append("颜色为空，");
			result.setPassing(false);
		}
		
		//校验季节
		if(hubProduct.getSeason()!=null){
			if(!hubCheckService.checkHubSeason(hubProduct.getMarketTime()+"_"+hubProduct.getSeason())){
				str.append("季节编号"+hubProduct.getMarketTime()+"_"+hubProduct.getSeason()+"不存在,") ;
				result.setPassing(false);
			}
		}else{
			str.append("季节为空，");
			result.setPassing(false);
		}
		//校验尺码
		if("尺码".equals(hubProduct.getSpecificationType())){
			if(hubProduct.getSkuSize()!=null){
				//String hubCategoryNo,String hubBrandNo,String supplierId,String supplierSize
				String size = null;
				size = hubCheckService.checkHubSize(hubProduct.getCategoryNo(),hubProduct.getBrandNo(),hubProduct.getSkuSize());
				if(size!=null){
					result.setSize(size);
				}else{
					str.append("尺码"+hubProduct.getSkuSize()+"不存在,") ;
					result.setPassing(false);
				}
			}else{
				str.append("尺码为空，");
				result.setPassing(false);
			}
		}
		//校验性别
		if(hubProduct.getGender()!=null){
			if(!hubCheckService.checkHubGender(hubProduct.getGender())){
				str.append("性别"+hubProduct.getGender()+"不存在,") ;
				result.setPassing(false);
			}	
		}else{
			str.append("性别为空，");
			result.setPassing(false);
		}
		
		if(hubProduct.getMaterial()!=null){
			if(!RegexUtil.excludeLetter(hubProduct.getMaterial())){
				result.setPassing(false);
				str.append("材质中含有英文字符："+hubProduct.getMaterial()) ;
	        }
		}else{
			str.append("材质为空，");
			result.setPassing(false);
		}
		
//		//货号
		BrandModelDto BrandModelDto = null;
		BrandModelResult brandModelResult= null;
		if(hubProduct.getSpuModel()!=null){
			BrandModelDto = new BrandModelDto();
			BrandModelDto.setBrandMode(hubProduct.getSpuModel());
			BrandModelDto.setHubBrandNo(hubProduct.getBrandNo());
			BrandModelDto.setHubCategoryNo(hubProduct.getCategoryNo());
			brandModelResult=  hubBrandModelRuleGateWay.verify(BrandModelDto);
			if(brandModelResult.isPassing()){
				result.setSpuModel(brandModelResult.getBrandMode());
			}else{
				str.append("spuModel："+hubProduct.getSpuModel()+"校验失败");
				result.setPassing(false);
			}
		}else{
			str.append("spuModel为空");
			result.setPassing(false);
		}
			
		result.setResult(str.toString());
		//校验产地
		return result;
	}
}
