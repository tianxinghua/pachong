package com.shangpin.ephub.client.data.mysql.categroy.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicDto;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicWithCriteriaDto;

/**
 * <p>Title:HubSupplierCategroyDicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:19:55
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSupplierCategroyDicGateWay {

	@RequestMapping(value = "/hub-supplier-categroy-dic/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubSupplierCategroyDicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-supplier-categroy-dic/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubSupplierCategroyDicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-supplier-categroy-dic/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long supplierCategoryDicId);
	
	@RequestMapping(value = "/hub-supplier-categroy-dic/insert", method = RequestMethod.POST,consumes = "application/json")
    public int insert(@RequestBody HubSupplierCategroyDicDto hubSupplierCategroyDic);
	
	@RequestMapping(value = "/hub-supplier-categroy-dic/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public int insertSelective(@RequestBody HubSupplierCategroyDicDto hubSupplierCategroyDic);
	
	@RequestMapping(value = "/hub-supplier-categroy-dic/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSupplierCategroyDicDto> selectByCriteriaWithRowbounds(@RequestBody HubSupplierCategroyDicCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-supplier-categroy-dic/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSupplierCategroyDicDto> selectByCriteria(@RequestBody HubSupplierCategroyDicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-supplier-categroy-dic/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubSupplierCategroyDicDto selectByPrimaryKey(Long supplierCategoryDicId);
	
	@RequestMapping(value = "/hub-supplier-categroy-dic/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubSupplierCategroyDicWithCriteriaDto hubSupplierCategroyDicWithCriteria);
	
	@RequestMapping(value = "/hub-supplier-categroy-dic/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubSupplierCategroyDicWithCriteriaDto hubSupplierCategroyDicWithCriteria);
	
	@RequestMapping(value = "/hub-supplier-categroy-dic/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubSupplierCategroyDicDto hubSupplierCategroyDic);
	
	@RequestMapping(value = "/hub-supplier-categroy-dic/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubSupplierCategroyDicDto hubSupplierCategroyDic);
}
