package com.shangpin.ephub.product.business.common.service.gms;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.common.dto.CategoryRequestDto;
import com.shangpin.ephub.product.business.common.dto.FourLevelCategory;
import com.shangpin.ephub.product.business.common.dto.HubResponseDto;
import com.shangpin.ephub.product.business.common.enumeration.GlobalConstant;
import com.shangpin.ephub.product.business.conf.rpc.ApiAddressProperties;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title:CategoryService </p>
 * <p>Description: 获取gms的品类信息 </p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2017年1月11日 下午10:10:40
 *
 */
@Service
@Slf4j
public class CategoryService {
	
	@Autowired
    private RestTemplate restTemplate;
	@Autowired
    private ApiAddressProperties apiAddressProperties;
	@Autowired
    private IShangpinRedis shangpinRedis;
	
	/**
	 * 通过品类编号获取品类信息
	 * @param categoryNo
	 * @return
	 */
	public FourLevelCategory getGmsCateGory(String categoryNo){
		try {
			if(StringUtils.isEmpty(categoryNo)){
				log.error("通过品类编号查询品类信息时，请传入有效的编号");
				return null;
			}
	        String supplierMsg = getGmsCateGoryByRedis(categoryNo);
	        log.info("从redis获取品类"+supplierMsg);
	        if(!StringUtils.isEmpty(supplierMsg)){
	        	return JsonUtil.deserialize(supplierMsg, FourLevelCategory.class);
	        }else{
	        	HubResponseDto<FourLevelCategory> body = getGmsCateGoryByApi(categoryNo);
	        	if(null == body || null == body.getResDatas() || body.getResDatas().size() == 0){
	        		return null;
	        	}else{
	        		FourLevelCategory category = body.getResDatas().get(0);
		        	setCateGoryIntoReids(categoryNo,category);
		        	return category;
	        	}
	        }
		} catch (Exception e) {
			log.error("获取品类时异常："+e.getMessage(),e);
		}
		return null;
	}
	/**
	 * 将品类缓存到redis
	 * @param categoryNo
	 * @param category
	 */
	public void setCateGoryIntoReids(String categoryNo, FourLevelCategory category){
		try {
			shangpinRedis.setex(GlobalConstant.REDIS_HUB_CATEGORY_KEY+"_"+categoryNo,1000*60*5,JsonUtil.serialize(category));
		} catch (Exception e) {
			log.error("缓存品类到redis时异常："+e.getMessage(),e);
		}
	}
	
	/**
	 * 通过redis获取品类
	 * @param categoryNo
	 * @return
	 */
	public String getGmsCateGoryByRedis(String categoryNo){
		try {
			return shangpinRedis.get(GlobalConstant.REDIS_HUB_CATEGORY_KEY+"_"+categoryNo);
		} catch (Exception e) {
			log.error("通过redis获取品类时异常："+e.getMessage(),e); 
			return "";
		}
	}

	/**
	 * 通过api调用获取品类信息
	 * @param categoryNo
	 * @return
	 */
	public HubResponseDto<FourLevelCategory> getGmsCateGoryByApi(String categoryNo){
		CategoryRequestDto request = new CategoryRequestDto();
        request.setCategoryNo(categoryNo);
        HttpEntity<CategoryRequestDto> requestEntity = new HttpEntity<CategoryRequestDto>(request);
        log.info("从api获取品类请求参数：{}"+apiAddressProperties.getGmsCategoryUrl(),request);
        ResponseEntity<HubResponseDto<FourLevelCategory>> entity = restTemplate.exchange(apiAddressProperties.getGmsCategoryUrl(), HttpMethod.POST, requestEntity, new ParameterizedTypeReference<HubResponseDto<FourLevelCategory>>() {});
        log.info("从api获取品类返回结果：{}",entity.getBody());
        return entity.getBody();
	}
}
