package com.shangpin.ephub.fdfs.client.service.upload.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	public String uploadFile(byte[] buffer) {
		return dfsClientManager.uploadFile(buffer);
	}

}
