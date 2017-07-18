package com.shangpin.ephub.client.data.studio.studio.gateway;

import java.util.List;

import com.shangpin.ephub.client.data.studio.studio.dto.StudioWithCriteriaDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.studio.studio.dto.StudioCriteriaDto;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioDto;
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
public interface StudioGateWay {

	
	@RequestMapping(value = "/studio/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody StudioCriteriaDto criteria);
	
	@RequestMapping(value = "/studio/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody StudioCriteriaDto criteria);
	
	@RequestMapping(value = "/studio/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long skuId);
	
	@RequestMapping(value = "/studio/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody StudioDto hubSku);
	
	@RequestMapping(value = "/studio/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody StudioDto hubSku);
	
	@RequestMapping(value = "/studio/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioDto> selectByCriteriaWithRowbounds(@RequestBody StudioCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/studio/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioDto> selectByCriteria(@RequestBody StudioCriteriaDto criteria);
	
	@RequestMapping(value = "/studio/select-by-primary-key/{studioId}", method = RequestMethod.POST,consumes = "application/json")
    public StudioDto selectByPrimaryKey(@PathVariable("studioId") Long studioId);
	
	@RequestMapping(value = "/studio/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody StudioWithCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/studio/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody StudioWithCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/studio/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody StudioDto hubSku);
	
	@RequestMapping(value = "/studio/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody StudioDto hubSku);

}
