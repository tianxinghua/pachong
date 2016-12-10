package com.shangpin.ep.order.module.order.bean;

import java.util.Date;

import com.shangpin.ep.order.enumeration.ErrorStatus;
import com.shangpin.ep.order.enumeration.OrderBusinessOperateType;
import com.shangpin.ep.order.enumeration.OrderBusinessType;
import com.shangpin.ep.order.enumeration.OrderStatus;
import com.shangpin.ep.order.enumeration.PushStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 */
@Getter
@Setter
@ToString

/**
 * 订单状态
 *
 状态请参考  com.shangpin.iog.ice.dto.OrderStatus
 */
public class OrderDTO {
    private Long id;
    private String supplierId; //供货商门户编号
    private String supplierName;//供货商名称
    private String supplierNo;//供货商编号
    private String uuid;  //和供货商公用的订单唯一标识
    private String spOrderId;    //尚品订单编号
    private String spMasterOrderNo;//尚品主订单编号
    private String spOrderDetailNo;//尚品订单明细号
    private String supplierOrderNo;//供货商的订单编号
    private String purchaseNo; //采购单编号
    private String spPurchaseDetailNo;  //采购单明细
    private String detail;//    订单明细  供货商skuId:数量,
    private String status;//订单状态
    private String memo;//订单明细  尚品skuId:数量,
    private String deliveryNo;//发货单
    private Date createTime;
    private Date updateTime;



    private String purchasePriceDetail; //采购价明细
    private String consumerMsg;//购买人信息

    private OrderStatus orderStatus;//订单状态
    private PushStatus pushStatus;//推送状态
    private Date lockStockTime;//推送时间（锁库存时间）
    private Date confirmTime;// 确认支付后的时间
    private Date payTime;//支付时间
    private Date shipTime;//发货时间
    private Date cancelTime;//取消时间
    private Date refundTime;//退款时间
    private ErrorStatus errorType;//异常类型
    private String description;  //错误描述
    private String spSkuNo;
    private String supplierSkuNo;
    private int quantity;


    private String logContent;//日志内容
    private OrderStatus exceptionOrderStatus;//异常时订单状态
    private PushStatus exceptionPushStatus;//异常时推送状态


    private OrderBusinessType businessType;//业务类型
    private OrderBusinessOperateType businessOperate;// 业务操作
    private String messageId;//消息唯一标示
    private String parentMessageId;
    private String messageDate; //发生的时间


}
