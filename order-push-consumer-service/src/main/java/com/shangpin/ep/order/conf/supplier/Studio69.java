package com.shangpin.ep.order.conf.supplier;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>Title:Studio69.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午8:33:43
 */
@Setter
@Getter
@ToString
public class Studio69 extends SupplierCommon implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6315141058938609396L;
	/**
	 * 
	 */
	private String url;
	/**
	 * 
	 */
	private String username;
	/**
	 * 
	 */
	private String password;
	/**
	 * 
	 */
	private String wsdlUrl;
	

}
