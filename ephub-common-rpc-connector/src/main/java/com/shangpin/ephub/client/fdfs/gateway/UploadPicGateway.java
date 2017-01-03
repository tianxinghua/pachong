package com.shangpin.ephub.client.fdfs.gateway;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.fdfs.dto.UploadPicDto;

/**
 * <p>Title:UploadPicGateway.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月3日 下午2:41:46
 */
@FeignClient("ephub-fdfs-client-service")
public interface UploadPicGateway {
	
	@RequestMapping(value = "/upload-pic/upload", method = RequestMethod.POST, consumes = "application/json")
	public String upload(@RequestBody UploadPicDto uploadPicDto);
}
