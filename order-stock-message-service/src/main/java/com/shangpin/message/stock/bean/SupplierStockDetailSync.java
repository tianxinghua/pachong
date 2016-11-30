package com.shangpin.message.stock.bean;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString 
public class SupplierStockDetailSync implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2938315127189057588L;
	/**
	 * 供应商编号
	 */
	private String supplierNo;
	/**
	 * SKU编号
	 */
	private String skuNo;
}