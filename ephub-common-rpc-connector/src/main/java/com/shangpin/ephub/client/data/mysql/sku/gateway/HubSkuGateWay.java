package com.shangpin.ephub.client.data.mysql.sku.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuWithCriteriaDto;

/**
 * <p>Title:HubSkuController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:08:37
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSkuGateWay {

	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSkuCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSkuCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long skuId);
	
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubSkuDto hubSku);
	
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubSkuDto hubSku);
	
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSkuDto> selectByCriteriaWithRowbounds(@RequestBody HubSkuCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSkuDto> selectByCriteria(@RequestBody HubSkuCriteriaDto criteria);
	
	@RequestMapping(value = "/select-by-primary-key")
    public HubSkuDto selectByPrimaryKey(Long skuId);
	
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSkuWithCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSkuWithCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSkuDto hubSku);
	
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSkuDto hubSku);
}
