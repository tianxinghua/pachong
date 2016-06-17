package com.shangpin.iog.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class CsvSupplierInfoDTO {

	private String supplierId;
	private String supplierNo;
	private String crontime;
	private String fetchUrl;
	private String state;//started,waitStart
}
