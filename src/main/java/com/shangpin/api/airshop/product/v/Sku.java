package com.shangpin.api.airshop.product.v;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>Title:SkuVO </p>
 * <p>Description: </p>
 * <p>Company: shangpin</p> 
 * @author : yanxiaobin
 * @date :2016年4月22日 下午3:59:16
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Sku implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4718449123955994677L;
	/**
	 * 尺码类别
	 */
	private String sizeClass;
	/**
	 * 尺码
	 */
	private String size;
	
	/**
	 * 供货商提供的sku编号
	 */
	private Long supplierSkuId;
	private String skuId;
	private String status;  //1:unsubmitted   2：editing  3：submitted  4:delete
	private String shangpinSKU;
	private String barcode;//条形码
	private String marketPrice;
	private String supplyPrice;
	private String currency;
}
