package com.shangpin.ephub.client.data.mysql.categroy.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
public interface HubSupplierCategroyDicController {

	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSupplierCategroyDicCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSupplierCategroyDicCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long supplierCategoryDicId);
	
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubSupplierCategroyDicDto hubSupplierCategroyDic);
	
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubSupplierCategroyDicDto hubSupplierCategroyDic);
	
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSupplierCategroyDicDto> selectByCriteriaWithRowbounds(@RequestBody HubSupplierCategroyDicCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSupplierCategroyDicDto> selectByCriteria(@RequestBody HubSupplierCategroyDicCriteriaDto criteria);
	
	@RequestMapping(value = "/select-by-primary-key")
    public HubSupplierCategroyDicDto selectByPrimaryKey(Long supplierCategoryDicId);
	
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSupplierCategroyDicWithCriteriaDto hubSupplierCategroyDicWithCriteria);
	
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSupplierCategroyDicWithCriteriaDto hubSupplierCategroyDicWithCriteria);
	
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSupplierCategroyDicDto hubSupplierCategroyDic);
	
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSupplierCategroyDicDto hubSupplierCategroyDic);
}
