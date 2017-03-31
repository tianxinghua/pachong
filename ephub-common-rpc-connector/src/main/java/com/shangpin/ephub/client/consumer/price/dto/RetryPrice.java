package com.shangpin.ephub.client.consumer.price.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
/**
 * <p>Title: RetryPrice</p>
 * <p>Description: 价格消息体 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年3月30日 上午11:17:23
 *
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
