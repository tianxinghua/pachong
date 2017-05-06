package com.shangpin.ephub.data.mysql.price.unionselect.po;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HubSupplierPrice {

	/**
	 * 供货商门户编号
	 */
	private String supplierId;
	/**
	 * 主键
	 */
	private String supplierSkuId;
	/**
	 * 供应商的sku no
	 */
	private String supplierSkuNo;
	/**
	 * 供应商季节名称
	 */
	private String supplierSeasonName;
	/**
	 * 尚品上市时间
	 */
	private String spSeasonYear;
	/**
	 * 尚品上市季节
	 */
	private String spSeasonName;
	/**
	 * 供应商市场价
	 */
	private String markPrice;
	/**
	 * 尚品sku id
	 */
	private String spSkuId;
	/**
	 * 供应商品类
	 */
	private String categoryName;
	/**
	 * 供应商品牌
	 */
	private String brandName;
}
