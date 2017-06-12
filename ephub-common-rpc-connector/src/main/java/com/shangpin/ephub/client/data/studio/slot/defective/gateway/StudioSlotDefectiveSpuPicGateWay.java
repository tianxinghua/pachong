package com.shangpin.ephub.client.data.studio.slot.defective.gateway;

import java.util.List;

import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuPicWithCriteriaDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuPicCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuPicCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuPicDto;
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
public interface StudioSlotDefectiveSpuPicGateWay {

	
	@RequestMapping(value = "/studio-slot-defective-spu-pic/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody StudioSlotDefectiveSpuPicCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-slot-defective-spu-pic/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody StudioSlotDefectiveSpuPicCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-slot-defective-spu-pic/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long skuId);
	
	@RequestMapping(value = "/studio-slot-defective-spu-pic/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody StudioSlotDefectiveSpuPicDto hubSku);
	
	@RequestMapping(value = "/studio-slot-defective-spu-pic/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody StudioSlotDefectiveSpuPicDto hubSku);
	
	@RequestMapping(value = "/studio-slot-defective-spu-pic/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioSlotDefectiveSpuPicDto> selectByCriteriaWithRowbounds(@RequestBody StudioSlotDefectiveSpuPicCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/studio-slot-defective-spu-pic/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioSlotDefectiveSpuPicDto> selectByCriteria(@RequestBody StudioSlotDefectiveSpuPicCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-slot-defective-spu-pic/select-by-primary-key/{skuId}", method = RequestMethod.POST,consumes = "application/json")
    public StudioSlotDefectiveSpuPicDto selectByPrimaryKey(@PathVariable("skuId") Long skuId);
	
	@RequestMapping(value = "/studio-slot-defective-spu-pic/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody StudioSlotDefectiveSpuPicWithCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/studio-slot-defective-spu-pic/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody StudioSlotDefectiveSpuPicWithCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/studio-slot-defective-spu-pic/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody StudioSlotDefectiveSpuPicDto hubSku);
	
	@RequestMapping(value = "/studio-slot-defective-spu-pic/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody StudioSlotDefectiveSpuPicDto hubSku);

}
