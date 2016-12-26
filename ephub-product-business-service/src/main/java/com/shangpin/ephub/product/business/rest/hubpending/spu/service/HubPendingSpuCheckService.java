package com.shangpin.ephub.product.business.rest.hubpending.spu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicDto;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicDto;
import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicDto;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.product.business.rest.hubpending.spu.dto.HubPendingSpuDto;
import com.shangpin.ephub.product.business.rest.hubpending.spu.manager.HubPendingSpuCheckManager;
import com.shangpin.ephub.product.business.rest.hubproduct.manager.HubProductCheckManager;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:HubCheckRuleService.java </p>
 * <p>Description: huaPendingSpu校验实现</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月23日 下午4:15:16
 */
@SuppressWarnings("unused")
@Service
@Slf4j
public class HubPendingSpuCheckService {
	
	@Autowired
	private HubPendingSpuCheckManager hubPendingSpuCheckManager;
	
	
	public String checkHubPendingSpu(HubPendingSpuDto hubProduct){
		
		StringBuffer str = new StringBuffer();
		//校验品牌
		if(hubProduct.getBrandNo()!=null){
			if(!checkHubBrand(hubProduct.getBrandNo())){
				str.append("品牌编号有误") ;
			}	
		}
		
		//校验品类
		
		//校验颜色
		if(hubProduct.getHubColor()!=null){
			if(!checkHubColor(hubProduct.getHubColor())){	
				str.append("颜色编号有误") ;
			}
		}
		
		//校验季节
		if(hubProduct.getSeason()!=null){
			if(!checkHubSeason(hubProduct.getSeason(),hubProduct.getMarketTime())){
				str.append("季节编号有误") ;
			}
		}
		//校验尺码
		
		//校验性别
		if(hubProduct.getGender()!=null){
			if(!checkHubGender(hubProduct.getGender())){
				str.append("性别编号有误") ;
			}	
		}
		//校验产地
		return str.toString();
	}
	
	private boolean checkHubBrand(String brandNo){
		HubBrandDicCriteriaDto hubBrandDicCriteriaDto = new HubBrandDicCriteriaDto();
		hubBrandDicCriteriaDto.createCriteria().andHubBrandNoEqualTo(brandNo);
		List<HubBrandDicDto> list = hubPendingSpuCheckManager.findBrandByCriteria(hubBrandDicCriteriaDto);
		if(list!=null&&list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	private boolean checkHubColor(String color){
		HubColorDicCriteriaDto hubColorDicCriteriaDto = new HubColorDicCriteriaDto();
		hubColorDicCriteriaDto.createCriteria().andColorNoEqualTo(color);
		List<HubColorDicDto> list = hubPendingSpuCheckManager.findColorByCriteria(hubColorDicCriteriaDto);
		if(list!=null&&list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	private boolean checkHubSeason(String season,String seasonYear){
		HubSeasonDicCriteriaDto hubSeasonDicCriteriaDto = new HubSeasonDicCriteriaDto();
		hubSeasonDicCriteriaDto.createCriteria().andHubSeasonEqualTo(season).andHubMarketTimeEqualTo(seasonYear);
		List<HubSeasonDicDto> list = hubPendingSpuCheckManager.findSeasonByCriteria(hubSeasonDicCriteriaDto);
		if(list!=null&&list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	private boolean checkHubGender(String gender){
		HubGenderDicCriteriaDto hubGenderDicCriteriaDto = new HubGenderDicCriteriaDto();
		hubGenderDicCriteriaDto.createCriteria().andHubGenderEqualTo(gender);
		List<HubGenderDicDto> list = hubPendingSpuCheckManager.findGenderByCriteria(hubGenderDicCriteriaDto);
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
