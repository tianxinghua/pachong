package com.shangpin.ephub.client.data.mysql.picture.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDeletedCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDeletedCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDeletedDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDeletedWithCriteriaDto;
/**
 * <p>Title:HubSpuPendingPicDeletedController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:53:21
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSpuPendingPicDeletedGateWay {

	
	@RequestMapping(value = "/hub-spu-pending-pic-deleted/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubSpuPendingPicDeletedCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-spu-pending-pic-deleted/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubSpuPendingPicDeletedCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-spu-pending-pic-deleted/delete-by-primary-key/{spuPicId}", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(@PathVariable("spuPicId") Long spuPicId);
	
	@RequestMapping(value = "/hub-spu-pending-pic-deleted/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody HubSpuPendingPicDeletedDto hubSpuPendingPicDeleted);
	
	@RequestMapping(value = "/hub-spu-pending-pic-deleted/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody HubSpuPendingPicDeletedDto hubSpuPendingPicDeleted);
	
	@RequestMapping(value = "/hub-spu-pending-pic-deleted/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSpuPendingPicDeletedDto> selectByCriteriaWithRowbounds(@RequestBody HubSpuPendingPicDeletedCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-spu-pending-pic-deleted/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSpuPendingPicDeletedDto> selectByCriteria(@RequestBody HubSpuPendingPicDeletedCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-spu-pending-pic-deleted/select-by-primary-key/{spuPicId}", method = RequestMethod.GET,consumes = "application/json")
    public HubSpuPendingPicDeletedDto selectByPrimaryKey(@PathVariable("spuPicId") Long spuPicId);
	
	@RequestMapping(value = "/hub-spu-pending-pic-deleted/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubSpuPendingPicDeletedWithCriteriaDto hubSpuPendingPicDeletedWithCriteria);
	
	@RequestMapping(value = "/hub-spu-pending-pic-deleted/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubSpuPendingPicDeletedWithCriteriaDto hubSpuPendingPicDeletedWithCriteria);
	
	@RequestMapping(value = "/hub-spu-pending-pic-deleted/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubSpuPendingPicDeletedDto hubSpuPendingPicDeleted);
	
	@RequestMapping(value = "/hub-spu-pending-pic-deleted/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubSpuPendingPicDeletedDto hubSpuPendingPicDeleted);	
}
