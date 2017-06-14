package com.shangpin.ephub.client.data.mysql.studio.spu.gateway;

import java.util.List;

import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuCriteriaDto;

/**
 
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSlotSpuGateWay {

	@RequestMapping(value = "/hub-slot-spu/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubSlotSpuCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-slot-spu/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubSlotSpuCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-slot-spu/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long spuId);
	
	@RequestMapping(value = "/hub-slot-spu/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody HubSlotSpuDto hubSpu);
	
	@RequestMapping(value = "/hub-slot-spu/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody HubSlotSpuDto hubSpu);
	
	@RequestMapping(value = "/hub-slot-spu/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSlotSpuDto> selectByCriteriaWithRowbounds(@RequestBody HubSpuCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-slot-spu/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSlotSpuDto> selectByCriteria(@RequestBody HubSlotSpuCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-slot-spu/select-by-primary-key/{id}", method = RequestMethod.POST,consumes = "application/json")
    public HubSlotSpuDto selectByPrimaryKey(@PathVariable("id") Long spuId);
	
	@RequestMapping(value = "/hub-slot-spu/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubSpuWithCriteriaDto hubSpuWithCriteria);
	
	@RequestMapping(value = "/hub-slot-spu/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubSpuWithCriteriaDto hubSpuWithCriteria);
	
	@RequestMapping(value = "/hub-slot-spu/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubSlotSpuDto hubSpu);
	
	@RequestMapping(value = "/hub-slot-spu/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubSlotSpuDto hubSpu);




}
