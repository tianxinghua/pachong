package com.shangpin.ephub.product.business.rest.hubproduct.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubBrandDicGateway;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicDto;
import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicDto;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.product.business.rest.hubproduct.dto.HubProductDto;
import com.shangpin.ephub.product.business.rest.hubproduct.manager.HubProductCheckRuleManager;

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
	HubProductCheckRuleManager hubProductCheckRuleManager;
	
	
	public String checkHubProduct(HubProductDto hubProduct){
		
		StringBuffer str = new StringBuffer();
		//校验品牌
		if(!checkHubBrand(hubProduct.getBrandNo())){
			str.append("品牌编号有误") ;
		}
		//校验品类
		
		//校验颜色
		if(!checkHubColor(hubProduct.getColor())){
			str.append("颜色编号有误") ;
		}
		//校验季节
		if(!checkHubSeason(hubProduct.getSeason(),hubProduct.getSeasonYear())){
			str.append("季节编号有误") ;
		}
		//校验尺码
		
		//校验性别
		if(!checkHubGender(hubProduct.getGender())){
			str.append("性别编号有误") ;
		}
		//校验产地
		return str.toString();
	}
	
	private boolean checkHubBrand(String brandNo){
		HubBrandDicCriteriaDto hubBrandDicCriteriaDto = new HubBrandDicCriteriaDto();
		hubBrandDicCriteriaDto.createCriteria().andHubBrandNoEqualTo(brandNo);
		List<HubBrandDicDto> list = hubProductCheckRuleManager.findBrandByCriteria(hubBrandDicCriteriaDto);
		if(list!=null&&list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	private boolean checkHubColor(String color){
		HubColorDicCriteriaDto hubColorDicCriteriaDto = new HubColorDicCriteriaDto();
		hubColorDicCriteriaDto.createCriteria().andColorNoEqualTo(color);
		List<HubColorDicDto> list = hubProductCheckRuleManager.findColorByCriteria(hubColorDicCriteriaDto);
		if(list!=null&&list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	private boolean checkHubSeason(String season,String seasonYear){
		HubSeasonDicCriteriaDto hubSeasonDicCriteriaDto = new HubSeasonDicCriteriaDto();
		hubSeasonDicCriteriaDto.createCriteria().andHubSeasonEqualTo(season).andHubMarketTimeEqualTo(seasonYear);
		List<HubSeasonDicDto> list = hubProductCheckRuleManager.findSeasonByCriteria(hubSeasonDicCriteriaDto);
		if(list!=null&&list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	private boolean checkHubGender(String gender){
		HubGenderDicCriteriaDto hubGenderDicCriteriaDto = new HubGenderDicCriteriaDto();
		hubGenderDicCriteriaDto.createCriteria().andHubGenderEqualTo(gender);
		List<HubGenderDicDto> list = hubProductCheckRuleManager.findGenderByCriteria(hubGenderDicCriteriaDto);
		if(list!=null&&list.size()>0){
			return true;
		}else{
			return false;
		}
	}
//	private boolean checkHubCategory(String categoryNo){
//		HubCat\CriteriaDto hubBrandDicCriteriaDto = new HubBrandDicCriteriaDto();
//		hubBrandDicCriteriaDto.createCriteria().andHubBrandNoEqualTo(categoryNo);
//		List<HubBrandDicDto> list = hubProductCheckRuleManager.findByCriteria(hubBrandDicCriteriaDto);
//		if(list!=null&&list.size()>0){
//			return true;
//		}else{
//			return false;
//		}
//	}

}
