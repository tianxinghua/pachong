package com.shangpin.supplier.product.message.original.conf.message.ostore;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title:AtelierDTO.java </p>
 * <p>Description: </p>
 * @author zhaogenchun
 * @date 2016年12月8日 下午21:09:53
 */
@Getter
@Setter
public class AtelierDTO {
	
	private String spuId;
	private String spu;
	private List<String> sku;
	private List<String> image;
	private String price;

}
