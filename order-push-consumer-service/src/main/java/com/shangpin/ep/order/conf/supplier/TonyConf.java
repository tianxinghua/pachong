package com.shangpin.ep.order.conf.supplier;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>Title:TonyConf.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午8:34:42
 */
@Setter
@Getter
@ToString
public class TonyConf extends SupplierCommon  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6463137958609873562L;
	/**
	 * 
	 */
	private String url;
	/**
	 * 
	 */
	private String key;
	/**
	 * 
	 */
	private Integer period;
	/**
	 * 
	 */
	private String merchantId;
	/**
	 * 
	 */
	private String token;

	

}
