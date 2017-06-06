package com.shangpin.ephub.client.data.studio.slot.logistic.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.studio.slot.logistic.dto.StudioSlotLogistictTrackCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.logistic.dto.StudioSlotLogistictTrackCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.studio.slot.logistic.dto.StudioSlotLogistictTrackDto;
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
public interface StudioSlotLogistictTrackGateWay {

	
	@RequestMapping(value = "/studio-slot-logistic-track/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody StudioSlotLogistictTrackCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-slot-logistic-track/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody StudioSlotLogistictTrackCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-slot-logistic-track/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long skuId);
	
	@RequestMapping(value = "/studio-slot-logistic-track/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody StudioSlotLogistictTrackDto hubSku);
	
	@RequestMapping(value = "/studio-slot-logistic-track/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody StudioSlotLogistictTrackDto hubSku);
	
	@RequestMapping(value = "/studio-slot-logistic-track/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioSlotLogistictTrackDto> selectByCriteriaWithRowbounds(@RequestBody StudioSlotLogistictTrackCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/studio-slot-logistic-track/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioSlotLogistictTrackDto> selectByCriteria(@RequestBody StudioSlotLogistictTrackCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-slot-logistic-track/select-by-primary-key/{skuId}", method = RequestMethod.POST,consumes = "application/json")
    public StudioSlotLogistictTrackDto selectByPrimaryKey(@PathVariable("skuId") Long skuId);
	
	@RequestMapping(value = "/studio-slot-logistic-track/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody StudioSlotLogistictTrackCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/studio-slot-logistic-track/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody StudioSlotLogistictTrackCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/studio-slot-logistic-track/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody StudioSlotLogistictTrackDto hubSku);
	
	@RequestMapping(value = "/studio-slot-logistic-track/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody StudioSlotLogistictTrackDto hubSku);

}
