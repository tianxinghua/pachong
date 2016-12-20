package com.shangpin.ephub.client.data.mysql.config.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

	
	@RequestMapping(value = "/hub-supplier-config/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubSupplierConfigCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-supplier-config/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubSupplierConfigCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-supplier-config/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long supplierConfigId);
	
	@RequestMapping(value = "/hub-supplier-config/insert", method = RequestMethod.POST,consumes = "application/json")
    public int insert(@RequestBody HubSupplierConfigDto hubSupplierConfig);
	
	@RequestMapping(value = "/hub-supplier-config/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public int insertSelective(@RequestBody HubSupplierConfigDto hubSupplierConfig);
	
	@RequestMapping(value = "/hub-supplier-config/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSupplierConfigDto> selectByCriteriaWithRowbounds(@RequestBody HubSupplierConfigCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-supplier-config/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSupplierConfigDto> selectByCriteria(@RequestBody HubSupplierConfigCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-supplier-config/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubSupplierConfigDto selectByPrimaryKey(Long supplierConfigId);
	
	@RequestMapping(value = "/hub-supplier-config/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubSupplierConfigWithCriteriaDto hubSupplierConfigWithCriteria);
	
	@RequestMapping(value = "/hub-supplier-config/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubSupplierConfigWithCriteriaDto hubSupplierConfigWithCriteria);
	
	@RequestMapping(value = "/hub-supplier-config/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubSupplierConfigDto hubBrandDic);
	
	@RequestMapping(value = "/hub-supplier-config/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubSupplierConfigDto hubBrandDic);
}
