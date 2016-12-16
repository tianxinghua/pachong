package com.shangpin.ephub.client.data.mysql.picture.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPicCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPicDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPicWithCriteriaDto;

/**
 * <p>Title:HubSpuPicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:55:19
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSpuPicGateWay {

	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSpuPicCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSpuPicCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long spuPicId);
	
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubSpuPicDto hubSpuPic);
	
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubSpuPicDto hubSpuPic);
	
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSpuPicDto> selectByCriteriaWithRowbounds(@RequestBody HubSpuPicCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSpuPicDto> selectByCriteria(@RequestBody HubSpuPicCriteriaDto criteria);
	
	@RequestMapping(value = "/select-by-primary-key")
    public HubSpuPicDto selectByPrimaryKey(Long spuPicId);
	
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSpuPicWithCriteriaDto hubSpuPicWithCriteria);
	
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSpuPicWithCriteriaDto hubSpuPicWithCriteria);
	
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSpuPicDto hubSpuPic);
	
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSpuPicDto hubSpuPic);
}
