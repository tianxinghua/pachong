package com.shangpin.ephub.client.data.mysql.mapping.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingWithCriteriaDto;

/**
 * <p>Title:HubSkuSupplierMappingController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:42:50
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSkuSupplierMappingGateWay {

	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSkuSupplierMappingCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSkuSupplierMappingCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long skuSupplierMappingId);
	
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubSkuSupplierMappingDto hubSkuSupplierMapping);
	
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubSkuSupplierMappingDto hubSkuSupplierMapping);
	
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSkuSupplierMappingDto> selectByCriteriaWithRowbounds(@RequestBody HubSkuSupplierMappingCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSkuSupplierMappingDto> selectByCriteria(@RequestBody HubSkuSupplierMappingCriteriaDto criteria);
	
	@RequestMapping(value = "/select-by-primary-key")
    public HubSkuSupplierMappingDto selectByPrimaryKey(Long skuSupplierMappingId);
	
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSkuSupplierMappingWithCriteriaDto hubSkuSupplierMappingWithCriteria);
	
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSkuSupplierMappingWithCriteriaDto hubSkuSupplierMappingWithCriteria);
	
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSkuSupplierMappingDto hubBrandDic);
	
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSkuSupplierMappingDto hubBrandDic);
}
