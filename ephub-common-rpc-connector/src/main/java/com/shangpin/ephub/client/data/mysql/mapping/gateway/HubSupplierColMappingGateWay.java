package com.shangpin.ephub.client.data.mysql.mapping.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierColMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierColMappingCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierColMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierColMappingWithCriteriaDto;
/**
 * <p>Title:HubSupplierColMappingController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:39:37
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSupplierColMappingGateWay {

	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSupplierColMappingCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSupplierColMappingCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long supplierColMappingId);
	
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubSupplierColMappingDto hubSupplierColMapping);
	
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubSupplierColMappingDto hubSupplierColMapping);
	
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSupplierColMappingDto> selectByCriteriaWithRowbounds(@RequestBody HubSupplierColMappingCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSupplierColMappingDto> selectByCriteria(@RequestBody HubSupplierColMappingCriteriaDto criteria);
	
	@RequestMapping(value = "/select-by-primary-key")
    public HubSupplierColMappingDto selectByPrimaryKey(Long supplierColMappingId);
	
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSupplierColMappingWithCriteriaDto hubSupplierColMappingWithCriteria);
	
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSupplierColMappingWithCriteriaDto hubSupplierColMappingWithCriteria);
	
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSupplierColMappingDto hubSupplierColMapping);
	
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSupplierColMappingDto hubSupplierColMapping);
}
