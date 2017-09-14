package com.shangpin.ephub.client.data.mysql.sp.gateway;



import com.shangpin.ephub.client.data.mysql.sp.dto.SpShippingPolicyDetailCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sp.dto.SpShippingPolicyDetailCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.sp.dto.SpShippingPolicyDetailDto;
import com.shangpin.ephub.client.data.mysql.sp.dto.SpShippingPolicyDetailWithCriteriaDto;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 
 */
@FeignClient("ephub-data-mysql-service")
public interface SpShippingPolicyDetailGateWay {

	@RequestMapping(value = "/sp-shipping-policy-detail/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody SpShippingPolicyDetailCriteriaDto criteria);
	
	@RequestMapping(value = "/sp-shipping-policy-detail/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody SpShippingPolicyDetailCriteriaDto criteria);
	
	@RequestMapping(value = "/sp-shipping-policy-detail/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long id);
	
	@RequestMapping(value = "/sp-shipping-policy-detail/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody SpShippingPolicyDetailDto obj);
	
	@RequestMapping(value = "/sp-shipping-policy-detail/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody SpShippingPolicyDetailDto obj);
	
	@RequestMapping(value = "/sp-shipping-policy-detail/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<SpShippingPolicyDetailDto> selectByCriteriaWithRowbounds(@RequestBody SpShippingPolicyDetailCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/sp-shipping-policy-detail/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<SpShippingPolicyDetailDto> selectByCriteria(@RequestBody SpShippingPolicyDetailCriteriaDto criteria);
	
	@RequestMapping(value = "/sp-shipping-policy-detail/select-by-primary-key/{id}", method = RequestMethod.POST,consumes = "application/json")
    public SpShippingPolicyDetailDto selectByPrimaryKey(@PathVariable("id") Long id);
	
	@RequestMapping(value = "/sp-shipping-policy-detail/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody SpShippingPolicyDetailWithCriteriaDto withCriteria);
	
	@RequestMapping(value = "/sp-shipping-policy-detail/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody SpShippingPolicyDetailWithCriteriaDto withCriteria);
	
	@RequestMapping(value = "/sp-shipping-policy-detail/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody SpShippingPolicyDetailDto obj);
	
	@RequestMapping(value = "/sp-shipping-policy-detail/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody SpShippingPolicyDetailDto obj);





}
