package com.shangpin.supplier.product.consumer.conf.stream.source.sender;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
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
	 * @param headers 
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean supplierPictureProductStream(SupplierPicture supplierPicture, Map<String, ?> headers) {
    	return pictureProductSource.supplierPictureProduct().send(MessageBuilder.withPayload(supplierPicture).copyHeaders(headers).build());
    }
    
    /**
	 * 发送品牌方图片流数据
	 * @param headers 
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean brandPictureProductStream(SupplierPicture supplierPicture, Map<String, ?> headers) {
    	return pictureProductSource.brandPictureProduct().send(MessageBuilder.withPayload(supplierPicture).copyHeaders(headers).build());
    }
    
    /**
   	 * 发送stefaniamode图片流数据
   	 * @param headers 
   	 * @param supplierProduct 消息体
   	 * @return 如果发送成功返回true,否则返回false
   	 */
       public boolean stefaniamodePictureProductStream(SupplierPicture supplierPicture, Map<String, ?> headers) {
       	return pictureProductSource.stefaniamodePictureProduct().send(MessageBuilder.withPayload(supplierPicture).copyHeaders(headers).build());
       }
	/**
	 * 发送mclabels图片流数据
	 * @param headers
	 * @param supplierPicture 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean mclablesPictureProductStream(SupplierPicture supplierPicture, Map<String, ?> headers) {
		return pictureProductSource.mclablesPictureProduct().send(MessageBuilder.withPayload(supplierPicture).copyHeaders(headers).build());
	}

	/**
	 * 发送eraldo图片流数据
	 * @param headers
	 * @param supplierPicture 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean eraldoPictureProductStream(SupplierPicture supplierPicture, Map<String, ?> headers) {
		return pictureProductSource.eraldoPictureProduct().send(MessageBuilder.withPayload(supplierPicture).copyHeaders(headers).build());
	}


	/**
	 * 发送frmoda图片流数据
	 * @param headers
	 * @param supplierPicture 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean frmodaPictureProductStream(SupplierPicture supplierPicture, Map<String, ?> headers) {
		return pictureProductSource.frmodaPictureProduct().send(MessageBuilder.withPayload(supplierPicture).copyHeaders(headers).build());
	}


	/**
	 * 发送frmoda图片流数据
	 * @param headers
	 * @param supplierPicture 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean reebonzPictureProductStream(SupplierPicture supplierPicture, Map<String, ?> headers) {
		return pictureProductSource.reebonzPictureProduct().send(MessageBuilder.withPayload(supplierPicture).copyHeaders(headers).build());
	}
}
