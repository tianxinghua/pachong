package com.shangpin.ephub.product.business.common.hubDic.category;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.esotericsoftware.minlog.Log;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicDto;
import com.shangpin.ephub.client.data.mysql.categroy.gateway.HubSupplierCategroyDicGateWay;
import com.shangpin.ephub.client.data.mysql.enumeration.InfoState;
import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicDto;
import com.shangpin.ephub.client.data.mysql.gender.gateway.HubGenderDicGateWay;
import com.shangpin.ephub.product.business.common.util.ConstantProperty;

/**
 * Created by loyalty on 16/12/16. 数据层的处理
 */
@Service
public class HubCategoryDicService {


	@Autowired
	private HubGenderDicGateWay hubGenderDicGateWay;
	@Autowired
	private HubSupplierCategroyDicGateWay hubSupplierCategroyDicGateWay;

	public List<HubSupplierCategroyDicDto> getSupplierCategoryBySupplierId(String supplierId,int pageNo,int pageSize,Byte pushState) {
		HubSupplierCategroyDicCriteriaDto criteria = new HubSupplierCategroyDicCriteriaDto();
		criteria.setPageNo(pageNo);
		criteria.setPageSize(pageSize);
		HubSupplierCategroyDicCriteriaDto.Criteria criterion = criteria.createCriteria();
		criterion.andSupplierIdEqualTo(supplierId)
				.andPushStateEqualTo(pushState);
		return hubSupplierCategroyDicGateWay.selectByCriteria(criteria);
	}
	public List<HubSupplierCategroyDicDto> getSupplierCategory(HubSupplierCategroyDicCriteriaDto criteria) {
		return hubSupplierCategroyDicGateWay.selectByCriteria(criteria);
	}
	public List<HubSupplierCategroyDicDto> getSupplierCategoryBySupplierIdAndType(String supplierId,int pageNo,int pageSize,Byte categoryType,String supplierCategory,String supplierGender) {
		HubSupplierCategroyDicCriteriaDto criteria = new HubSupplierCategroyDicCriteriaDto();
		criteria.setPageNo(pageNo);
		criteria.setPageSize(pageSize);
		HubSupplierCategroyDicCriteriaDto.Criteria criterion = criteria.createCriteria();
		if(StringUtils.isNotBlank(supplierId)){
			criterion.andSupplierIdEqualTo(supplierId);	
		}
		if(categoryType!=0){
			if(categoryType==5){
				criterion.andMappingStateNotEqualTo((byte)1);
			}else{
				criterion.andCategoryTypeEqualTo(categoryType);
			}
		}
		if(StringUtils.isNotBlank(supplierCategory)){
			criterion.andSupplierCategoryLike("%"+supplierCategory+"%");
		}
		if(StringUtils.isNotBlank(supplierGender)){
			criterion.andSupplierGenderEqualTo(supplierGender);
		}
		criteria.setOrderByClause("update_time desc");
		return hubSupplierCategroyDicGateWay.selectByCriteria(criteria);
	}

	public int countSupplierCategoryBySupplierIdAndType(String supplierId,Byte categoryType,String supplilerCategory,String supplierGender) {
		HubSupplierCategroyDicCriteriaDto criteria = new HubSupplierCategroyDicCriteriaDto();
		HubSupplierCategroyDicCriteriaDto.Criteria criterion = criteria.createCriteria();
		if(StringUtils.isNotBlank(supplierId)){
			criterion.andSupplierIdEqualTo(supplierId);	
		}
		if(categoryType!=0){
			if(categoryType==5){
				criterion.andMappingStateNotEqualTo((byte)1);
			}else{
				criterion.andCategoryTypeEqualTo(categoryType);
			}
		}
		if(StringUtils.isNotBlank(supplilerCategory)){
			Log.info("supplilerCategory:"+supplilerCategory);
			criterion.andSupplierCategoryLike("%"+supplilerCategory.trim()+"%");
			
		}
		if(StringUtils.isNotBlank(supplierGender)){
			criterion.andSupplierGenderEqualTo(supplierGender);
		}
		return hubSupplierCategroyDicGateWay.countByCriteria(criteria);
	}
	
	public HubSupplierCategroyDicDto getSupplierCategoryBySupplierIdAndSupplierCategoryAndSupplierGender(
			String supplierId, String supplierCategory, String supplierGender) {
		HubSupplierCategroyDicCriteriaDto criteria = new HubSupplierCategroyDicCriteriaDto();
		HubSupplierCategroyDicCriteriaDto.Criteria criterion = criteria.createCriteria();
		criterion.andSupplierIdEqualTo(supplierId).andSupplierCategoryEqualTo(supplierCategory);
		if (StringUtils.isBlank(supplierGender)) {
			criterion.andSupplierGenderIsNull();
		} else {
			criterion.andSupplierGenderEqualTo(supplierGender);
		}
		List<HubSupplierCategroyDicDto> hubSupplierCategroyDicDtos = hubSupplierCategroyDicGateWay
				.selectByCriteria(criteria);
		if (null != hubSupplierCategroyDicDtos && hubSupplierCategroyDicDtos.size() > 0) {
			return hubSupplierCategroyDicDtos.get(0);
		} else {
			return null;
		}
	}
	
	public void saveHubCategory(String supplierId, String supplierCategory, String supplierGender) throws Exception {

		// 先获取性别字典的值 目的是品类映射表需要性别字典的主键
		HubGenderDicDto hubGenderDicDto = this.getHubGenderDicBySupplierIdAndSupplierGender(null, supplierGender);

		HubSupplierCategroyDicDto dto = new HubSupplierCategroyDicDto();

		if (null != getSupplierCategoryBySupplierIdAndSupplierCategoryAndSupplierGender(supplierId, supplierCategory,
				supplierGender)) {
			return;
		}
		Date date = new Date();
		dto.setSupplierId(supplierId);
		dto.setSupplierCategory(supplierCategory);
		dto.setSupplierGender(supplierGender);
		dto.setMappingState(InfoState.IMPERFECT.getIndex());
		dto.setGenderDicId(null == hubGenderDicDto ? null : hubGenderDicDto.getGenderDicId());
		dto.setCreateTime(date);
		dto.setUpdateTime(date);
		try {
			hubSupplierCategroyDicGateWay.insert(dto);
		} catch (Exception e) {
			if (e instanceof DuplicateKeyException) {

			} else {
				e.printStackTrace();
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

	public void updateHubCategoryDic(Long id, String categoryNo,String createUser) {
		HubSupplierCategroyDicDto hubSupplierCategroyDic = new HubSupplierCategroyDicDto();
		hubSupplierCategroyDic.setHubCategoryNo(categoryNo);
		hubSupplierCategroyDic.setSupplierCategoryDicId(id);
		hubSupplierCategroyDic.setCategoryType((byte)4);
		hubSupplierCategroyDic.setUpdateTime(new Date());
		hubSupplierCategroyDic.setUpdateUser(createUser);
		hubSupplierCategroyDicGateWay.updateByPrimaryKeySelective(hubSupplierCategroyDic);
		
	}

	public HubSupplierCategroyDicDto getSupplierCategoryById(Long id) {
		return hubSupplierCategroyDicGateWay.selectByPrimaryKey(id);
	}
	public void updateHubCategoryDicByPrimaryKey(HubSupplierCategroyDicDto dicDto) {
		hubSupplierCategroyDicGateWay.updateByPrimaryKeySelective(dicDto);
	}
	public void deleteHubSupplierCateoryById(Long id) {
		hubSupplierCategroyDicGateWay.deleteByPrimaryKey(id);
	}
}
