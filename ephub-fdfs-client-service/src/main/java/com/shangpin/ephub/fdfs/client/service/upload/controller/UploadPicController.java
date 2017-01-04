package com.shangpin.ephub.fdfs.client.service.upload.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.fdfs.client.service.upload.dto.UploadPicDto;
import com.shangpin.ephub.fdfs.client.service.upload.service.UploadPicService;

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
			buffer = new BASE64Decoder().decodeBuffer(uploadPicDto.getBase64());
			return uploadPicService.uploadFile(buffer);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
    }
}
