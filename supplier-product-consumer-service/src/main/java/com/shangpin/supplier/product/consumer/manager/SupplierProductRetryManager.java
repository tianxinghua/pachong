package com.shangpin.supplier.product.consumer.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSupplierValueMappingGateWay;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.client.data.mysql.season.gateway.HubSeasonDicGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;

/**
 * <p>Title:SupplierProductPictureManager.java </p>
 * <p>Description: 负责外部系统调用的管理器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2017年1月2日 下午12:44:02
 */
@Component
public class SupplierProductRetryManager {
	
	@Autowired
	HubSupplierValueMappingGateWay hubSupplierValueMappingGateWay;
	@Autowired
	private HubSupplierSpuGateWay hubSupplierSpuGateWay;
	@Autowired
	private HubSeasonDicGateWay hubSeasonDicGateWay;
	
	public int getSupplierProductRetryCount(HubSupplierSpuCriteriaDto criteria) {
		return hubSupplierSpuGateWay.countByCriteria(criteria);
	}
	public List<HubSupplierSpuDto> findSupplierProduct(HubSupplierSpuCriteriaDto criteria) {
		return hubSupplierSpuGateWay.selectByCriteria(criteria);
	}
	public HubSupplierValueMappingDto findHubSupplierValueMapping(String supplierId){
	
		HubSupplierValueMappingCriteriaDto hubSupplierValueMappingCriteriaDto = new HubSupplierValueMappingCriteriaDto();
		hubSupplierValueMappingCriteriaDto.createCriteria().andHubValTypeEqualTo((byte)5).andSupplierIdEqualTo(supplierId);
		List<HubSupplierValueMappingDto> supplierList = hubSupplierValueMappingGateWay.selectByCriteria(hubSupplierValueMappingCriteriaDto);
		if(supplierList!=null&&supplierList.size()>0){
			return supplierList.get(0);
		}else{
			return null;	
		}
	}
	public void updateSupplierSpu(HubSupplierSpuDto hubSupplierSpu) {
		hubSupplierSpuGateWay.updateByPrimaryKeySelective(hubSupplierSpu);		
	}

	public void insert(HubSupplierValueMappingDto dto){
		hubSupplierValueMappingGateWay.insert(dto);
	}
	public HubSeasonDicDto findCurrentSeason(String supplierId) {
		
		HubSeasonDicCriteriaDto criteria = new HubSeasonDicCriteriaDto();
		criteria.createCriteria().andSupplieridEqualTo(supplierId).andFilterFlagEqualTo((byte)1);
		List<HubSeasonDicDto> list = hubSeasonDicGateWay.selectByCriteria(criteria);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
}
