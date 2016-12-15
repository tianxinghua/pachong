package com.shangpin.ephub.client.data.mysql.season.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicWithCriteriaDto;
/**
 * <p>Title:HubSeasonDicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:37:10
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSeasonDicController {

	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSeasonDicCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSeasonDicCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long seasonDicId);
	
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubSeasonDicDto hubSeasonDic);
	
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubSeasonDicDto hubSeasonDic);
	
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSeasonDicDto> selectByCriteriaWithRowbounds(@RequestBody HubSeasonDicCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSeasonDicDto> selectByCriteria(@RequestBody HubSeasonDicCriteriaDto criteria);
	
	@RequestMapping(value = "/select-by-primary-key")
    public HubSeasonDicDto selectByPrimaryKey(Long seasonDicId);
	
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSeasonDicWithCriteriaDto hubSeasonDicWithCriteria);
	
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSeasonDicWithCriteriaDto hubSeasonDicWithCriteria);
	
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSeasonDicDto hubSeasonDic);
	
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSeasonDicDto hubSeasonDic);
}
