package com.shangpin.ephub.product.business.ui.airshop.product.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductDetailSkuResponseDTO {
	/**
	 * 尺码类别
	 */
	private String sizeClass;
	/**
	 * 尺码
	 */
	private String size;
	
	/**
	 * 供货商提供的sku编号
	 */
	private Long supplierSkuId;
	private String skuId;
	private String status;  //1:unsubmitted   2：editing  3：submitted  4:delete
	private String shangpinSKU;
	private String barcode;//条形码
	private String marketPrice;
	private String supplyPrice;
	private String currency;
}
