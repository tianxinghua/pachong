package com.shangpin.ephub.data.mysql.spu.hub.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.spu.hub.bean.HubSpuCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.spu.hub.bean.HubSpuWithCriteria;
import com.shangpin.ephub.data.mysql.spu.hub.mapper.HubSpuMapper;
import com.shangpin.ephub.data.mysql.spu.hub.po.HubSpu;
import com.shangpin.ephub.data.mysql.spu.hub.po.HubSpuCriteria;

/**
 * <p>Title:HubSpuService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:17:25
 */
@Service
public class HubSpuService {

	@Autowired
	private HubSpuMapper hubSpuMapper;

	public int countByCriteria(HubSpuCriteria criteria) {
		return hubSpuMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubSpuCriteria criteria) {
		return hubSpuMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long spuId) {
		return hubSpuMapper.deleteByPrimaryKey(spuId);
	}

	public int insert(HubSpu hubSpu) {
		return hubSpuMapper.insert(hubSpu);
	}

	public int insertSelective(HubSpu hubSpu) {
		return hubSpuMapper.insertSelective(hubSpu);
	}

	public List<HubSpu> selectByCriteriaWithRowbounds(HubSpuCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubSpuMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubSpu> selectByCriteria(HubSpuCriteria criteria) {
		return hubSpuMapper.selectByExample(criteria);
	}

	public HubSpu selectByPrimaryKey(Long spuId) {
		return hubSpuMapper.selectByPrimaryKey(spuId);
	}

	public int updateByCriteriaSelective(HubSpuWithCriteria hubSpuWithCriteria) {
		return hubSpuMapper.updateByExampleSelective(hubSpuWithCriteria.getHubSpu(), hubSpuWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubSpuWithCriteria hubSpuWithCriteria) {
		return hubSpuMapper.updateByExample(hubSpuWithCriteria.getHubSpu(), hubSpuWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubSpu hubSpu) {
		return hubSpuMapper.updateByPrimaryKeySelective(hubSpu);
	}

	public int updateByPrimaryKey(HubSpu hubSpu) {
		return hubSpuMapper.updateByPrimaryKey(hubSpu);
	}
}
