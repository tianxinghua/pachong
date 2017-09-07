package com.shangpin.ephub.data.mysql.hub.waitselect.controller;

import java.util.ArrayList;
import java.util.Arrays;
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

import lombok.extern.slf4j.Slf4j;
/**
 * <p>HubWaitSelectGateWay.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月28日 下午16:52:02
 */
@RestController
@RequestMapping("/hub-waitselect")
@Slf4j
public class HubWaitSelectController {

	@Autowired
	private HubWaitSelectService hubSkuService;
	
	@RequestMapping(value = "/count")
    public Long countByCriteria(@RequestBody HubWaitSelectRequest criteria){
		List<String> spSkuNoList = new ArrayList<String>();
		if(criteria.getSpSkuNo()!=null){
			spSkuNoList = Arrays.asList(criteria.getSpSkuNo().split(","));	
			criteria.setSpSkuNoList(spSkuNoList);
		}
    	return hubSkuService.count(criteria);
    }
	
	@RequestMapping(value = "/select-with-page")
    public List<HubWaitSelectResponse> selectByCriteriaWithRowbounds(@RequestBody HubWaitSelectRequestWithPage criteriaWithRowBounds){
		List<String> spSkuNoList = new ArrayList<String>();
		if(criteriaWithRowBounds.getSpSkuNo()!=null){
			spSkuNoList = Arrays.asList(criteriaWithRowBounds.getSpSkuNo().split(","));	
			criteriaWithRowBounds.setSpSkuNoList(spSkuNoList);
		}
    	return hubSkuService.selectList(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-detail")
    public List<HubWaitSelectResponse> selectDetail(@RequestBody HubWaitSelectDetailRequest criteriaWithRowBounds){
		log.info("待选品详情参数：{}",criteriaWithRowBounds);
		List<HubWaitSelectResponse> list = hubSkuService.selectDetail(criteriaWithRowBounds);
		log.info("待选品详情返回结果：{}",list);
    	return list;
    }
	
}
