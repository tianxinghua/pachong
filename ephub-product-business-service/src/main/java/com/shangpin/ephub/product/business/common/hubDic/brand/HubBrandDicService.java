package com.shangpin.ephub.product.business.common.hubDic.brand;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.esotericsoftware.minlog.Log;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubSupplierBrandDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubSupplierBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubBrandDicGateway;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubSupplierBrandDicGateWay;
import com.shangpin.ephub.client.data.mysql.enumeration.ConstantProperty;
import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
import com.shangpin.ephub.client.data.mysql.enumeration.FilterFlag;

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

	public void saveSupplierBrand(String supplierId, String supplierBrandName) throws Exception {

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

	public void saveHubBrand(String hubBrand, String supplierBrandName,String createUser) throws Exception {
		List<HubBrandDicDto> hubBrandList = getHubBrandDic(supplierBrandName);
		if(hubBrandList!=null&&hubBrandList.size()>0){
			Log.info("品牌"+supplierBrandName+"已存在");
			return;
		}
		HubBrandDicDto dic = new HubBrandDicDto();
		dic.setHubBrandNo(hubBrand);
		dic.setSupplierBrand(supplierBrandName);
		dic.setCreateTime(new Date());
		dic.setUpdateTime(new Date());
		dic.setDataState((byte)1);
		dic.setCreateUser(createUser);
		dic.setUpdateUser(createUser);
		brandDicGateway.insertSelective(dic);
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

	public int countSupplierBrandBySupplierIdAndType(String supplierId, String supplierBrand,String hubBrandNo,String startTime,String endTime) throws ParseException {
		
		HubSupplierBrandDicCriteriaDto hubSupplierBrandDicCriteriaDto = new HubSupplierBrandDicCriteriaDto();
		HubSupplierBrandDicCriteriaDto.Criteria criteria = hubSupplierBrandDicCriteriaDto.createCriteria();
		if(StringUtils.isNotBlank(supplierId)){
			criteria.andSupplierIdEqualTo(supplierId);	
		}
		if(StringUtils.isNotBlank(supplierBrand)){
			criteria.andSupplierBrandLike("%"+supplierBrand+"%");
		}
		if(StringUtils.isNotBlank(hubBrandNo)){
			criteria.andHubBrandNoEqualTo(hubBrandNo);
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
		criteria.andPushStateEqualTo((byte)0);
		
		HubSupplierBrandDicCriteriaDto.Criteria criteria2 = hubSupplierBrandDicCriteriaDto.createCriteria();
		if(StringUtils.isNotBlank(supplierId)){
			criteria2.andSupplierIdEqualTo(supplierId);	
		}
		if(StringUtils.isNotBlank(supplierBrand)){
			criteria2.andSupplierBrandEqualTo(supplierBrand);
		}
		if(StringUtils.isNotBlank(hubBrandNo)){
			criteria2.andHubBrandNoEqualTo(hubBrandNo);
		}
		if(StringUtils.isNotBlank(startTime)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date parse = format.parse(startTime+" 00:00:00");
			criteria2.andUpdateTimeGreaterThanOrEqualTo(parse);
		}
		if(StringUtils.isNotBlank(endTime)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date parse = format.parse(endTime);
			Calendar calendar   = Calendar.getInstance();
			calendar.setTime(parse);
			calendar.add(calendar.DAY_OF_MONTH,1);
			criteria2.andUpdateTimeLessThan(calendar.getTime());
		}
		hubSupplierBrandDicCriteriaDto.or(criteria2.andPushStateIsNull());
		return supplierBrandDicGateWay.countByCriteria(hubSupplierBrandDicCriteriaDto);
	}

	public List<HubSupplierBrandDicDto> getSupplierBrandBySupplierIdAndType(String supplierId, String supplierBrand,String hubBrandNo,
			int pageNo, int pageSize,String startTime,String endTime) throws ParseException {
		HubSupplierBrandDicCriteriaDto hubSupplierBrandDicCriteriaDto = new HubSupplierBrandDicCriteriaDto();
		HubSupplierBrandDicCriteriaDto.Criteria criteria = hubSupplierBrandDicCriteriaDto.createCriteria();
		hubSupplierBrandDicCriteriaDto.setPageNo(pageNo);
		hubSupplierBrandDicCriteriaDto.setPageSize(pageSize);
		if(StringUtils.isNotBlank(supplierId)){
			criteria.andSupplierIdEqualTo(supplierId);	
		}
		if(StringUtils.isNotBlank(supplierBrand)){
			criteria.andSupplierBrandLike("%"+supplierBrand+"%");
		}
		if(StringUtils.isNotBlank(hubBrandNo)){
			criteria.andHubBrandNoEqualTo(hubBrandNo);
		}
		if(StringUtils.isNotBlank(startTime)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date parse = format.parse(startTime+" 00:00:00");
			criteria.andUpdateTimeGreaterThan(parse);
		}
		if(StringUtils.isNotBlank(endTime)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date parse = format.parse(endTime+" 23:59:59");
			criteria.andUpdateTimeLessThanOrEqualTo(parse);
		}
		criteria.andPushStateEqualTo((byte)0);
		
		HubSupplierBrandDicCriteriaDto.Criteria criteria2 = hubSupplierBrandDicCriteriaDto.createCriteria();
		if(StringUtils.isNotBlank(supplierId)){
			criteria2.andSupplierIdEqualTo(supplierId);	
		}
		if(StringUtils.isNotBlank(supplierBrand)){
			criteria2.andSupplierBrandEqualTo(supplierBrand);
		}
		if(StringUtils.isNotBlank(hubBrandNo)){
			criteria2.andHubBrandNoEqualTo(hubBrandNo);
		}
		if(StringUtils.isNotBlank(startTime)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date parse = format.parse(startTime+" 00:00:00");
			criteria2.andUpdateTimeGreaterThan(parse);
		}
		if(StringUtils.isNotBlank(endTime)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date parse = format.parse(endTime+" 23:59:59");
			criteria2.andUpdateTimeLessThanOrEqualTo(parse);
		}
		hubSupplierBrandDicCriteriaDto.or(criteria2.andPushStateIsNull());
		
		hubSupplierBrandDicCriteriaDto.setOrderByClause("update_time desc");
		return supplierBrandDicGateWay.selectByCriteria(hubSupplierBrandDicCriteriaDto);
	}

	public int countHubBrand() {
		return brandDicGateway.count();
	}
	public int countHubBrand(String supplierBrand, String hubBrandNo,String startTime,String endTime) throws ParseException {
		HubBrandDicCriteriaDto cruteria = new HubBrandDicCriteriaDto();
		HubBrandDicCriteriaDto.Criteria criteria = cruteria.createCriteria();
		if(StringUtils.isNotBlank(hubBrandNo)){
			criteria.andHubBrandNoEqualTo(hubBrandNo);
		}
		if(StringUtils.isNotBlank(supplierBrand)){
			criteria.andSupplierBrandEqualTo(supplierBrand);
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
		return brandDicGateway.countByCriteria(cruteria);
	}
	public List<HubBrandDicDto> getHubBrand(String supplierBrand, String hubBrandNo, int pageNo, int pageSize,String startTime,String endTime) throws ParseException {
		HubBrandDicCriteriaDto cruteria = new HubBrandDicCriteriaDto();
		HubBrandDicCriteriaDto.Criteria criteria = cruteria.createCriteria();
		if(StringUtils.isNotBlank(supplierBrand)){
			criteria.andSupplierBrandEqualTo(supplierBrand);
		}
		if(StringUtils.isNotBlank(hubBrandNo)){
			criteria.andHubBrandNoEqualTo(hubBrandNo);
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
		cruteria.setPageNo(pageNo);
		cruteria.setPageSize(pageSize);
		cruteria.setOrderByClause("update_time desc");

//	    select hub_brand_no from hub_brand_dic  GROUP BY hub_brand_no LIMIT 10,10
	    
		return brandDicGateway.selectByCriteria(cruteria);
	}

	public List<HubBrandDicDto> getSupplierBrandByHubBrand(String hubBrandNo, int pageNo, int pageSize) {
		HubBrandDicCriteriaDto cruteria = new HubBrandDicCriteriaDto();
		cruteria.createCriteria().andHubBrandNoEqualTo(hubBrandNo);
		cruteria.setPageNo(pageNo);
		cruteria.setPageSize(pageSize);
		return brandDicGateway.selectByCriteria(cruteria);
	}

	public void updateHubSupplierBrandDicById(HubSupplierBrandDicDto dicDto) {
		supplierBrandDicGateWay.updateByPrimaryKeySelective(dicDto);
	}

	public int countHubBrandByHubBrand(String hubBrandNo) {
		HubBrandDicCriteriaDto crite = new HubBrandDicCriteriaDto();
		crite.createCriteria().andHubBrandNoEqualTo(hubBrandNo);
		return brandDicGateway.countByCriteria(crite);
	}

	public void updateHubBrandDicById(HubSupplierBrandDicDto dicDto) {
		supplierBrandDicGateWay.updateByPrimaryKeySelective(dicDto);
	}

	public void deleteHubBrandById(Long id) {
		brandDicGateway.deleteByPrimaryKey(id);
	}

	public List<HubBrandDicDto> getHubBrandList(HubBrandDicCriteriaDto cruteria ) {
		return brandDicGateway.selectHUbBrandNo(cruteria);
	}

	public HubBrandDicDto getHubBrandById(Long id) {
		return brandDicGateway.selectByPrimaryKey(id);
	}
	
	public void updateHubBrandById(HubBrandDicDto dic){
		brandDicGateway.updateByPrimaryKeySelective(dic);
	}
}
