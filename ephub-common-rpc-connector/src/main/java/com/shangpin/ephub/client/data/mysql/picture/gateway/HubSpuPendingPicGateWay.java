package com.shangpin.ephub.client.data.mysql.picture.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

	
	@RequestMapping(value = "/hub-spu-pending-pic/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubSpuPendingPicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-spu-pending-pic/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubSpuPendingPicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-spu-pending-pic/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long spuPicId);
	
	@RequestMapping(value = "/hub-spu-pending-pic/insert", method = RequestMethod.POST,consumes = "application/json")
    public int insert(@RequestBody HubSpuPendingPicDto hubSpuPendingPic);
	
	@RequestMapping(value = "/hub-spu-pending-pic/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public int insertSelective(@RequestBody HubSpuPendingPicDto hubSpuPendingPic);
	
	@RequestMapping(value = "/hub-spu-pending-pic/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSpuPendingPicDto> selectByCriteriaWithRowbounds(@RequestBody HubSpuPendingPicCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-spu-pending-pic/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSpuPendingPicDto> selectByCriteria(@RequestBody HubSpuPendingPicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-spu-pending-pic/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubSpuPendingPicDto selectByPrimaryKey(Long spuPicId);
	
	@RequestMapping(value = "/hub-spu-pending-pic/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubSpuPendingPicWithCriteriaDto hubSpuPendingPicWithCriteria);
	
	@RequestMapping(value = "/hub-spu-pending-pic/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubSpuPendingPicWithCriteriaDto hubSpuPendingPicWithCriteria);
	
	@RequestMapping(value = "/hub-spu-pending-pic/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubSpuPendingPicDto hubSpuPendingPic);
	
	@RequestMapping(value = "/hub-spu-pending-pic/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubSpuPendingPicDto hubSpuPendingPic);	
}
