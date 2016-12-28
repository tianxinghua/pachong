package com.shangpin.ephub.product.business.ui.hub.waitselected.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubWaitSelectedResponse implements Serializable {
	private static final long serialVersionUID = -9080013205013160520L;
	private String skuSupplierMappingId;
	private String supplierNo;
	private String skuNo;
    private String url;
    private String brandNo;
    private String spuModel;
    private String categoryNo;
    private String hubColor;
    private String material;
    private String origin;
    private String gender;
    private String skuSize;
    private byte spuState;
    private Date updateTime;

}