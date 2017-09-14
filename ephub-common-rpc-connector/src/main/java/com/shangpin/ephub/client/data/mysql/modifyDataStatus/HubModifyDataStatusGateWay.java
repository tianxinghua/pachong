package com.shangpin.ephub.client.data.mysql.modifyDataStatus;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <p>Title:HubMaterialDicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月15日 下午3:02:13
 */
@FeignClient("ephub-data-mysql-service")
public interface HubModifyDataStatusGateWay {

	
	@RequestMapping(value = "/modifyDataStatus/updateStatus", method = RequestMethod.POST,consumes = "application/json")
    public void updateStatus();
	
	@RequestMapping(value = "/modifyDataStatus/updatePicStatus", method = RequestMethod.POST,consumes = "application/json")
    public void updatePicStatus();
	
	@RequestMapping(value = "/modifyDataStatus/updateNewPicStatus", method = RequestMethod.POST,consumes = "application/json")
    public void updateNewPicStatus();
	
}
