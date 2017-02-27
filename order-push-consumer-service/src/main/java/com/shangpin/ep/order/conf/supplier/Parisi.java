package com.shangpin.ep.order.conf.supplier;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>Title:PozzileiParam.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午8:02:41
 */
@Getter
@Setter
@ToString
public class Parisi extends SupplierCommon implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -562859012186115434L;

	/**
	 *  主站地址
	 */
	private String hostUrl;

	/**
	 * 下单soap 地址
	 */
	private String setOrderUrl;

	/**
	 * 
	 */
	private String cancelOrderUrl;


	private String  confirmOrderUrl;


	private String  deleteOrderUrl;

	/**
	 * 
	 */
	private String stockUrl;
	/**
	 * 
	 */
	private String strKey;



}
