package com.shangpin.iog.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SpecialSkuDTO {

	private String supplierId;
	private String supplierSkuId;
	private Date createTime = new Date();
}
