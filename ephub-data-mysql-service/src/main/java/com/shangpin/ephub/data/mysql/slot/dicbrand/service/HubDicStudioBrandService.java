package com.shangpin.ephub.data.mysql.slot.dicbrand.service;

import com.shangpin.ephub.data.mysql.slot.dicbrand.bean.HubDicStudioBrandCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.slot.dicbrand.bean.HubDicStudioBrandWithCriteria;
import com.shangpin.ephub.data.mysql.slot.dicbrand.mapper.HubDicStudioBrandMapper;
import com.shangpin.ephub.data.mysql.slot.dicbrand.po.HubDicStudioBrand;
import com.shangpin.ephub.data.mysql.slot.dicbrand.po.HubDicStudioBrandCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>Title:HubDicStudioBrandService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p>
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:09:16
 */
@Service
public class HubDicStudioBrandService {

	@Autowired
	private HubDicStudioBrandMapper hubDicStudioBrandMapper;

	public int countByCriteria(HubDicStudioBrandCriteria criteria) {
		return hubDicStudioBrandMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubDicStudioBrandCriteria criteria) {
		return hubDicStudioBrandMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long skuId) {
		return hubDicStudioBrandMapper.deleteByPrimaryKey(skuId);
	}

	public int   insert(HubDicStudioBrand hubDicStudioBrand) {
		return hubDicStudioBrandMapper.insert(hubDicStudioBrand);
	}


	public int   insertSelective(HubDicStudioBrand hubDicStudioBrand) {
		return   hubDicStudioBrandMapper.insertSelective(hubDicStudioBrand);

	}

	public List<HubDicStudioBrand> selectByCriteriaWithRowbounds(HubDicStudioBrandCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubDicStudioBrandMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubDicStudioBrand> selectByCriteria(HubDicStudioBrandCriteria criteria) {
		return hubDicStudioBrandMapper.selectByExample(criteria);
	}

	public HubDicStudioBrand selectByPrimaryKey(Long skuId) {
		return hubDicStudioBrandMapper.selectByPrimaryKey(skuId);
	}

	public int updateByCriteriaSelective(HubDicStudioBrandWithCriteria HubDicStudioBrandWithCriteria) {
		return hubDicStudioBrandMapper.updateByExampleSelective(HubDicStudioBrandWithCriteria.getHubDicStudioBrand(), HubDicStudioBrandWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubDicStudioBrandWithCriteria HubDicStudioBrandWithCriteria) {
		return hubDicStudioBrandMapper.updateByExample(HubDicStudioBrandWithCriteria.getHubDicStudioBrand(), HubDicStudioBrandWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubDicStudioBrand HubDicStudioBrand) {
		return hubDicStudioBrandMapper.updateByPrimaryKeySelective(HubDicStudioBrand);
	}

	public int updateByPrimaryKey(HubDicStudioBrand HubDicStudioBrand) {
		return hubDicStudioBrandMapper.updateByPrimaryKey(HubDicStudioBrand);
	}
}
