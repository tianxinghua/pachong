package com.shangpin.ep.order.conf.supplier;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>Title:Stefania.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午8:21:38
 */
@Setter
@Getter
@ToString
public class Stefania extends SupplierCommon implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4168933008467718747L;
	/**
	 * 
	 */
	private String authKey;
	/**
	 * 
	 */
	private String channel;
	/**
	 * 
	 */
	private String jssecacerts;
	

}
