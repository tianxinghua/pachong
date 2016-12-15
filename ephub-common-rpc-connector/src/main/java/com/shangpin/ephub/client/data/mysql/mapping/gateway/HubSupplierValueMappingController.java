package com.shangpin.ephub.client.data.mysql.mapping.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingWithCriteriaDto;
/**
 * <p>Title:HubSupplierValueMappingController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:47:53
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSupplierValueMappingController {

	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSupplierValueMappingCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSupplierValueMappingCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long valueMappingId);
	
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubSupplierValueMappingDto hubSupplierValueMapping);
	
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubSupplierValueMappingDto hubSupplierValueMapping);
	
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSupplierValueMappingDto> selectByCriteriaWithRowbounds(@RequestBody HubSupplierValueMappingCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSupplierValueMappingDto> selectByCriteria(@RequestBody HubSupplierValueMappingCriteriaDto criteria);
	
	@RequestMapping(value = "/select-by-primary-key")
    public HubSupplierValueMappingDto selectByPrimaryKey(Long valueMappingId);
	
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSupplierValueMappingWithCriteriaDto hubSupplierValueMappingWithCriteria);
	
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSupplierValueMappingWithCriteriaDto hubSupplierValueMappingWithCriteria);
	
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSupplierValueMappingDto hubSupplierValueMapping);
	
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSupplierValueMappingDto hubSupplierValueMapping);
}
