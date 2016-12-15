package com.shangpin.supplier.product.consumer.service.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title:Spu </p>
 * <p>Description: 推送Pending消息头实体类</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月14日 下午8:45:45
 *
 */
@Getter
@Setter
public class Spu {

	private String supplierId;
	private String spuNo;
	private int status;
	private List<Sku>	skus; 
}
