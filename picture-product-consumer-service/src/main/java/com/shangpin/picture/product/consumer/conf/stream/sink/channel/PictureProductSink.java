package com.shangpin.picture.product.consumer.conf.stream.sink.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * <p>Title:PictureProductSource.java </p>
 * <p>Description: 供货商商品图片通道配置</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月7日 下午3:24:36
 */
public interface PictureProductSink {

	public String SUPPLIER_PICTURE = "supplierPictureProduct";
	
	public String BRAND_PICTURE = "brandPictureProduct";
	
	/**
	 * @return 供应商商品通用图片数据流通道组件
	 */
	@Input(value = PictureProductSink.SUPPLIER_PICTURE)
    public SubscribableChannel supplierPictureProduct();
	
	/**
	 * @return 供应商商品通用图片数据流通道组件
	 */
	@Input(value = PictureProductSink.BRAND_PICTURE)
    public SubscribableChannel brandPictureProduct();
}
