package com.shangpin.ephub.price.consumer.conf.stream.source.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

/**

 * <p>商品价格信息: </p>

 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RetryPrice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8329268266608806631L;
	String sopUserNo; //门户编号
	String skuNo;     //尚品的SKU编号
	String supplierSkuNo;//供货商skuNo
	String marketPrice;//市场价
	String purchasePrice;//采购价
	String createUserName;
	String marketYear;
	String marketSeason;
	String currency;
	String memo;
}
