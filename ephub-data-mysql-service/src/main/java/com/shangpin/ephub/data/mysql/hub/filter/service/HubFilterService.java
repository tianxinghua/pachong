package com.shangpin.ephub.data.mysql.hub.filter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.hub.filter.bean.HubFilterRequest;
import com.shangpin.ephub.data.mysql.hub.filter.mapper.HubFilterMapper;
import com.shangpin.ephub.data.mysql.hub.filter.po.HubFilterResponse;

/**
 * <p>HubWaitSelectGateWay.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月28日 下午16:52:02
 */
@Service
public class HubFilterService {

	@Autowired
	private HubFilterMapper hubSkuMapper;
	public List<HubFilterResponse> selectHubBrandByHubCategory(HubFilterRequest request) {
		return hubSkuMapper.selectHubBrandByHubCategory(request);
	}

}
