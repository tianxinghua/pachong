package com.shangpin.ephub.data.mysql.hub.waitselect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.hub.waitselect.bean.HubWaitSelectDetailRequest;
import com.shangpin.ephub.data.mysql.hub.waitselect.bean.HubWaitSelectRequest;
import com.shangpin.ephub.data.mysql.hub.waitselect.bean.HubWaitSelectRequestWithPage;
import com.shangpin.ephub.data.mysql.hub.waitselect.po.HubWaitSelectResponse;
import com.shangpin.ephub.data.mysql.hub.waitselect.service.HubWaitSelectService;
/**
 * <p>HubWaitSelectGateWay.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月28日 下午16:52:02
 */
@RestController
@RequestMapping("/hub-waitselect")
public class HubWaitSelectController {

	@Autowired
	private HubWaitSelectService hubSkuService;
	
	@RequestMapping(value = "/count")
    public Long countByCriteria(@RequestBody HubWaitSelectRequest criteria){
    	return hubSkuService.count(criteria);
    }
	
	@RequestMapping(value = "/select-with-page")
    public List<HubWaitSelectResponse> selectByCriteriaWithRowbounds(@RequestBody HubWaitSelectRequestWithPage criteriaWithRowBounds){
    	return hubSkuService.selectList(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-detail")
    public List<HubWaitSelectResponse> selectDetail(@RequestBody HubWaitSelectDetailRequest criteriaWithRowBounds){
    	return hubSkuService.selectDetail(criteriaWithRowBounds);
    }
	
}
