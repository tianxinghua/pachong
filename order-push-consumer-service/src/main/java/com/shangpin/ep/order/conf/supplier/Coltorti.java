package com.shangpin.ep.order.conf.supplier;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>Title:Coltorti.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午6:07:00
 */
@Setter
@Getter
@ToString
public class Coltorti extends SupplierCommon implements Serializable {

	/**
	 * 版本ID
	 */
	private static final long serialVersionUID = 2392088572016874649L;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 认证
	 */
	private String auth;
	/**
	 * 产品
	 */
	private String product;
	/**
	 * 库存
	 */
	private String stock;
	/**
	 * 属性集合
	 */
	private String attributes;
	/**
	 * 分类折扣
	 */
	private String discountCat;
	/**
	 * 推广产品
	 */
	private String promo;
	/**
	 * 套装
	 */
	private String outFits;
	

}
