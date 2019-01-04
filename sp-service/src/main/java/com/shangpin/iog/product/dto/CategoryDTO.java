package com.shangpin.iog.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {

	private String supplierCategoryName;//供应商品类名称
	private String firCategory;//一级品类
	private String secCategory;//二级品类
	private String thirCategory;
	private String fortCategory;
	private String categoryId;//商品品类编码
	private String gender;
	private String color;
	private String brand;
	
}
