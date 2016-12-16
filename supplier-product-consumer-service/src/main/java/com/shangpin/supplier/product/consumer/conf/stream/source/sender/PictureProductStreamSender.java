package com.shangpin.supplier.product.consumer.conf.stream.source.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;

import com.shangpin.supplier.product.consumer.conf.stream.source.channel.PictureProductSource;

/**
 * <p>Title:PictureProductStreamSender.java </p>
 * <p>Description: 商品图片数据流发送器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月12日 下午7:47:52
 */
@EnableBinding({PictureProductSource.class})
public class PictureProductStreamSender {
	
	@Autowired
	private PictureProductSource pictureProductSource;
	
	/**
	 * 发送供应商biondioni商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
   /* public boolean commonPictureProductStream(SupplierProduct supplierProduct) {
    	return pictureProductSource.commonPictureProduct().send(MessageBuilder.withPayload(supplierProduct).build());
    }*/
}
