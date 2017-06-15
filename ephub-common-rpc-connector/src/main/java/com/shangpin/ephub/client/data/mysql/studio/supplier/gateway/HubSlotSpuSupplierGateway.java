package com.shangpin.ephub.client.data.mysql.studio.supplier.gateway;

import java.util.List;

import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierWithCriteriaDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierCriteriaDto;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierDto;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierWithCriteriaDto;
/**
 * <p>Title:HubSlotSpuSupplierDtoGateway.java </p>
 * <p>Description: EPHUB品牌字典接口网关</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午1:46:35
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSlotSpuSupplierGateway {

	@RequestMapping(value = "/hub-slot-spu-supplier/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubSlotSpuSupplierCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-slot-spu-supplier/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubSlotSpuSupplierCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-slot-spu-supplier/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long brandDicId);
	
	@RequestMapping(value = "/hub-slot-spu-supplier/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody HubSlotSpuSupplierDto HubSlotSpuSupplierDto);
	
	@RequestMapping(value = "/hub-slot-spu-supplier/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody HubSlotSpuSupplierDto HubSlotSpuSupplierDto);
	
	@RequestMapping(value = "/hub-slot-spu-supplier/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSlotSpuSupplierDto> selectByCriteriaWithRowbounds(@RequestBody HubSlotSpuSupplierCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-slot-spu-supplier/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSlotSpuSupplierDto> selectByCriteria(@RequestBody HubSlotSpuSupplierCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-slot-spu-supplier/select-by-primary-key/{id}", method = RequestMethod.POST,consumes = "application/json")
    public HubSlotSpuSupplierDto selectByPrimaryKey(@PathVariable(value = "id") Long id);
	
	@RequestMapping(value = "/hub-slot-spu-supplier/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubSlotSpuSupplierWithCriteriaDto HubSlotSpuSupplierDtoWithCriteria);
	
	@RequestMapping(value = "/hub-slot-spu-supplier/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubSlotSpuSupplierWithCriteriaDto HubSlotSpuSupplierDtoWithCriteria);
	
	@RequestMapping(value = "/hub-slot-spu-supplier/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubSlotSpuSupplierDto HubSlotSpuSupplierDto);
	
	@RequestMapping(value = "/hub-slot-spu-supplier/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubSlotSpuSupplierDto HubSlotSpuSupplierDto);
}
