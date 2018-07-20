package com.shangpin.ephub.product.business.service.check;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubBrandDicGateway;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicDto;
import com.shangpin.ephub.client.data.mysql.categroy.gateway.HubSupplierCategroyDicGateWay;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicDto;
import com.shangpin.ephub.client.data.mysql.color.gateway.HubColorDicGateWay;
import com.shangpin.ephub.client.data.mysql.enumeration.PicState;
import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicDto;
import com.shangpin.ephub.client.data.mysql.gender.gateway.HubGenderDicGateWay;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSupplierValueMappingGateWay;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.client.data.mysql.season.gateway.HubSeasonDicGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.product.business.hubpending.sku.result.HubPendingSkuCheckResult;
import com.shangpin.ephub.client.product.business.hubpending.spu.result.HubPendingSpuCheckResult;
import com.shangpin.ephub.client.util.RegexUtil;
import com.shangpin.ephub.product.business.conf.rpc.ApiAddressProperties;
import com.shangpin.ephub.product.business.rest.gms.dto.BrandDom;
import com.shangpin.ephub.product.business.rest.gms.dto.CategoryScreenSizeDom;
import com.shangpin.ephub.product.business.rest.gms.dto.FourLevelCategory;
import com.shangpin.ephub.product.business.rest.gms.dto.SizeStandardItem;
import com.shangpin.ephub.product.business.rest.gms.service.BrandService;
import com.shangpin.ephub.product.business.rest.gms.service.CategoryService;
import com.shangpin.ephub.product.business.rest.gms.service.SizeService;
import com.shangpin.ephub.product.business.rest.model.controller.HubBrandModelRuleController;
import com.shangpin.ephub.product.business.rest.model.dto.BrandModelDto;
import com.shangpin.ephub.product.business.rest.model.result.BrandModelResult;

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
    ApiAddressProperties apiAddressProperties;
    @Autowired
    RestTemplate restTemplate;
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
	@Autowired
	HubSupplierValueMappingGateWay hubSupplierValueMappingGateWay;
	@Autowired
	CategoryService categoryService;
	@Autowired
	BrandService brandService;
	@Autowired
	SizeService sizeService;
	
	public boolean checkCategoryNo(String categoryNo) {
		if(categoryNo!=null&&categoryNo.matches("A[0-9]{2}B[0-9]{2}C[0-9]{2}D[0-9]{2}")){
			FourLevelCategory category = categoryService.getGmsCateGory(categoryNo);
	        if(null != category){
	        	return true;
	        }
		}
		return false;
	}
	public boolean checkBrand(String brandNo) {
		BrandDom brand = brandService.getGmsBrand(brandNo);
        if(null != brand){
        	if(1!=brand.getIsValid()){
        		return false;
			}
        	return true;
        }else{
        	return false;
        }
	}
	
	public String checkSpuModel(String spuModel,String hubBrandNo,String hubCategoryNo){
		
		String model = null;
		if(StringUtils.isNotBlank(spuModel)&&StringUtils.isNotBlank(hubBrandNo)&&StringUtils.isNotBlank(hubCategoryNo)){
			BrandModelDto brandModelDto = new BrandModelDto();
			brandModelDto.setBrandMode(spuModel);
			brandModelDto.setHubBrandNo(hubBrandNo);
			brandModelDto.setHubCategoryNo(hubCategoryNo);
			BrandModelResult brandModelResult=  HubBrandModelRuleService.verifyWithCategory(brandModelDto);
			if(brandModelResult.isPassing()){
				model = brandModelResult.getBrandMode();
			}
		}
		return model;
	}
	
	public HubPendingSpuCheckResult checkSpu(HubSpuPendingDto hubProduct){
		
		StringBuffer str = new StringBuffer();
		//品牌
		HubPendingSpuCheckResult result = new HubPendingSpuCheckResult();
		result.setPassing(true);


		//校验品牌
		if(StringUtils.isNotBlank(hubProduct.getHubBrandNo())){
			if(!checkBrand(hubProduct.getHubBrandNo())){
				str.append("品牌编号:"+hubProduct.getHubBrandNo()+"不存在,或已失效") ;
				result.setBrand(false);
				result.setPassing(false);
			}else{
				result.setBrand(true);
			}
		}else{
			str.append("品牌编号为空，");
			result.setPassing(false);
			result.setBrand(false);
		}

		//校验品类
		if(StringUtils.isNotBlank(hubProduct.getHubCategoryNo())){
			if(hubProduct.getHubCategoryNo().matches("A[0-9]{2}B[0-9]{2}C[0-9]{2}D[0-9]{2}")){
				if(checkCategoryNo(hubProduct.getHubCategoryNo())){
					result.setCategory(true);
				}else{
					str.append("品类编号"+hubProduct.getHubCategoryNo()+"不存在,") ;
					result.setPassing(false);
					result.setCategory(false);
				}
			}else{
				str.append("品类编号"+hubProduct.getHubCategoryNo()+"不存在,") ;
				result.setPassing(false);
				result.setCategory(false);
			}

		}else{
			str.append("品类编号为空，");
			result.setPassing(false);
			result.setCategory(false);
		}

		
		String spuModel = checkSpuModel(hubProduct.getSpuModel(),hubProduct.getHubBrandNo(),hubProduct.getHubCategoryNo());
		if(spuModel!=null){
			result.setSpuModel(true);
			result.setModel(spuModel);
		}else{
			str.append("spuModel："+hubProduct.getSpuModel()+"校验失败");
			result.setPassing(false);
			result.setSpuModel(false);
		}
		

		
		//校验颜色
		if(StringUtils.isNoneBlank(hubProduct.getHubColor())){
			if(!checkHubColor(hubProduct.getHubColor())){	
				str.append("颜色编号"+hubProduct.getHubColor()+"不存在,") ;
				result.setPassing(false);
				result.setColor(false);
			}else{
				result.setColor(true);
			}
		}else{
			str.append("颜色为空，");
			result.setPassing(false);
			result.setColor(false);
		}
		
		//校验季节
		if(StringUtils.isNoneBlank(hubProduct.getHubSeason())){
			if(!checkHubSeason(hubProduct.getHubSeason())){
				str.append("季节编号"+hubProduct.getHubSeason()+"不存在,") ;
				result.setPassing(false);
				result.setSeasonName(false);
			}else{
				result.setSeasonName(true);
			}
		}else{
			str.append("季节为空，");
			result.setSeasonName(false);
			result.setPassing(false);
		}
		
		//校验性别
		if(StringUtils.isNoneBlank(hubProduct.getHubGender())){
			if(!checkHubGender(hubProduct.getHubGender())){
				str.append("性别编号"+hubProduct.getHubGender()+"不存在") ;
				result.setPassing(false);
				result.setGender(false);
			}else{
				result.setGender(true);
			}
		}else{
			str.append("性别为空，");
			result.setPassing(false);
			result.setGender(false);
		}
		//校验材质
		if(StringUtils.isNoneBlank(hubProduct.getHubMaterial())){
			String resultMaterial = RegexUtil.specialCategoryMatch(hubProduct.getHubCategoryNo(),hubProduct.getHubMaterial());
			if(resultMaterial!=null){
				result.setPassing(false);
				str.append(resultMaterial+":"+hubProduct.getHubMaterial()) ;
				result.setMaterial(false);
	        }else{
	        	result.setMaterial(true);
	        }
		}else{
			str.append("材质为空，");
			result.setPassing(false);
			result.setMaterial(false);
		}
		//校验产地
		if(StringUtils.isNotBlank(hubProduct.getHubOrigin())){
			if(!checkHubOrigin(hubProduct.getHubOrigin())){
				str.append("产地"+hubProduct.getHubOrigin()+"不存在，") ;
				result.setPassing(false);
				result.setOriginal(false);
			}else{
				result.setOriginal(true);
			}	
		}else{
			str.append("产地为空，");
			result.setPassing(false);
			result.setOriginal(false);
		}
//		//校验图片
//		if(null == hubProduct.getPicState() || PicState.HANDLED.getIndex() != hubProduct.getPicState()){
//			str.append("图片不完整");
//			result.setPassing(false);
//		}
		
		result.setResult(str.toString());
		//校验产地
		return result;
	}

	public boolean checkHubOrigin(String hubOrigin) {
		
		HubSupplierValueMappingCriteriaDto critera = new HubSupplierValueMappingCriteriaDto();
		critera.createCriteria().andHubValTypeEqualTo((byte)3).andHubValEqualTo(hubOrigin.trim());
		List<HubSupplierValueMappingDto> mapp = hubSupplierValueMappingGateWay.selectByCriteria(critera);
		if(mapp!=null&&mapp.size()>0){
			return true;
		}
		return false;
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
	 * 验证尺码是否在标准库中，如果存在返回空字符串，否则返回校验不通过原因
	 * @param hubCategoryNo
	 * @param hubBrandNo
	 * @param size
	 * @return
	 */
	public HubPendingSkuCheckResult hubSizeExist(String hubCategoryNo,String hubBrandNo,String sizeType,String size){
		HubPendingSkuCheckResult checkResult = new HubPendingSkuCheckResult();
		
		String regex = "\\s+";
		if(StringUtils.isNotBlank(size)){
			size = size.replaceAll(regex, "");	
		}
		checkResult.setPassing(false);
		try {
			CategoryScreenSizeDom sizeDom =  sizeService.getGmsSize(hubBrandNo, hubCategoryNo);
			if(sizeDom!=null){
				List<SizeStandardItem> sizeStandardItemList = sizeDom.getSizeStandardItemList();
	            for(SizeStandardItem sizeItem : sizeStandardItemList){
	            	if(sizeItem.getSizeStandardValue()!=null&&sizeItem.getIsScreening()==0){
	            		String [] sizeScmArr = sizeItem.getSizeStandardValue().split("\\|",-1);
	            		for(String sizeScm:sizeScmArr){
	            			 if((sizeItem.getSizeStandardName() + ":" +sizeScm).equals(sizeType+":"+size)){
	 		                	checkResult.setPassing(true);
	 		                	checkResult.setSizeId(String.valueOf(sizeItem.getScreenSizeStandardValueId()));
	 		                	checkResult.setSizeType(sizeItem.getSizeStandardName());
	 		                	checkResult.setSizeValue(sizeItem.getSizeStandardValue());
	 		                	checkResult.setMessage("校验成功");
	 		                	return checkResult;
	 		                }
	            		}
	            	}
	            }
	            checkResult.setMessage(size+"校验不通过");
			}else{
				  checkResult.setMessage(size+"在scm中未查到记录");
			}
			
		} catch (Exception e) {
			log.error("校验尺码是否存在时异常："+e.getMessage(),e);
			checkResult.setMessage("服务器异常");
		}
		return checkResult;		
	}
}
