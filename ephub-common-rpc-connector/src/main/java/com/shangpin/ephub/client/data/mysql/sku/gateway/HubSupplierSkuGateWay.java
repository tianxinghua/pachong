package com.shangpin.ephub.client.data.mysql.sku.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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

	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSupplierSkuCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSupplierSkuCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long supplierSkuId);
	
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubSupplierSkuDto hubSupplierSku);
	
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubSupplierSkuDto hubSupplierSku);
	
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSupplierSkuDto> selectByCriteriaWithRowbounds(@RequestBody HubSupplierSkuCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSupplierSkuDto> selectByCriteria(@RequestBody HubSupplierSkuCriteriaDto criteria);
	
	@RequestMapping(value = "/select-by-primary-key")
    public HubSupplierSkuDto selectByPrimaryKey(Long supplierSkuId);
	
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSupplierSkuWithCriteriaDto hubSupplierSkuWithCriteria);
	
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSupplierSkuWithCriteriaDto hubSupplierSkuWithCriteria);
	
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSupplierSkuDto hubSupplierSku);
	
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSupplierSkuDto hubSupplierSku);
}
