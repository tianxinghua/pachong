package com.shangpin.ep.order.rest.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ep.order.module.order.bean.HubOrderDetail;
import com.shangpin.ep.order.module.order.service.IHubOrderService;
import com.shangpin.ep.order.module.order.service.impl.OrderCommonUtil;
import com.shangpin.ep.order.module.orderapiservice.impl.OrderHandleSearch;
import com.shangpin.ep.order.rest.controller.dto.OrderDetailRequestDto;

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
    public String getOrderCountBySpSkuNo(@RequestBody OrderDetailRequestDto orderDetail){
    	log.info("请求参数：{}",orderDetail);
    	List<HubOrderDetail> hubOrderDetails = hubOrderService.getOrderDetailBySupplierIdAndSpMasterOrderNoAndSpSkuNo(orderDetail.getSupplierId(), orderDetail.getMasterOrderNo(),orderDetail.getSpSkuNo());
    	if(hubOrderDetails!=null&&hubOrderDetails.size()>0){
    		return hubOrderDetails.get(0).getOrderNo();
    	}
        return null;
    }



}
