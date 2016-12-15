package com.shangpin.supplier.product.consumer.service.dto;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title:Sku </p>
 * <p>Description: 推送Pending消息头实体类</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月14日 下午8:53:26
 *
 */
@Getter
@Setter
public class Sku {

	private String supplierId;
	private String skuNo;
	private int status;
}
