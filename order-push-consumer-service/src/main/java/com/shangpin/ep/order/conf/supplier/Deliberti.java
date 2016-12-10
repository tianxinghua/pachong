package com.shangpin.ep.order.conf.supplier;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>Title:Deliberti.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午6:39:21
 */
@Setter
@Getter
@ToString
public class Deliberti extends SupplierCommon implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 269142605063256998L;

	private String url;
	

}
