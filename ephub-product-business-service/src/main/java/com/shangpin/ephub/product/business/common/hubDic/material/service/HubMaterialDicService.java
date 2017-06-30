package com.shangpin.ephub.product.business.common.hubDic.material.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.enumeration.ConstantProperty;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubMaterialMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubMaterialMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubMaterialMappingGateWay;
import com.shangpin.ephub.product.business.common.hubDic.material.dto.MaterialDTO;

/**
 * Created by loyalty on 16/12/16. 数据层的处理
 */
@Service
public class HubMaterialDicService {


	@Autowired
	private HubMaterialMappingGateWay hubMaterialMappingGateWay;
	public List<MaterialDTO> getMaterialMapping() {

		HubMaterialMappingCriteriaDto criteria = new HubMaterialMappingCriteriaDto();
		criteria.setOrderByClause("mapping_level");
		criteria.setPageSize(ConstantProperty.MAX_MATERIAL_QUERY_NUM);
		List<HubMaterialMappingDto> hubMaterialMappingDtos = hubMaterialMappingGateWay.selectByCriteria(criteria);
		List<MaterialDTO> materialDTOS = new ArrayList<>();

		for (HubMaterialMappingDto itemDto : hubMaterialMappingDtos) {
			MaterialDTO materialDTO = new MaterialDTO();
			if (StringUtils.isBlank(itemDto.getSupplierMaterial()) || StringUtils.isBlank(itemDto.getHubMaterial()))
				continue;

			materialDTO.setSupplierMaterial(itemDto.getSupplierMaterial());
			materialDTO.setHubMaterial(itemDto.getHubMaterial());

			materialDTOS.add(materialDTO);
		}
		return materialDTOS;

	}
	public int countSupplierMaterialByType(Byte type, String supplierMaterial, String hubMaterial) {
		HubMaterialMappingCriteriaDto hubMaterialMappingCriteriaDto = new HubMaterialMappingCriteriaDto();
		HubMaterialMappingCriteriaDto.Criteria criteria = hubMaterialMappingCriteriaDto.createCriteria();
		criteria.andMappingLevelEqualTo(type);
		
		if(StringUtils.isNotBlank(hubMaterial)){
			criteria.andHubMaterialLike("%"+hubMaterial+"%");
		}
		if(StringUtils.isNotBlank(supplierMaterial)){
			criteria.andSupplierMaterialLike("%"+supplierMaterial+"%");
		}
		
		if(type==0){
			HubMaterialMappingCriteriaDto.Criteria criteriaOr = hubMaterialMappingCriteriaDto.createCriteria();
			if(StringUtils.isNotBlank(hubMaterial)){
				criteriaOr.andHubMaterialLike("%"+hubMaterial+"%");
			}
			if(StringUtils.isNotBlank(supplierMaterial)){
				criteriaOr.andSupplierMaterialLike("%"+supplierMaterial+"%");
			}
			hubMaterialMappingCriteriaDto.or(criteriaOr.andMappingLevelIsNull());
		}
		return hubMaterialMappingGateWay.countByCriteria(hubMaterialMappingCriteriaDto);
	}
	public List<HubMaterialMappingDto> getSupplierMaterialByType(int pageNo, int pageSize, Byte type,
			String supplierMaterial, String hubMaterial) {
		HubMaterialMappingCriteriaDto hubMaterialMappingCriteriaDto = new HubMaterialMappingCriteriaDto();
		HubMaterialMappingCriteriaDto.Criteria criteria = hubMaterialMappingCriteriaDto.createCriteria();
		criteria.andMappingLevelEqualTo(type);
		if(StringUtils.isNotBlank(hubMaterial)){
			criteria.andHubMaterialLike("%"+hubMaterial+"%");
		}
		if(StringUtils.isNotBlank(supplierMaterial)){
			criteria.andSupplierMaterialLike("%"+supplierMaterial+"%");
		}
		if(type==0){
			HubMaterialMappingCriteriaDto.Criteria criteriaOr = hubMaterialMappingCriteriaDto.createCriteria();
			if(StringUtils.isNotBlank(hubMaterial)){
				criteriaOr.andHubMaterialLike("%"+hubMaterial+"%");
			}
			if(StringUtils.isNotBlank(supplierMaterial)){
				criteriaOr.andSupplierMaterialLike("%"+supplierMaterial+"%");
			}
			hubMaterialMappingCriteriaDto.or(criteriaOr.andMappingLevelIsNull());
		}
		hubMaterialMappingCriteriaDto.setPageNo(pageNo);
		hubMaterialMappingCriteriaDto.setPageSize(pageSize);
		hubMaterialMappingCriteriaDto.setOrderByClause("update_time desc");
		return hubMaterialMappingGateWay.selectByCriteria(hubMaterialMappingCriteriaDto);
	}
	public int countHubMaterialDicByHubMaterialId(String materialMappingId) {
		HubMaterialMappingCriteriaDto criteria = new HubMaterialMappingCriteriaDto();
		criteria.createCriteria().andHubMaterialEqualTo(materialMappingId);
		return hubMaterialMappingGateWay.countByCriteria(criteria);
	}
	public HubMaterialMappingDto getSupplierMaterialByHubMaterialId(Long materialMappingId) {
		return hubMaterialMappingGateWay.selectByPrimaryKey(materialMappingId);
	}
	public void saveHubSupplierMaterial(HubMaterialMappingDto dicDto) {
		hubMaterialMappingGateWay.insertSelective(dicDto);
	}
	public List<HubMaterialMappingDto> getHubMaterialDic(String supplierMaterial) {
		HubMaterialMappingCriteriaDto criteria = new HubMaterialMappingCriteriaDto();
		criteria.createCriteria().andSupplierMaterialEqualTo(supplierMaterial);
		return hubMaterialMappingGateWay.selectByCriteria(criteria);
	}
	public void updateSupplierMaterialById(HubMaterialMappingDto dicDto) {
		hubMaterialMappingGateWay.updateByPrimaryKeySelective(dicDto);
	}
	public void deleteHubSupplierMaterialById(Long id) {
		hubMaterialMappingGateWay.deleteByPrimaryKey(id);		
	}
}
