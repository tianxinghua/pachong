package com.shangpin.picture.product.consumer.processor;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.util.IOUtils;
import com.shangpin.ephub.client.message.picture.ProductPicture;
import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.picture.product.consumer.service.SupplierProductPictureService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:SupplierProductPictureProcessor.java </p>
 * <p>Description: 供应商商品图片处理器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月2日 下午12:39:14
 */
@Component
@Slf4j
public class SupplierProductPictureProcessor {
	
	@Autowired
	private SupplierProductPictureService supplierProductPictureService;
	/**
	 * 供应商商品图片处理器处理图片
	 * @param message 接收到的消息体
	 * @param headers 接收到的消息头
	 */
	public void processProductPicture(SupplierPicture message, Map<String, Object> headers) {
		log.info("图片处理系统接收到数据：{}",message);
		String supplierId = message.getSupplierId();
		String supplierName = message.getSupplierName();
		ProductPicture productPicture = message.getProductPicture();
		String supplierSpuNo = productPicture.getSupplierSpuNo();
		List<Image> images = productPicture.getImages();
		if (CollectionUtils.isNotEmpty(images)) {
			for (Image image : images) {
				try {
					URL url = new URL(image.getUrl());
					InputStream inputStream = url.openStream();
					// TODO 调用接口上传图片 
				} catch (Exception e) {
					e.printStackTrace();
					log.error("下载图片失败", e);
				}
			}
		} else {
			log.warn("图片处理系统接收到的消息中没有图片数据");
		}
	}

}
