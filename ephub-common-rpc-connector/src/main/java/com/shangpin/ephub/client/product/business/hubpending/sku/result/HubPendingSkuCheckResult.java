package com.shangpin.ephub.client.product.business.hubpending.sku.result;

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
public class HubPendingSkuCheckResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2215182249308660796L;

	/**
	 * 
	 */
	private boolean isPassing;
    /**
     * 校验不通过记录原因，通过为空
     */
    private String result;
}
