package com.shangpin.ep.order.conf.supplier;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>Title:Leam.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午7:25:46
 */
@Getter
@Setter
@ToString
public class Leam extends SupplierCommon implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1457426529028882901L;
	private String url;
	private String createOrderInterface;
	private String setStatusInterface;
	private String getStatusInterface;
	private String getItemStockInterface;
	private String user;
	private String password;
	

}
