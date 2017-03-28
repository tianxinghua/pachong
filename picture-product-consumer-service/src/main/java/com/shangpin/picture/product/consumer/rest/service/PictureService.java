package com.shangpin.picture.product.consumer.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.picture.product.consumer.conf.stream.source.message.RetryPicture;
import com.shangpin.picture.product.consumer.conf.stream.source.sender.RetryPictureProductStreamSender;

/**
 * <p>PictureService </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月2日 下午5:34:23
 */
@Service
public class PictureService {
	
	@Autowired
	private RetryPictureProductStreamSender streamSender;
    /**
     * 发送重试拉取图片消息
     * @param id 需要重试拉取处理的图片表主键
     * @return 
     */
	public boolean sendRetryPicId(Long id) {
		return streamSender.supplierPictureProductStream(new RetryPicture(id) , null);
	}

}
