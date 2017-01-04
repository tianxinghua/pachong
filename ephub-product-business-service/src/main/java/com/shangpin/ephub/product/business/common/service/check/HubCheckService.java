package com.shangpin.ephub.product.business.common.service.check;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubBrandDicGateway;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicDto;
import com.shangpin.ephub.client.data.mysql.categroy.gateway.HubSupplierCategroyDicGateWay;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicDto;
import com.shangpin.ephub.client.data.mysql.color.gateway.HubColorDicGateWay;
import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicDto;
import com.shangpin.ephub.client.data.mysql.gender.gateway.HubGenderDicGateWay;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.client.data.mysql.season.gateway.HubSeasonDicGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.product.business.rest.model.controller.HubBrandModelRuleController;
import com.shangpin.ephub.product.business.rest.model.dto.BrandModelDto;
import com.shangpin.ephub.product.business.rest.model.result.BrandModelResult;
import com.shangpin.ephub.product.business.rest.model.service.impl.HubBrandModelRuleService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:HubCheckRuleService.java </p>
 * <p>Description: hua商品校验品牌号、品类号、颜色号等</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月23日 下午4:15:16
 */
@SuppressWarnings("unused")
@Service
@Slf4j
public class HubCheckService {
	@Autowired
	HubBrandDicGateway hubBrandDicGateway;
	@Autowired
	HubColorDicGateWay hubColorDicGateway;
	@Autowired
	HubSeasonDicGateWay hubSeasonDicGateWay;
	@Autowired
	HubGenderDicGateWay hubGenderDicGateWay;
	@Autowired
	HubSupplierCategroyDicGateWay hubSupplierCategroyDicGateWay;
	@Autowired
	HubBrandModelRuleController HubBrandModelRuleService;
	public String checkSpu(HubSpuPendingDto hubProduct){
		boolean flag = false;
		StringBuffer str = new StringBuffer();
		//品牌
		if(hubProduct.getHubBrandNo()!=null){
			flag = checkHubBrand(hubProduct.getHubBrandNo());
			if(!flag){
				str.append("brandNo："+hubProduct.getHubBrandNo()+"不存在,");
			}
		}else{
			str.append("brandNo为空");
		}
		
		//货号
		if(hubProduct.getSpuModel()!=null){
			BrandModelDto BrandModelDto = new BrandModelDto();
			BrandModelDto.setBrandMode(hubProduct.getSpuModel());
			BrandModelDto.setHubBrandNo(hubProduct.getHubBrandNo());
			BrandModelDto.setHubCategoryNo(hubProduct.getHubCategoryNo());
			BrandModelResult result= checkSpuModel(BrandModelDto);
			if(!result.isPassing()){
				str.append("spuModel："+hubProduct.getSpuModel()+"校验失败,校验结果："+result.getBrandMode());
			}
		}else{
			str.append("spuModel为空");
		}
		
		//校验品类
		if(hubProduct.getHubCategoryNo()!=null){
			if(!checkHubCategory(hubProduct.getHubCategoryNo())){
				str.append("品类编号:"+hubProduct.getHubCategoryNo()+"不存在,") ;
			}	
		}else{
			str.append("品类为空,");
		}
		
		//颜色
		if(hubProduct.getHubColor()!=null){
			flag = checkHubColor(hubProduct.getHubColor());
			if(!flag){
				str.append("hubColor:"+hubProduct.getHubColor()+"不存在,");
			}
		}else{
			str.append("hubColor为空");
		}
		
		//性别
		if(hubProduct.getHubGender()!=null){
			flag = checkHubGender(hubProduct.getHubGender());
			if(!flag){
				str.append("hubGender:"+hubProduct.getHubGender()+"不存在,");
			}
		}else{
			str.append("hubGender为空,");
		}
		
		//季节
		if(hubProduct.getHubSeason()!=null){
			flag = checkHubSeason(hubProduct.getHubSeason());
			if(!flag){
				str.append("hubSeason:"+hubProduct.getHubSeason()+"不存在,");
			}
		}else{
			str.append("hubSeason为空");
		}
		return str.toString();
	}
	
	private BrandModelResult checkSpuModel(BrandModelDto BrandModelDto) {
		return HubBrandModelRuleService.verify(BrandModelDto);
		
	}

	/**
	 * 校验品牌编号
	 * @param brandNo
	 * @return
	 */
	public boolean checkHubBrand(String brandNo){
		HubBrandDicCriteriaDto hubBrandDicCriteriaDto = new HubBrandDicCriteriaDto();
		hubBrandDicCriteriaDto.createCriteria().andHubBrandNoEqualTo(brandNo);
		List<HubBrandDicDto> list = hubBrandDicGateway.selectByCriteria(hubBrandDicCriteriaDto);
		if(list!=null&&list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 校验颜色
	 * @param color
	 * @return
	 */
	public boolean checkHubColor(String color){
		HubColorDicCriteriaDto hubColorDicCriteriaDto = new HubColorDicCriteriaDto();
		hubColorDicCriteriaDto.createCriteria().andColorNameEqualTo(color);
		List<HubColorDicDto> list = hubColorDicGateway.selectByCriteria(hubColorDicCriteriaDto);
		if(list!=null&&list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 校验季节
	 * @param season 例如：2016_春夏
	 * @return
	 */
	public boolean checkHubSeason(String season){
		
		if(season.split("_").length<2){
			return false;
		}
		HubSeasonDicCriteriaDto hubSeasonDicCriteriaDto = new HubSeasonDicCriteriaDto();
		
		hubSeasonDicCriteriaDto.createCriteria().andHubSeasonEqualTo(season.split("_")[1]).andHubMarketTimeEqualTo(season.split("_")[0]);
		List<HubSeasonDicDto> list = hubSeasonDicGateWay.selectByCriteria(hubSeasonDicCriteriaDto);
		if(list!=null&&list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 校验性别
	 * @param gender
	 * @return
	 */
	public boolean checkHubGender(String gender){
		HubGenderDicCriteriaDto hubGenderDicCriteriaDto = new HubGenderDicCriteriaDto();
		hubGenderDicCriteriaDto.createCriteria().andHubGenderEqualTo(gender);
		List<HubGenderDicDto> list = hubGenderDicGateWay.selectByCriteria(hubGenderDicCriteriaDto);
		if(list!=null&&list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 校验品类
	 * @param categoryNo
	 * @return
	 */
	public boolean checkHubCategory(String categoryNo){
		HubSupplierCategroyDicCriteriaDto criteria = new HubSupplierCategroyDicCriteriaDto();
		criteria.createCriteria().andHubCategoryNoEqualTo(categoryNo);
		List<HubSupplierCategroyDicDto> list = hubSupplierCategroyDicGateWay.selectByCriteria(criteria);
		if(list!=null&&list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 校验尺码
	 * @param hubSkuSize
	 * @return
	 */
	public boolean checkHubSize(String hubSkuSize) {
		return true;
	}
	
}
