package com.shangpin.ephub.client.data.mysql.color.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicDto;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicWithCriteriaDto;

/**
 * <p>Title:HubColorDicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:24:58
 */
@FeignClient("ephub-data-mysql-service")
public interface HubColorDicGateWay {

	
	@RequestMapping(value = "/hub-color-dic/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubColorDicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-color-dic/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubColorDicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-color-dic/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long colorDicId);
	
	@RequestMapping(value = "/hub-color-dic/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody HubColorDicDto hubColorDic);
	
	@RequestMapping(value = "/hub-color-dic/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody HubColorDicDto hubColorDic);
	
	@RequestMapping(value = "/hub-color-dic/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubColorDicDto> selectByCriteriaWithRowbounds(@RequestBody HubColorDicCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-color-dic/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubColorDicDto> selectByCriteria(@RequestBody HubColorDicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-color-dic/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubColorDicDto selectByPrimaryKey(Long colorDicId);
	
	@RequestMapping(value = "/hub-color-dic/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubColorDicWithCriteriaDto hubColorDicWithCriteria);
	
	@RequestMapping(value = "/hub-color-dic/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubColorDicWithCriteriaDto hubColorDicWithCriteria);
	
	@RequestMapping(value = "/hub-color-dic/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubColorDicDto hubColorDic);
	
	@RequestMapping(value = "/hub-color-dic/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubColorDicDto hubColorDic);
}
