package com.shangpin.ephub.client.fdfs.gateway;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.fdfs.dto.DeletePicDto;

/**
 * <p>DeletePicGateWay </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月2日 下午5:27:46
 */
@FeignClient("ephub-fdfs-client-service")
public interface DeletePicGateWay {
	
	@RequestMapping(value = "/delete-pic/delete", method = RequestMethod.POST, consumes = "application/json")
	public Map<String,Integer> delete(@RequestBody DeletePicDto deletePicDto);
	
}
