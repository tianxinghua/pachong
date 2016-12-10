package com.shangpin.ep.order.conf.supplier;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>Title:PapiniConf.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午7:47:38
 */
@Setter
@Getter
@ToString
public class PapiniConf extends SupplierCommon implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1271346520047761575L;
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
	private String dBContext;
	/**
	 * 
	 */
	private String key;
	

}
