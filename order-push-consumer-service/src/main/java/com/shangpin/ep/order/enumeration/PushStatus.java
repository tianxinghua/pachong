package com.shangpin.ep.order.enumeration;
/**
 * <p>Title:PushStatus.java </p>
 * <p>Description: 订单推送状态枚举类</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午12:22:09
 */
public enum PushStatus {
	/**
	 * 无锁库API
	 */
	NO_LOCK_API(0,"无锁库API"),
	/**
	 * 锁库
	 */
	LOCK_PLACED(1,"锁库"),
	/**
	 * 无库存
	 */
	NO_STOCK(2,"无库存"),
	/**
	 * 锁库失败
	 */
	LOCK_PLACED_ERROR(3,"锁库失败"),
	/**
	 * 订单推送确认
	 */
	ORDER_CONFIRMED(4,"订单推送确认"),
	/**
	 * 订单推送失败
	 */
	ORDER_CONFIRMED_ERROR(5,"订单推送失败"),
	/**
	 * 锁库取消
	 */
	LOCK_CANCELLED(6,"锁库取消"),
	/**
	 * 锁库取消失败
	 */
	LOCK_CANCELLED_ERROR(7,"锁库取消失败"),
	/**
	 *  无锁库取消API
	 */
	NO_LOCK_CANCELLED_API(8,"无锁库取消API"),
	/**
	 * 退单
	 */
	REFUNDED(9,"退单"),
	/**
	 * 退单失败
	 */
	REFUNDED_ERROR(10,"退单失败"),
	/**
	 * 无退单API
	 */
	NO_REFUNDED_API(11,"无退单API");
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
	private PushStatus(Integer index, String description) {
		this.index = index;
		this.description = description;
	}

	// 获取枚举实例
	public static PushStatus getPushStatus(int index) {
		for (PushStatus c : PushStatus.values()) {
			if (c.getIndex() == index) {
				return c;
			}
		}
		return null;
	}
}
