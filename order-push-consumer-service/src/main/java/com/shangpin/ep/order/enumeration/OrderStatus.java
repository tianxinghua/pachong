package com.shangpin.ep.order.enumeration;
/**
 * <p>Title:OrderStatus.java </p>
 * <p>Description: 订单状态枚举：用于描述系统中所有的订单状态</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 上午10:01:27
 */
public enum OrderStatus {
	/**
	 * 未支付
	 */
	NO_PAY(0,"未支付"),
	/**
	 * 未支付取消
	 */
	NO_PAY_CANCELLED(1,"未支付取消"),
	/**
	 * 支付
	 */
	PAYED(2,"支付"),
	/**
	 * 退款
	 */
	REFUNDED(3,"退款"),
	/**
	 * 已发货
	 */
	SHIPPED(4,"已发货"),
	/**
	 * 采购异常
	 */
	PURCHASE_EXCEPTION(5,"采购异常"),

	/**
	 * 应该采购异常
	 */
	PURCHASE_EXCEPTION_FAKE(6,"应该采购异常"),


	/**
	 * 采购异常失败
	 */
	PURCHASE_EXCEPTION_ERROR(7,"采购异常失败");


	/**
	 * 数字索引标识
	 */
	private Integer index;
	/**
	 * 描述信息
	 */
	private String description;
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	private OrderStatus(Integer index, String description) {
		this.index = index;
		this.description = description;
	}

	// 获取枚举实例
	public static OrderStatus getOrderStatus(int index) {
		for (OrderStatus c : OrderStatus.values()) {
			if (c.getIndex() == index) {
				return c;
			}
		}
		return null;
	}
}
