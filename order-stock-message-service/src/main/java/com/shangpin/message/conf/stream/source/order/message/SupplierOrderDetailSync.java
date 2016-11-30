package com.shangpin.message.conf.stream.source.order.message;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *@Project: scm-message-service
 *@Author: yanxiaobin
 *@Date: 2016年10月25日
 *@Copyright: 2016 www.shangpin.com Inc. All rights reserved.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString 
public class SupplierOrderDetailSync implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7487369015353784852L;
	/**
	 * 供应商编号
	 */
	private String supplierNo;
	/**
	 * SKU编号
	 */
	private String skuNo;
	/**
	 * 订购数量
	 */
	private Integer quantity;
	/**
	 * 子订单编号（支付后使用该字段）
	 */
	private String supplierOrderNo;
	/**
	 * 采购单号（支付后使用该字段）
	 */
	private String purchaseOrderNo;
	/**
	 * 原商户订单号
 	 */
	private String  originalSupplierOrderNo;
	/**
	 * 推送给供货商的订单编号
	 */
	private String orderNo;
}