package com.shangpin.ephub.data.mysql.dictionary.brand.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.dictionary.brand.bean.HubSupplierBrandDicCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.dictionary.brand.bean.HubSupplierBrandDicWithCriteria;
import com.shangpin.ephub.data.mysql.dictionary.brand.mapper.HubSupplierBrandDicMapper;
import com.shangpin.ephub.data.mysql.dictionary.brand.po.HubSupplierBrandDic;
import com.shangpin.ephub.data.mysql.dictionary.brand.po.HubSupplierBrandDicCriteria;

/**
 * <p>Title:HubBrandDicService.java </p>
 * <p>Description: EPHUB品牌字典数据访问层逻辑处理</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午2:46:25
 */
@Service
public class HubSupplierBrandDicService {

	@Autowired
	private HubSupplierBrandDicMapper hubSupplierBrandDicMapper;

	public int countByCriteria(HubSupplierBrandDicCriteria criteria) {
		return hubSupplierBrandDicMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubSupplierBrandDicCriteria criteria) {
		return hubSupplierBrandDicMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long supplierBrandDicId) {
		return hubSupplierBrandDicMapper.deleteByPrimaryKey(supplierBrandDicId);
	}

	public int insert(HubSupplierBrandDic hubSupplierBrandDic) {
		return hubSupplierBrandDicMapper.insert(hubSupplierBrandDic);
	}

	public int insertSelective(HubSupplierBrandDic hubSupplierBrandDic) {
		return hubSupplierBrandDicMapper.insertSelective(hubSupplierBrandDic);
	}

	public List<HubSupplierBrandDic> selectByCriteriaWithRowbounds(
			HubSupplierBrandDicCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubSupplierBrandDicMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubSupplierBrandDic> selectByCriteria(HubSupplierBrandDicCriteria criteria) {
		return hubSupplierBrandDicMapper.selectByExample(criteria);
	}

	public HubSupplierBrandDic selectByPrimaryKey(Long supplierBrandDicId) {
		return hubSupplierBrandDicMapper.selectByPrimaryKey(supplierBrandDicId);
	}

	public int updateByCriteriaSelective(HubSupplierBrandDicWithCriteria hubSupplierBrandDicWithCriteria) {
		return hubSupplierBrandDicMapper.updateByExampleSelective(hubSupplierBrandDicWithCriteria.getHubSupplierBrandDic(), hubSupplierBrandDicWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubSupplierBrandDicWithCriteria hubSupplierBrandDicWithCriteria) {
		return hubSupplierBrandDicMapper.updateByExample(hubSupplierBrandDicWithCriteria.getHubSupplierBrandDic(), hubSupplierBrandDicWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubSupplierBrandDic hubSupplierBrandDic) {
		return hubSupplierBrandDicMapper.updateByPrimaryKeySelective(hubSupplierBrandDic);
	}

	public int updateByPrimaryKey(HubSupplierBrandDic hubSupplierBrandDic) {
		return hubSupplierBrandDicMapper.updateByPrimaryKey(hubSupplierBrandDic);
	}
}
