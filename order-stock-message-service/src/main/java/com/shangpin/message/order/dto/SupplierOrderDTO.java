/**
 * @项目名称: scm-message-service
 * @文件名称: SupplierOrderSyncDTO.java
 * @Date: 2016年10月25日
 * @Copyright: 2016 www.shangpin.com Inc. All rights reserved.
 * 注意：本内容仅限于xxx公司内部传阅，禁止外泄以及用于其他的商业目的.
*/
package com.shangpin.message.order.dto;

import java.io.Serializable;
import java.util.List;

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
public class SupplierOrderDTO implements Serializable {

	/**
	 * 版本ID
	 */
	private static final long serialVersionUID = 4997328435882166072L;
	/**
	 * 消息唯一标识
	 */
	private String messageId;
	/**
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 同步业务类型：下单:CreateOrder , 取消：CancelOrder, 支付：PayedOrder,重采购：RePurchaseSupplierOrder,退款：RefundedOrder ,发货: Shipped
	 */
	private String syncType;
	/**
	 * 订单明细
	 */
	private List<SupplierOrderDetailDTO> syncDetailDto; 
}