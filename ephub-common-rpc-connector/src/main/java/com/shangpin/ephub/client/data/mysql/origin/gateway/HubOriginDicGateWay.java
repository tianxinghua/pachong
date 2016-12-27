package com.shangpin.ephub.client.data.mysql.origin.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mysql.origin.dto.HubOriginDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.origin.dto.HubOriginDicCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.origin.dto.HubOriginDicDto;
import com.shangpin.ephub.client.data.mysql.origin.dto.HubOriginDicWithCriteriaDto;

/**
 * <p>Title:HubOriginDicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月22日 下午12:25:04
 */
@FeignClient("ephub-data-mysql-service")
public interface HubOriginDicGateWay {
	
	@RequestMapping(value = "/hub-origin-dic/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubOriginDicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-origin-dic/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubOriginDicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-origin-dic/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long originDicId);
	
	@RequestMapping(value = "/hub-origin-dic/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody HubOriginDicDto hubOriginDicDto);
	
	@RequestMapping(value = "/hub-origin-dic/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody HubOriginDicDto hubOriginDicDto);
	
	@RequestMapping(value = "/hub-origin-dic/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubOriginDicDto> selectByCriteriaWithRowbounds(@RequestBody HubOriginDicCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-origin-dic/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubOriginDicDto> selectByCriteria(@RequestBody HubOriginDicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-origin-dic/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubOriginDicDto selectByPrimaryKey(Long originDicId);
	
	@RequestMapping(value = "/hub-origin-dic/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubOriginDicWithCriteriaDto hubOriginDicWithCriteriaDto);
	
	@RequestMapping(value = "/hub-origin-dic/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubOriginDicWithCriteriaDto hubOriginDicWithCriteriaDto);
	
	@RequestMapping(value = "/hub-origin-dic/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubOriginDicDto hubOriginDicDto);
	
	@RequestMapping(value = "/hub-origin-dic/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubOriginDicDto hubOriginDicDto);
}
