package com.shangpin.ep.order.conf.supplier;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>Title:VelaParam.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午8:49:31
 */
@Setter
@Getter
@ToString
public class VelaParam extends SupplierCommon implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -812709648971391364L;
	/**
	 * 
	 */
	private String setOrderUrl;
	/**
	 * 
	 */
	private String queryOrderUrl;
	/**
	 * 
	 */
	private String cancelUrl;
	/**
	 * 
	 */
	private String qtyUrl;
	/**
	 * 
	 */
	private String dBContext;
	/**
	 * 
	 */
	private String key;

	

}
