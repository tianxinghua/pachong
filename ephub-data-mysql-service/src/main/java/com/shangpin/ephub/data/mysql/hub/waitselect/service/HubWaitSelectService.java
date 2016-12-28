package com.shangpin.ephub.data.mysql.hub.waitselect.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.hub.waitselect.bean.HubWaitSelectRequest;
import com.shangpin.ephub.data.mysql.hub.waitselect.bean.HubWaitSelectRequestWithPage;
import com.shangpin.ephub.data.mysql.hub.waitselect.mapper.HubWaitSelectMapper;
import com.shangpin.ephub.data.mysql.hub.waitselect.po.HubWaitSelectResponse;

/**
 * <p>HubWaitSelectGateWay.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月28日 下午16:52:02
 */
@Service
public class HubWaitSelectService {

	@Autowired
	private HubWaitSelectMapper hubSkuMapper;

	public int count(HubWaitSelectRequest criteria) {
		return hubSkuMapper.count(criteria);
	}

	public List<HubWaitSelectResponse> selectList(HubWaitSelectRequestWithPage criteriaWithRowBounds) {
		return hubSkuMapper.selectList(criteriaWithRowBounds);
	}
}
