package com.shangpin.ephub.client.message.pending.body.sku;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>Title:PendingSku.java </p>
 * <p>Description: 消息体中的待处理sku</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月12日 下午4:22:39
 */
@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class PendingSku extends HubSkuPendingDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1666563055692203604L;

}
