package com.shangpin.ephub.data.mysql.dictionary.categroy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.dictionary.categroy.bean.HubSupplierCategroyDicCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.dictionary.categroy.bean.HubSupplierCategroyDicWithCriteria;
import com.shangpin.ephub.data.mysql.dictionary.categroy.mapper.HubSupplierCategroyDicMapper;
import com.shangpin.ephub.data.mysql.dictionary.categroy.po.HubSupplierCategroyDic;
import com.shangpin.ephub.data.mysql.dictionary.categroy.po.HubSupplierCategroyDicCriteria;

/**
 * <p>Title:HubSupplierCategroyDicService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:19:36
 */
@Service
public class HubSupplierCategroyDicService {

	@Autowired
	private HubSupplierCategroyDicMapper hubSupplierCategroyDicMapper;

	public int countByCriteria(HubSupplierCategroyDicCriteria criteria) {
		return hubSupplierCategroyDicMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubSupplierCategroyDicCriteria criteria) {
		return hubSupplierCategroyDicMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long supplierCategoryDicId) {
		return hubSupplierCategroyDicMapper.deleteByPrimaryKey(supplierCategoryDicId);
	}

	public int insert(HubSupplierCategroyDic hubSupplierCategroyDic) {
		return hubSupplierCategroyDicMapper.insert(hubSupplierCategroyDic);
	}

	public int insertSelective(HubSupplierCategroyDic hubSupplierCategroyDic) {
		return hubSupplierCategroyDicMapper.insertSelective(hubSupplierCategroyDic);
	}

	public List<HubSupplierCategroyDic> selectByCriteriaWithRowbounds(
			HubSupplierCategroyDicCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubSupplierCategroyDicMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubSupplierCategroyDic> selectByCriteria(HubSupplierCategroyDicCriteria criteria) {
		return hubSupplierCategroyDicMapper.selectByExample(criteria);
	}

	public HubSupplierCategroyDic selectByPrimaryKey(Long supplierCategoryDicId) {
		return hubSupplierCategroyDicMapper.selectByPrimaryKey(supplierCategoryDicId);
	}

	public int updateByCriteriaSelective(HubSupplierCategroyDicWithCriteria hubSupplierCategroyDicWithCriteria) {
		return hubSupplierCategroyDicMapper.updateByExampleSelective(hubSupplierCategroyDicWithCriteria.getHubSupplierCategroyDic(), hubSupplierCategroyDicWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubSupplierCategroyDicWithCriteria hubSupplierCategroyDicWithCriteria) {
		return hubSupplierCategroyDicMapper.updateByExample(hubSupplierCategroyDicWithCriteria.getHubSupplierCategroyDic(), hubSupplierCategroyDicWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubSupplierCategroyDic hubSupplierCategroyDic) {
		return hubSupplierCategroyDicMapper.updateByPrimaryKeySelective(hubSupplierCategroyDic);
	}

	public int updateByPrimaryKey(HubSupplierCategroyDic hubSupplierCategroyDic) {
		return hubSupplierCategroyDicMapper.updateByPrimaryKey(hubSupplierCategroyDic);
	}
}
