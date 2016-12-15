package com.shangpin.ephub.client.data.mysql.rule.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleCriteriaDto;
import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleDto;
import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleWithCriteriaDto;

/**
 * <p>Title:HubBrandModelRuleControlller.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:56:41
 */
@FeignClient("ephub-data-mysql-service")
public interface HubBrandModelRuleControlller {

	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubBrandModelRuleCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubBrandModelRuleCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long brandModelRuleId);
	
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubBrandModelRuleDto hubBrandModelRule);
    
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubBrandModelRuleDto hubBrandModelRule);
	
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubBrandModelRuleDto> selectByCriteriaWithRowbounds(@RequestBody HubBrandModelRuleCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/select-by-criteria")
    public List<HubBrandModelRuleDto> selectByCriteria(@RequestBody HubBrandModelRuleCriteriaDto criteria);
	
	@RequestMapping(value = "/select-by-primary-key")
    public HubBrandModelRuleDto selectByPrimaryKey(Long brandModelRuleId);
	
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubBrandModelRuleWithCriteriaDto hubBrandModelRuleWithCriteria);
	
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubBrandModelRuleWithCriteriaDto hubBrandModelRuleWithCriteria);
	
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubBrandModelRuleDto hubBrandModelRule);
	
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubBrandModelRuleDto hubBrandModelRule);
}
