package com.shangpin.supplier.product.message.original.conf.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.shangpin.supplier.product.message.original.conf.channel.OriginalProductSource;
import com.shangpin.supplier.product.message.original.conf.message.SupplierProduct;
/**
 * <p>Title:OrderStreamSender.java </p>
 * <p>Description: 订单流发送器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月23日 下午3:28:09
 */
@EnableBinding({OriginalProductSource.class})
public class OriginalProductStreamSender {
	
	@Autowired
	private OriginalProductSource originalProductSource;
	/**
	 * 发送供应商biondioni商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean biondioniStream(SupplierProduct supplierProduct) {
    	return originalProductSource.biondioni().send(MessageBuilder.withPayload(supplierProduct).build());
    }
	/**
	 * 发送供应商brunarosso商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean brunarossoStream(SupplierProduct supplierProduct) {
    	return originalProductSource.brunarosso().send(MessageBuilder.withPayload(supplierProduct).build());
    }
	/**
	 * 发送供应商coltorti商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean coltortiStream(SupplierProduct supplierProduct) {
    	return originalProductSource.coltorti().send(MessageBuilder.withPayload(supplierProduct).build());
    }
	/**
	 * 发送供应商ostore商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean ostoreStream(SupplierProduct supplierProduct) {
    	return originalProductSource.ostore().send(MessageBuilder.withPayload(supplierProduct).build());
    }
	/**
	 * 发送供应商spinnaker商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean spinnakerStream(SupplierProduct supplierProduct) {
    	return originalProductSource.spinnaker().send(MessageBuilder.withPayload(supplierProduct).build());
    }
	/**
	 * 发送供应商stefania商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean stefaniaStream(SupplierProduct supplierProduct) {
    	return originalProductSource.stefania().send(MessageBuilder.withPayload(supplierProduct).build());
    }
	/**
	 * 发送供应商tony商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean tonyStream(SupplierProduct supplierProduct) {
    	return originalProductSource.tony().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商geb商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean gebStream(SupplierProduct supplierProduct) {
    	return originalProductSource.geb().send(MessageBuilder.withPayload(supplierProduct).build());
    }
}
