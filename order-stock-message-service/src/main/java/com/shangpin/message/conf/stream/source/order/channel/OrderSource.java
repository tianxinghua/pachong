package com.shangpin.message.conf.stream.source.order.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
/**
 * <p>Title:OrderSink.java </p>
 * <p>Description: 消息流数据源配置</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月23日 下午3:14:33
 */
public interface OrderSource {
	
	public String ORDERS = "orders";
	
	/**
	 * 订单通道组件配置
	 * @return 订单通道组件
	 */
	@Output(value = OrderSource.ORDERS)
    public MessageChannel orders();
}
