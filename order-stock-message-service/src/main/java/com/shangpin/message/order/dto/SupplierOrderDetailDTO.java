/**
 * @项目名称: scm-message-service
 * @文件名称: SupplierOrderDetailSyncDTO.java
 * @Date: 2016年10月25日
 * @Copyright: 2016 www.shangpin.com Inc. All rights reserved.
 * 注意：本内容仅限于xxx公司内部传阅，禁止外泄以及用于其他的商业目的.
*/
package com.shangpin.message.order.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *@Project: scm-message-service
 *@Author: yanxiaobin
 *@Date: 2016年10月25日
 *@Copyright: 2016 www.shangpin.com Inc. All rights reserved.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierOrderDetailDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1309650639379469916L;
	/**
	 * 供应商编号
	 */
	private String supplierNo;
	/**
	 * SKU编号
	 */
	private String skuNo;
	/**
	 * 订购数量
	 */
	private Integer quantity;
	/**
	 * 子订单编号（支付后使用该字段）
	 */
	private String supplierOrderNo;
	/**
	 * 采购单号（支付后使用该字段）
	 */
	private String purchaseOrderNo;
	/**
	 * 原商户订单号
 	 */
	private String  originalSupplierOrderNo;
}