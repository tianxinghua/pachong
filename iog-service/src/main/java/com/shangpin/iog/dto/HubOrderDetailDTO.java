package com.shangpin.iog.dto;


import java.math.BigInteger;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
/**
 * 子订单
 */
public class HubOrderDetailDTO {
	private BigInteger id;
	private String uuid;
	private String supplierOrderNo;//供应商订单标号
    private String supplierId; //供货商门户编号
    private String supplierNo;//供货商编号
    private String supplierName;//供货商编号
    private String spMasterOrderNo;     //尚品主订单号
    private String epMasterOrderNo;    //ep主订单号
    private String spOrderDetailNo;    //尚品拆单后子订单号
    private String orderNo;    //EP订单号(EP拆单后的订单号）
    private String spPurchaseNo;
    private String spPurchaseDetailNo;
    private String spSku;
    private String supplierSku;
    private String quantity;
    private String purchasePriceDetail;
    private String orderStatus;
    private String pushStatus;
    private String deliveryNo;
    private Date createTime;
    private Date lockTime;
    private Date payTime;
    private Date confirmTime;
    private Date refundTime;
    private String excState;  //异常标记 发生错误 赋值为 1
    private String excDesc;  //错误描述
    private Date excTime;
}
