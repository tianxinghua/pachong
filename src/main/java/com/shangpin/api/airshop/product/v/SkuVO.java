package com.shangpin.api.airshop.product.v;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class SkuVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7519420045841729005L;
	private String image;
	private String itemName;
	private String brand;
	private String itemCode;
	private String shangpinSKU;
	private BigDecimal price;
	private Integer editStatus;
	private Integer shelveStatus;
	private String updateTime;
}
