package com.shangpin.ephub.product.business.rest.hubpending.sku.dto;

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
public class HubPendingSkuDto implements Serializable {
	private String skuSize;
	private String marketPrice;
	private String marketCurrency;
	private static final long serialVersionUID = 2215182249308660796L;
}
