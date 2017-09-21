package com.shangpin.ephub.client.data.mysql.sp.gateway;


import com.shangpin.ephub.client.data.mysql.sp.dto.SpShippingPolicyCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sp.dto.SpShippingPolicyCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.sp.dto.SpShippingPolicyDto;
import com.shangpin.ephub.client.data.mysql.sp.dto.SpShippingPolicyWithCriteriaDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**

 */
@FeignClient("ephub-data-mysql-service")
public interface SpShippingPolicyGateWay {

	@RequestMapping(value = "/sp-shipping-policy/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody SpShippingPolicyCriteriaDto criteria);
	
	@RequestMapping(value = "/sp-shipping-policy/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody SpShippingPolicyCriteriaDto criteria);
	
	@RequestMapping(value = "/sp-shipping-policy/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long id);
	
	@RequestMapping(value = "/sp-shipping-policy/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody SpShippingPolicyDto obj);
	
	@RequestMapping(value = "/sp-shipping-policy/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody SpShippingPolicyDto obj);
	
	@RequestMapping(value = "/sp-shipping-policy/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<SpShippingPolicyDto> selectByCriteriaWithRowbounds(@RequestBody SpShippingPolicyCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/sp-shipping-policy/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<SpShippingPolicyDto> selectByCriteria(@RequestBody SpShippingPolicyCriteriaDto criteria);
	
	@RequestMapping(value = "/sp-shipping-policy/select-by-primary-key/{id}", method = RequestMethod.POST,consumes = "application/json")
    public SpShippingPolicyDto selectByPrimaryKey(@PathVariable("id") Long id);
	
	@RequestMapping(value = "/sp-shipping-policy/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody SpShippingPolicyWithCriteriaDto withCriteria);
	
	@RequestMapping(value = "/sp-shipping-policy/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody SpShippingPolicyWithCriteriaDto withCriteria);
	
	@RequestMapping(value = "/sp-shipping-policy/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody SpShippingPolicyDto obj);
	
	@RequestMapping(value = "/sp-shipping-policy/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody SpShippingPolicyDto obj);




   
}
