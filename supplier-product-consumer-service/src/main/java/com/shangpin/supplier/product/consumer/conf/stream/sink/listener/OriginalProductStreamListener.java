package com.shangpin.supplier.product.consumer.conf.stream.sink.listener;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.supplier.product.consumer.conf.stream.sink.channel.OriginalProductSink;
import com.shangpin.supplier.product.consumer.supplier.adapter.OriginalProductStreamListenerAdapter;

/**
 * 订单流处理配置
 * <p>Title:StreamConf.java </p>
 * <p>Description: 订单流数据处理</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月9日 下午6:07:52
 */
@EnableBinding({OriginalProductSink.class})
public class OriginalProductStreamListener {
	
	@Autowired
	private OriginalProductStreamListenerAdapter adapter;
	/**
	 * 供应商BIONDIONI原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.BIONDIONI)
    public void biondioniStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.biondioniStreamListen(message,headers);
    }
	/**
	 * 供应商BRUNAROSSO原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.BRUNAROSSO)
    public void brunarossoStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.brunarossoStreamListen(message,headers);
    }
	/**
	 * 供应商COLTORTI原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.COLTORTI)
    public void coltortiStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.coltortiStreamListen(message,headers);
    }
	/**
	 * 供应商GEB原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.GEB)
    public void gebStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.gebStreamListen(message,headers);
    }
	/**
	 * 供应商OSTORE原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.OSTORE)
    public void ostoreStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.ostoreStreamListen(message,headers);
    }
	/**
	 * 供应商SPINNAKER原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.SPINNAKER)
    public void spinnakerStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.spinnakerStreamListen(message,headers);
    }
	/**
	 * 供应商STEFANIA原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.STEFANIA)
    public void stefaniaStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.stefaniaStreamListen(message,headers);
    }
	/**
	 * 供应商TONY原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.TONY)
    public void tonyStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.tonyStreamListen(message,headers);
    }
}
