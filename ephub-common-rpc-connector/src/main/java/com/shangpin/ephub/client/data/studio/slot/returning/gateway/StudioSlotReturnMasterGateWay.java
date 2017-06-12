package com.shangpin.ephub.client.data.studio.slot.returning.gateway;

import java.util.List;

import com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnMasterWithCriteriaDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnMasterCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnMasterCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnMasterDto;
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
public interface StudioSlotReturnMasterGateWay {

	
	@RequestMapping(value = "/studio-slot-return-master/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody StudioSlotReturnMasterCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-slot-return-master/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody StudioSlotReturnMasterCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-slot-return-master/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long skuId);
	
	@RequestMapping(value = "/studio-slot-return-master/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody StudioSlotReturnMasterDto hubSku);
	
	@RequestMapping(value = "/studio-slot-return-master/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody StudioSlotReturnMasterDto hubSku);
	
	@RequestMapping(value = "/studio-slot-return-master/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioSlotReturnMasterDto> selectByCriteriaWithRowbounds(@RequestBody StudioSlotReturnMasterCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/studio-slot-return-master/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioSlotReturnMasterDto> selectByCriteria(@RequestBody StudioSlotReturnMasterCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-slot-return-master/select-by-primary-key/{skuId}", method = RequestMethod.POST,consumes = "application/json")
    public StudioSlotReturnMasterDto selectByPrimaryKey(@PathVariable("skuId") Long skuId);
	
	@RequestMapping(value = "/studio-slot-return-master/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody StudioSlotReturnMasterWithCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/studio-slot-return-master/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody StudioSlotReturnMasterWithCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/studio-slot-return-master/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody StudioSlotReturnMasterDto hubSku);
	
	@RequestMapping(value = "/studio-slot-return-master/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody StudioSlotReturnMasterDto hubSku);

}
