package com.shangpin.ephub.client.data.mysql.sku.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuWithCriteriaDto;

/**
 * <p>Title:HubSupplierSkuController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:15:43
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSupplierSkuGateWay {

	@RequestMapping(value = "/hub-supplier-sku/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubSupplierSkuCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-supplier-sku/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubSupplierSkuCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-supplier-sku/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long supplierSkuId);
	
	@RequestMapping(value = "/hub-supplier-sku/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody HubSupplierSkuDto hubSupplierSku);
	
	@RequestMapping(value = "/hub-supplier-sku/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody HubSupplierSkuDto hubSupplierSku);
	
	@RequestMapping(value = "/hub-supplier-sku/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSupplierSkuDto> selectByCriteriaWithRowbounds(@RequestBody HubSupplierSkuCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-supplier-sku/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSupplierSkuDto> selectByCriteria(@RequestBody HubSupplierSkuCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-supplier-sku/select-by-primary-key/{supplierSkuId}", method = RequestMethod.POST,consumes = "application/json")
    public HubSupplierSkuDto selectByPrimaryKey(@PathVariable("supplierSkuId") Long supplierSkuId);
	
	@RequestMapping(value = "/hub-supplier-sku/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubSupplierSkuWithCriteriaDto hubSupplierSkuWithCriteria);
	
	@RequestMapping(value = "/hub-supplier-sku/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubSupplierSkuWithCriteriaDto hubSupplierSkuWithCriteria);
	
	@RequestMapping(value = "/hub-supplier-sku/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubSupplierSkuDto hubSupplierSku);
	
	@RequestMapping(value = "/hub-supplier-sku/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubSupplierSkuDto hubSupplierSku);
}
