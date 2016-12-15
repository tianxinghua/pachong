package com.shangpin.ephub.data.mysql.dictionary.season.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.dictionary.season.bean.HubSeasonDicCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.dictionary.season.bean.HubSeasonDicWithCriteria;
import com.shangpin.ephub.data.mysql.dictionary.season.mapper.HubSeasonDicMapper;
import com.shangpin.ephub.data.mysql.dictionary.season.po.HubSeasonDic;
import com.shangpin.ephub.data.mysql.dictionary.season.po.HubSeasonDicCriteria;

/**
 * <p>Title:HubSeasonDicService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:36:33
 */
@Service
public class HubSeasonDicService {

	@Autowired
	private HubSeasonDicMapper hubSeasonDicMapper;

	public int countByCriteria(HubSeasonDicCriteria criteria) {
		return hubSeasonDicMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubSeasonDicCriteria criteria) {
		return hubSeasonDicMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long seasonDicId) {
		return hubSeasonDicMapper.deleteByPrimaryKey(seasonDicId);
	}

	public int insert(HubSeasonDic hubSeasonDic) {
		return hubSeasonDicMapper.insert(hubSeasonDic);
	}

	public int insertSelective(HubSeasonDic hubSeasonDic) {
		return hubSeasonDicMapper.insertSelective(hubSeasonDic);
	}

	public List<HubSeasonDic> selectByCriteriaWithRowbounds(HubSeasonDicCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubSeasonDicMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubSeasonDic> selectByCriteria(HubSeasonDicCriteria criteria) {
		return hubSeasonDicMapper.selectByExample(criteria);
	}

	public HubSeasonDic selectByPrimaryKey(Long seasonDicId) {
		return hubSeasonDicMapper.selectByPrimaryKey(seasonDicId);
	}

	public int updateByCriteriaSelective(HubSeasonDicWithCriteria hubSeasonDicWithCriteria) {
		return hubSeasonDicMapper.updateByExampleSelective(hubSeasonDicWithCriteria.getHubSeasonDic(), hubSeasonDicWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubSeasonDicWithCriteria hubSeasonDicWithCriteria) {
		return hubSeasonDicMapper.updateByExample(hubSeasonDicWithCriteria.getHubSeasonDic(), hubSeasonDicWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubSeasonDic hubSeasonDic) {
		return hubSeasonDicMapper.updateByPrimaryKeySelective(hubSeasonDic);
	}

	public int updateByPrimaryKey(HubSeasonDic hubSeasonDic) {
		return hubSeasonDicMapper.updateByPrimaryKey(hubSeasonDic);
	}
}
