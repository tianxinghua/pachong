package com.shangpin.ephub.client.data.mysql.mapping.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

	
	@RequestMapping(value = "/hub-supplier-col-mapping/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubSupplierColMappingCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-supplier-col-mapping/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubSupplierColMappingCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-supplier-col-mapping/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long supplierColMappingId);
	
	@RequestMapping(value = "/hub-supplier-col-mapping/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody HubSupplierColMappingDto hubSupplierColMapping);
	
	@RequestMapping(value = "/hub-supplier-col-mapping/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody HubSupplierColMappingDto hubSupplierColMapping);
	
	@RequestMapping(value = "/hub-supplier-col-mapping/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSupplierColMappingDto> selectByCriteriaWithRowbounds(@RequestBody HubSupplierColMappingCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-supplier-col-mapping/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSupplierColMappingDto> selectByCriteria(@RequestBody HubSupplierColMappingCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-supplier-col-mapping/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubSupplierColMappingDto selectByPrimaryKey(Long supplierColMappingId);
	
	@RequestMapping(value = "/hub-supplier-col-mapping/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubSupplierColMappingWithCriteriaDto hubSupplierColMappingWithCriteria);
	
	@RequestMapping(value = "/hub-supplier-col-mapping/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubSupplierColMappingWithCriteriaDto hubSupplierColMappingWithCriteria);
	
	@RequestMapping(value = "/hub-supplier-col-mapping/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubSupplierColMappingDto hubSupplierColMapping);
	
	@RequestMapping(value = "/hub-supplier-col-mapping/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubSupplierColMappingDto hubSupplierColMapping);
}
