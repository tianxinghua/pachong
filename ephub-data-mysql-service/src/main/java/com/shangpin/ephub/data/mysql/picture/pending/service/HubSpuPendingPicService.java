package com.shangpin.ephub.data.mysql.picture.pending.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.picture.pending.bean.HubSpuPendingPicCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.picture.pending.bean.HubSpuPendingPicWithCriteria;
import com.shangpin.ephub.data.mysql.picture.pending.mapper.HubSpuPendingPicMapper;
import com.shangpin.ephub.data.mysql.picture.pending.po.HubSpuPendingPic;
import com.shangpin.ephub.data.mysql.picture.pending.po.HubSpuPendingPicCriteria;

/**
 * <p>Title:HubSpuPendingPicService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:52:45
 */
@Service
public class HubSpuPendingPicService {

	@Autowired
	private HubSpuPendingPicMapper hubSpuPendingPicMapper;

	public int countByCriteria(HubSpuPendingPicCriteria criteria) {
		return hubSpuPendingPicMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubSpuPendingPicCriteria criteria) {
		return hubSpuPendingPicMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long spuPicId) {
		return hubSpuPendingPicMapper.deleteByPrimaryKey(spuPicId);
	}

	public int insert(HubSpuPendingPic hubSpuPendingPic) {
		return hubSpuPendingPicMapper.insert(hubSpuPendingPic);
	}

	public int insertSelective(HubSpuPendingPic hubSpuPendingPic) {
		return hubSpuPendingPicMapper.insertSelective(hubSpuPendingPic);
	}

	public List<HubSpuPendingPic> selectByCriteriaWithRowbounds(
			HubSpuPendingPicCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubSpuPendingPicMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubSpuPendingPic> selectByCriteria(HubSpuPendingPicCriteria criteria) {
		return hubSpuPendingPicMapper.selectByExample(criteria);
	}

	public HubSpuPendingPic selectByPrimaryKey(Long spuPicId) {
		return hubSpuPendingPicMapper.selectByPrimaryKey(spuPicId);
	}

	public int updateByCriteriaSelective(HubSpuPendingPicWithCriteria hubSpuPendingPicWithCriteria) {
		return hubSpuPendingPicMapper.updateByExampleSelective(hubSpuPendingPicWithCriteria.getHubSpuPendingPic(), hubSpuPendingPicWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubSpuPendingPicWithCriteria hubSpuPendingPicWithCriteria) {
		return hubSpuPendingPicMapper.updateByExample(hubSpuPendingPicWithCriteria.getHubSpuPendingPic(), hubSpuPendingPicWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubSpuPendingPic hubSpuPendingPic) {
		return hubSpuPendingPicMapper.updateByPrimaryKeySelective(hubSpuPendingPic);
	}

	public int updateByPrimaryKey(HubSpuPendingPic hubSpuPendingPic) {
		return hubSpuPendingPicMapper.updateByPrimaryKey(hubSpuPendingPic);
	}
}
