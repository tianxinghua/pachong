package com.shangpin.ephub.product.business.common.hubDic.color.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicDto;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicItemCriteriaDto;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicItemDto;
import com.shangpin.ephub.client.data.mysql.color.gateway.HubColorDicGateWay;
import com.shangpin.ephub.client.data.mysql.color.gateway.HubColorDicItemGateWay;
import com.shangpin.ephub.product.business.common.hubDic.color.dto.ColorDTO;
import com.shangpin.ephub.product.business.common.util.ConstantProperty;

/**
 * Created by loyalty on 16/12/16. 数据层的处理
 */
@Service
public class HubColorDicService {


	@Autowired
	private HubColorDicItemGateWay hubColorDicItemGateWay;
	@Autowired
	private HubColorDicGateWay hubColorDicGateWay;
	
	public List<ColorDTO> getColorDTO() {

		HubColorDicItemCriteriaDto criteria = new HubColorDicItemCriteriaDto();
		criteria.setPageSize(ConstantProperty.MAX_COLOR_ITEM_QUERY_NUM);
		criteria.setPageNo(1);
		List<HubColorDicItemDto> hubColorDicItemDtos = hubColorDicItemGateWay.selectByCriteria(criteria);

		HubColorDicCriteriaDto colorDicCriteriaDto = new HubColorDicCriteriaDto();
		colorDicCriteriaDto.setPageSize(ConstantProperty.MAX_COMMON_QUERY_NUM);
		colorDicCriteriaDto.setPageNo(1);
		List<HubColorDicDto> dicDtos = hubColorDicGateWay.selectByCriteria(colorDicCriteriaDto);
		Map<Long, HubColorDicDto> colorDicMap = new HashMap<>();
		for (HubColorDicDto colorDicDto : dicDtos) {
			colorDicMap.put(colorDicDto.getColorDicId(), colorDicDto);
		}

		List<ColorDTO> colorDTOS = new ArrayList<>();

		for (HubColorDicItemDto itemDto : hubColorDicItemDtos) {
			ColorDTO colorDTO = new ColorDTO();
			colorDTO.setColorItemId(itemDto.getColorDicItemId());
			colorDTO.setSupplierColor(itemDto.getColorItemName().trim());
			if (colorDicMap.containsKey(itemDto.getColorDicId())) {
				colorDTO.setColorDicId(itemDto.getColorDicId());
				colorDTO.setSupplierColor(itemDto.getColorItemName());
				colorDTO.setHubColorNo(colorDicMap.get(itemDto.getColorDicId()).getColorNo());
				colorDTO.setHubColorName(colorDicMap.get(itemDto.getColorDicId()).getColorName());
			}
			colorDTOS.add(colorDTO);
		}
		return colorDTOS;

	}
	
	public HubColorDicItemDto getHubColorDicItem(String supplierColor) {
		if (StringUtils.isBlank(supplierColor))
			return null;

		HubColorDicItemCriteriaDto criteria = new HubColorDicItemCriteriaDto();
		criteria.createCriteria().andColorItemNameEqualTo(supplierColor);
		List<HubColorDicItemDto> hubColorDicItemDtos = hubColorDicItemGateWay.selectByCriteria(criteria);
		if (null != hubColorDicItemDtos && hubColorDicItemDtos.size() > 0) {
			return hubColorDicItemDtos.get(0);
		} else {
			return null;
		}
	}
	public void saveColorItem(String supplierColor) throws Exception {
		// 供货商数据为空 不插入
		if (StringUtils.isBlank(supplierColor))
			return;

		// 查询是否存在
		if (null != this.getHubColorDicItem(supplierColor)) {
			return;
		}
		HubColorDicItemDto dto = new HubColorDicItemDto();
		dto.setCreateTime(new Date());
		dto.setCreateUser(ConstantProperty.DATA_CREATE_USER);
		dto.setColorItemName(supplierColor);
		try {
			hubColorDicItemGateWay.insert(dto);
		} catch (Exception e) {
			if (e instanceof DuplicateKeyException) {

			} else {
				e.printStackTrace();
				throw e;
			}
		}
	}

}
