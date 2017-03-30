package com.shangpin.ephub.client.consumer.picture.gateway;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.consumer.picture.dto.RetryPictureDto;

/**
 * <p>Title:UploadController.java </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月2日 下午5:27:46
 */
@FeignClient("picture-product-consumer-service")
public interface PictureGateWay {
	
	/**
	 * 发送重试拉取图片的消息
	 * @param retryPictureDto
	 */
	@RequestMapping(value = "/picture/retry", method = RequestMethod.POST, consumes = "application/json")
	public void retry(@RequestBody RetryPictureDto retryPictureDto);
}
