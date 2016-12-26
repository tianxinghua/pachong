package com.shangpin.ephub.product.business.rest.hubproduct.dto;

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
public class HubProductDto implements Serializable {
	private String supplierNo;
	private String categoryNo;
	private String brandNo;
	private String spuModel;
	private String gender;
	private String hubColor;
	private String season;
	private String marketTime;
	private String skuSize;
	private String material;
	private String origin;
	private String marketPrice;
	private String marketCurrency;
	private static final long serialVersionUID = 2215182249308660796L;
}
