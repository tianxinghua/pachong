package com.shangpin.api.airshop.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class DeliveryOrderDetailResDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1149409833533984187L;
	private String deliveryPurOrderNo;
	private String taskBatchNo;

}
