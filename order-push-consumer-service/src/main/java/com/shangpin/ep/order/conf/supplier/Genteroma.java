package com.shangpin.ep.order.conf.supplier;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>Title:Genteroma.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午7:10:18
 */
@Getter
@Setter
@ToString
public class Genteroma extends SupplierCommon implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2284855968817180587L;
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
	


}
