package com.shangpin.picture.product.consumer.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.message.picture.ProductPicture;
import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.picture.product.consumer.conf.stream.source.message.RetryPicture;
import com.shangpin.picture.product.consumer.e.PicHandleState;
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
		long start = System.currentTimeMillis();
		String messageId = message.getMessageId();
		if (log.isDebugEnabled()) {
			log.debug("图片处理系统接收到消息数据======:{}",message);
		} else {
			log.info("系统接收到消息,编号为{},发送时间为{}",messageId,message.getMessageDate());
		}
		try {
			List<HubSpuPendingPicDto> picDtos = transform(message);
			supplierProductPictureService.processProductPicture(picDtos);
			log.info("系统处理消息完毕，耗时{}",System.currentTimeMillis() - start);
		} catch (Throwable e) {
			log.error("系统处理编号为"+messageId+"的消息时发生异常",e);
			e.printStackTrace();
		}
	}
	/**
	 * 将消息格式转换为数据传输对象格式
	 * @param message 消息体格式数据
	 * @return 数据传输格式数据
	 */
	private List<HubSpuPendingPicDto> transform(SupplierPicture message) {
		List<HubSpuPendingPicDto> dtos = null;
		String supplierId = message.getSupplierId();
		Long supplierSpuId = message.getSupplierSpuId();
		ProductPicture productPicture = message.getProductPicture();
		if (productPicture != null &&  CollectionUtils.isNotEmpty(productPicture.getImages())) {
			List<Image> images = productPicture.getImages();
			String supplierSpuNo = productPicture.getSupplierSpuNo();
			dtos = new ArrayList<>();
			for (Image image : images) {
				String url = image.getUrl();
				HubSpuPendingPicDto dto = new HubSpuPendingPicDto();
				dto.setCreateTime(new Date());
				dto.setDataState(DataState.NOT_DELETED.getIndex());
				dto.setPicHandleState(PicHandleState.UNHANDLED.getIndex());
				dto.setPicUrl(url);
				dto.setSupplierSpuNo(supplierSpuNo);
				dto.setSupplierId(supplierId);
				dto.setSupplierSpuId(supplierSpuId);
				dtos.add(dto);
			}
		} else {
			log.warn("图片处理系统接收到的消息数据中没有图片地址，消息编号为{},消息发送时间为{}",message.getMessageId(),message.getMessageDate());
		}
		return dtos;
	}
	/**
	 * 处理重试拉取图片
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void processRetryProductPicture(RetryPicture message, Map<String, Object> headers) {
		try {
			supplierProductPictureService.processRetryProductPicture(message.getSpuPendingPicId());
		} catch (Throwable e) {
			log.error("重试拉取图片发生异常", e);
			e.printStackTrace();
		}
	}
}
