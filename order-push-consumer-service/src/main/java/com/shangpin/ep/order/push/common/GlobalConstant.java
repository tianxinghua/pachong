package com.shangpin.ep.order.push.common;

public interface GlobalConstant {
	/**
	 * 消息头中包含的重试次数的Key
	 */
	public static final String MESSAGE_HEADER_X_RETRIES_KEY = "x-retries";
	/**
	 * 消息头中包含的订单状态的Key
	 */
	public static final String MESSAGE_HEADER_ORDER_STATUS_KEY = "MESSAGE_HEADER_ORDER_STATUS_KEY";
	/**
	 * 消息头中包含的推送状态的Key
	 */
	public static final String MESSAGE_HEADER_PUSH_STATUS_KEY = "MESSAGE_HEADER_PUSH_STATUS_KEY";
	/**
	 * 消息头中包含的错误的推送给供货商的订单编号
	 */
	public static final String MESSAGE_HEADER_ORDER_NO_KEY = "MESSAGE_HEADER_ORDER_NO_KEY";

	/**
	 *  消息头中包含的推送失败的错我原因
	 */
	public static final String MESSAGE_HEADER_ORDER_PUSH_ERROR_TYPE = "MESSAGE_HEADER_ORDER_PUSH_ERROR_TYPE";

	/**
	 * 下单
	 */
    public static final String SYNC_TYPE_CREATE_ORDER = "CreateOrder";
	/**
	 * 取消
	 */
	public static final String SYNC_TYPE_CANCEL_ORDER = "CancelOrder";
	/**
	 * 支付
	 */
	public static final String SYNC_TYPE_PAYED_ORDER = "PayedOrder";
	/**
	 *  重采
	 */
	public static final String SYNC_TYPE_REPURCHASE_ORDER = "RePurchaseSupplierOrder";
	/**
	 * 退款
	 */
	public static final String SYNC_TYPE_REFUNDED = "RefundedOrder";
	/**
	 * 发货
	 */
	public static final String SYNC_TYPE_SHIPPED = "Shipped";

	public static final String  REDIS_ORDER_SUPPLIER_KEY="REDIS_ORDER_SUPPLIER_KEY";

}
