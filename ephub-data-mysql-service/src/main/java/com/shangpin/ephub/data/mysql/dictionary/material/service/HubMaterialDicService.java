package com.shangpin.ephub.data.mysql.dictionary.material.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.dictionary.material.bean.HubMaterialDicCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.dictionary.material.bean.HubMaterialDicWithCriteria;
import com.shangpin.ephub.data.mysql.dictionary.material.mapper.HubMaterialDicMapper;
import com.shangpin.ephub.data.mysql.dictionary.material.po.HubMaterialDic;
import com.shangpin.ephub.data.mysql.dictionary.material.po.HubMaterialDicCriteria;

/**
 * <p>Title:HubMaterialDicService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:34:23
 */
@Service
public class HubMaterialDicService {

	@Autowired
	private HubMaterialDicMapper hubMaterialDicMapper;

	public int countByCriteria(HubMaterialDicCriteria criteria) {
		return hubMaterialDicMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubMaterialDicCriteria criteria) {
		return hubMaterialDicMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long materialDicId) {
		return hubMaterialDicMapper.deleteByPrimaryKey(materialDicId);
	}

	public int insert(HubMaterialDic hubMaterialDic) {
		return hubMaterialDicMapper.insert(hubMaterialDic);
	}

	public int insertSelective(HubMaterialDic hubMaterialDic) {
		return hubMaterialDicMapper.insertSelective(hubMaterialDic);
	}

	public List<HubMaterialDic> selectByCriteriaWithRowbounds(
			HubMaterialDicCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubMaterialDicMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubMaterialDic> selectByCriteria(HubMaterialDicCriteria criteria) {
		return hubMaterialDicMapper.selectByExample(criteria);
	}

	public HubMaterialDic selectByPrimaryKey(Long materialDicId) {
		return hubMaterialDicMapper.selectByPrimaryKey(materialDicId);
	}

	public int updateByCriteriaSelective(HubMaterialDicWithCriteria hubMaterialDicWithCriteria) {
		return hubMaterialDicMapper.updateByExampleSelective(hubMaterialDicWithCriteria.getHubMaterialDic(), hubMaterialDicWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubMaterialDicWithCriteria hubMaterialDicWithCriteria) {
		return hubMaterialDicMapper.updateByExample(hubMaterialDicWithCriteria.getHubMaterialDic(), hubMaterialDicWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubMaterialDic hubMaterialDic) {
		return hubMaterialDicMapper.updateByPrimaryKeySelective(hubMaterialDic);
	}

	public int updateByPrimaryKey(HubMaterialDic hubMaterialDic) {
		return hubMaterialDicMapper.updateByPrimaryKey(hubMaterialDic);
	}
}
