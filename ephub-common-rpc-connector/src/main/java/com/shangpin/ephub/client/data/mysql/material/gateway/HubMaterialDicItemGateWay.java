package com.shangpin.ephub.client.data.mysql.material.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shangpin.ephub.client.data.mysql.material.dto.HubMaterialDicItemCriteriaDto;
import com.shangpin.ephub.client.data.mysql.material.dto.HubMaterialDicItemCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.material.dto.HubMaterialDicItemDto;
import com.shangpin.ephub.client.data.mysql.material.dto.HubMaterialDicItemWithCriteriaDto;

/**
 * 
 * <p>Title:HubMaterialDicItemController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:33:00
 */
@FeignClient("ephub-data-mysql-service")
public interface HubMaterialDicItemGateWay {

	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubMaterialDicItemCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubMaterialDicItemCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long materialDicItemId);
	
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubMaterialDicItemDto hubMaterialDicItem);
	
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubMaterialDicItemDto hubMaterialDicItem);
	
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubMaterialDicItemDto> selectByCriteriaWithRowbounds(@RequestBody HubMaterialDicItemCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/select-by-criteria")
    public List<HubMaterialDicItemDto> selectByCriteria(@RequestBody HubMaterialDicItemCriteriaDto criteria);
	
	@RequestMapping(value = "/select-by-primary-key")
    public HubMaterialDicItemDto selectByPrimaryKey(Long materialDicItemId);
	
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubMaterialDicItemWithCriteriaDto hubMaterialDicItemWithCriteria);
	
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubMaterialDicItemWithCriteriaDto hubMaterialDicItemWithCriteria);
	
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubMaterialDicItemDto hubMaterialDicItem);
	
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubMaterialDicItemDto hubMaterialDicItem);
}
