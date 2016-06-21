package com.shangpin.iog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CsvSupplierInfoDTO {

	private String supplierId;
	private String supplierNo;
	private String crontime;
	private String fetchUrl;
	private String state;//started,waitStart
	private String classPath;
	private String dataType;
	private String filePath;
}
