package com.shangpin.picture.product.consumer.rest.controller;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.picture.product.consumer.rest.dto.RetryPictureDto;
import com.shangpin.picture.product.consumer.rest.service.PictureService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:UploadController.java </p>
 * <p>Description: 图片服务rest服务</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月2日 下午5:27:46
 */
@RestController
@RequestMapping(value = "/picture")
@Slf4j
public class PictureController {
	
	@Autowired
	private PictureService pictureService;
	/**
	 * 图片上传
	 * @param file 文件
	 * @param request 
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/retry", method = RequestMethod.POST)
	public void retry(@RequestBody RetryPictureDto retryPictureDto) {
		List<Long> ids = retryPictureDto.getIds();
		log.info("重试拉取图片服务接收到请求参数：{}",ids);
		if (CollectionUtils.isNotEmpty(ids)) {
			for (Long id : ids) {
				if (id != null) {
					if(pictureService.sendRetryPicId(id)){
						log.info("图片服务发送重试拉取图片消息主键{}成功",id);
					}else{
						log.error("图片服务发送重试拉取图片消息主键{}失败",id);
					}
				} 
			}
		}
		log.info("重试拉取图片服务发送{}完毕",ids);
    }
}
