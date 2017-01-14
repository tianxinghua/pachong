package com.shangpin.ephub.product.business.rest.hubpending.spu.result;

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
public class HubSizeCheckResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8315497351473540682L;
	
	/**
	 * 
	 */
	private boolean isPassing;
    /**
     * 校验不通过记录原因，通过为空
     */
    private String result;

}
