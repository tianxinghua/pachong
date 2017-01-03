package com.shangpin.ephub.fdfs.client.service.upload.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shangpin.ephub.fdfs.client.service.upload.service.UploadPicService;

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
	public String upload(MultipartFile file) throws Exception {
		return uploadPicService.uploadFile(file);
    }
}
