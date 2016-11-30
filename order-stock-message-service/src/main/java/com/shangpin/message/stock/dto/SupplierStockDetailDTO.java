package com.shangpin.message.stock.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierStockDetailDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6300146595386994642L;
	/**
	 * 供应商编号
	 */
	private String supplierNo;
	/**
	 * SKU编号
	 */
	private String skuNo;
}