package com.shangpin.ephub.client.data.mysql.brand.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mysql.brand.dto.HubSupplierBrandDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubSupplierBrandDicCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubSupplierBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubSupplierBrandDicWithCriteriaDto;

/**
 * <p>Title:HubSupplierBrandDicController.java </p>
 * <p>Description: 供应商品牌字典</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月13日 下午3:37:59
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSupplierBrandDicGateWay {

	@RequestMapping(value = "/hub-supplier-brand-dic/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubSupplierBrandDicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-supplier-brand-dic/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubSupplierBrandDicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-supplier-brand-dic/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long supplierBrandDicId);
	
	@RequestMapping(value = "/hub-supplier-brand-dic/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody HubSupplierBrandDicDto hubSupplierBrandDic);
	
	@RequestMapping(value = "/hub-supplier-brand-dic/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody HubSupplierBrandDicDto hubSupplierBrandDic);
	
	@RequestMapping(value = "/hub-supplier-brand-dic/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSupplierBrandDicDto> selectByCriteriaWithRowbounds(@RequestBody HubSupplierBrandDicCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-supplier-brand-dic/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSupplierBrandDicDto> selectByCriteria(@RequestBody HubSupplierBrandDicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-supplier-brand-dic/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubSupplierBrandDicDto selectByPrimaryKey(Long supplierBrandDicId);
	
	@RequestMapping(value = "/hub-supplier-brand-dic/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubSupplierBrandDicWithCriteriaDto hubSupplierBrandDicWithCriteria);
	
	@RequestMapping(value = "/hub-supplier-brand-dic/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubSupplierBrandDicWithCriteriaDto hubSupplierBrandDicWithCriteria);
	
	@RequestMapping(value = "/hub-supplier-brand-dic/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubSupplierBrandDicDto hubSupplierBrandDic);
	
	@RequestMapping(value = "/hub-supplier-brand-dic/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubSupplierBrandDicDto hubSupplierBrandDic);
}
