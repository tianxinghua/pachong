package com.shangpin.ep.order.conf.supplier;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>Title:Brunarosso.java </p>
 * <p>Description: Brunarosso供应商配置</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午5:45:15
 */
@Setter
@Getter
@ToString
public class Vietti extends SupplierCommon implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 913972800909461549L;
	/**
	 * 
	 */
	private String url;
	/**
	 * 
	 */
	private String createOrderInterface;
	/**
	 * 
	 */
	private String setStatusInterface;
	/**
	 * 
	 */
	private String getStatusInterface;
	/**
	 * 
	 */
	private String getItemStockInterface;
	/**
	 * 
	 */
	private String user;
	/**
	 * 
	 */
	private String password;
	/**
	 * 
	 */
	private String messageType;
	


}
