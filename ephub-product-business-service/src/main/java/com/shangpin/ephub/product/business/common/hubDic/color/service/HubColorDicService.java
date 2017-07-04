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
import com.shangpin.ephub.client.data.mysql.enumeration.ConstantProperty;
import com.shangpin.ephub.product.business.common.hubDic.color.dto.ColorDTO;

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
		dto.setUpdateTime(new Date());
		dto.setCreateUser(ConstantProperty.DATA_CREATE_USER);
		dto.setColorItemName(supplierColor);
		dto.setPushState((byte)0);
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

	public int countSupplierColorByType(Byte type, String supplierColor, Long colorDicId) {
		HubColorDicItemCriteriaDto hubColorDicItemCriteriaDto = new HubColorDicItemCriteriaDto();
		HubColorDicItemCriteriaDto.Criteria criteria = hubColorDicItemCriteriaDto.createCriteria();
		if(StringUtils.isNotBlank(supplierColor)){
			criteria.andColorItemNameLike("%"+supplierColor+"%");
		}
		if(colorDicId!=null){
			criteria.andColorDicIdEqualTo(colorDicId);
		}
		criteria.andPushStateEqualTo(type);
		if(type==0){
			HubColorDicItemCriteriaDto.Criteria criteria1 = hubColorDicItemCriteriaDto.createCriteria();
			if(StringUtils.isNotBlank(supplierColor)){
				criteria1.andColorItemNameLike("%"+supplierColor+"%");
			}
			if(colorDicId!=null){
				criteria1.andColorDicIdEqualTo(colorDicId);
			}
			hubColorDicItemCriteriaDto.or(criteria1.andPushStateIsNull());
		}
		return hubColorDicItemGateWay.countByCriteria(hubColorDicItemCriteriaDto);
	}

	public List<HubColorDicItemDto> getSupplierColorByType(int pageNo,int pageSize,Byte type, String supplierColor, Long colorDicId) {
		HubColorDicItemCriteriaDto hubColorDicItemCriteriaDto = new HubColorDicItemCriteriaDto();
		HubColorDicItemCriteriaDto.Criteria criteria = hubColorDicItemCriteriaDto.createCriteria();
		hubColorDicItemCriteriaDto.setPageNo(pageNo);
		hubColorDicItemCriteriaDto.setPageSize(pageSize);
		if(StringUtils.isNotBlank(supplierColor)){
			criteria.andColorItemNameLike("%"+supplierColor+"%");
		}
		if(colorDicId!=null){
			criteria.andColorDicIdEqualTo(colorDicId);
		}
		criteria.andPushStateEqualTo(type);
		if(type==0){
			
			HubColorDicItemCriteriaDto.Criteria criteria1 = hubColorDicItemCriteriaDto.createCriteria();
			if(StringUtils.isNotBlank(supplierColor)){
				criteria1.andColorItemNameLike("%"+supplierColor+"%");
			}
			if(colorDicId!=null){
				criteria1.andColorDicIdEqualTo(colorDicId);
			}
			hubColorDicItemCriteriaDto.or(criteria1.andPushStateIsNull());
		}
		hubColorDicItemCriteriaDto.setOrderByClause("update_time desc");
		return hubColorDicItemGateWay.selectByCriteria(hubColorDicItemCriteriaDto);
	}

	public List<HubColorDicDto> getSpColorList(int pageNo,int pageSize,Long id) {
		HubColorDicCriteriaDto hubColorDicCriteriaDto = new HubColorDicCriteriaDto();
		HubColorDicCriteriaDto.Criteria criteria = hubColorDicCriteriaDto.createCriteria();
		if(id!=null){
			criteria.andColorDicIdEqualTo(id);
		}
		hubColorDicCriteriaDto.setPageNo(pageNo);
		hubColorDicCriteriaDto.setPageSize(pageSize);
		return hubColorDicGateWay.selectByCriteria(hubColorDicCriteriaDto);
	}

	public int countSupplierColorByType(Long id) {
		HubColorDicCriteriaDto hubColorDicCriteriaDto = new HubColorDicCriteriaDto();
		HubColorDicCriteriaDto.Criteria criteria = hubColorDicCriteriaDto.createCriteria();
		if(id!=null){
			criteria.andColorDicIdEqualTo(id);
		}
		return hubColorDicGateWay.countByCriteria(hubColorDicCriteriaDto);
	}

	public HubColorDicItemDto getSupplierColorByHubColorId(Long id) {
		return hubColorDicItemGateWay.selectByPrimaryKey(id);
		
	}

	public List<HubColorDicItemDto> getHubColorDicItemBySupplierColor(String supplierColor) {
		HubColorDicItemCriteriaDto crireria = new HubColorDicItemCriteriaDto();
		crireria.createCriteria().andColorItemNameEqualTo(supplierColor);
		crireria.setPageNo(1);
		crireria.setPageSize(10000);
		return hubColorDicItemGateWay.selectByCriteria(crireria);
	}

	public void saveColorItem(HubColorDicItemDto dicDto) {
		hubColorDicItemGateWay.insertSelective(dicDto);
	}

	public int countHubColorDicByHubColorId(Long colorDicId) {
		HubColorDicItemCriteriaDto crireria = new HubColorDicItemCriteriaDto();
		crireria.createCriteria().andColorDicIdEqualTo(colorDicId);
		return hubColorDicItemGateWay.countByCriteria(crireria);
	}

	public void updateSupplierColorById(HubColorDicItemDto dicDto) {
		hubColorDicItemGateWay.updateByPrimaryKeySelective(dicDto);
	}

	public void deleteHubSupplierColorById(Long id) {
		hubColorDicItemGateWay.deleteByPrimaryKey(id);		
	}
}
