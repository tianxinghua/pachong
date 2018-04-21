package com.shangpin.asynchronous.task.consumer.rest.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.esotericsoftware.minlog.Log;
import com.shangpin.asynchronous.task.consumer.rest.service.MessageSendService;
import com.shangpin.ephub.client.data.mysql.product.dto.SpuModelDto;

/**

 * <p>Description: 选品服务</p>
 *
 */
@RestController
@RequestMapping(value = "/hub-spu-pending")
public class SpuPendingAuditController {
	
	@Autowired
	private MessageSendService spuPendingAuditService;
	/**
	 *
	 * @throws Exception
	 */
	@RequestMapping(value = "/audit", method = RequestMethod.POST)
	public boolean  auditSpu(@RequestBody SpuModelDto spuModelDto) {
		Log.info("推送到审核队列："+spuModelDto.getSpuModel());
		return spuPendingAuditService.sendMessageToMQ(spuModelDto);
    }
}
