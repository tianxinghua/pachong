package com.shangpin.ephub.client.data.mysql.studio.fixedproperty.gateway;




import com.shangpin.ephub.client.data.mysql.studio.fixedproperty.dto.HubSlotSpuFixedPropertyCriteriaDto;
import com.shangpin.ephub.client.data.mysql.studio.fixedproperty.dto.HubSlotSpuFixedPropertyCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.studio.fixedproperty.dto.HubSlotSpuFixedPropertyDto;
import com.shangpin.ephub.client.data.mysql.studio.fixedproperty.dto.HubSlotSpuFixedPropertyWithCriteriaDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
   固定属性
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSlotSpuFixedPropertyGateway {

	@RequestMapping(value = "/studio-slot-fixed-property/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubSlotSpuFixedPropertyCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-slot-fixed-property/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubSlotSpuFixedPropertyCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-slot-fixed-property/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long brandDicId);
	
	@RequestMapping(value = "/studio-slot-fixed-property/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody HubSlotSpuFixedPropertyDto dto);
	
	@RequestMapping(value = "/studio-slot-fixed-property/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody HubSlotSpuFixedPropertyDto dto);
	
	@RequestMapping(value = "/studio-slot-fixed-property/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSlotSpuFixedPropertyDto> selectByCriteriaWithRowbounds(@RequestBody HubSlotSpuFixedPropertyCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/studio-slot-fixed-property/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSlotSpuFixedPropertyDto> selectByCriteria(@RequestBody HubSlotSpuFixedPropertyCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-slot-fixed-property/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubSlotSpuFixedPropertyDto selectByPrimaryKey(Long brandDicId);
	
	@RequestMapping(value = "/studio-slot-fixed-property/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubSlotSpuFixedPropertyWithCriteriaDto withCriteria);
	
	@RequestMapping(value = "/studio-slot-fixed-property/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubSlotSpuFixedPropertyWithCriteriaDto withCriteria);
	
	@RequestMapping(value = "/studio-slot-fixed-property/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubSlotSpuFixedPropertyDto dto);
	
	@RequestMapping(value = "/studio-slot-fixed-property/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubSlotSpuFixedPropertyDto dto);
}
