package com.shangpin.ephub.client.data.mysql.spu.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingWithCriteriaDto;
/**
 * <p>Title:HubSpuPendingController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:19:51
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSpuPendingGateWay {

	@RequestMapping(value = "/hub-spu-pending/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubSpuPendingCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-spu-pending/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubSpuPendingCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-spu-pending/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long spuPendingId);
	
	@RequestMapping(value = "/hub-spu-pending/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody HubSpuPendingDto hubSpuPending);
	
	@RequestMapping(value = "/hub-spu-pending/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody HubSpuPendingDto hubSpuPending);
	@RequestMapping(value = "/hub-spu-pending/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSpuPendingDto> selectByCriteriaWithRowbounds(@RequestBody HubSpuPendingCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-spu-pending/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSpuPendingDto> selectByCriteria(@RequestBody HubSpuPendingCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-spu-pending/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubSpuPendingDto selectByPrimaryKey(Long spuPendingId);
	
	@RequestMapping(value = "/hub-spu-pending/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubSpuPendingWithCriteriaDto hubSpuPendingWithCriteria);
	
	@RequestMapping(value = "/hub-spu-pending/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubSpuPendingWithCriteriaDto hubSpuPendingWithCriteria);
	
	@RequestMapping(value = "/hub-spu-pending/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubSpuPendingDto hubSpuPending);
	
	@RequestMapping(value = "/hub-spu-pending/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubSpuPendingDto hubSpuPending);
}
