package com.shangpin.ephub.client.data.mysql.color.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicItemCriteriaDto;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicItemCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicItemDto;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicItemWithCriteriaDto;
/**
 * <p>Title:HubColorDicItemController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:28:19
 */
@FeignClient("ephub-data-mysql-service")
public interface HubColorDicItemGateWay {

	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubColorDicItemCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubColorDicItemCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long colorDicItemId);
	
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubColorDicItemDto hubColorDicItem);
	
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubColorDicItemDto hubColorDicItem);
	
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubColorDicItemDto> selectByCriteriaWithRowbounds(@RequestBody HubColorDicItemCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/select-by-criteria")
    public List<HubColorDicItemDto> selectByCriteria(@RequestBody HubColorDicItemCriteriaDto criteria);
	
	@RequestMapping(value = "/select-by-primary-key")
    public HubColorDicItemDto selectByPrimaryKey(Long colorDicItemId);
	
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubColorDicItemWithCriteriaDto hubColorDicItemWithCriteria);
	
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubColorDicItemWithCriteriaDto hubColorDicItemWithCriteria);
	
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubColorDicItemDto hubColorDicItem);
	
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubColorDicItemDto hubColorDicItem);
}
