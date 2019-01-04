package com.shangpin.iog.picture.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Supplier {

	private String supplierId;
	private String ftpUrl;
	private String port;
	private String userName;
	private String password;
	private String remote;
	private String startDate;
	private String endDate;
}
