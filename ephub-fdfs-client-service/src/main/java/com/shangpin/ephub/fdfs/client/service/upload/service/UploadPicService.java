package com.shangpin.ephub.fdfs.client.service.upload.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.shangpin.ephub.fdfs.client.service.upload.manager.FastDFSClientManager;

/**
 * <p>Title:UploadPicService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月2日 下午5:34:23
 */
@Service
public class UploadPicService {
	
	@Autowired
	private FastDFSClientManager dfsClientManager;
	/**
	 * 上传图片
	 * @param file 文件
	 * @return 图片路径
	 */
	public String uploadFile(MultipartFile file) {
		try {
			return dfsClientManager.uploadFile(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
