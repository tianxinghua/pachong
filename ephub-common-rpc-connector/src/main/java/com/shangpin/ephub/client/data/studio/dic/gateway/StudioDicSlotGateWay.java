package com.shangpin.ephub.client.data.studio.dic.gateway;

import java.util.List;

import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicSlotWithCriteriaDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicSlotCriteriaDto;
import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicSlotCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicSlotDto;
/**
 * <p>Title: StudioDicCalendarGateWay</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月6日 下午2:27:35
 *
 */
@FeignClient("studio-data-mysql-service")
public interface StudioDicSlotGateWay {

	
	@RequestMapping(value = "/studio-dic-slot/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody StudioDicSlotCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-dic-slot/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody StudioDicSlotCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-dic-slot/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long skuId);
	
	@RequestMapping(value = "/studio-dic-slot/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody StudioDicSlotDto hubSku);
	
	@RequestMapping(value = "/studio-dic-slot/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody StudioDicSlotDto hubSku);
	
	@RequestMapping(value = "/studio-dic-slot/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioDicSlotDto> selectByCriteriaWithRowbounds(@RequestBody StudioDicSlotCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/studio-dic-slot/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioDicSlotDto> selectByCriteria(@RequestBody StudioDicSlotCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-dic-slot/select-by-primary-key/{skuId}", method = RequestMethod.POST,consumes = "application/json")
    public StudioDicSlotDto selectByPrimaryKey(@PathVariable("skuId") Long skuId);
	
	@RequestMapping(value = "/studio-dic-slot/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody StudioDicSlotWithCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/studio-dic-slot/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody StudioDicSlotWithCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/studio-dic-slot/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody StudioDicSlotDto hubSku);
	
	@RequestMapping(value = "/studio-dic-slot/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody StudioDicSlotDto hubSku);

}
