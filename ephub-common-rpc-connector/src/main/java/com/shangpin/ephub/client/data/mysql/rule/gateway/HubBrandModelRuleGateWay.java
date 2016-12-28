package com.shangpin.ephub.client.data.mysql.rule.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
public interface HubBrandModelRuleGateWay {

	
	@RequestMapping(value = "/hub-brand-model-rule/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubBrandModelRuleCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-brand-model-rule/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubBrandModelRuleCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-brand-model-rule/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long brandModelRuleId);
	
	@RequestMapping(value = "/hub-brand-model-rule/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody HubBrandModelRuleDto hubBrandModelRule);
    
	@RequestMapping(value = "/hub-brand-model-rule/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody HubBrandModelRuleDto hubBrandModelRule);
	
	@RequestMapping(value = "/hub-brand-model-rule/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubBrandModelRuleDto> selectByCriteriaWithRowbounds(@RequestBody HubBrandModelRuleCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-brand-model-rule/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubBrandModelRuleDto> selectByCriteria(@RequestBody HubBrandModelRuleCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-brand-model-rule/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubBrandModelRuleDto selectByPrimaryKey(Long brandModelRuleId);
	
	@RequestMapping(value = "/hub-brand-model-rule/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubBrandModelRuleWithCriteriaDto hubBrandModelRuleWithCriteria);
	
	@RequestMapping(value = "/hub-brand-model-rule/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubBrandModelRuleWithCriteriaDto hubBrandModelRuleWithCriteria);
	
	@RequestMapping(value = "/hub-brand-model-rule/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubBrandModelRuleDto hubBrandModelRule);
	
	@RequestMapping(value = "/hub-brand-model-rule/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubBrandModelRuleDto hubBrandModelRule);
}
