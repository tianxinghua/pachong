package com.shangpin.ep.order.conf.supplier;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title: Bagheera</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年9月5日 下午4:57:55
 *
 */
@Getter
@Setter
public class Bagheera extends SupplierCommon implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3228410686458791145L;
	
	private String url;
	private String createOrderInterface;
	private String setStatusInterface;
	private String getStatusInterface;
	private String getItemStockInterface;
	private String user;
	private String password;

}
