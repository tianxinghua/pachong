package com.shangpin.ep.order.conf.supplier;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title: JulianFashion</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年9月27日 下午4:43:51
 *
 */
@Getter
@Setter
public class JulianFashion extends SupplierCommon implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4565126763251667574L;

	private String url;
	private String createOrderInterface;
	private String setStatusInterface;
	private String getStatusInterface;
	private String getItemStockInterface;
	private String user;
	private String password;
}
