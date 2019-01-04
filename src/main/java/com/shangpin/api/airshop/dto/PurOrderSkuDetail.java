package com.shangpin.api.airshop.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PurOrderSkuDetail implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1606542399478033490L;

	private String skuNo;
	private String productModel;
	private String supplierSkuNo;
	private String barCode;
	private String detailStatus;
	private String isStock;

	private String skuPrice;
	private String skuPriceCurrency;
	private String brandNo; //品牌编号
	private String brandName; //品牌
	private String size; //尺码
	private String qty; //数量
	
	private String picUrl;
	private String sopDeliverOrderNo;
	
	private String productName;
}
