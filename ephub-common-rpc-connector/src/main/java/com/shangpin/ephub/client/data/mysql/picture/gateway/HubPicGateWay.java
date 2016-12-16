package com.shangpin.ephub.client.data.mysql.picture.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shangpin.ephub.client.data.mysql.picture.dto.HubPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubPicCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubPicDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubPicWithCriteriaDto;

/**
 * <p>Title:HubPicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:50:53
 */
@FeignClient("ephub-data-mysql-service")
public interface HubPicGateWay {

	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubPicCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubPicCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long picId);
	
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubPicDto hubPic);
	
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubPicDto hubPic);
	
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubPicDto> selectByCriteriaWithRowbounds(@RequestBody HubPicCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/select-by-criteria")
    public List<HubPicDto> selectByCriteria(@RequestBody HubPicCriteriaDto criteria);
	
	@RequestMapping(value = "/select-by-primary-key")
    public HubPicDto selectByPrimaryKey(Long picId);
	
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubPicWithCriteriaDto hubPicWithCriteria);
	
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubPicWithCriteriaDto hubPicWithCriteria);
	
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubPicDto hubPic);
	
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubPicDto hubPic);
}
