package com.shangpin.supplier.product.consumer.supplier.common.atelier.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtelierDate {

	private String spuId;
	private String spu;
	private List<String> sku;
	private List<String> image;
	private String price;
}
