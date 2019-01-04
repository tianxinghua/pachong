package com.shangpin.api.airshop.dto;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class TMSTask {
	 @JsonProperty("OpUser")
	private String OpUser;
	 @JsonProperty("SupplierOrderNoList")
	private List<String> SupplierOrderNoList;

}
