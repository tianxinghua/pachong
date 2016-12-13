package com.shangpin.supplier.product.consumer.supplier.adapter;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.shangpin.supplier.product.consumer.conf.stream.sink.message.SupplierProduct;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;

/**
 * <p>Title:OriginalProductStreamListenerAdapter.java </p>
 * <p>Description: 消息流适配器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月8日 上午11:29:45
 */
@Component
public class OriginalProductStreamListenerAdapter {
	
	@Autowired
	@Qualifier("stefaniaHandler")
	private ISupplierHandler stefaniaHandler;	
	@Autowired
	@Qualifier("ostoreHandler")
	private ISupplierHandler ostoreHandler;
	@Autowired
	@Qualifier("brunarossoHandler")
	private ISupplierHandler brunarossoHandler;
	@Autowired
	@Qualifier("spinnakerHandler")
	private ISupplierHandler spinnakerHandler;	
	@Autowired
	@Qualifier("gebHandler")
	private ISupplierHandler gebHandler;
	@Autowired
	@Qualifier("biondioniHandler")
	private ISupplierHandler biondioniHandler;
	
	/**
	 * biondioni供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void biondioniStreamListen(SupplierProduct message, Map<String, Object> headers) {
		biondioniHandler.handleOriginalProduct(message, headers); 
		
	}
	/**
	 * brunarosso供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void brunarossoStreamListen(SupplierProduct message, Map<String, Object> headers) {
		brunarossoHandler.handleOriginalProduct(message, headers);		
	}
	/**
	 * coltorti供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void coltortiStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * geb供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void gebStreamListen(SupplierProduct message, Map<String, Object> headers) {
		gebHandler.handleOriginalProduct(message, headers); 
		
	}
	/**
	 * ostore供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void ostoreStreamListen(SupplierProduct message, Map<String, Object> headers) {
		ostoreHandler.handleOriginalProduct(message, headers);		
	}
	/**
	 * spinnaker供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void spinnakerStreamListen(SupplierProduct message, Map<String, Object> headers) {
		spinnakerHandler.handleOriginalProduct(message, headers); 
		
	}
	/**
	 * stefania供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void stefaniaStreamListen(SupplierProduct message, Map<String, Object> headers) {
		stefaniaHandler.handleOriginalProduct(message,headers);
	}
	/**
	 * tony供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void tonyStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}

}
