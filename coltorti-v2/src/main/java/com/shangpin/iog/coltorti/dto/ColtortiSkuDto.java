package com.shangpin.iog.coltorti.dto;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title: ColtortiSkuDto</p>
 * <p>Description: 自定义一个sku dto，用来转化为一个简单的实体 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年9月8日 下午12:11:25
 *
 */
@Getter
@Setter
public class ColtortiSkuDto {

	/**
	 * 尺码编号，比如3，5，7...
	 */
	private String sizeId;
	private String size;
	private Integer stock;
}
