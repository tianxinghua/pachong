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
import com.shangpin.ephub.product.business.common.dto.BrandDom;
import com.shangpin.ephub.product.business.common.dto.BrandRequstDto;
import com.shangpin.ephub.product.business.common.enumeration.GlobalConstant;
import com.shangpin.ephub.product.business.conf.rpc.ApiAddressProperties;
import com.shangpin.ephub.product.business.rest.hubpending.sku.dto.HubResponseDto;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title:BrandService </p>
 * <p>Description: 获取gms的品牌信息 </p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2017年1月11日 下午10:48:37
 *
 */
@Service
@Slf4j
public class BrandService {

	@Autowired
    private RestTemplate restTemplate;
	@Autowired
    private ApiAddressProperties apiAddressProperties;
	@Autowired
    private IShangpinRedis shangpinRedis;
	
	public BrandDom getGmsCateGory(String brandNo){
		try {
			if(StringUtils.isEmpty(brandNo)){
				log.error("通过品类编号查询品类信息时，请传入有效的编号");
				return null;
			}
	        String supplierMsg = getGmsBrandByRedis(brandNo);
	        if(!StringUtils.isEmpty(supplierMsg)){
	        	return JsonUtil.deserialize(supplierMsg, BrandDom.class);
	        }else{
	        	HubResponseDto<BrandDom> body = getGmsBrandByApi(brandNo);
	        	if(null == body || null == body.getResDatas() || body.getResDatas().size() == 0){
	        		return null;
	        	}else{
	        		BrandDom brand = body.getResDatas().get(0);
		        	setBrandIntoReids(brandNo,brand);
		        	return brand;
	        	}
	        }
		} catch (Exception e) {
			log.error("获取品牌时异常："+e.getMessage());
		}
		return null;
	}
	/**
	 * 将品牌缓存到redis
	 * @param brandNo
	 * @param brand
	 */
	public void setBrandIntoReids(String brandNo, BrandDom brand){
		try {
			shangpinRedis.setex(GlobalConstant.REDIS_ORDER_BRAND_KEY+"_"+brandNo,1000*60*5,JsonUtil.serialize(brand));
		} catch (Exception e) {
			log.error("缓存品类到redis时异常："+e.getMessage(),e);
		}
	}
	
	/**
	 * 通过redis获取品牌
	 * @param brandNo
	 * @return
	 */
	public String getGmsBrandByRedis(String brandNo){
		try {
			return shangpinRedis.get(GlobalConstant.REDIS_ORDER_BRAND_KEY+"_"+brandNo);
		} catch (Exception e) {
			log.error("通过redis获取品牌时异常："+e.getMessage(),e); 
			return "";
		}
	}

	/**
	 * 通过api调用获取品牌信息
	 * @param brandNo
	 * @return
	 */
	public HubResponseDto<BrandDom> getGmsBrandByApi(String brandNo){
		BrandRequstDto request = new BrandRequstDto();
        request.setBrandNo(brandNo);
        HttpEntity<BrandRequstDto> requestEntity = new HttpEntity<BrandRequstDto>(request);
        ResponseEntity<HubResponseDto<BrandDom>> entity = restTemplate.exchange(apiAddressProperties.getGmsBrandUrl(), HttpMethod.POST, requestEntity, new ParameterizedTypeReference<HubResponseDto<BrandDom>>() {});
        return entity.getBody();
	}
}
