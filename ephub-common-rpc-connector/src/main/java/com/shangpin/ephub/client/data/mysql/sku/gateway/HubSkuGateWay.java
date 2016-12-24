package com.shangpin.ephub.client.data.mysql.sku.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

	
	@RequestMapping(value = "/hub-sku/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubSkuCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-sku/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubSkuCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-sku/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long skuId);
	
	@RequestMapping(value = "/hub-sku/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody HubSkuDto hubSku);
	
	@RequestMapping(value = "/hub-sku/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody HubSkuDto hubSku);
	
	@RequestMapping(value = "/hub-sku/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSkuDto> selectByCriteriaWithRowbounds(@RequestBody HubSkuCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-sku/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSkuDto> selectByCriteria(@RequestBody HubSkuCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-sku/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubSkuDto selectByPrimaryKey(Long skuId);
	
	@RequestMapping(value = "/hub-sku/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubSkuWithCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/hub-sku/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubSkuWithCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/hub-sku/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubSkuDto hubSku);
	
	@RequestMapping(value = "/hub-sku/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubSkuDto hubSku);
}
