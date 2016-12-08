package com.shangpin.supplier.product.consumer.supplier.common.atelier.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtelierPrice {

	private String price1;//2 从0开始第2列 一般作为供价
	private String price2;//3 从0开始第3列 一般是这个作为市场价
	private String price3;//4 从0开始第4列
	private String price4;//5 ...
	private String price5;//6
	private String price6;//7
}
