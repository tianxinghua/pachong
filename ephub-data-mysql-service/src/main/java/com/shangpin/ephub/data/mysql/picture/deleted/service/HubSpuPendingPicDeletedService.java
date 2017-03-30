package com.shangpin.ephub.data.mysql.picture.deleted.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.picture.deleted.bean.HubSpuPendingPicDeletedCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.picture.deleted.bean.HubSpuPendingPicDeletedWithCriteria;
import com.shangpin.ephub.data.mysql.picture.deleted.mapper.HubSpuPendingPicDeletedMapper;
import com.shangpin.ephub.data.mysql.picture.deleted.po.HubSpuPendingPicDeleted;
import com.shangpin.ephub.data.mysql.picture.deleted.po.HubSpuPendingPicDeletedCriteria;
/**
 * <p>Title:HubSpuPendingPicService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:52:45
 */
@Service
public class HubSpuPendingPicDeletedService {

	@Autowired
	private HubSpuPendingPicDeletedMapper hubSpuPendingPicDeletedMapper;

	public int countByCriteria(HubSpuPendingPicDeletedCriteria criteria) {
		return hubSpuPendingPicDeletedMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubSpuPendingPicDeletedCriteria criteria) {
		return hubSpuPendingPicDeletedMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long spuPicId) {
		return hubSpuPendingPicDeletedMapper.deleteByPrimaryKey(spuPicId);
	}

	public int insert(HubSpuPendingPicDeleted hubSpuPendingPicDeleted) {
		return hubSpuPendingPicDeletedMapper.insert(hubSpuPendingPicDeleted);
	}

	public int insertSelective(HubSpuPendingPicDeleted hubSpuPendingPicDeleted) {
		return hubSpuPendingPicDeletedMapper.insertSelective(hubSpuPendingPicDeleted);
	}

	public List<HubSpuPendingPicDeleted> selectByCriteriaWithRowbounds(
			HubSpuPendingPicDeletedCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubSpuPendingPicDeletedMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubSpuPendingPicDeleted> selectByCriteria(HubSpuPendingPicDeletedCriteria criteria) {
		return hubSpuPendingPicDeletedMapper.selectByExample(criteria);
	}

	public HubSpuPendingPicDeleted selectByPrimaryKey(Long spuPicId) {
		return hubSpuPendingPicDeletedMapper.selectByPrimaryKey(spuPicId);
	}

	public int updateByCriteriaSelective(HubSpuPendingPicDeletedWithCriteria hubSpuPendingPicDeletedWithCriteria) {
		return hubSpuPendingPicDeletedMapper.updateByExampleSelective(hubSpuPendingPicDeletedWithCriteria.getHubSpuPendingPic(), hubSpuPendingPicDeletedWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubSpuPendingPicDeletedWithCriteria hubSpuPendingPicDeletedWithCriteria) {
		return hubSpuPendingPicDeletedMapper.updateByExample(hubSpuPendingPicDeletedWithCriteria.getHubSpuPendingPic(), hubSpuPendingPicDeletedWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubSpuPendingPicDeleted hubSpuPendingPicDeleted) {
		return hubSpuPendingPicDeletedMapper.updateByPrimaryKeySelective(hubSpuPendingPicDeleted);
	}

	public int updateByPrimaryKey(HubSpuPendingPicDeleted hubSpuPendingPicDeleted) {
		return hubSpuPendingPicDeletedMapper.updateByPrimaryKey(hubSpuPendingPicDeleted);
	}
}
