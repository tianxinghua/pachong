package com.shangpin.ephub.client.data.mysql.rule.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
public interface HubSupplierPicRuleController {

	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSupplierPicRuleCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSupplierPicRuleCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long supplierPicRuleId);
	
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubSupplierPicRuleDto hubSupplierPicRule);
	
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubSupplierPicRuleDto hubSupplierPicRule);
	
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSupplierPicRuleDto> selectByCriteriaWithRowbounds(@RequestBody HubSupplierPicRuleCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSupplierPicRuleDto> selectByCriteria(@RequestBody HubSupplierPicRuleCriteriaDto criteria);
	
	@RequestMapping(value = "/select-by-primary-key")
    public HubSupplierPicRuleDto selectByPrimaryKey(Long supplierPicRuleId);
	
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSupplierPicRuleWithCriteriaDto hubSupplierPicRuleWithCriteria);
	
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSupplierPicRuleWithCriteriaDto hubSupplierPicRuleWithCriteria);
	
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSupplierPicRuleDto hubSupplierPicRule);
	
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSupplierPicRuleDto hubSupplierPicRule);
}
