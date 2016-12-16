package com.shangpin.ephub.client.data.mysql.picture.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicWithCriteriaDto;
/**
 * <p>Title:HubSpuPendingPicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:53:21
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSpuPendingPicGateWay {

	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSpuPendingPicCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSpuPendingPicCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long spuPicId);
	
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubSpuPendingPicDto hubSpuPendingPic);
	
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubSpuPendingPicDto hubSpuPendingPic);
	
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSpuPendingPicDto> selectByCriteriaWithRowbounds(@RequestBody HubSpuPendingPicCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSpuPendingPicDto> selectByCriteria(@RequestBody HubSpuPendingPicCriteriaDto criteria);
	
	@RequestMapping(value = "/select-by-primary-key")
    public HubSpuPendingPicDto selectByPrimaryKey(Long spuPicId);
	
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSpuPendingPicWithCriteriaDto hubSpuPendingPicWithCriteria);
	
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSpuPendingPicWithCriteriaDto hubSpuPendingPicWithCriteria);
	
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSpuPendingPicDto hubSpuPendingPic);
	
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSpuPendingPicDto hubSpuPendingPic);	
}
