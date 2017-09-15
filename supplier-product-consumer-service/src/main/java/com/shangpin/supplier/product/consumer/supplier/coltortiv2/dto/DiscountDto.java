package com.shangpin.supplier.product.consumer.supplier.coltortiv2.dto;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title: DiscountDto</p>
 * <p>Description: 折扣信息 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年9月11日 下午3:46:01
 *
 */
@Getter
@Setter
public class DiscountDto {

	private String retail_price;
	private String your_price;
	private String rate;
	private String updated_at;
}
