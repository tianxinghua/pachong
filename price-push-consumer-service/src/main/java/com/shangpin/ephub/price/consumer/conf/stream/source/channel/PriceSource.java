package com.shangpin.ephub.price.consumer.conf.stream.source.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * <p>Title:PictureProductSource.java </p>
 * <p>Description: 供货商商品图片通道配置</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月7日 下午3:24:36
 */
public interface PriceSource {

	public String SUPPLIER_PRICE = "supplierProducerPrice";
	
	/**
	 * @return 供应商商品价格数据流通道组件
	 */
	@Output(value = PriceSource.SUPPLIER_PRICE)
    public MessageChannel supplierProducerPrice();
}
