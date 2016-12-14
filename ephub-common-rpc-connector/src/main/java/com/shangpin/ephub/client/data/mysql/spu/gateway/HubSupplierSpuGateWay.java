package com.shangpin.ephub.client.data.mysql.spu.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuWithCriteriaDto;
/**
 * <p>Title:HubSupplierSpuController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:21:45
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSupplierSpuGateWay {

	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSupplierSpuCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSupplierSpuCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long supplierSpuId);
	
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubSupplierSpuDto hubSupplierSpu);
	
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubSupplierSpuDto hubSupplierSpu);
	
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSupplierSpuDto> selectByCriteriaWithRowbounds(@RequestBody HubSupplierSpuCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSupplierSpuDto> selectByCriteria(@RequestBody HubSupplierSpuCriteriaDto criteria);
	
	@RequestMapping(value = "/select-by-primary-key")
    public HubSupplierSpuDto selectByPrimaryKey(Long supplierSpuId);
	
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSupplierSpuWithCriteriaDto hubSupplierSpuWithCriteria);
	
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSupplierSpuWithCriteriaDto hubSupplierSpuWithCriteria);
	
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSupplierSpuDto hubSupplierSpu);
	
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSupplierSpuDto hubSupplierSpu);
}
