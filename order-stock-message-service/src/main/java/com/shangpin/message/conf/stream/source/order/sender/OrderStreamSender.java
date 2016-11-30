package com.shangpin.message.conf.stream.source.order.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.shangpin.message.conf.stream.source.order.channel.OrderSource;
import com.shangpin.message.conf.stream.source.order.message.SupplierOrderSync;
/**
 * <p>Title:OrderStreamSender.java </p>
 * <p>Description: 订单流发送器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月23日 下午3:28:09
 */
@EnableBinding({OrderSource.class})
public class OrderStreamSender {
	
	@Autowired
	private OrderSource orderSource;
	/**
	 * 发送订单流数据
	 * @param supplierOrder 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean orderStreamSend(SupplierOrderSync supplierOrder) {
    	return orderSource.orders().send(MessageBuilder.withPayload(supplierOrder).build());
    }
}
