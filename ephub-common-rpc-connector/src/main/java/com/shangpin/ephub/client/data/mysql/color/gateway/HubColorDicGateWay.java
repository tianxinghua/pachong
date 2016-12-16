package com.shangpin.ephub.client.data.mysql.color.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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

	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubColorDicCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubColorDicCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long colorDicId);
	
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubColorDicDto hubColorDic);
	
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubColorDicDto hubColorDic);
	
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubColorDicDto> selectByCriteriaWithRowbounds(@RequestBody HubColorDicCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/select-by-criteria")
    public List<HubColorDicDto> selectByCriteria(@RequestBody HubColorDicCriteriaDto criteria);
	
	@RequestMapping(value = "/select-by-primary-key")
    public HubColorDicDto selectByPrimaryKey(Long colorDicId);
	
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubColorDicWithCriteriaDto hubColorDicWithCriteria);
	
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubColorDicWithCriteriaDto hubColorDicWithCriteria);
	
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubColorDicDto hubColorDic);
	
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubColorDicDto hubColorDic);
}
