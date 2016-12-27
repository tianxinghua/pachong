package com.shangpin.ephub.product.business.ui.hub.waitselected.vo;

import java.io.Serializable;

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
public class HubWaitSelectedResponseDto implements Serializable {
	private static final long serialVersionUID = -9080013205013160520L;
    /**
     * 供应商
     */
    private String url;
    private String brandName;
    private String productCode;
    private String categoryName;
    private String color;
    private String material;
    private String madeIn;
    private String gender;
    private String supplierName;
    private String size;
    private byte productState;
    private String updateDate;
    private String updateUser;

}