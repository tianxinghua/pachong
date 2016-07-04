package com.shangpin.iog.gibilogic.dto;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Category {
	@SerializedName("virtuemart_category_id")
	private String categoryId;
	@SerializedName("category_name")
	private String categoryName;
}
