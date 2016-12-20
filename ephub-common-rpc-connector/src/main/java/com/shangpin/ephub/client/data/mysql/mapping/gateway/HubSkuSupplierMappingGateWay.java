package com.shangpin.ephub.client.data.mysql.mapping.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingWithCriteriaDto;

/**
 * <p>Title:HubSkuSupplierMappingController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:42:50
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSkuSupplierMappingGateWay {

	
	@RequestMapping(value = "/hub-sku-supplier-mapping/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubSkuSupplierMappingCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-sku-supplier-mapping/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubSkuSupplierMappingCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-sku-supplier-mapping/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long skuSupplierMappingId);
	
	@RequestMapping(value = "/hub-sku-supplier-mapping/insert", method = RequestMethod.POST,consumes = "application/json")
    public int insert(@RequestBody HubSkuSupplierMappingDto hubSkuSupplierMapping);
	
	@RequestMapping(value = "/hub-sku-supplier-mapping/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public int insertSelective(@RequestBody HubSkuSupplierMappingDto hubSkuSupplierMapping);
	
	@RequestMapping(value = "/hub-sku-supplier-mapping/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSkuSupplierMappingDto> selectByCriteriaWithRowbounds(@RequestBody HubSkuSupplierMappingCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-sku-supplier-mapping/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSkuSupplierMappingDto> selectByCriteria(@RequestBody HubSkuSupplierMappingCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-sku-supplier-mapping/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubSkuSupplierMappingDto selectByPrimaryKey(Long skuSupplierMappingId);
	
	@RequestMapping(value = "/hub-sku-supplier-mapping/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubSkuSupplierMappingWithCriteriaDto hubSkuSupplierMappingWithCriteria);
	
	@RequestMapping(value = "/hub-sku-supplier-mapping/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubSkuSupplierMappingWithCriteriaDto hubSkuSupplierMappingWithCriteria);
	
	@RequestMapping(value = "/hub-sku-supplier-mapping/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubSkuSupplierMappingDto hubBrandDic);
	
	@RequestMapping(value = "/hub-sku-supplier-mapping/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubSkuSupplierMappingDto hubBrandDic);
}
