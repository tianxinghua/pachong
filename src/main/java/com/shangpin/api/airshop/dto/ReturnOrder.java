package com.shangpin.api.airshop.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 返货列表
 * Created by ZRS on 2016/1/14.
 */
@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReturnOrder implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sopPurchaseOrderNo;
	private String supplierSkuNo;
	private String returnTime;
	private String createTime;
	private String skuPrice;
	
	private String brandName;
	private String productName;
}
