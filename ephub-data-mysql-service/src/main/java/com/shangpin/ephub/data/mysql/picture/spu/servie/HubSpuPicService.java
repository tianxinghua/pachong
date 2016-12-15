package com.shangpin.ephub.data.mysql.picture.spu.servie;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.picture.spu.bean.HubSpuPicCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.picture.spu.bean.HubSpuPicWithCriteria;
import com.shangpin.ephub.data.mysql.picture.spu.mapper.HubSpuPicMapper;
import com.shangpin.ephub.data.mysql.picture.spu.po.HubSpuPic;
import com.shangpin.ephub.data.mysql.picture.spu.po.HubSpuPicCriteria;

/**
 * <p>Title:HubSpuPicService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:54:30
 */
@Service
public class HubSpuPicService {

	@Autowired
	private HubSpuPicMapper hubSpuPicMapper;

	public int countByCriteria(HubSpuPicCriteria criteria) {
		return hubSpuPicMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubSpuPicCriteria criteria) {
		return hubSpuPicMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long spuPicId) {
		return hubSpuPicMapper.deleteByPrimaryKey(spuPicId);
	}

	public int insert(HubSpuPic hubSpuPic) {
		return hubSpuPicMapper.insert(hubSpuPic);
	}

	public int insertSelective(HubSpuPic hubSpuPic) {
		return hubSpuPicMapper.insertSelective(hubSpuPic);
	}

	public List<HubSpuPic> selectByCriteriaWithRowbounds(HubSpuPicCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubSpuPicMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubSpuPic> selectByCriteria(HubSpuPicCriteria criteria) {
		return hubSpuPicMapper.selectByExample(criteria);
	}

	public HubSpuPic selectByPrimaryKey(Long spuPicId) {
		return hubSpuPicMapper.selectByPrimaryKey(spuPicId);
	}

	public int updateByCriteriaSelective(HubSpuPicWithCriteria hubSpuPicWithCriteria) {
		return hubSpuPicMapper.updateByExampleSelective(hubSpuPicWithCriteria.getHubSpuPic(), hubSpuPicWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubSpuPicWithCriteria hubSpuPicWithCriteria) {
		return hubSpuPicMapper.updateByExample(hubSpuPicWithCriteria.getHubSpuPic(), hubSpuPicWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubSpuPic hubSpuPic) {
		return hubSpuPicMapper.updateByPrimaryKeySelective(hubSpuPic);
	}

	public int updateByPrimaryKey(HubSpuPic hubSpuPic) {
		return hubSpuPicMapper.updateByPrimaryKey(hubSpuPic);
	}
}
