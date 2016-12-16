package com.shangpin.ephub.client.message.pending.header;
/**
 * <p>Title:MessageHeaderKey.java </p>
 * <p>Description: 发往待处理消息队列的消息的消息头的key</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月16日 上午11:05:43
 */
public interface MessageHeaderKey {
	/**
	 * 该常量用于发往待处理商品消息队列的消息所带的消息头，用于存储和获取消息头时的key
	 */
	public static final String PENDING_PRODUCT_MESSAGE_HEADER_KEY = "PENDING_PRODUCT_MAP_KEY";
}
