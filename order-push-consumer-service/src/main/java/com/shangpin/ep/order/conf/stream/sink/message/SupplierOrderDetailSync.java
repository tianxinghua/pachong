/**
 * @项目名称: scm-message-service
 * @文件名称: SupplierOrderDetailSync.java
 * @Date: 2016年10月25日
 * @Copyright: 2016 www.shangpin.com Inc. All rights reserved.
 * 注意：本内容仅限于xxx公司内部传阅，禁止外泄以及用于其他的商业目的.
*/
package com.shangpin.ep.order.conf.stream.sink.message;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Title:SupplierOrderDetailSync.java </p>
 * <p>Description: 订单详情消息体</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月10日 上午11:07:36
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierOrderDetailSync implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7487369015353784852L;
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
	/**
	 * 推送给供货商的订单编号
	 */
	private String orderNo;
}
