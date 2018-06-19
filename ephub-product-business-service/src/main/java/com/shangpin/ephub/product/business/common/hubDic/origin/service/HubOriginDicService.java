package com.shangpin.ephub.product.business.common.hubDic.origin.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.enumeration.ConstantProperty;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSupplierValueMappingGateWay;
import com.shangpin.ephub.product.business.common.enumeration.SupplierValueMappingType;

/**
 * Created by loyalty on 16/12/16. 数据层的处理
 */
@Service
public class HubOriginDicService {

	@Autowired
	HubSupplierValueMappingGateWay hubSupplierValueMappingGateWay;

	public List<HubSupplierValueMappingDto> getHubSupplierValueMappingByType(Integer type) {
		HubSupplierValueMappingCriteriaDto criteria = new HubSupplierValueMappingCriteriaDto();
		criteria.setPageSize(ConstantProperty.MAX_COMMON_QUERY_NUM);
		criteria.createCriteria().andHubValTypeEqualTo(type.byteValue());
		return hubSupplierValueMappingGateWay.selectByCriteria(criteria);
	}

	public List<HubSupplierValueMappingDto> getSupplierCommonSizeValueMapping() {
		HubSupplierValueMappingCriteriaDto criteria = new HubSupplierValueMappingCriteriaDto();
		criteria.setOrderByClause("sort_val");
		criteria.setPageSize(ConstantProperty.MAX_COMMON_QUERY_NUM);
		criteria.createCriteria().andSupplierIdEqualTo("").andHubValTypeEqualTo(SupplierValueMappingType.TYPE_SIZE.getIndex().byteValue());
		criteria.or(criteria.createCriteria().andSupplierIdIsNull().andHubValTypeEqualTo(SupplierValueMappingType.TYPE_SIZE.getIndex().byteValue()));
		return hubSupplierValueMappingGateWay.selectByCriteria(criteria);
	}

	public List<HubSupplierValueMappingDto> getHubSupplierValueMappingBySupplierIdAndType(String supplierId,
			Integer type) {
		HubSupplierValueMappingCriteriaDto criteria = new HubSupplierValueMappingCriteriaDto();
		criteria.setPageSize(ConstantProperty.MAX_COMMON_QUERY_NUM);
		criteria.setPageNo(1);
		criteria.createCriteria().andSupplierIdEqualTo(supplierId).andHubValTypeEqualTo(type.byteValue());
		return hubSupplierValueMappingGateWay.selectByCriteria(criteria);
	}

	public int countHubSupplierValueMapping(String hubVal, String supplierVal,Byte type,String startTime,String endTime) throws ParseException {
		HubSupplierValueMappingCriteriaDto hubSupplierValueMappingCriteriaDto  = new HubSupplierValueMappingCriteriaDto();
		HubSupplierValueMappingCriteriaDto.Criteria criteria = hubSupplierValueMappingCriteriaDto.createCriteria();
		if(StringUtils.isNotBlank(hubVal)){
			criteria.andHubValEqualTo(hubVal);	
		}
		if(StringUtils.isNotBlank(supplierVal)){
			criteria.andSupplierValEqualTo("%"+supplierVal+"%");
		}

		if(StringUtils.isNotBlank(startTime)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date parse = format.parse(startTime+" 00:00:00");
			criteria.andUpdateTimeGreaterThanOrEqualTo(parse);
		}
		if(StringUtils.isNotBlank(endTime)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date parse = format.parse(endTime);
			Calendar calendar   = Calendar.getInstance();
			calendar.setTime(parse);
			calendar.add(calendar.DAY_OF_MONTH,1);
			criteria.andUpdateTimeLessThan(calendar.getTime());
		}
		if(type!=null){
			criteria.andMappingTypeEqualTo(type);
		}
		criteria.andHubValTypeEqualTo(SupplierValueMappingType.TYPE_ORIGIN.getIndex().byteValue());
		return hubSupplierValueMappingGateWay.countByCriteria(hubSupplierValueMappingCriteriaDto);
	}

	public List<HubSupplierValueMappingDto> getHubSupplierValueMappingBySupplierIdAndType(String hubVal,
			String supplierVal, int pageNo, int pageSize,Byte type,String startTime,String endTime) throws ParseException {
		HubSupplierValueMappingCriteriaDto hubSupplierValueMappingCriteriaDto  = new HubSupplierValueMappingCriteriaDto();
		HubSupplierValueMappingCriteriaDto.Criteria criteria = hubSupplierValueMappingCriteriaDto.createCriteria();
		hubSupplierValueMappingCriteriaDto.setPageNo(pageNo);
		hubSupplierValueMappingCriteriaDto.setPageSize(pageSize);
		hubSupplierValueMappingCriteriaDto.setOrderByClause("update_time desc");
		if(type!=null){
			criteria.andMappingTypeEqualTo(type);
		}
		criteria.andHubValTypeEqualTo(SupplierValueMappingType.TYPE_ORIGIN.getIndex().byteValue());
		if(StringUtils.isNotBlank(hubVal)){
			criteria.andHubValEqualTo(hubVal);	
		}
		if(StringUtils.isNotBlank(supplierVal)){
			criteria.andSupplierValEqualTo("%"+supplierVal+"%");
		}
		if(StringUtils.isNotBlank(startTime)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date parse = format.parse(startTime+" 00:00:00");
			criteria.andUpdateTimeGreaterThanOrEqualTo(parse);
		}
		if(StringUtils.isNotBlank(endTime)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date parse = format.parse(endTime);
			Calendar calendar   = Calendar.getInstance();
			calendar.setTime(parse);
			calendar.add(calendar.DAY_OF_MONTH,1);
			criteria.andUpdateTimeLessThan(calendar.getTime());
		}
		return hubSupplierValueMappingGateWay.selectByCriteria(hubSupplierValueMappingCriteriaDto);
	}

	public HubSupplierValueMappingDto getHubSupplierValueMappingById(Long id) {
		return hubSupplierValueMappingGateWay.selectByPrimaryKey(id);
	}

	public void updateHubSupplierValueMappingByPrimaryKey(HubSupplierValueMappingDto hubSupplierValueMappingDto) {
		hubSupplierValueMappingGateWay.updateByPrimaryKeySelective(hubSupplierValueMappingDto);
	}

	public List<HubSupplierValueMappingDto> getHubSupplierValueMappingBySupplierVal(String supplierVal) {
		HubSupplierValueMappingCriteriaDto criteria = new HubSupplierValueMappingCriteriaDto();
		criteria.createCriteria().andSupplierValEqualTo(supplierVal).andHubValTypeEqualTo(SupplierValueMappingType.TYPE_ORIGIN.getIndex().byteValue());
		return hubSupplierValueMappingGateWay.selectByCriteria(criteria);
	}

	public void insertHubSupplierValueMapping(HubSupplierValueMappingDto hubSupplierValueMappingDto) {
		hubSupplierValueMappingGateWay.insertSelective(hubSupplierValueMappingDto);
	}

	public void deleteHubSupplierValueMapping(Long id) {
		hubSupplierValueMappingGateWay.deleteByPrimaryKey(id);
	}
}
