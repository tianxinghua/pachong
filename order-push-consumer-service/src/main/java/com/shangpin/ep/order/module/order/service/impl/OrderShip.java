package com.shangpin.ep.order.module.order.service.impl;

import com.shangpin.ep.order.module.order.bean.OrderMq;
import com.shangpin.ep.order.module.order.service.IHubOrderService;
import com.shangpin.ep.order.module.orderapiservice.impl.OrderHandleSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lizhongren on 2016/11/20.
 */
@Component
public class OrderShip {

    @Autowired
    OrderHandleSearch orderHandleSearch;

    @Autowired
    IHubOrderService hubOrderService;

    @Autowired
    OrderCommonUtil  orderCommonUtil;




    public   void handleRePurchasePayOrder(OrderMq orderMq) {


    }

}
