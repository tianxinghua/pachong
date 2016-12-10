package com.shangpin.ep.order.enumeration;
/**
 * <p>Title:ErrorStatus.java </p>
 * <p>Description: 错误状态枚举</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午12:35:04
 */
public enum ErrorStatus {
	/**
	 *
	 */
	SUCCESS(0,"成功"),

	/**
	 * 网络异常
	 */
	NETWORK_ERROR(1,"网络异常"),
	/**
	 * 供货商接口出错
	 */
	API_ERROR(2,"供货商接口出错"),
	/**
	 * 其它错误
	 */
	OTHER_ERROR(3,"其它错误"),
	/**
	 *   重新推送
	 *   供货商收到信息 但返回时发生网络异常 ，下次可能会重复推送
	 */

	REPEAT_PUSH(4,"重新推送");
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
	private ErrorStatus(Integer index, String description) {
		this.index = index;
		this.description = description;
	}
}
