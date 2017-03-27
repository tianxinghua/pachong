package com.shangpin.picture.product.consumer.conf.stream.source.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * <p>Title:PictureProductSource.java </p>
 * <p>Description: 供货商商品图片通道配置</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月7日 下午3:24:36
 */
public interface RetryPictureProductSource {

	public String SUPPLIER_PICTURE = "supplierRetryPictureProduct";
	
	/**
	 * @return 供应商商品通用图片数据流通道组件
	 */
	@Output(value = RetryPictureProductSource.SUPPLIER_PICTURE)
    public MessageChannel supplierRetryPictureProduct();
}
