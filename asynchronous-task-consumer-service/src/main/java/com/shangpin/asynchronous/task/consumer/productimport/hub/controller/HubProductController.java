package com.shangpin.asynchronous.task.consumer.productimport.hub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.asynchronous.task.consumer.productimport.hub.HubProductImportHandler;
import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;
import com.shangpin.ephub.response.HubResponse;


/**
 * <p>HubSpuImportTaskController </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月17日 下午5:25:30
 */
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/test")
public class HubProductController {
	
	@Autowired
	HubProductImportHandler hubProductImportHandler;
	
	@RequestMapping(value = "/test",method = RequestMethod.POST)
//	@ResponseBody
    public void importSpu(@RequestBody ProductImportTask dto){
	        	
		try {
			 hubProductImportHandler.hubProductImportStreamListen(dto,null);
		} catch (Exception e) {
		}
    }
}
