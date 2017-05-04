package com.shangpin.ephub.product.business.common.hubDic.brand;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubSupplierBrandDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubSupplierBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubBrandDicGateway;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubSupplierBrandDicGateWay;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicDto;
import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
import com.shangpin.ephub.client.data.mysql.enumeration.FilterFlag;
import com.shangpin.ephub.product.business.common.util.ConstantProperty;

/**
 * Created by loyalty on 16/12/16. 数据层的处理
 */
@Service
public class HubBrandDicService {

	@Autowired
	private HubBrandDicGateway brandDicGateway;
	@Autowired
	HubSupplierBrandDicGateWay supplierBrandDicGateWay;

	public HubSupplierBrandDicDto getHubSupplierBrand(String supplierId, String supplierBrandName) {
		HubSupplierBrandDicCriteriaDto criteria = new HubSupplierBrandDicCriteriaDto();
		criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierBrandEqualTo(supplierBrandName);
		List<HubSupplierBrandDicDto> hubSupplierBrandDicDtos = supplierBrandDicGateWay.selectByCriteria(criteria);
		if (null != hubSupplierBrandDicDtos && hubSupplierBrandDicDtos.size() > 0) {
			return hubSupplierBrandDicDtos.get(0);
		} else {
			return null;
		}
	}

	public void saveBrand(String supplierId, String supplierBrandName) throws Exception {

		if (null != getHubSupplierBrand(supplierId, supplierBrandName)) {// 重复不做处理
			return;
		}
		HubSupplierBrandDicDto supplierBrandDicDto = new HubSupplierBrandDicDto();
		List<HubBrandDicDto> hubBrandList = getHubBrandDic(supplierBrandName);
		if(null != hubBrandList && hubBrandList.size()>0){
			supplierBrandDicDto.setPushState((byte)1);
			supplierBrandDicDto.setMappingState((byte)1);
		}else{
			supplierBrandDicDto.setPushState((byte)0);
			supplierBrandDicDto.setMappingState((byte)0);
		}
		Date date = new Date();
		supplierBrandDicDto.setSupplierId(supplierId);
		supplierBrandDicDto.setSupplierBrand(supplierBrandName);
		supplierBrandDicDto.setCreateUser(ConstantProperty.DATA_CREATE_USER);
		supplierBrandDicDto.setDataState(DataState.NOT_DELETED.getIndex());
		supplierBrandDicDto.setCreateTime(date);
		supplierBrandDicDto.setUpdateTime(date);
		supplierBrandDicDto.setFilterFlag(FilterFlag.EFFECTIVE.getIndex());
		try {
			supplierBrandDicGateWay.insert(supplierBrandDicDto);
		} catch (Exception e) {
			if (e instanceof DuplicateKeyException) {

			} else {
				e.printStackTrace();
				throw e;
			}
		}
	}

	private List<HubBrandDicDto> getHubBrandDic(String supplierBrandName) {
		HubBrandDicCriteriaDto criteria = new HubBrandDicCriteriaDto();
		criteria.createCriteria().andSupplierBrandEqualTo(supplierBrandName);
		return brandDicGateway.selectByCriteria(criteria);
	}

	public List<HubBrandDicDto> getBrand() throws Exception {

		HubBrandDicCriteriaDto criteria = new HubBrandDicCriteriaDto();
		criteria.setPageNo(1);
		criteria.setPageSize(ConstantProperty.MAX_BRANDK_MAPPING_QUERY_NUM);
		return brandDicGateway.selectByCriteria(criteria);

	}

	public int countSupplierBrandBySupplierIdAndType(String supplierId, String supplierBrand) {
		
		HubSupplierBrandDicCriteriaDto criteria = new HubSupplierBrandDicCriteriaDto();
		if(StringUtils.isNotBlank(supplierId)){
			criteria.createCriteria().andSupplierIdEqualTo(supplierId);	
		}
		if(StringUtils.isNotBlank(supplierBrand)){
			criteria.createCriteria().andSupplierBrandEqualTo(supplierBrand);
		}
		return supplierBrandDicGateWay.countByCriteria(criteria);
	}

	public List<HubSupplierBrandDicDto> getSupplierBrandBySupplierIdAndType(String supplierId, String supplierBrand,
			int pageNo, int pageSize) {
		HubSupplierBrandDicCriteriaDto criteria = new HubSupplierBrandDicCriteriaDto();
		criteria.setPageNo(pageNo);
		criteria.setPageSize(pageSize);
		if(StringUtils.isNotBlank(supplierId)){
			criteria.createCriteria().andSupplierIdEqualTo(supplierId);	
		}
		if(StringUtils.isNotBlank(supplierBrand)){
			criteria.createCriteria().andSupplierBrandEqualTo(supplierBrand);
		}
		return supplierBrandDicGateWay.selectByCriteria(criteria);
	}

}
