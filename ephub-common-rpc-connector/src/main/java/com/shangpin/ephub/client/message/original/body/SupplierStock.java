package com.shangpin.ephub.client.message.original.body;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Title: SupplierStock</p>
 * <p>Description: 供应商原始库存信息 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月24日 下午3:29:59
 *
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierStock {
	
	/**
	 * 消息ID
	 */
	private String messageId;
	/**
	 * 供应商门户id
	 */
	private String supplierId;
	/**
	 * 供应商原始sku编号
	 */
	private String supplierSkuNo;
	/**
	 * 供应商库存
	 */
	private int stock;

}
