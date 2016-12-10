package com.shangpin.ep.order.conf.stream.sink.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
/**
 * 订单流汇聚点配置接口
 * <p>Title:OrderSink.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月9日 下午6:17:24
 */
public interface OrderSink {
	
	public String ORDERS = "orders";
	
	/**
	 * 订单通道组件配置
	 * @return 订单通道组件
	 */
	@Input(value = OrderSink.ORDERS)
    public SubscribableChannel orders();
}
