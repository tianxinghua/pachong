package com.shangpin.ephub.client.data.studio.dic.gateway;

import java.util.List;

import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicSupplierWithCriteriaDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicSupplierCriteriaDto;
import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicSupplierCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicSupplierDto;

/**

 */
@FeignClient("studio-data-mysql-service")
public interface StudioDicSupplierGateWay {

	
	@RequestMapping(value = "/studio-dic-supplier/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody StudioDicSupplierCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-dic-supplier/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody StudioDicSupplierCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-dic-supplier/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long skuId);
	
	@RequestMapping(value = "/studio-dic-supplier/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody StudioDicSupplierDto hubSku);
	
	@RequestMapping(value = "/studio-dic-supplier/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody StudioDicSupplierDto hubSku);
	
	@RequestMapping(value = "/studio-dic-supplier/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioDicSupplierDto> selectByCriteriaWithRowbounds(@RequestBody StudioDicSupplierCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/studio-dic-supplier/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioDicSupplierDto> selectByCriteria(@RequestBody StudioDicSupplierCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-dic-supplier/select-by-primary-key/{skuId}", method = RequestMethod.POST,consumes = "application/json")
    public StudioDicSupplierDto selectByPrimaryKey(@PathVariable("skuId") Long skuId);
	
	@RequestMapping(value = "/studio-dic-supplier/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody StudioDicSupplierWithCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/studio-dic-supplier/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody StudioDicSupplierWithCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/studio-dic-supplier/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody StudioDicSupplierDto hubSku);
	
	@RequestMapping(value = "/studio-dic-supplier/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody StudioDicSupplierDto hubSku);




    //---------------------- 人工处理
    @RequestMapping(value = "/studio-dic-supplier/get-skuno/{spuno}", method = RequestMethod.POST,consumes = "application/json")
    public String createSkuNo(@PathVariable("spuno") String spuno);
}
