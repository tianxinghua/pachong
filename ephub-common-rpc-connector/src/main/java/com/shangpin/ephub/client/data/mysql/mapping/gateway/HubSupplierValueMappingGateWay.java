package com.shangpin.ephub.client.data.mysql.mapping.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
public interface HubSupplierValueMappingGateWay {

	@RequestMapping(value = "/hub-supplier-value-mapping/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubSupplierValueMappingCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-supplier-value-mapping/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubSupplierValueMappingCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-supplier-value-mapping/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long valueMappingId);
	
	@RequestMapping(value = "/hub-supplier-value-mapping/insert", method = RequestMethod.POST,consumes = "application/json")
    public int insert(@RequestBody HubSupplierValueMappingDto hubSupplierValueMapping);
	
	@RequestMapping(value = "/hub-supplier-value-mapping/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public int insertSelective(@RequestBody HubSupplierValueMappingDto hubSupplierValueMapping);
	
	@RequestMapping(value = "/hub-supplier-value-mapping/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSupplierValueMappingDto> selectByCriteriaWithRowbounds(@RequestBody HubSupplierValueMappingCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-supplier-value-mapping/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSupplierValueMappingDto> selectByCriteria(@RequestBody HubSupplierValueMappingCriteriaDto hubSupplierValueMappingCriteria);
	
	@RequestMapping(value = "/hub-supplier-value-mapping/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubSupplierValueMappingDto selectByPrimaryKey(Long valueMappingId);
	
	@RequestMapping(value = "/hub-supplier-value-mapping/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubSupplierValueMappingWithCriteriaDto hubSupplierValueMappingWithCriteria);
	
	@RequestMapping(value = "/hub-supplier-value-mapping/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubSupplierValueMappingWithCriteriaDto hubSupplierValueMappingWithCriteria);
	
	@RequestMapping(value = "/hub-supplier-value-mapping/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubSupplierValueMappingDto hubSupplierValueMapping);
	
	@RequestMapping(value = "/hub-supplier-value-mapping/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubSupplierValueMappingDto hubSupplierValueMapping);
}
