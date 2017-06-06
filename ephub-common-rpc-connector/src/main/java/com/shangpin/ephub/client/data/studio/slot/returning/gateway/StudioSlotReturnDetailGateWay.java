package com.shangpin.ephub.client.data.studio.slot.returning.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnDetailCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnDetailCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnDetailDto;
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
public interface StudioSlotReturnDetailGateWay {

	
	@RequestMapping(value = "/studio-slot-return-detail/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody StudioSlotReturnDetailCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-slot-return-detail/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody StudioSlotReturnDetailCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-slot-return-detail/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long skuId);
	
	@RequestMapping(value = "/studio-slot-return-detail/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody StudioSlotReturnDetailDto hubSku);
	
	@RequestMapping(value = "/studio-slot-return-detail/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody StudioSlotReturnDetailDto hubSku);
	
	@RequestMapping(value = "/studio-slot-return-detail/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioSlotReturnDetailDto> selectByCriteriaWithRowbounds(@RequestBody StudioSlotReturnDetailCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/studio-slot-return-detail/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioSlotReturnDetailDto> selectByCriteria(@RequestBody StudioSlotReturnDetailCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-slot-return-detail/select-by-primary-key/{skuId}", method = RequestMethod.POST,consumes = "application/json")
    public StudioSlotReturnDetailDto selectByPrimaryKey(@PathVariable("skuId") Long skuId);
	
	@RequestMapping(value = "/studio-slot-return-detail/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody StudioSlotReturnDetailCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/studio-slot-return-detail/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody StudioSlotReturnDetailCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/studio-slot-return-detail/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody StudioSlotReturnDetailDto hubSku);
	
	@RequestMapping(value = "/studio-slot-return-detail/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody StudioSlotReturnDetailDto hubSku);

}
