package com.shangpin.ep.order.conf.openapi;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>Title:SupplierProperties.java </p>
 * <p>Description: openApi接口名称</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年11月25日 下午3:39:07
 */
@Setter
@Getter
@ToString
public class OpenApi {
	
	/**
	 * openApi地址
	 */
	private String host;
	
	/**
	 * 更新库存
	 */
	private String updatestock;
	
	/**
	 * 取消采购单
	 */
	private String cancelpurchase;
	
	/**
	 * 查询采购单明细
	 */
	private String findporder;

}
