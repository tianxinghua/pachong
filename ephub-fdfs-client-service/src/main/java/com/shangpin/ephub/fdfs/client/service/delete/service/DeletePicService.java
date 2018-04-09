package com.shangpin.ephub.fdfs.client.service.delete.service;

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
public class DeletePicService {
	
	@Autowired
	private FastDFSClientManager dfsClientManager;
	/**
	 * 上传图片
	 * @param buffer
	 * @param extension
	 * @return
	 */
	public String uploadFile(byte[] buffer, String extension,String requestId) {
		return dfsClientManager.uploadFile(buffer,extension,requestId);
	}
	/**
	 * 删除图片
	 * @param url 图片地址
	 */
	public void deleteByUrl(String url) {
		dfsClientManager.deleteFile(url);
	}

}
