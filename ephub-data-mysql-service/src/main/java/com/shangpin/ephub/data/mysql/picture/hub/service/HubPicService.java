package com.shangpin.ephub.data.mysql.picture.hub.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.picture.hub.bean.HubPicCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.picture.hub.bean.HubPicWithCriteria;
import com.shangpin.ephub.data.mysql.picture.hub.mapper.HubPicMapper;
import com.shangpin.ephub.data.mysql.picture.hub.po.HubPic;
import com.shangpin.ephub.data.mysql.picture.hub.po.HubPicCriteria;

/**
 * <p>Title:HubPicService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:50:03
 */
@Service
public class HubPicService {

	@Autowired
	private HubPicMapper hubPicMapper;

	public int countByCriteria(HubPicCriteria criteria) {
		return hubPicMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubPicCriteria criteria) {
		return hubPicMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long picId) {
		return hubPicMapper.deleteByPrimaryKey(picId);
	}

	public int insert(HubPic hubPic) {
		return hubPicMapper.insert(hubPic);
	}

	public int insertSelective(HubPic hubPic) {
		return hubPicMapper.insertSelective(hubPic);
	}

	public List<HubPic> selectByCriteriaWithRowbounds(HubPicCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubPicMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubPic> selectByCriteria(HubPicCriteria criteria) {
		return hubPicMapper.selectByExample(criteria);
	}

	public HubPic selectByPrimaryKey(Long picId) {
		return hubPicMapper.selectByPrimaryKey(picId);
	}

	public int updateByCriteriaSelective(HubPicWithCriteria hubPicWithCriteria) {
		return hubPicMapper.updateByExampleSelective(hubPicWithCriteria.getHubPic(), hubPicWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubPicWithCriteria hubPicWithCriteria) {
		return hubPicMapper.updateByExample(hubPicWithCriteria.getHubPic(), hubPicWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubPic hubPic) {
		return hubPicMapper.updateByPrimaryKeySelective(hubPic);
	}

	public int updateByPrimaryKey(HubPic hubPic) {
		return hubPicMapper.updateByPrimaryKey(hubPic);
	}
}
