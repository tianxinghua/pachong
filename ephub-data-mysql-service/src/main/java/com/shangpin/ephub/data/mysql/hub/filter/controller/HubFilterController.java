package com.shangpin.ephub.data.mysql.hub.filter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.hub.filter.bean.HubFilterRequest;
import com.shangpin.ephub.data.mysql.hub.filter.po.HubFilterResponse;
import com.shangpin.ephub.data.mysql.hub.filter.service.HubFilterService;
/**
 * <p>HubWaitSelectGateWay.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月28日 下午16:52:02
 */
@RestController
@RequestMapping("/hub-filter")
public class HubFilterController {

	@Autowired
	private HubFilterService hubSkuService;
	@RequestMapping(value = "/select")
    public List<HubFilterResponse> selectByCriteria(@RequestBody HubFilterRequest request){
    	return hubSkuService.selectHubBrandByHubCategory(request);
    }
	
}
