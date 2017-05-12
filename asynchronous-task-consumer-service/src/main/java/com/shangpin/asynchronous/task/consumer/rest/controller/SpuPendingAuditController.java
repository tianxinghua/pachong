package com.shangpin.asynchronous.task.consumer.rest.controller;


import com.shangpin.asynchronous.task.consumer.rest.service.SkuSelectedMappingService;
import com.shangpin.asynchronous.task.consumer.rest.service.SpuPendingAuditService;
import com.shangpin.ephub.client.consumer.hubskusuppliermapping.dto.ProductMessageDto;
import com.shangpin.ephub.client.data.mysql.product.dto.SpuModelDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**

 * <p>Description: 选品服务</p>
 *
 */
@RestController
@RequestMapping(value = "/hub-spu-pending")
public class SpuPendingAuditController {
	
	@Autowired
	private SpuPendingAuditService spuPendingAuditService;
	/**
	 *
	 * @throws Exception
	 */
	@RequestMapping(value = "/audit", method = RequestMethod.POST)
	public boolean  auditSpu(@RequestBody SpuModelDto spuModelDto) {
		return spuPendingAuditService.sendMessageToMQ(spuModelDto);
    }
}
