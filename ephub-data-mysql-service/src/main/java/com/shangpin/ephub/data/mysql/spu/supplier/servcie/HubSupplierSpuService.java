package com.shangpin.ephub.data.mysql.spu.supplier.servcie;



import com.shangpin.ephub.data.mysql.spu.supplier.bean.HubSupplierSpuCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.spu.supplier.bean.HubSupplierSpuWithCriteria;
import com.shangpin.ephub.data.mysql.spu.supplier.mapper.HubSupplierSpuMapper;
import com.shangpin.ephub.data.mysql.spu.supplier.po.HubSupplierSpu;
import com.shangpin.ephub.data.mysql.spu.supplier.po.HubSupplierSpuCriteria;
import com.shangpin.ephub.data.mysql.spu.supplier.po.HubSupplierSpuQureyDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
  HubSupplierSpu ����
 */
@Service
@Slf4j
public class HubSupplierSpuService {

	@Autowired
	private HubSupplierSpuMapper mapper;

	public int countByCriteria(HubSupplierSpuCriteria criteria) {
		return mapper.countByExample(criteria);
	}

	public int deleteByCriteria(HubSupplierSpuCriteria criteria) {
		return mapper.deleteByExample(criteria);
	}

	public int deleteByPrimaryKey(Long skuPendingId) {
		return mapper.deleteByPrimaryKey(skuPendingId);
	}

	public int insert(HubSupplierSpu obj) {
		return mapper.insert(obj);
	}

	public int insertSelective(HubSupplierSpu obj) {
		return mapper.insertSelective(obj);
	}

	public List<HubSupplierSpu> selectByCriteriaWithRowbounds(HubSupplierSpuCriteriaWithRowBounds criteriaWithRowBounds) {
		return mapper.selectByExampleWithRowbounds(criteriaWithRowBounds.getCriteria(), criteriaWithRowBounds.getRowBounds());
	}

	public List<HubSupplierSpu> selectByCriteria(HubSupplierSpuCriteria criteria) {
		return mapper.selectByExample(criteria);
	}

	public HubSupplierSpu selectByPrimaryKey(Long id) {
		return mapper.selectByPrimaryKey(id);
	}

	public int updateByCriteriaSelective(HubSupplierSpuWithCriteria criteria) {
		return mapper.updateByExampleSelective(criteria.getHubSupplierSpu(), criteria.getCriteria());
	}

	public int updateByCriteria(HubSupplierSpuWithCriteria criteria) {
		return mapper.updateByExample(criteria.getHubSupplierSpu(), criteria.getCriteria());
	}

	public int updateByPrimaryKeySelective(HubSupplierSpu obj) {
		return mapper.updateByPrimaryKeySelective(obj);
	}

	public int updateByPrimaryKey(HubSupplierSpu obj) {
		return mapper.updateByPrimaryKey(obj);
	}

	public List<HubSupplierSpu> selectByBrand(HubSupplierSpuQureyDto dto) {
		return mapper.selectByBrand(dto);
	}

	public int count(HubSupplierSpuQureyDto dto) {
		return mapper.count(dto);
	}

}
