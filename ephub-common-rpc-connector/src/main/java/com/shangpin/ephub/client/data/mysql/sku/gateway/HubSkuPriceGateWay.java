package com.shangpin.ephub.client.data.mysql.sku.gateway;



import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierPriceChangeRecordCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierPriceChangeRecordDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierPriceChangeRecordWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierPriceChangeRecordWithRowBoundsDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**

 * <p>Description: 价格变化处理 </p>

 */
@FeignClient("ephub-data-mysql-service")
public interface HubSkuPriceGateWay {

	
	@RequestMapping(value = "/hub-supplier-price/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubSupplierPriceChangeRecordCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-supplier-price/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubSupplierPriceChangeRecordCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-supplier-price/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long supplierPriceChangeRecordId);
	
	@RequestMapping(value = "/hub-supplier-price/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody HubSupplierPriceChangeRecordDto hubSku);
	
	@RequestMapping(value = "/hub-supplier-price/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody HubSupplierPriceChangeRecordDto hubSku);
	
	@RequestMapping(value = "/hub-supplier-price/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSupplierPriceChangeRecordDto> selectByCriteriaWithRowbounds(@RequestBody HubSupplierPriceChangeRecordWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-supplier-price/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSupplierPriceChangeRecordDto> selectByCriteria(@RequestBody HubSupplierPriceChangeRecordCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-supplier-price/select-by-primary-key/{skuId}", method = RequestMethod.POST,consumes = "application/json")
    public HubSupplierPriceChangeRecordDto selectByPrimaryKey(@PathVariable("skuId") Long skuId);
	
	@RequestMapping(value = "/hub-supplier-price/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubSupplierPriceChangeRecordWithCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/hub-supplier-price/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubSupplierPriceChangeRecordWithCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/hub-supplier-price/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubSupplierPriceChangeRecordDto hubSku);
	
	@RequestMapping(value = "/hub-supplier-price/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubSupplierPriceChangeRecordDto hubSku);

}
