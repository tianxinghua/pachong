package com.shangpin.api.airshop.dto;

import java.io.Serializable;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateDeliveryPurOrderReqDTO implements Serializable {

	private static final long serialVersionUID = -3791601775787757110L;
	/**
	 * 系统用户编码
	 */
	private String sopUserNo;
	/**
	 * 物流公司
	 */
	
	private String logisticsName;
	/**
	 * 物流单号
	 */
	private String logisticsOrderNo;
	/**
	 * 发货时间 以北京时间为准； 北京时间=格林威治时间+8小时； 格式：2015-01-17 14:00:00 否
	 */
	private String dateDeliver;
	private String sopPurchaseOrderDetailNo;

}
