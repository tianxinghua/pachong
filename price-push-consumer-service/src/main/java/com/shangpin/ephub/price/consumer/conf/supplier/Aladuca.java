package com.shangpin.ephub.price.consumer.conf.supplier;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>Title:Aladuca.java </p>
 * <p>Description: Aladuca供货商配置信息</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午5:24:57
 */
@Setter
@Getter
@ToString
public class Aladuca extends SupplierCommon implements Serializable {

	/**
	 * 版本ID
	 */
	private static final long serialVersionUID = -1144353964828241044L;
	/**
	 * 
	 */
	private String serviceUrl;
	/**
	 * 
	 */
	private String sopAction;
	/**
	 * 
	 */
	private String contentType;
	/**
	 * 
	 */
	private String identifier;
	/**
	 * 
	 */
	private String deleteOrderUrl;
	/**
	 * 
	 */
	private String deleteOrderSop;
	

}
