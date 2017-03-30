package com.shangpin.ephub.price.consumer.conf.supplier;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>Title:EfashionConf.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午7:04:00
 */
@Setter
@Getter
@ToString
public class EfashionConf extends SupplierCommon implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4581578565651618776L;
	/**
	 * 
	 */
	private String url;
	/**
	 * 
	 */
	private String cancelUrl;
	/**
	 * 
	 */
	private String placeUrl;

}
