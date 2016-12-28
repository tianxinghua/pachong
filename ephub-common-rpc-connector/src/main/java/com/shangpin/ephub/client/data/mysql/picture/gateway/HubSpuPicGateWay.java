package com.shangpin.ephub.client.data.mysql.picture.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

	
	@RequestMapping(value = "/hub-spu-pic/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubSpuPicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-spu-pic/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubSpuPicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-spu-pic/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long spuPicId);
	
	@RequestMapping(value = "/hub-spu-pic/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody HubSpuPicDto hubSpuPic);
	
	@RequestMapping(value = "/hub-spu-pic/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody HubSpuPicDto hubSpuPic);
	
	@RequestMapping(value = "/hub-spu-pic/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSpuPicDto> selectByCriteriaWithRowbounds(@RequestBody HubSpuPicCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-spu-pic/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSpuPicDto> selectByCriteria(@RequestBody HubSpuPicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-spu-pic/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubSpuPicDto selectByPrimaryKey(Long spuPicId);
	
	@RequestMapping(value = "/hub-spu-pic/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubSpuPicWithCriteriaDto hubSpuPicWithCriteria);
	
	@RequestMapping(value = "/hub-spu-pic/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubSpuPicWithCriteriaDto hubSpuPicWithCriteria);
	
	@RequestMapping(value = "/hub-spu-pic/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubSpuPicDto hubSpuPic);
	
	@RequestMapping(value = "/hub-spu-pic/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubSpuPicDto hubSpuPic);
}
