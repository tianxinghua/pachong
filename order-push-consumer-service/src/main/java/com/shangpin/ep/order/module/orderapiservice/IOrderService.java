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
     //锁库  下单了，但是我没付钱
      public void handleSupplierOrder(OrderDTO orderDTO) ;

    /**
     * 订单从下单到支付后的处理
     * @param orderDTO  订单信息
     */
     //已支付，调用供应商下单接口 进行下单
     public void handleConfirmOrder(OrderDTO orderDTO);

    /**
     * 取消订单 （未支付）
     * @throws
     */
    //下单了，但是没付钱，取消锁库状态
    public void handleCancelOrder(OrderDTO deleteOrder) ;

    /**
     * 退款
     * @param deleteOrder
     */
      //已支付，退单，调用对方退单接口
      public void handleRefundlOrder(OrderDTO deleteOrder) ;
}
