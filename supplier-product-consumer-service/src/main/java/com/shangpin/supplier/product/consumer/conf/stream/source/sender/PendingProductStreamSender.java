package com.shangpin.supplier.product.consumer.conf.stream.source.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.shangpin.supplier.product.consumer.conf.stream.sink.message.SupplierProduct;
import com.shangpin.supplier.product.consumer.conf.stream.source.channel.PendingProductSource;
/**
 * <p>Title:OrderStreamSender.java </p>
 * <p>Description: 待处理商品数据流发送器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月23日 下午3:28:09
 */
@EnableBinding({PendingProductSource.class})
public class PendingProductStreamSender {
	
	@Autowired
	private PendingProductSource pendingProductSource;
	/**
	 * 发送供应商biondioni商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean biondioniPendingProductStream(SupplierProduct supplierProduct) {
    	return pendingProductSource.biondioniPendingProduct().send(MessageBuilder.withPayload(supplierProduct).build());
    }
	/**
	 * 发送供应商brunarosso商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean brunarossoPendingProductStream(SupplierProduct supplierProduct) {
    	return pendingProductSource.brunarossoPendingProduct().send(MessageBuilder.withPayload(supplierProduct).build());
    }
	/**
	 * 发送供应商coltorti商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean coltortiPendingProductStream(SupplierProduct supplierProduct) {
    	return pendingProductSource.coltortiPendingProduct().send(MessageBuilder.withPayload(supplierProduct).build());
    }
	/**
	 * 发送供应商ostore商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean ostorePendingProductStream(SupplierProduct supplierProduct) {
    	return pendingProductSource.ostorePendingProduct().send(MessageBuilder.withPayload(supplierProduct).build());
    }
	/**
	 * 发送供应商spinnaker商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean spinnakerPendingProductStream(SupplierProduct supplierProduct) {
    	return pendingProductSource.spinnakerPendingProduct().send(MessageBuilder.withPayload(supplierProduct).build());
    }
	/**
	 * 发送供应商stefania商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean stefaniaPendingProductStream(SupplierProduct supplierProduct) {
    	return pendingProductSource.stefaniaPendingProduct().send(MessageBuilder.withPayload(supplierProduct).build());
    }
	/**
	 * 发送供应商tony商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean tonyPendingProductStream(SupplierProduct supplierProduct) {
    	return pendingProductSource.tonyPendingProduct().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商geb商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean gebPendingProductStream(SupplierProduct supplierProduct) {
    	return pendingProductSource.gebPendingProduct().send(MessageBuilder.withPayload(supplierProduct).build());
    }
}
