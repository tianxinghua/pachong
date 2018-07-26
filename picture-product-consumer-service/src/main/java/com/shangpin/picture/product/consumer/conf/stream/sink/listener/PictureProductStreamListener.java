package com.shangpin.picture.product.consumer.conf.stream.sink.listener;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.picture.product.consumer.conf.stream.sink.adapter.PictureProductStreamListenerAdapter;
import com.shangpin.picture.product.consumer.conf.stream.sink.channel.PictureProductSink;
import com.shangpin.picture.product.consumer.conf.supplier.BrandSupplier;

/**
 * <p>Title:PictureProductStreamSender.java </p>
 * <p>Description: 商品图片数据流发送器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月12日 下午7:47:52
 */
@EnableBinding({PictureProductSink.class})
public class PictureProductStreamListener {
	
	@Autowired
	BrandSupplier brandSupplier;
	@Autowired
	private PictureProductStreamListenerAdapter pictureProductStreamListenerAdapter;
	

	
	@StreamListener(PictureProductSink.SUPPLIER_PICTURE)
    public void supplierPictureProductStreamListen(@Payload SupplierPicture message, @Headers Map<String,Object> headers) throws Exception  {
		pictureProductStreamListenerAdapter.supplierPictureProductStreamListen(message,headers);
    }
	@StreamListener(PictureProductSink.stefaniamode_picture)
    public void stefaniamodePictureProductStreamListen(@Payload SupplierPicture message, @Headers Map<String,Object> headers) throws Exception  {
		pictureProductStreamListenerAdapter.supplierPictureProductStreamListen(message,headers);
    }
	
	@StreamListener(PictureProductSink.BRAND_PICTURE)
    public void brandPictureProductStreamListen(@Payload SupplierPicture message, @Headers Map<String,Object> headers) throws Exception  {
		try {
			if(brandSupplier.getBrandSupplierIds().contains(message.getSupplierId())){
				Thread.sleep(1000*10);	
			}else{
				Thread.sleep(1000*1);
			}
			pictureProductStreamListenerAdapter.supplierPictureProductStreamListen(message,headers);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
    }
	
}
