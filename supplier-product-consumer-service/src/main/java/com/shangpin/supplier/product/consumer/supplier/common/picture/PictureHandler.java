package com.shangpin.supplier.product.consumer.supplier.common.picture;

import java.util.List;

import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.ephub.client.message.picture.ProductPicture;
import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.supplier.product.consumer.util.DateTimeUtil;
import com.shangpin.supplier.product.consumer.util.UUIDGenerator;
/**
 * <p>Title:PictureHandler </p>
 * <p>Description: 图片处理器 </p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2017年1月3日 下午2:30:48
 *
 */
@Component
public class PictureHandler {

	/**
	 * 初始化发送图片消息体
	 * @param message 供应商原始消息
	 * @param hubSpu
	 * @param images
	 * @return
	 */
	public SupplierPicture initSupplierPicture(SupplierProduct message,HubSupplierSpuDto hubSpu,List<Image> images){
		SupplierPicture supplierPicture = new SupplierPicture();
		supplierPicture.setMessageId(UUIDGenerator.getUUID());
		supplierPicture.setMessageDate(DateTimeUtil.getDateTime());
		supplierPicture.setSupplierId(message.getSupplierId());
		supplierPicture.setSupplierName(message.getSupplierName());
		ProductPicture productPicture = new ProductPicture();
		productPicture.setSupplierSpuNo(hubSpu.getSupplierSpuNo());
		productPicture.setImages(images); 
		supplierPicture.setProductPicture(productPicture);
		return supplierPicture;
	}
}
