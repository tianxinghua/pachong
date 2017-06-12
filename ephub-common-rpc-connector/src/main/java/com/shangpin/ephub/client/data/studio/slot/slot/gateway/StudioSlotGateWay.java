package com.shangpin.ephub.client.data.studio.slot.slot.gateway;

import java.util.List;

import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotWithCriteriaDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
/**
 * 
 * <p>Title: StudioMatchSpuGateWay</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月6日 下午12:10:08
 *
 */
@FeignClient("studio-data-mysql-service")
public interface StudioSlotGateWay {

	
	@RequestMapping(value = "/studio-slot/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody StudioSlotCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-slot/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody StudioSlotCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-slot/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long skuId);
	
	@RequestMapping(value = "/studio-slot/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody StudioSlotDto hubSku);
	
	@RequestMapping(value = "/studio-slot/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody StudioSlotDto hubSku);
	
	@RequestMapping(value = "/studio-slot/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioSlotDto> selectByCriteriaWithRowbounds(@RequestBody StudioSlotCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/studio-slot/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioSlotDto> selectByCriteria(@RequestBody StudioSlotCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-slot/select-by-primary-key/{skuId}", method = RequestMethod.POST,consumes = "application/json")
    public StudioSlotDto selectByPrimaryKey(@PathVariable("skuId") Long skuId);
	
	@RequestMapping(value = "/studio-slot/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody StudioSlotWithCriteriaDto withCriteria);
	
	@RequestMapping(value = "/studio-slot/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody StudioSlotWithCriteriaDto withCriteria);
	
	@RequestMapping(value = "/studio-slot/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody StudioSlotDto hubSku);
	
	@RequestMapping(value = "/studio-slot/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody StudioSlotDto hubSku);

}
