package com.shangpin.ephub.data.mysql.dictionary.color.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.dictionary.color.bean.HubColorDicCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.dictionary.color.bean.HubColorDicWithCriteria;
import com.shangpin.ephub.data.mysql.dictionary.color.mapper.HubColorDicMapper;
import com.shangpin.ephub.data.mysql.dictionary.color.po.HubColorDic;
import com.shangpin.ephub.data.mysql.dictionary.color.po.HubColorDicCriteria;

/**
 * <p>Title:HubColorDicService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:24:17
 */
@Service
public class HubColorDicService {

	@Autowired
	private HubColorDicMapper hubColorDicMapper;

	public int countByCriteria(HubColorDicCriteria criteria) {
		return hubColorDicMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubColorDicCriteria criteria) {
		return hubColorDicMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long colorDicId) {
		return hubColorDicMapper.deleteByPrimaryKey(colorDicId);
	}

	public int insert(HubColorDic hubColorDic) {
		return hubColorDicMapper.insert(hubColorDic);
	}

	public int insertSelective(HubColorDic hubColorDic) {
		return hubColorDicMapper.insertSelective(hubColorDic);
	}

	public List<HubColorDic> selectByCriteriaWithRowbounds(HubColorDicCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubColorDicMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubColorDic> selectByCriteria(HubColorDicCriteria criteria) {
		return hubColorDicMapper.selectByExample(criteria);
	}

	public HubColorDic selectByPrimaryKey(Long colorDicId) {
		return hubColorDicMapper.selectByPrimaryKey(colorDicId);
	}

	public int updateByCriteriaSelective(HubColorDicWithCriteria hubColorDicWithCriteria) {
		return hubColorDicMapper.updateByExampleSelective(hubColorDicWithCriteria.getHubColorDic(), hubColorDicWithCriteria.getHubColorDicCriteria());
	}

	public int updateByCriteria(HubColorDicWithCriteria hubColorDicWithCriteria) {
		return hubColorDicMapper.updateByExample(hubColorDicWithCriteria.getHubColorDic(), hubColorDicWithCriteria.getHubColorDicCriteria());
	}

	public int updateByPrimaryKeySelective(HubColorDic hubColorDic) {
		return hubColorDicMapper.updateByPrimaryKeySelective(hubColorDic);
	}

	public int updateByPrimaryKey(HubColorDic hubColorDic) {
		return hubColorDicMapper.updateByPrimaryKey(hubColorDic);
	}
}
