package com.shangpin.ep.order.conf.supplier;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>Title:SanremoParam.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午8:06:50
 */
@Setter
@Getter
@ToString
public class SanremoParam extends SupplierCommon implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3652068151920830509L;
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
	private String key;
	private String dBContext;


}
