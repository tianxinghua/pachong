package com.shangpin.ephub.client.data.mysql.mapping.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mysql.mapping.dto.HubMaterialMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubMaterialMappingCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubMaterialMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubMaterialMappingWithCriteriaDto;

/**
 * <p>Title:HubMaterialMappingController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月22日 上午11:52:02
 */
@FeignClient("ephub-data-mysql-service")
public interface HubMaterialMappingGateWay {

	@RequestMapping(value = "/hub-material-mapping/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubMaterialMappingCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-material-mapping/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubMaterialMappingCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-material-mapping/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long materialMappingId);
	
	@RequestMapping(value = "/hub-material-mapping/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody HubMaterialMappingDto hubMaterialMappingDto);
	
	@RequestMapping(value = "/hub-material-mapping/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody HubMaterialMappingDto hubMaterialMappingDto);
	
	@RequestMapping(value = "/hub-material-mapping/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubMaterialMappingDto> selectByCriteriaWithRowbounds(@RequestBody HubMaterialMappingCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-material-mapping/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubMaterialMappingDto> selectByCriteria(@RequestBody HubMaterialMappingCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-material-mapping/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubMaterialMappingDto selectByPrimaryKey(Long materialMappingId);
	
	@RequestMapping(value = "/hub-material-mapping/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubMaterialMappingWithCriteriaDto hubMaterialMappingWithCriteriaDto);
	
	@RequestMapping(value = "/hub-material-mapping/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubMaterialMappingWithCriteriaDto hubMaterialMappingWithCriteriaDto);
	
	@RequestMapping(value = "/hub-material-mapping/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubMaterialMappingDto hubMaterialMappingDto);
	
	@RequestMapping(value = "/hub-material-mapping/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubMaterialMappingDto hubMaterialMappingDto);
}
