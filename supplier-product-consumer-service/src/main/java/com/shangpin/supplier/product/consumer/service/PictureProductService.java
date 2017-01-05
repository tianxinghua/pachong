package com.shangpin.supplier.product.consumer.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.supplier.product.consumer.conf.stream.source.sender.PictureProductStreamSender;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title:PictureProductService </p>
 * <p>Description: 供应商处理图片service </p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2017年1月3日 下午4:29:27
 *
 */
@Service
@Slf4j
public class PictureProductService {

	@Autowired
	private PictureProductStreamSender pictureProductStreamSender;
	
	/**
	 * 发送供应商图片到图片消息队列
	 * @param supplierPicture
	 * @param headers
	 */
	public void sendSupplierPicture(SupplierPicture supplierPicture, Map<String, ?> headers){
		try {
			boolean result = pictureProductStreamSender.supplierPictureProductStream(supplierPicture, headers);
			log.info(supplierPicture.getSupplierName()+":"+supplierPicture.getProductPicture().getSupplierSpuNo()+" 发送图片 "+result);
		} catch (Exception e) {
			log.error(supplierPicture.getSupplierName()+":"+supplierPicture.getProductPicture().getSupplierSpuNo()+" 发送图片异常："+e.getMessage(),e); 
		}
	}
}
