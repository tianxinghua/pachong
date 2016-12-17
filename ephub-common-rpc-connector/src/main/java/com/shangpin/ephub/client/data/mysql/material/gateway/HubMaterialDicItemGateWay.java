package com.shangpin.ephub.client.data.mysql.material.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

	
	@RequestMapping(value = "/hub-material-dic-item/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubMaterialDicItemCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-material-dic-item/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubMaterialDicItemCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-material-dic-item/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long materialDicItemId);
	
	@RequestMapping(value = "/hub-material-dic-item/insert", method = RequestMethod.POST,consumes = "application/json")
    public int insert(@RequestBody HubMaterialDicItemDto hubMaterialDicItem);
	
	@RequestMapping(value = "/hub-material-dic-item/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public int insertSelective(@RequestBody HubMaterialDicItemDto hubMaterialDicItem);
	
	@RequestMapping(value = "/hub-material-dic-item/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubMaterialDicItemDto> selectByCriteriaWithRowbounds(@RequestBody HubMaterialDicItemCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-material-dic-item/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubMaterialDicItemDto> selectByCriteria(@RequestBody HubMaterialDicItemCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-material-dic-item/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubMaterialDicItemDto selectByPrimaryKey(Long materialDicItemId);
	
	@RequestMapping(value = "/hub-material-dic-item/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubMaterialDicItemWithCriteriaDto hubMaterialDicItemWithCriteria);
	
	@RequestMapping(value = "/hub-material-dic-item/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubMaterialDicItemWithCriteriaDto hubMaterialDicItemWithCriteria);
	
	@RequestMapping(value = "/hub-material-dic-item/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubMaterialDicItemDto hubMaterialDicItem);
	
	@RequestMapping(value = "/hub-material-dic-item/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubMaterialDicItemDto hubMaterialDicItem);
}
