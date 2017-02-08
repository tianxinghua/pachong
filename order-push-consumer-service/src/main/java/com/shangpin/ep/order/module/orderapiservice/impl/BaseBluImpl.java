package com.shangpin.ep.order.module.orderapiservice.impl;

import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import org.springframework.stereotype.Component;

/**
 * Created by lizhongren on 2017/2/8.
 */
@Component("baseBluServiceImpl")
public class BaseBluImpl implements IOrderService {
    @Override
    public void handleSupplierOrder(OrderDTO orderDTO) {

    }

    @Override
    public void handleConfirmOrder(OrderDTO orderDTO) {

    }

    @Override
    public void handleCancelOrder(OrderDTO deleteOrder) {

    }

    @Override
    public void handleRefundlOrder(OrderDTO deleteOrder) {

    }
}
