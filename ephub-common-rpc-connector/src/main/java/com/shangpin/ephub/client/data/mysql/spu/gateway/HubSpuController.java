package com.shangpin.ephub.client.data.mysql.spu.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuWithCriteriaDto;
/**
 * <p>Title:HubSpuController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:18:04
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSpuController {

	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSpuCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSpuCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long spuId);
	
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubSpuDto hubSpu);
	
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubSpuDto hubSpu);
	
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSpuDto> selectByCriteriaWithRowbounds(@RequestBody HubSpuCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSpuDto> selectByCriteria(@RequestBody HubSpuCriteriaDto criteria);
	
	@RequestMapping(value = "/select-by-primary-key")
    public HubSpuDto selectByPrimaryKey(Long spuId);
	
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSpuWithCriteriaDto hubSpuWithCriteria);
	
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSpuWithCriteriaDto hubSpuWithCriteria);
	
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSpuDto hubSpu);
	
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSpuDto hubSpu);
}
