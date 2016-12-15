package com.shangpin.ephub.data.mysql.dictionary.gender.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.dictionary.gender.bean.HubGenderDicCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.dictionary.gender.bean.HubGenderDicWithCriteria;
import com.shangpin.ephub.data.mysql.dictionary.gender.mapper.HubGenderDicMapper;
import com.shangpin.ephub.data.mysql.dictionary.gender.po.HubGenderDic;
import com.shangpin.ephub.data.mysql.dictionary.gender.po.HubGenderDicCriteria;

/**
 * <p>Title:HubGenderDicService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:29:52
 */
@Service
public class HubGenderDicService {
	
	@Autowired
	private HubGenderDicMapper hubGenderDicMapper;

	public int countByCriteria(HubGenderDicCriteria criteria) {
		return hubGenderDicMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubGenderDicCriteria criteria) {
		return hubGenderDicMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long genderDicId) {
		return hubGenderDicMapper.deleteByPrimaryKey(genderDicId);
	}

	public int insert(HubGenderDic hubGenderDic) {
		return hubGenderDicMapper.insert(hubGenderDic);
	}

	public int insertSelective(HubGenderDic hubGenderDic) {
		return hubGenderDicMapper.insertSelective(hubGenderDic);
	}

	public List<HubGenderDic> selectByCriteriaWithRowbounds(HubGenderDicCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubGenderDicMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubGenderDic> selectByCriteria(HubGenderDicCriteria criteria) {
		return hubGenderDicMapper.selectByExample(criteria);
	}

	public HubGenderDic selectByPrimaryKey(Long genderDicId) {
		return hubGenderDicMapper.selectByPrimaryKey(genderDicId);
	}

	public int updateByCriteriaSelective(HubGenderDicWithCriteria hubGenderDicWithCriteria) {
		return hubGenderDicMapper.updateByExampleSelective(hubGenderDicWithCriteria.getHubGenderDic(), hubGenderDicWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubGenderDicWithCriteria hubGenderDicWithCriteria) {
		return hubGenderDicMapper.updateByExample(hubGenderDicWithCriteria.getHubGenderDic(), hubGenderDicWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubGenderDic hubGenderDic) {
		return hubGenderDicMapper.updateByPrimaryKeySelective(hubGenderDic);
	}

	public int updateByPrimaryKey(HubGenderDic hubGenderDic) {
		return hubGenderDicMapper.updateByPrimaryKey(hubGenderDic);
	}

}
