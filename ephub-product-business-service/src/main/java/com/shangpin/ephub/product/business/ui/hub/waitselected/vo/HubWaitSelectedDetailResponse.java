package com.shangpin.ephub.product.business.ui.hub.waitselected.vo;

import java.io.Serializable;
import java.util.List;

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
public class HubWaitSelectedDetailResponse implements Serializable {
	private static final long serialVersionUID = -9080013205013160520L;
	private Long skuSupplierMappingId;
	private String supplierNo;
	private String spuNo;
	private String skuNo;
	private Long skuId;
	private Long spuId;
    private String spuName;
    private String brandNo;
    private String barcode;
    private String categoryNo;
    private String supplierSkuNo;
    private String spuModel;
    private String hubColor;
    private String material;
    private String origin;
    private String gender;
    private String skuSize;
    
    private String price;
    private String currency;
    private List<String> picUrl;
   
}