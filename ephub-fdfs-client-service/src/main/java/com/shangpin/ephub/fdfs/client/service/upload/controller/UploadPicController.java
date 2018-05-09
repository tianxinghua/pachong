package com.shangpin.ephub.fdfs.client.service.upload.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.fdfs.client.service.upload.dto.UploadPicDto;
import com.shangpin.ephub.fdfs.client.service.upload.service.UploadPicService;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;

/**
 * <p>Title:UploadController.java </p>
 * <p>Description: 图片上传控制器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月2日 下午5:27:46
 */
@RestController
@RequestMapping(value = "/upload-pic")
@Slf4j
public class UploadPicController {
	
	@Autowired
	private UploadPicService uploadPicService;
	/**
	 * 图片上传
	 * @param file 文件
	 * @param request 
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String upload(@RequestBody UploadPicDto uploadPicDto) {
		byte[] buffer = null;
		try {
			log.info("id="+uploadPicDto.getRequestId()+"==第二步==>> 图片服务上传图片前接收到的数据为"+uploadPicDto.getBase64().substring(0, 100)+"，长度为"+uploadPicDto.getBase64().length()+" ， 下一步将调用上传图片到图片服务器");
			buffer = new BASE64Decoder().decodeBuffer(uploadPicDto.getBase64());
			String fdfsUrl = uploadPicService.uploadFile(buffer,uploadPicDto.getExtension(),uploadPicDto.getRequestId());
			log.info("id="+uploadPicDto.getRequestId()+"==第三步==>> 图片服务上传图片后返回的图片URL为"+fdfsUrl+"， 下一步将返回结果（上传图片到图片服务器后返回的地址）给调用方");
			return fdfsUrl;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
    }
}
