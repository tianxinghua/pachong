package com.shangpin.ephub.client.data.mysql.color.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

	
	@RequestMapping(value = "/hub-color-dic-item/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubColorDicItemCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-color-dic-item/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubColorDicItemCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-color-dic-item/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long colorDicItemId);
	
	@RequestMapping(value = "/hub-color-dic-item/insert", method = RequestMethod.POST,consumes = "application/json")
    public int insert(@RequestBody HubColorDicItemDto hubColorDicItem);
	
	@RequestMapping(value = "/hub-color-dic-item/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public int insertSelective(@RequestBody HubColorDicItemDto hubColorDicItem);
	
	@RequestMapping(value = "/hub-color-dic-item/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubColorDicItemDto> selectByCriteriaWithRowbounds(@RequestBody HubColorDicItemCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-color-dic-item/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubColorDicItemDto> selectByCriteria(@RequestBody HubColorDicItemCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-color-dic-item/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubColorDicItemDto selectByPrimaryKey(Long colorDicItemId);
	
	@RequestMapping(value = "/hub-color-dic-item/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubColorDicItemWithCriteriaDto hubColorDicItemWithCriteria);
	
	@RequestMapping(value = "/hub-color-dic-item/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubColorDicItemWithCriteriaDto hubColorDicItemWithCriteria);
	
	@RequestMapping(value = "/hub-color-dic-item/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubColorDicItemDto hubColorDicItem);
	
	@RequestMapping(value = "/hub-color-dic-item/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubColorDicItemDto hubColorDicItem);
}
