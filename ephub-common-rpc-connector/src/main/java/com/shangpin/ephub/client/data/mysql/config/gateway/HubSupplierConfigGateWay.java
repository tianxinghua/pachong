package com.shangpin.ephub.client.data.mysql.config.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shangpin.ephub.client.data.mysql.config.dto.HubSupplierConfigCriteriaDto;
import com.shangpin.ephub.client.data.mysql.config.dto.HubSupplierConfigCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.config.dto.HubSupplierConfigDto;
import com.shangpin.ephub.client.data.mysql.config.dto.HubSupplierConfigWithCriteriaDto;

/**
 * <p>Title:HubSupplierConfigController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:23:34
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSupplierConfigGateWay {

	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSupplierConfigCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSupplierConfigCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long supplierConfigId);
	
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubSupplierConfigDto hubSupplierConfig);
	
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubSupplierConfigDto hubSupplierConfig);
	
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSupplierConfigDto> selectByCriteriaWithRowbounds(@RequestBody HubSupplierConfigCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSupplierConfigDto> selectByCriteria(@RequestBody HubSupplierConfigCriteriaDto criteria);
	
	@RequestMapping(value = "/select-by-primary-key")
    public HubSupplierConfigDto selectByPrimaryKey(Long supplierConfigId);
	
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSupplierConfigWithCriteriaDto hubSupplierConfigWithCriteria);
	
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSupplierConfigWithCriteriaDto hubSupplierConfigWithCriteria);
	
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSupplierConfigDto hubBrandDic);
	
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSupplierConfigDto hubBrandDic);
}
