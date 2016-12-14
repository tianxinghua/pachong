package com.shangpin.ephub.data.mysql.spu.supplier.servcie;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.spu.supplier.bean.HubSupplierSpuCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.spu.supplier.bean.HubSupplierSpuWithCriteria;
import com.shangpin.ephub.data.mysql.spu.supplier.mapper.HubSupplierSpuMapper;
import com.shangpin.ephub.data.mysql.spu.supplier.po.HubSupplierSpu;
import com.shangpin.ephub.data.mysql.spu.supplier.po.HubSupplierSpuCriteria;

/**
 * <p>Title:HubSupplierSpuService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:21:05
 */
@Service
public class HubSupplierSpuService {

	@Autowired
	private HubSupplierSpuMapper hubSupplierSpuMapper;

	public int countByCriteria(HubSupplierSpuCriteria criteria) {
		return hubSupplierSpuMapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubSupplierSpuCriteria criteria) {
		return hubSupplierSpuMapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long supplierSpuId) {
		return hubSupplierSpuMapper.deleteByPrimaryKey(supplierSpuId);
	}

	public int insert(HubSupplierSpu hubSupplierSpu) {
		return hubSupplierSpuMapper.insert(hubSupplierSpu);
	}

	public int insertSelective(HubSupplierSpu hubSupplierSpu) {
		return hubSupplierSpuMapper.insertSelective(hubSupplierSpu);
	}

	public List<HubSupplierSpu> selectByCriteriaWithRowbounds(
			HubSupplierSpuCriteriaWithRowBounds criteriaWithRowBounds) {
		return hubSupplierSpuMapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubSupplierSpu> selectByCriteria(HubSupplierSpuCriteria criteria) {
		return hubSupplierSpuMapper.selectByExample(criteria);
	}

	public HubSupplierSpu selectByPrimaryKey(Long supplierSpuId) {
		return hubSupplierSpuMapper.selectByPrimaryKey(supplierSpuId);
	}

	public int updateByCriteriaSelective(HubSupplierSpuWithCriteria hubSupplierSpuWithCriteria) {
		return hubSupplierSpuMapper.updateByExampleSelective(hubSupplierSpuWithCriteria.getHubSupplierSpu(), hubSupplierSpuWithCriteria.getCriteria());
	}

	public int updateByCriteria(HubSupplierSpuWithCriteria hubSupplierSpuWithCriteria) {
		return hubSupplierSpuMapper.updateByExample(hubSupplierSpuWithCriteria.getHubSupplierSpu(), hubSupplierSpuWithCriteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubSupplierSpu hubSupplierSpu) {
		return hubSupplierSpuMapper.updateByPrimaryKeySelective(hubSupplierSpu);
	}

	public int updateByPrimaryKey(HubSupplierSpu hubSupplierSpu) {
		return hubSupplierSpuMapper.updateByPrimaryKey(hubSupplierSpu);
	}
}
