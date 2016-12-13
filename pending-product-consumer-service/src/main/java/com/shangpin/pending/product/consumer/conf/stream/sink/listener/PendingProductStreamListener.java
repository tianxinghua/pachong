package com.shangpin.pending.product.consumer.conf.stream.sink.listener;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import com.shangpin.pending.product.consumer.conf.stream.sink.channel.PendingProductSink;
import com.shangpin.pending.product.consumer.conf.stream.sink.message.PendingProduct;
import com.shangpin.pending.product.consumer.supplier.adapter.PendingProductStreamListenerAdapter;

/**
 * <p>Title:StreamConf.java </p>
 * <p>Description: 待处理商品数据流监听器配置</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月9日 下午6:07:52
 */
@EnableBinding({PendingProductSink.class})
public class PendingProductStreamListener {
	
	@Autowired
	private PendingProductStreamListenerAdapter adapter;
	/**
	 * 供应商BIONDIONI待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.BIONDIONI)
    public void biondioniPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.biondioniPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商BRUNAROSSO待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.BRUNAROSSO)
    public void brunarossoPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.brunarossoPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商COLTORTI待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.COLTORTI)
    public void coltortiPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.coltortiPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商GEB待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.GEB)
    public void gebPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.gebPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商OSTORE待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.OSTORE)
    public void ostorePendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.ostorePendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商SPINNAKER待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.SPINNAKER)
    public void spinnakerPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.spinnakerPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商STEFANIA待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.STEFANIA)
    public void stefaniaPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.stefaniaPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商TONY待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.TONY)
    public void tonyPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.tonyPendingProductStreamListen(message,headers);
    }
}
