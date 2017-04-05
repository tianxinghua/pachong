package com.shangpin.ephub.product.business.common.hubDic.gender.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicDto;
import com.shangpin.ephub.client.data.mysql.gender.gateway.HubGenderDicGateWay;
import com.shangpin.ephub.product.business.common.enumeration.DataBusinessStatus;
import com.shangpin.ephub.product.business.common.util.ConstantProperty;

/**
 * Created by loyalty on 16/12/16. 数据层的处理
 */
@Service
public class HubGenderDicService {


	@Autowired
	private HubGenderDicGateWay hubGenderDicGateWay;
	
	public List<HubGenderDicDto> getHubGenderDicBySupplierId(String supplierId) {
		HubGenderDicCriteriaDto criteria = new HubGenderDicCriteriaDto();
		criteria.setPageSize(ConstantProperty.MAX_COMMON_QUERY_NUM);

		HubGenderDicCriteriaDto.Criteria criterion = criteria.createCriteria();
		criterion.andPushStateEqualTo(DataBusinessStatus.PUSH.getIndex().byteValue());
		if (StringUtils.isNotBlank(supplierId)) {
			criterion.andSupplierIdEqualTo(supplierId);

		}
		return hubGenderDicGateWay.selectByCriteria(criteria);
	}

	public void saveHubGender(String supplierId, String supplierGender) throws Exception {
		if (StringUtils.isBlank(supplierGender)) {// 供货商的性别为空时 不做处理
			return;
		}
		// 如果存在 不再保存
		if (null != this.getHubGenderDicBySupplierIdAndSupplierGender(supplierId, supplierGender)) {
			return;
		}

		HubGenderDicDto hubGenderDicDto = new HubGenderDicDto();
		hubGenderDicDto.setCreateTime(new Date());
		hubGenderDicDto.setCreateUser(ConstantProperty.DATA_CREATE_USER);
		hubGenderDicDto.setSupplierId(null);
		hubGenderDicDto.setSupplierGender(supplierGender);
		hubGenderDicDto.setPushState(DataBusinessStatus.NO_PUSH.getIndex().byteValue());
		try {
			hubGenderDicGateWay.insert(hubGenderDicDto);
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof DuplicateKeyException) {

			} else {
				throw e;
			}

		}
	}

	public HubGenderDicDto getHubGenderDicBySupplierIdAndSupplierGender(String supplierId, String supplierGender) {
		if (StringUtils.isBlank(supplierGender)) {
			return null;
		}

		HubGenderDicCriteriaDto criteria = new HubGenderDicCriteriaDto();
		criteria.setPageSize(ConstantProperty.MAX_COMMON_QUERY_NUM);
		HubGenderDicCriteriaDto.Criteria criterion = criteria.createCriteria();
		if (StringUtils.isNotBlank(supplierId)) {
			criterion.andSupplierIdEqualTo(supplierId);
		}

		criterion.andSupplierGenderEqualTo(supplierGender);
		List<HubGenderDicDto> hubGenderDicDtos = hubGenderDicGateWay.selectByCriteria(criteria);
		if (null != hubGenderDicDtos && hubGenderDicDtos.size() > 0) {
			return hubGenderDicDtos.get(0);
		} else {
			return null;
		}
	}
}