package com.shangpin.ephub.client.data.mysql.spu.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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

	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSpuPendingCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSpuPendingCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long spuPendingId);
	
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubSpuPendingDto hubSpuPending);
	
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubSpuPendingDto hubSpuPending);
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSpuPendingDto> selectByCriteriaWithRowbounds(@RequestBody HubSpuPendingCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSpuPendingDto> selectByCriteria(@RequestBody HubSpuPendingCriteriaDto criteria);
	
	@RequestMapping(value = "/select-by-primary-key")
    public HubSpuPendingDto selectByPrimaryKey(Long spuPendingId);
	
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSpuPendingWithCriteriaDto hubSpuPendingWithCriteria);
	
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSpuPendingWithCriteriaDto hubSpuPendingWithCriteria);
	
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSpuPendingDto hubSpuPending);
	
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSpuPendingDto hubSpuPending);
}
