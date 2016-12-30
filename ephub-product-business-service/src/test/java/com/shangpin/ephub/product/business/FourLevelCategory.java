package com.shangpin.ephub.product.business;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FourLevelCategory {

	@JsonProperty("FirstNo")
	private String firstNo;
	@JsonProperty("FirstName")
	private String firstName ;
	@JsonProperty("SecondNo")
	private String secondNo;
	@JsonProperty("SecondName")
	private String secondName;
	@JsonProperty("ThirdNo")
	private String thirdNo;
	@JsonProperty("ThirdName")
	private String thirdName;
	@JsonProperty("FourthNo")
	private String fourthNo;
	@JsonProperty("FourthName")
	private String fourthName;
	@JsonProperty("SizeTmpNo")
	private String sizeTmpNo;
}
