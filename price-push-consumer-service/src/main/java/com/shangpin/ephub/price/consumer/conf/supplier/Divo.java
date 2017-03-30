package com.shangpin.ephub.price.consumer.conf.supplier;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>Title:Divo.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午6:49:45
 */
@Setter
@Getter
@ToString
public class Divo extends SupplierCommon implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5862918252305636028L;
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
