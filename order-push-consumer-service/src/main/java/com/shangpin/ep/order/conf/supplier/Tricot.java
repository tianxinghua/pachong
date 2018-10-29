package com.shangpin.ep.order.conf.supplier;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>Title:Tricot.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author myw
 * @date 2018年10月25日 上午10:40:46
 */
@Getter
@Setter
@ToString
public class Tricot extends SupplierCommon implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1457426529028382904L;
	private String url;
	private String createOrderInterface;
	private String setStatusInterface;
	private String getStatusInterface;
	private String getItemStockInterface;
	private String user;
	private String password;
	

}
