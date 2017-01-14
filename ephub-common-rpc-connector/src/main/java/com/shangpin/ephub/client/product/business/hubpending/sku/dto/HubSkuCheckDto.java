package com.shangpin.ephub.client.product.business.hubpending.sku.dto;

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
public class HubSkuCheckDto implements Serializable {
	
	private String categoryNo;
	private String brandNo;
	private String spuModel;
	private String specificationType;
	private String sizeType;
	private String skuSize;
	
	private static final long serialVersionUID = 2215182249308660796L;
}
