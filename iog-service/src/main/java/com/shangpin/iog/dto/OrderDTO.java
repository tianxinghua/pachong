package com.shangpin.iog.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by huxia on 2015/9/10.
 */
@Getter
@Setter

/**
 * 订单状态
 *


 状态请参考  com.shangpin.iog.ice.dto.OrderStatus
 */
public class OrderDTO {
    private BigInteger id;
    private String supplierId; //供货商门户编号
    private String supplierName;//供货商名称
    private String supplierNo;//供货商编号
    private String uuId;  //和供货商公用的订单唯一标识
    private String spOrderId;    //尚品订单编号
    private String supplierOrderNo;//供货商的订单编号
    private String spPurchaseNo; //采购单编号
    private String spPurchaseDetailNo;  //采购单明细
    private String detail;//    订单明细  供货商skuId:数量,
    private String status;//订单状态
    private String memo;//订单明细  尚品skuId:数量,
    private String deliveryNo;//发货单
    private Date createTime;
    private Date updateTime;
    private String excState;  //异常标记 发生错误 赋值为 1
    private String excDesc;  //错误描述
    private Date excTime;
    private String purchasePriceDetail; //采购价明细
    private String consumerMsg;//购买人信息


    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + id +
                ", supplierId='" + supplierId + '\'' +
                ", supplierNo='" + supplierNo + '\'' +
                ", uuId='" + uuId + '\'' +
                ", spOrderId='" + spOrderId + '\'' +
                ", supplierOrderNo='" + supplierOrderNo + '\'' +
                ", spPurchaseNo='" + spPurchaseNo + '\'' +
                ", spPurchaseDetailNo='" + spPurchaseDetailNo + '\'' +
                ", detail='" + detail + '\'' +
                ", status='" + status + '\'' +
                ", memo='" + memo + '\'' +
                ", deliveryNo='" + deliveryNo + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", excState='" + excState + '\'' +
                ", excDesc='" + excDesc + '\'' +
                ", excTime=" + excTime +
                ", purchasePriceDetail='" + purchasePriceDetail + '\'' +
                ", consumerMsg='" + consumerMsg + '\'' +
                '}';
    }
}
