package com.shangpin.pending.product.consumer.conf.stream.sink.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.pending.product.consumer.conf.stream.sink.message.spu.PendingSpu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Title:PendingProduct.java </p>
 * <p>Description: 接收到的待处理商品消息体</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月12日 下午4:09:09
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PendingProduct {
	/**
	 * 消息ID
	 */
	private String messageId;
	/**
	 * 消息发生的时间 Dateime
	 */
	private String messageDate;
	/**
	 * 供货商ID
	 */
	private String supplierId;
	/**
	 * 供货商名称
	 */
	private String supplierName;
	
	private PendingSpu data;
}
