package com.shangpin.ephub.client.data.mysql.sku.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

	@RequestMapping(value = "/hub-sku-pending/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubSkuPendingCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-sku-pending/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubSkuPendingCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-sku-pending/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long skuPendingId);
	
	@RequestMapping(value = "/hub-sku-pending/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody HubSkuPendingDto hubSkuPending);
	
	@RequestMapping(value = "/hub-sku-pending/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody HubSkuPendingDto hubSkuPending);
	
	@RequestMapping(value = "/hub-sku-pending/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSkuPendingDto> selectByCriteriaWithRowbounds(@RequestBody HubSkuPendingCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-sku-pending/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSkuPendingDto> selectByCriteria(@RequestBody HubSkuPendingCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-sku-pending/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubSkuPendingDto selectByPrimaryKey(Long skuPendingId);
	
	@RequestMapping(value = "/hub-sku-pending/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubSkuPendingWithCriteriaDto hubSkuPendingWithCriteria);
	
	@RequestMapping(value = "/hub-sku-pending/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubSkuPendingWithCriteriaDto hubSkuPendingWithCriteria);
	
	@RequestMapping(value = "/hub-sku-pending/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubSkuPendingDto hubSkuPending);
	
	@RequestMapping(value = "/hub-sku-pending/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubSkuPendingDto hubSkuPending);
}
