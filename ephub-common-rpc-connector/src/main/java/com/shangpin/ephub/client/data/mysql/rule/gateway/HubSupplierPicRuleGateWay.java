package com.shangpin.ephub.client.data.mysql.rule.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mysql.rule.dto.HubSupplierPicRuleCriteriaDto;
import com.shangpin.ephub.client.data.mysql.rule.dto.HubSupplierPicRuleCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.rule.dto.HubSupplierPicRuleDto;
import com.shangpin.ephub.client.data.mysql.rule.dto.HubSupplierPicRuleWithCriteriaDto;

/**
 * <p>Title:HubSupplierPicRuleController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:58:33
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSupplierPicRuleGateWay {

	
	@RequestMapping(value = "/hub-supplier-pic-rule/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubSupplierPicRuleCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-supplier-pic-rule/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubSupplierPicRuleCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-supplier-pic-rule/hub-supplier-pic-rule/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long supplierPicRuleId);
	
	@RequestMapping(value = "/hub-supplier-pic-rule/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody HubSupplierPicRuleDto hubSupplierPicRule);
	
	@RequestMapping(value = "/hub-supplier-pic-rule/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody HubSupplierPicRuleDto hubSupplierPicRule);
	
	@RequestMapping(value = "/hub-supplier-pic-rule/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSupplierPicRuleDto> selectByCriteriaWithRowbounds(@RequestBody HubSupplierPicRuleCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-supplier-pic-rule/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSupplierPicRuleDto> selectByCriteria(@RequestBody HubSupplierPicRuleCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-supplier-pic-rule/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubSupplierPicRuleDto selectByPrimaryKey(Long supplierPicRuleId);
	
	@RequestMapping(value = "/hub-supplier-pic-rule/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubSupplierPicRuleWithCriteriaDto hubSupplierPicRuleWithCriteria);
	
	@RequestMapping(value = "/hub-supplier-pic-rule/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubSupplierPicRuleWithCriteriaDto hubSupplierPicRuleWithCriteria);
	
	@RequestMapping(value = "/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubSupplierPicRuleDto hubSupplierPicRule);
	
	@RequestMapping(value = "/hub-supplier-pic-rule/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubSupplierPicRuleDto hubSupplierPicRule);
}
