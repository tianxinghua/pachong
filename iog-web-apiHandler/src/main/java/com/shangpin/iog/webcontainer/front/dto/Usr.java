package com.shangpin.iog.webcontainer.front.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Usr {

	private String supplierId;
	private String supplierNo;
	private String crontime;
	private String fetchUrl;
	private String state;//
	private String classPath;
	private String dataType;
	private String filePath;
	private String toMapCondition;
	private String sep;
	private String xmlSkuTag;
	private String xmlSpuTag;
	private String picFlag;//SKU or SPU
	private String picPath;
}
