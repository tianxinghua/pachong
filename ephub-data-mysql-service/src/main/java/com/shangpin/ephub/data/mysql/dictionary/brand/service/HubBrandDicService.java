package com.shangpin.ephub.data.mysql.dictionary.brand.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.dictionary.brand.bean.HubBrandDicCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.dictionary.brand.bean.HubBrandDicWithCriteria;
import com.shangpin.ephub.data.mysql.dictionary.brand.mapper.HubBrandDicMapper;
import com.shangpin.ephub.data.mysql.dictionary.brand.po.HubBrandDic;
import com.shangpin.ephub.data.mysql.dictionary.brand.po.HubBrandDicCriteria;

/**
 * <p>Title:HubBrandDicService.java </p>
 * <p>Description: EPHUB品牌字典数据访问层逻辑处理</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午2:46:25
 */
@Service
public class HubBrandDicService {

	@Autowired
	private HubBrandDicMapper hubBrandDicMapper;
	
	public int countByCriteria(HubBrandDicCriteria criteria) {
		return hubBrandDicMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubBrandDicCriteria criteria) {
		return hubBrandDicMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long brandDicId) {
		return hubBrandDicMapper.deleteByPrimaryKey(brandDicId);
	}

	public int insert(HubBrandDic hubBrandDic) {
		return hubBrandDicMapper.insert(hubBrandDic);
	}

	public int insertSelective(HubBrandDic hubBrandDic) {
		return hubBrandDicMapper.insertSelective(hubBrandDic);
	}

	public List<HubBrandDic> selectByCriteriaWithRowbounds(HubBrandDicCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubBrandDicMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubBrandDic> selectByCriteria(HubBrandDicCriteria criteria) {
		return hubBrandDicMapper.selectByExample(criteria);
	}

	public HubBrandDic selectByPrimaryKey(Long brandDicId) {
		return hubBrandDicMapper.selectByPrimaryKey(brandDicId);
	}

	public int updateByCriteriaSelective(HubBrandDicWithCriteria hubBrandDicWithCriteria) {
		return hubBrandDicMapper.updateByExampleSelective(hubBrandDicWithCriteria.getHubBrandDic(), hubBrandDicWithCriteria.getHubBrandDicCriteria());
	}

	public int updateByCriteria(HubBrandDicWithCriteria hubBrandDicWithCriteria) {
		return hubBrandDicMapper.updateByExample(hubBrandDicWithCriteria.getHubBrandDic(), hubBrandDicWithCriteria.getHubBrandDicCriteria());
	}

	public int updateByPrimaryKeySelective(HubBrandDic hubBrandDic) {
		return hubBrandDicMapper.updateByPrimaryKeySelective(hubBrandDic);
	}

	public int updateByPrimaryKey(HubBrandDic hubBrandDic) {
		return hubBrandDicMapper.updateByPrimaryKey(hubBrandDic);
	}
}
