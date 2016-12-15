package com.shangpin.ephub.client.data.mysql.material.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shangpin.ephub.client.data.mysql.material.dto.HubMaterialDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.material.dto.HubMaterialDicCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.material.dto.HubMaterialDicDto;
import com.shangpin.ephub.client.data.mysql.material.dto.HubMaterialDicWithCriteriaDto;

/**
 * <p>Title:HubMaterialDicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月15日 下午3:02:13
 */
@FeignClient("ephub-data-mysql-service")
public interface HubMaterialDicController {

	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubMaterialDicCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubMaterialDicCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long materialDicId);
	
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubMaterialDicDto hubMaterialDic);
	
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubMaterialDicDto hubMaterialDic);
	
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubMaterialDicDto> selectByCriteriaWithRowbounds(@RequestBody HubMaterialDicCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/select-by-criteria")
    public List<HubMaterialDicDto> selectByCriteria(@RequestBody HubMaterialDicCriteriaDto criteria);
	
	@RequestMapping(value = "/select-by-primary-key")
    public HubMaterialDicDto selectByPrimaryKey(Long materialDicId);
	
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubMaterialDicWithCriteriaDto hubMaterialDicWithCriteria);
	
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubMaterialDicWithCriteriaDto hubMaterialDicWithCriteria);
	
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubMaterialDicDto hubMaterialDic);
	
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubMaterialDicDto hubMaterialDic);
}
