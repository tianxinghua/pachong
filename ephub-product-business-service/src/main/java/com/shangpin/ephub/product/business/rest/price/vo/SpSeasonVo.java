package com.shangpin.ephub.product.business.rest.price.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpSeasonVo {

	private String supplierId;//供货商门户编号
	private String supplierSeasonName; //必须
	private String spSeasonYear;
	private String spSeasonName; //必须
	private String markPrice;//
	private String spSkuId;//
	private String categoryName;//
	private String brandName;//
	private String currency;
}
