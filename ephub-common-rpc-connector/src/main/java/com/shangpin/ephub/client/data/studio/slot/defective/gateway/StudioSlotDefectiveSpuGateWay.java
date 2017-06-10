package com.shangpin.ephub.client.data.studio.slot.defective.gateway;

import java.util.List;

import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuWithCriteriaDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuDto;
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
public interface StudioSlotDefectiveSpuGateWay {

	
	@RequestMapping(value = "/studio-slot-defective-spu/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody StudioSlotDefectiveSpuCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-slot-defective-spu/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody StudioSlotDefectiveSpuCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-slot-defective-spu/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long skuId);
	
	@RequestMapping(value = "/studio-slot-defective-spu/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody StudioSlotDefectiveSpuDto hubSku);
	
	@RequestMapping(value = "/studio-slot-defective-spu/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody StudioSlotDefectiveSpuDto hubSku);
	
	@RequestMapping(value = "/studio-slot-defective-spu/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioSlotDefectiveSpuDto> selectByCriteriaWithRowbounds(@RequestBody StudioSlotDefectiveSpuCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/studio-slot-defective-spu/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioSlotDefectiveSpuDto> selectByCriteria(@RequestBody StudioSlotDefectiveSpuCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-slot-defective-spu/select-by-primary-key/{skuId}", method = RequestMethod.POST,consumes = "application/json")
    public StudioSlotDefectiveSpuDto selectByPrimaryKey(@PathVariable("skuId") Long skuId);
	
	@RequestMapping(value = "/studio-slot-defective-spu/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody StudioSlotDefectiveSpuWithCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/studio-slot-defective-spu/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody StudioSlotDefectiveSpuWithCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/studio-slot-defective-spu/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody StudioSlotDefectiveSpuDto hubSku);
	
	@RequestMapping(value = "/studio-slot-defective-spu/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody StudioSlotDefectiveSpuDto hubSku);

}
