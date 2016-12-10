package com.shangpin.ep.order.module.order.bean;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by sunny on 2015/9/19.
 */
@Getter
@Setter
public class ReturnOrderDTO {
    private BigInteger id;
    private String supplierId;
    private String supplierNo;
    private String uuId;  //和供货商公用的订单唯一标识
    private String spOrderId;//尚品退单编号
    private String supplierOrderNo;//供货商的订单编号
    private String spPurchaseNo; //采购单编号
    private String detail;//订单明细  供货商skuId:数量,
    private String memo;//   订单明细  尚品skuId:数量,
    private String status;//订单状态
    private String deliveryNo;
    private Date createTime;
    private Date updateTime;
    private String excState;
    private String excDesc;
    private Date excTime;


    @Override
    public String toString() {
        return "ReturnOrderDTO{" +
                "id=" + id +
                ", supplierId='" + supplierId + '\'' +
                ", supplierNo='" + supplierNo + '\'' +
                ", uuId='" + uuId + '\'' +
                ", spOrderId='" + spOrderId + '\'' +
                ", spPurchaseNo='" + spPurchaseNo + '\'' +
                ", detail='" + detail + '\'' +
                ", memo='" + memo + '\'' +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                ", excState='" + excState + '\'' +
                ", excDesc='" + excDesc + '\'' +
                ", excTime=" + excTime +
                '}';
    }
}
