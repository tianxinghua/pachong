package com.shangpin.ephub.product.business.common.hubDic.season;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.enumeration.FilterFlag;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.client.data.mysql.season.gateway.HubSeasonDicGateWay;
import com.shangpin.ephub.product.business.common.enumeration.DataBusinessStatus;
import com.shangpin.ephub.product.business.common.util.ConstantProperty;

/**
 * Created by loyalty on 16/12/16. 数据层的处理
 */
@Service
public class HubSeasonDicService {

	@Autowired
	HubSeasonDicGateWay hubSeasonDicGateWay;

	/**
	 * 查询所有有效的供应商季节
	 * 
	 * @return
	 */
	public List<HubSeasonDicDto> getEffectiveHubSeasons() {
		HubSeasonDicCriteriaDto criteria = new HubSeasonDicCriteriaDto();
		criteria.createCriteria().andFilterFlagEqualTo(FilterFlag.EFFECTIVE.getIndex());
		criteria.setFields("supplierid,supplier_season,filter_flag");
		return hubSeasonDicGateWay.selectByCriteria(criteria);
	}

	public List<HubSeasonDicDto> getHubSeasonDic() {
		HubSeasonDicCriteriaDto criteria = new HubSeasonDicCriteriaDto();
		criteria.setPageSize(ConstantProperty.MAX_COMMON_QUERY_NUM);
		@SuppressWarnings("unused")
		HubSeasonDicCriteriaDto.Criteria criterion = criteria.createCriteria();
		return hubSeasonDicGateWay.selectByCriteria(criteria);

	}

	public HubSeasonDicDto getHubSeasonDicBySupplierIdAndsupplierSeason(String supplierId, String supplierSeason) {
		HubSeasonDicCriteriaDto criteria = new HubSeasonDicCriteriaDto();
		criteria.createCriteria().andSupplieridEqualTo(supplierId).andSupplierSeasonEqualTo(supplierSeason);
		List<HubSeasonDicDto> hubSeasonDicDtos = hubSeasonDicGateWay.selectByCriteria(criteria);
		if (null != hubSeasonDicDtos && hubSeasonDicDtos.size() > 0) {
			return hubSeasonDicDtos.get(0);
		} else {
			return null;
		}

	}

	public void saveSeason(String supplierId, String supplierSeason) {
		// 先查询实付存在 存在不做处理
		if (null != this.getHubSeasonDicBySupplierIdAndsupplierSeason(supplierId, supplierSeason)) {
			return;
		}
		HubSeasonDicDto dto = new HubSeasonDicDto();
		dto.setCreateUser(ConstantProperty.DATA_CREATE_USER);
		dto.setCreateTime(new Date());
		dto.setPushState(DataBusinessStatus.NO_PUSH.getIndex().byteValue());
		dto.setSupplierid(supplierId);
		dto.setSupplierSeason(supplierSeason);
		try {
			hubSeasonDicGateWay.insert(dto);
		} catch (Exception e) {
			if (e instanceof DuplicateKeyException) {

			} else {
				e.printStackTrace();
				throw e;
			}
		}
	}
}
