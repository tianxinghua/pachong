package com.shangpin.ephub.client.message.pending.body.spu;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.message.pending.body.sku.PendingSku;

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
public class PendingSpu extends HubSpuPendingDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 813944688977632297L;

	private List<PendingSku> skus;
}
