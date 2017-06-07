package com.shangpin.ephub.client.data.studio.dic.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicCalendarCriteriaDto;
import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicCalendarCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicCalendarDto;
/**
 * <p>Title: StudioDicCalendarGateWay</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月6日 下午2:27:35
 *
 */
@FeignClient("studio-data-mysql-service")
public interface StudioDicCalendarGateWay {

	
	@RequestMapping(value = "/studio-dic-calendar/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody StudioDicCalendarCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-dic-calendar/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody StudioDicCalendarCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-dic-calendar/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long skuId);
	
	@RequestMapping(value = "/studio-dic-calendar/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody StudioDicCalendarDto hubSku);
	
	@RequestMapping(value = "/studio-dic-calendar/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody StudioDicCalendarDto hubSku);
	
	@RequestMapping(value = "/studio-dic-calendar/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioDicCalendarDto> selectByCriteriaWithRowbounds(@RequestBody StudioDicCalendarCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/studio-dic-calendar/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioDicCalendarDto> selectByCriteria(@RequestBody StudioDicCalendarCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-dic-calendar/select-by-primary-key/{skuId}", method = RequestMethod.POST,consumes = "application/json")
    public StudioDicCalendarDto selectByPrimaryKey(@PathVariable("skuId") Long skuId);
	
	@RequestMapping(value = "/studio-dic-calendar/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody StudioDicCalendarCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/studio-dic-calendar/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody StudioDicCalendarCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/studio-dic-calendar/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody StudioDicCalendarDto hubSku);
	
	@RequestMapping(value = "/studio-dic-calendar/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody StudioDicCalendarDto hubSku);

}
