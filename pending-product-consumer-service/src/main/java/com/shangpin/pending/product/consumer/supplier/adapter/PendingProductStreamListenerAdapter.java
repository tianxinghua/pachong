package com.shangpin.pending.product.consumer.supplier.adapter;

import java.util.Map;

import com.shangpin.pending.product.consumer.supplier.common.PendingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.message.pending.body.PendingProduct;

/**
 * <p>Title:PendingProductStreamListenerAdapter.java </p>
 * <p>Description: 待处理商品数据流监听适配器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月12日 下午4:30:09
 */
@Component
@Slf4j
public class PendingProductStreamListenerAdapter {

	@Autowired
	PendingHandler pendingHandler;


	private void messageHandle(PendingProduct message, Map<String, Object> headers){
		try {
			pendingHandler.receiveMsg(message,headers);
		} catch (Exception e) {
			log.error(" exception message = message boday : "  + message.toString()
					+ "  message header :" + headers.toString() + " exception reason :" + e.getMessage(),e);
			e.printStackTrace();
		}
	}

	/**
	 * 供应商biondioni待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void biondioniPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		this.messageHandle(message, headers);

	}
	/**
	 * 供应商ostore待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void ostorePendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		this.messageHandle(message, headers);
		
	}
	/**
	 * 供应商spinnaker待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void spinnakerPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		this.messageHandle(message, headers);
		
	}
	/**
	 * 供应商stefania待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void stefaniaPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		this.messageHandle(message, headers);
		
	}
	/**
	 * 供应商tony待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void tonyPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		this.messageHandle(message, headers);
		
	}
	/**
	 * 供应商geb待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void gebPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		this.messageHandle(message, headers);
		
	}
	/**
	 * 供应商coltorti待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void coltortiPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		this.messageHandle(message, headers);
		
	}
	/**
	 * 供应商brunarosso待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void brunarossoPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		this.messageHandle(message, headers);
		
	}

}
