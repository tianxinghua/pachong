package com.shangpin.ep.order.conf.supplier;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Title:RussoCapri.java </p>
 * <p>Description: RussoCapri供应商配置</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月05日 下午5:45:15
 */
@Setter
@Getter
@ToString
public class RussoCapri extends SupplierCommon implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 供应商主站地址
	 */
	private String url;
	/**
	 * 创建订单api接口
	 */
	private String createOrderInterface;
	/**
	 * 设置接口，包括退单等
	 */
	private String setStatusInterface;
	/**
	 * 查询订单状态
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
	
}
