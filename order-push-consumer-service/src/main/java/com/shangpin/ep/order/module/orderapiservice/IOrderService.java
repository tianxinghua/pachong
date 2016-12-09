package com.shangpin.ep.order.module.orderapiservice;

import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.order.bean.ReturnOrderDTO;


/**
 * Created by lizhongren on 2016/11/18.
 */
public interface IOrderService {

    /**
     * 处理供货商订单信息
     * @param orderDTO
     * 订单状态需求参是 orderDTO 里的状态
     * @throws
     */
      public void handleSupplierOrder(OrderDTO orderDTO) ;

    /**
     * 订单从下单到支付后的处理
     * @param orderDTO  订单信息
     */
     public void handleConfirmOrder(OrderDTO orderDTO);

    /**
     * 取消订单 （未支付）
     * @throws
     */
      public void handleCancelOrder(OrderDTO deleteOrder) ;

    /**
     * 退款
     * @param deleteOrder
     */
      public void handleRefundlOrder(OrderDTO deleteOrder) ;
}
