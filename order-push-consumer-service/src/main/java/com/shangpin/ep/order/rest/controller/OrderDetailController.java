package com.shangpin.ep.order.rest.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ep.order.module.order.bean.HubOrderDetail;
import com.shangpin.ep.order.module.order.bean.SkuSearchResult;
import com.shangpin.ep.order.module.order.service.IHubOrderService;
import com.shangpin.ep.order.module.order.service.impl.OrderCommonUtil;
import com.shangpin.ep.order.module.orderapiservice.impl.OrderHandleSearch;

/**
 * Created by lizhongren on 2016/11/18.
 * 获取尚未发货的订单数量
 */
@RestController
@RequestMapping("/orderDetail")
@Slf4j
public class OrderDetailController {
    @Autowired
    IHubOrderService hubOrderService;

    @Autowired
    OrderHandleSearch orderHandleSearch;

    @Autowired
    OrderCommonUtil orderCommonUtil;

    @RequestMapping(value="/getOrderNoByMasterNo", method = RequestMethod.POST)
    public String getOrderCountBySpSkuNo(@RequestParam(value = "supplierId")  String  supplierId,@RequestParam(value = "masterOrderNo")  String  masterOrderNo
    		,@RequestParam(value = "spSkuNo")  String  spSkuNo){
    	
    	List<HubOrderDetail> hubOrderDetails = hubOrderService.getOrderDetailBySupplierIdAndSpMasterOrderNoAndSpSkuNo(supplierId, masterOrderNo,spSkuNo);
    	if(hubOrderDetails!=null&&hubOrderDetails.size()>0){
    		return hubOrderDetails.get(0).getOrderNo();
    	}
        return null;
    }



}
