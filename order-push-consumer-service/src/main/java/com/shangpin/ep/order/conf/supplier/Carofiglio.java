package com.shangpin.ep.order.conf.supplier;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>Title:Carofiglio </p>
 * <p>Description: 供应商carofiglio配置类 </p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月13日 下午3:01:25
 *
 */
@Getter
@Setter
public class Carofiglio extends SupplierCommon implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 供应商网站地址
	 */
	private String url;
	/**
	 * 下单接口名称
	 */
	private String createOrderInterface;
	/**
	 * 更改订单状态（包括取消订单）接口
	 */
	private String setStatusInterface;
	/**
	 * 查询接口
	 */
	private String getStatusInterface;
	/**
	 * 查询库存接口
	 */
	private String getItemStockInterface;
	/**
	 * 用户名
	 */
	private String user;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 无用
	 */
	private String messageType;

}
