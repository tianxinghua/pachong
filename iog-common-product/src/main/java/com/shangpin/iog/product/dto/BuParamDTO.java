package com.shangpin.iog.product.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuParamDTO {

	private List<String> categories;
	private String isExport_cat;
	private List<String> brands;
	private String isExport_brand;
	private List<String> genders;
	private String isExport_gender;
}
