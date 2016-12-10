package com.shangpin.ep.order.conf.supplier;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>Title:PozzileiForte.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午7:58:07
 */
@Getter
@Setter
@ToString
public class PozzileiForte extends SupplierCommon implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4338823869976846967L;
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
