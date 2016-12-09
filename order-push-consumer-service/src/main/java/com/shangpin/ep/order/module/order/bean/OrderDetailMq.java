package com.shangpin.ep.order.module.order.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by lizhongren on 2016/10/24.
 */
@Getter
@Setter
@ToString
public class OrderDetailMq {

    private String messageId;
    /**
     * 父ID
     */
    private String parentMessageId;
    /**
     * 消息发生的时间
     */
    private String messageDate;

    /**
     * 同步业务类型：下单:CreateOrder , 取消：CancelOrder, 支付：PayedOrder,重采购：RePurchaseSupplierOrder,退款：RefundedOrder ,发货: Shipped
     */
    private String syncType;

    private String supplierNo;
    private String spMasterOrderNo;
    private String skuNo;
    private int quantity;
    private String supplierOrderNo;//子订单编号（支付后使用该字段）
    private String purchaseOrderNo;//采购单号（支付后使用该字段）
    private String  originalSupplierOrderNo;//原子订单号 重采使用
    private String orderNo;//推送给供货商的订单编号

}
