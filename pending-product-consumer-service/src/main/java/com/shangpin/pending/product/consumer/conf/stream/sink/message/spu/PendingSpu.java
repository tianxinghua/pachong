package com.shangpin.pending.product.consumer.conf.stream.sink.message.spu;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.pending.product.consumer.conf.clients.mysql.spu.bean.HubSpuPending;
import com.shangpin.pending.product.consumer.conf.stream.sink.message.sku.PendingSku;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Title:PendingSpu.java </p>
 * <p>Description: 消息体中的待处理spu</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月12日 下午4:23:10
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PendingSpu extends HubSpuPending {

	/**
	 * 
	 */
	private static final long serialVersionUID = 813944688977632297L;

	private List<PendingSku> skus;
}
