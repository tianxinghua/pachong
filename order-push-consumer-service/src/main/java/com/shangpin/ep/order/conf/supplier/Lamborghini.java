package com.shangpin.ep.order.conf.supplier;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>Title:EfashionConf.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午7:04:00
 */
@Setter
@Getter
@ToString
public class Lamborghini extends SupplierCommon implements Serializable {
	private static final long serialVersionUID = -5578499231869193297L;
	/**
	 */
	private String cancelUrl;
	/**
	 * 
	 */
	private String placeUrl;

}
