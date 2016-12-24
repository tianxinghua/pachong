package com.shangpin.ephub.product.business.rest.hubproduct.result;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

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
public class HubProductCheckResult implements Serializable {

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
