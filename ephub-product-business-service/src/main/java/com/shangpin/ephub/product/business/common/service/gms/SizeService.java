package com.shangpin.ephub.product.business.common.service.gms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.common.dto.CategoryScreenSizeDom;
import com.shangpin.ephub.product.business.common.dto.HubResponseDto;
import com.shangpin.ephub.product.business.common.dto.SizeRequestDto;
import com.shangpin.ephub.product.business.common.enumeration.GlobalConstant;
import com.shangpin.ephub.product.business.conf.rpc.ApiAddressProperties;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title:SizeService </p>
 * <p>Description: 获取标准尺码</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2017年1月13日 下午5:29:28
 *
 */
@Service
@Slf4j
public class SizeService {
	
	@Autowired
    private RestTemplate restTemplate;
	@Autowired
    private ApiAddressProperties apiAddressProperties;
	@Autowired
    private IShangpinRedis shangpinRedis;

	/**
	 * 根据品牌编号和品类编号获取标准尺码
	 * @param hubBrandNo
	 * @param hubCategoryNo
	 * @return
	 */
	public CategoryScreenSizeDom getGmsSize(String hubBrandNo,String hubCategoryNo){
		try {
			if(StringUtils.isEmpty(hubBrandNo) || StringUtils.isEmpty(hubCategoryNo)){
				log.error("请传入有效的品牌编号和品类编号");
				return null;
			}
			String retMsg = getGmsSizeByRedis(hubBrandNo,hubCategoryNo);
			log.info("检验尺码从redis获取"+retMsg);
			if(!StringUtils.isEmpty(retMsg)){
				return JsonUtil.deserialize(retMsg,CategoryScreenSizeDom.class);
			}else{
				HubResponseDto<CategoryScreenSizeDom> sizes =  getGmsSizeByApi(hubBrandNo,hubCategoryNo);
				if(null == sizes || null == sizes.getResDatas() || sizes.getResDatas().size() == 0 ){
					return null;
				}else{
					CategoryScreenSizeDom sizeDom = sizes.getResDatas().get(0);
					setGmsSizeIntoReids(hubBrandNo, hubCategoryNo, sizeDom); 
					return sizeDom;
				}
			}
		} catch (Exception e) {
			log.error("查询尺码异常："+e.getMessage(),e); 
		}
		return null;
	}
	
	/**
	 * 通过请求api查询该品牌和该品类下的所有标准尺码
	 * @param hubBrandNo hub标准品牌
	 * @param hubCategoryNo hub标准品类
	 * @return
	 */
	public HubResponseDto<CategoryScreenSizeDom> getGmsSizeByApi(String hubBrandNo,String hubCategoryNo){
		SizeRequestDto requestDto = new SizeRequestDto();
        requestDto.setBrandNo(hubBrandNo);
        requestDto.setCategoryNo(hubCategoryNo);
        log.info("检验尺码请求api参数：{},"+apiAddressProperties.getGmsSizeUrl(),requestDto);
        HttpEntity<SizeRequestDto> requestEntity = new HttpEntity<SizeRequestDto>(requestDto);
        ResponseEntity<HubResponseDto<CategoryScreenSizeDom>> entity = restTemplate.exchange(apiAddressProperties.getGmsSizeUrl(), HttpMethod.POST,
                requestEntity, new ParameterizedTypeReference<HubResponseDto<CategoryScreenSizeDom>>() {
                });
        log.info("检验尺码api返回结果：{}",entity.getBody());
        return entity.getBody();
	}
	
	/**
	 * 通过reids取该品牌和该品类下的所有标准尺码
	 * @param hubBrandNo
	 * @param hubCategoryNo
	 * @return
	 */
	public String getGmsSizeByRedis(String hubBrandNo,String hubCategoryNo){
		try {
			return shangpinRedis.get(GlobalConstant.REDIS_HUB_SIZE_KEY+"_"+hubCategoryNo+"_"+hubBrandNo);
		} catch (Exception e) {
			log.error("通过redis获取尺码时异常："+e.getMessage(),e); 
			return "";
		}
	}
	/**
	 * 缓存到redis
	 * @param hubBrandNo
	 * @param hubCategoryNo
	 * @param categoryScreenSizeDom
	 */
	public void setGmsSizeIntoReids(String hubBrandNo,String hubCategoryNo,CategoryScreenSizeDom categoryScreenSizeDom){
		try {
			log.info("尺码缓存到redis:{}",categoryScreenSizeDom);
			shangpinRedis.setex(GlobalConstant.REDIS_HUB_SIZE_KEY+"_"+hubCategoryNo+"_"+hubBrandNo,1000*60*5,JsonUtil.serialize(categoryScreenSizeDom));
		} catch (Exception e) {
			log.error("缓存尺码到redis时异常："+e.getMessage(),e);
		}
	}
	
	
}
