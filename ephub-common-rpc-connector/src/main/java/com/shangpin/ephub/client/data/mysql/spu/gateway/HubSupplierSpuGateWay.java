package com.shangpin.ephub.client.data.mysql.spu.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

	@RequestMapping(value = "/hub-supplier-spu/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubSupplierSpuCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-supplier-spu/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubSupplierSpuCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-supplier-spu/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long supplierSpuId);
	
	@RequestMapping(value = "/hub-supplier-spu/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody HubSupplierSpuDto hubSupplierSpu);
	
	@RequestMapping(value = "/hub-supplier-spu/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody HubSupplierSpuDto hubSupplierSpu);
	
	@RequestMapping(value = "/hub-supplier-spu/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSupplierSpuDto> selectByCriteriaWithRowbounds(@RequestBody HubSupplierSpuCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-supplier-spu/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSupplierSpuDto> selectByCriteria(@RequestBody HubSupplierSpuCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-supplier-spu/select-by-primary-key/{supplierSpuId}", method = RequestMethod.POST,consumes = "application/json")
    public HubSupplierSpuDto selectByPrimaryKey(@PathVariable("supplierSpuId") Long supplierSpuId);
	
	@RequestMapping(value = "/hub-supplier-spu/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubSupplierSpuWithCriteriaDto hubSupplierSpuWithCriteria);
	
	@RequestMapping(value = "/hub-supplier-spu/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubSupplierSpuWithCriteriaDto hubSupplierSpuWithCriteria);
	
	@RequestMapping(value = "/hub-supplier-spu/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubSupplierSpuDto hubSupplierSpu);
	
	@RequestMapping(value = "/hub-supplier-spu/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubSupplierSpuDto hubSupplierSpu);
}
