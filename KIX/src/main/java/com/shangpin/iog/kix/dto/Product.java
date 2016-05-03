
package com.shangpin.iog.kix.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Product {
	private String id;
	private String title;//商品名称
	private String vendor;// brandName
	private String product_type;//categoryName
	private Variant[] variants;//sku 
	private Image[] images;
	private String tags;
}
