package com.shangpin.ephub.product.business.rest.hubproduct.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.util.RegexUtil;
import com.shangpin.ephub.product.business.common.service.check.HubCheckService;
import com.shangpin.ephub.product.business.rest.hubpending.spu.result.HubSizeCheckResult;
import com.shangpin.ephub.product.business.rest.hubproduct.dto.HubProductDto;
import com.shangpin.ephub.product.business.rest.hubproduct.manager.HubProductCheckManager;
import com.shangpin.ephub.product.business.rest.hubproduct.result.HubProductCheckResult;
import com.shangpin.ephub.product.business.rest.model.controller.HubBrandModelRuleController;
import com.shangpin.ephub.product.business.rest.model.dto.BrandModelDto;
import com.shangpin.ephub.product.business.rest.model.result.BrandModelResult;

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
	HubBrandModelRuleController hubBrandModelRule;
	
	public HubProductCheckResult checkHubProduct(HubProductDto hubProduct){
		HubProductCheckResult result = new HubProductCheckResult();
		result.setPassing(true);
		StringBuffer str = new StringBuffer();
		//校验品牌
		if(StringUtils.isNoneBlank(hubProduct.getBrandNo())){
			if(!hubCheckService.getBrand(hubProduct.getBrandNo())){
				str.append("品牌编号"+hubProduct.getBrandNo()+"不存在,") ;
				result.setPassing(false);
			}	
		}else{
			str.append("品牌编号为空，");
			result.setPassing(false);
		}
		
		//校验品类
		if(StringUtils.isNoneBlank(hubProduct.getCategoryNo())){
			if(!hubCheckService.getCategoryName(hubProduct.getCategoryNo())){
				str.append("品类编号"+hubProduct.getCategoryNo()+"不存在,") ;
				result.setPassing(false);
			}	
		}else{
			str.append("品类编号为空，");
			result.setPassing(false);
		}
		
		//校验颜色
		if(StringUtils.isNoneBlank(hubProduct.getHubColor())){
			if(!hubCheckService.checkHubColor(hubProduct.getHubColor())){	
				str.append("颜色"+hubProduct.getHubColor()+"不存在,") ;
				result.setPassing(false);
			}
		}else{
			str.append("颜色为空，");
			result.setPassing(false);
		}
		
		//校验季节
		if(StringUtils.isNoneBlank(hubProduct.getSeason())){
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
			if(StringUtils.isNoneBlank(hubProduct.getSkuSize())){
				//String hubCategoryNo,String hubBrandNo,String supplierId,String supplierSize
				HubSizeCheckResult checkResult = null;
				checkResult = hubCheckService.hubSizeExist(hubProduct.getCategoryNo(),hubProduct.getBrandNo(),hubProduct.getSkuSize());
				if(checkResult.isPassing()){
					result.setSize(checkResult.getScreenSizeStandardValueId()+","+hubProduct.getSizeType()+":"+hubProduct.getSkuSize());
				}else{
					str.append("尺码"+hubProduct.getSizeType()+":"+hubProduct.getSkuSize()+"不存在,") ;
					result.setPassing(false);
				}
			}else{
				str.append("尺码为空，");
				result.setPassing(false);
			}
		}
		//校验性别
		if(StringUtils.isNoneBlank(hubProduct.getGender())){
			if(!hubCheckService.checkHubGender(hubProduct.getGender())){
				str.append("性别"+hubProduct.getGender()+"不存在,") ;
				result.setPassing(false);
			}	
		}else{
			str.append("性别为空，");
			result.setPassing(false);
		}
		
		if(StringUtils.isNoneBlank(hubProduct.getMaterial())){
			if(!RegexUtil.excludeLetter(hubProduct.getMaterial())){
				result.setPassing(false);
				str.append("材质中含有英文字符："+hubProduct.getMaterial()) ;
	        }
		}else{
			str.append("材质为空，");
			result.setPassing(false);
		}
		
		if(StringUtils.isNoneBlank(hubProduct.getOrigin())){
			if(!hubCheckService.checkHubOrigin(hubProduct.getOrigin())){
				str.append(",产地"+hubProduct.getOrigin()+"不存在") ;
				result.setPassing(false);
			}	
		}else{
			str.append("产地为空，");
			result.setPassing(false);
		}
		
//		//货号
		BrandModelDto brandModelDto = null;
		BrandModelResult brandModelResult= null;
		if(StringUtils.isNoneBlank(hubProduct.getSpuModel())){
			brandModelDto = new BrandModelDto();
			brandModelDto.setBrandMode(hubProduct.getSpuModel());
			brandModelDto.setHubBrandNo(hubProduct.getBrandNo());
			brandModelDto.setHubCategoryNo(hubProduct.getCategoryNo());
			brandModelResult=  hubBrandModelRule.verify(brandModelDto);
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
