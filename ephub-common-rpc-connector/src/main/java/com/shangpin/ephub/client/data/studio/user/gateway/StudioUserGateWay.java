package com.shangpin.ephub.client.data.studio.user.gateway;

import com.shangpin.ephub.client.data.studio.user.dto.StudioUserCriteriaDto;
import com.shangpin.ephub.client.data.studio.user.dto.StudioUserCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.studio.user.dto.StudioUserDto;
import com.shangpin.ephub.client.data.studio.user.dto.StudioUserWithCriteriaDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

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
public interface StudioUserGateWay {

	
	@RequestMapping(value = "/studio-user/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody StudioUserCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-user/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody StudioUserCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-user/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long skuId);
	
	@RequestMapping(value = "/studio-user/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody StudioUserDto hubSku);
	
	@RequestMapping(value = "/studio-user/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody StudioUserDto hubSku);
	
	@RequestMapping(value = "/studio-user/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioUserDto> selectByCriteriaWithRowbounds(@RequestBody StudioUserCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/studio-user/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioUserDto> selectByCriteria(@RequestBody StudioUserCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-user/select-by-primary-key/{studioUserId}", method = RequestMethod.POST,consumes = "application/json")
    public StudioUserDto selectByPrimaryKey(@PathVariable("studioUserId") Long studioUserId);
	
	@RequestMapping(value = "/studio-user/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody StudioUserWithCriteriaDto withCriteriaDto);
	
	@RequestMapping(value = "/studio-user/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody StudioUserWithCriteriaDto withCriteriaDto);
	
	@RequestMapping(value = "/studio-user/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody StudioUserDto hubSku);
	
	@RequestMapping(value = "/studio-user/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody StudioUserDto hubSku);

}
