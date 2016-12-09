/**
 * @项目名称: scm-message-service
 * @文件名称: SupplierOrderSync.java
 * @Date: 2016年10月25日
 * @Copyright: 2016 www.shangpin.com Inc. All rights reserved.
 * 注意：本内容仅限于xxx公司内部传阅，禁止外泄以及用于其他的商业目的.
*/
package com.shangpin.ep.order.conf.stream.sink.message;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Title:SupplierOrderSync.java </p>
 * <p>Description: 订单消息体</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月10日 上午11:08:04
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString 
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierOrderSync implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5748665008598006260L;
	/**
	 * 消息唯一标识
	 */
	private String messageId;
	/**
	 * 父ID
	 */
	private String parentMessageId;
	/**
	 * 消息发生的时间
	 */
	private String messageDate;
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
	private List<SupplierOrderDetailSync> syncDetailDto;
	
}
