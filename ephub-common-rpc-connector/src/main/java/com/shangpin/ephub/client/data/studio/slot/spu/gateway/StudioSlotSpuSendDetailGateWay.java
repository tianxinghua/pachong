package com.shangpin.ephub.client.data.studio.slot.spu.gateway;

import java.util.List;

import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailWithCriteriaDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailDto;
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
public interface StudioSlotSpuSendDetailGateWay {

	
	@RequestMapping(value = "/studio-slot-spu-send-detail/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody StudioSlotSpuSendDetailCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-slot-spu-send-detail/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody StudioSlotSpuSendDetailCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-slot-spu-send-detail/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long skuId);
	
	@RequestMapping(value = "/studio-slot-spu-send-detail/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody StudioSlotSpuSendDetailDto hubSku);
	
	@RequestMapping(value = "/studio-slot-spu-send-detail/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody StudioSlotSpuSendDetailDto hubSku);
	
	@RequestMapping(value = "/studio-slot-spu-send-detail/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioSlotSpuSendDetailDto> selectByCriteriaWithRowbounds(@RequestBody StudioSlotSpuSendDetailCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/studio-slot-spu-send-detail/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioSlotSpuSendDetailDto> selectByCriteria(@RequestBody StudioSlotSpuSendDetailCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-slot-spu-send-detail/select-by-primary-key/{skuId}", method = RequestMethod.POST,consumes = "application/json")
    public StudioSlotSpuSendDetailDto selectByPrimaryKey(@PathVariable("skuId") Long skuId);
	
	@RequestMapping(value = "/studio-slot-spu-send-detail/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody StudioSlotSpuSendDetailWithCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/studio-slot-spu-send-detail/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody StudioSlotSpuSendDetailWithCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/studio-slot-spu-send-detail/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody StudioSlotSpuSendDetailDto hubSku);
	
	@RequestMapping(value = "/studio-slot-spu-send-detail/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody StudioSlotSpuSendDetailDto hubSku);

}
