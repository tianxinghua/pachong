package com.shangpin.ep.order.conf.supplier;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>Title:TonySub.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午8:44:48
 */
@Setter
@Getter
@ToString
public class TonySub extends SupplierCommon implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6773963195641075084L;
	/**
	 * 
	 */
	private String url;
	/**
	 * 
	 */
	private String key;
	/**
	 * 
	 */
	private Integer period;
	/**
	 * 
	 */
	private String merchantId;
	/**
	 * 
	 */
	private String token;





}
