package com.shangpin.ephub.client.data.mysql.sku.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingWithCriteriaDto;

/**
 * <p>Title:HubSkuPendingController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:12:44
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSkuPendingGateWay {

	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSkuPendingCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSkuPendingCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long skuPendingId);
	
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubSkuPendingDto hubSkuPending);
	
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubSkuPendingDto hubSkuPending);
	
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSkuPendingDto> selectByCriteriaWithRowbounds(@RequestBody HubSkuPendingCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSkuPendingDto> selectByCriteria(@RequestBody HubSkuPendingCriteriaDto criteria);
	
	@RequestMapping(value = "/select-by-primary-key")
    public HubSkuPendingDto selectByPrimaryKey(Long skuPendingId);
	
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSkuPendingWithCriteriaDto hubSkuPendingWithCriteria);
	
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSkuPendingWithCriteriaDto hubSkuPendingWithCriteria);
	
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSkuPendingDto hubSkuPending);
	
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSkuPendingDto hubSkuPending);
}
