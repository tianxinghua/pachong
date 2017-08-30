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
public class Clothing extends SupplierCommon implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -562859012186115449L;

	private String strKey;
	
	private String strPassword;



}
