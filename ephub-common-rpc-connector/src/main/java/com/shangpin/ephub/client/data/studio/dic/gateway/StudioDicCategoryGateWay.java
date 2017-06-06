package com.shangpin.ephub.client.data.studio.dic.gateway;

import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicCategoryCriteriaDto;
import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicCategoryCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicSupplierDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**

 */
@FeignClient("studio-data-mysql-service")
public interface StudioDicCategoryGateWay {

	
	@RequestMapping(value = "/studio-dic-category/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody StudioDicCategoryCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-dic-category/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody StudioDicCategoryCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-dic-category/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long skuId);
	
	@RequestMapping(value = "/studio-dic-category/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody StudioDicSupplierDto hubSku);
	
	@RequestMapping(value = "/studio-dic-category/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody StudioDicSupplierDto hubSku);
	
	@RequestMapping(value = "/studio-dic-category/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioDicSupplierDto> selectByCriteriaWithRowbounds(@RequestBody StudioDicCategoryCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/studio-dic-category/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioDicSupplierDto> selectByCriteria(@RequestBody StudioDicCategoryCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-dic-category/select-by-primary-key/{skuId}", method = RequestMethod.POST,consumes = "application/json")
    public StudioDicSupplierDto selectByPrimaryKey(@PathVariable("skuId") Long skuId);
	
	@RequestMapping(value = "/studio-dic-category/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody StudioDicCategoryCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/studio-dic-category/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody StudioDicCategoryCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/studio-dic-category/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody StudioDicSupplierDto hubSku);
	
	@RequestMapping(value = "/studio-dic-category/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody StudioDicSupplierDto hubSku);




    //---------------------- 人工处理
    @RequestMapping(value = "/studio-dic-category/get-skuno/{spuno}", method = RequestMethod.POST,consumes = "application/json")
    public String createSkuNo(@PathVariable("spuno") String spuno);
}
