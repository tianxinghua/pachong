package com.shangpin.message.conf.stream.source.order.message;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document
public class SupplierOrderSync implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5748665008598006260L;
	/**
	 * 消息唯一标识
	 */
	@Id
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
	 * 同步业务类型：下单:CreateOrder , 取消：CancelOrder, 支付：PayedOrder
	 */
	private String syncType;
	/**
	 * 订单明细
	 */
	private List<SupplierOrderDetailSync> syncDetailDto;
	
}