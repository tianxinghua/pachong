package com.shangpin.ephub.data.mysql.dictionary.origin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.dictionary.origin.bean.HubOriginDicCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.dictionary.origin.bean.HubOriginDicWithCriteria;
import com.shangpin.ephub.data.mysql.dictionary.origin.mapper.HubOriginDicMapper;
import com.shangpin.ephub.data.mysql.dictionary.origin.po.HubOriginDic;
import com.shangpin.ephub.data.mysql.dictionary.origin.po.HubOriginDicCriteria;

/**
 * <p>Title:HubOriginDicService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月22日 下午12:25:32
 */
@Service
public class HubOriginDicService {

	@Autowired
	private HubOriginDicMapper hubOriginDicMapper;

	public int countByCriteria(HubOriginDicCriteria criteria) {
		return hubOriginDicMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubOriginDicCriteria criteria) {
		return hubOriginDicMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long originDicId) {
		return hubOriginDicMapper.deleteByPrimaryKey(originDicId);
	}

	public int insert(HubOriginDic hubOriginDic) {
		return hubOriginDicMapper.insert(hubOriginDic);
	}

	public int insertSelective(HubOriginDic hubSeasonDic) {
		return hubOriginDicMapper.insertSelective(hubSeasonDic);
	}

	public List<HubOriginDic> selectByCriteriaWithRowbounds(HubOriginDicCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubOriginDicMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubOriginDic> selectByCriteria(HubOriginDicCriteria criteria) {
		return hubOriginDicMapper.selectByExample(criteria);
	}

	public HubOriginDic selectByPrimaryKey(Long originDicId) {
		return hubOriginDicMapper.selectByPrimaryKey(originDicId);
	}

	public int updateByCriteriaSelective(HubOriginDicWithCriteria hubOriginDicWithCriteria) {
		return hubOriginDicMapper.updateByExampleSelective(hubOriginDicWithCriteria.getHubOriginDic(), hubOriginDicWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubOriginDicWithCriteria hubOriginDicWithCriteria) {
		return hubOriginDicMapper.updateByExample(hubOriginDicWithCriteria.getHubOriginDic(), hubOriginDicWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubOriginDic hubOriginDic) {
		return hubOriginDicMapper.updateByPrimaryKeySelective(hubOriginDic);
	}

	public int updateByPrimaryKey(HubOriginDic hubOriginDic) {
		return hubOriginDicMapper.updateByPrimaryKey(hubOriginDic);
	}
}
