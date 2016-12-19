package com.shangpin.ephub.client.data.mysql.picture.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

	
	@RequestMapping(value = "/hub-pic/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubPicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-pic/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubPicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-pic/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long picId);
	
	@RequestMapping(value = "/hub-pic/insert", method = RequestMethod.POST,consumes = "application/json")
    public int insert(@RequestBody HubPicDto hubPic);
	
	@RequestMapping(value = "/hub-pic/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public int insertSelective(@RequestBody HubPicDto hubPic);
	
	@RequestMapping(value = "/hub-pic/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubPicDto> selectByCriteriaWithRowbounds(@RequestBody HubPicCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-pic/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubPicDto> selectByCriteria(@RequestBody HubPicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-pic/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubPicDto selectByPrimaryKey(Long picId);
	
	@RequestMapping(value = "/hub-pic/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubPicWithCriteriaDto hubPicWithCriteria);
	
	@RequestMapping(value = "/hub-pic/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubPicWithCriteriaDto hubPicWithCriteria);
	
	@RequestMapping(value = "/hub-pic/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubPicDto hubPic);
	
	@RequestMapping(value = "/hub-pic/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubPicDto hubPic);
}
