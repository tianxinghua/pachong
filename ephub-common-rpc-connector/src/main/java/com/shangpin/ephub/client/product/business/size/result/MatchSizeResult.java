package com.shangpin.ephub.client.product.business.size.result;

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
public class MatchSizeResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6139278703106906369L;
	/**
	 * 校验是否通过
	 */
	private boolean isPassing;
	/**
	 * 加工处理后校验通过的品牌型号:如果校验通过，则为通过的品牌型号，如果校验不通过，则该字段为null.
	 */
	private String result;
}
