/**
 * @项目名称: scm-message-service
 * @文件名称: StockSyncAPI.java
 * @Date: 2016年10月25日
 * @Copyright: 2016 www.shangpin.com Inc. All rights reserved.
 * 注意：本内容仅限于xxx公司内部传阅，禁止外泄以及用于其他的商业目的.
*/
package com.shangpin.message.stock.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.message.common.e.Code;
import com.shangpin.message.common.response.APIRsponse;
import com.shangpin.message.stock.bean.SupplierStockDetailSync;
import com.shangpin.message.stock.bean.SupplierStockSync;
import com.shangpin.message.stock.dto.SupplierStockDTO;
import com.shangpin.message.stock.dto.SupplierStockDetailDTO;
import com.shangpin.message.stock.service.SupplierStockService;

import lombok.extern.slf4j.Slf4j;

/**
 * 供应商库存同步接口服务
 *@Project: scm-message-service
 *@Author: yanxiaobin
 *@Date: 2016年10月25日
 *@Copyright: 2016 www.shangpin.com Inc. All rights reserved.
 */
@Slf4j
@RestController
@RequestMapping(value = "/message/api")
public class SupplierStockSyncAPI {
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 注入消息生产者
	 */
	@Autowired
	private SupplierStockService supplierStockService;
	/**
	 * 同步供应商库存实现：该服务将会往RabbitMQ发送到指定的消息队列供后台异步程序去消费
	 * @param dto 数据传输对象
	 * @return APIRsponse
	 */
	@RequestMapping(value = "/stock-sync")
	public APIRsponse stockSync(@RequestBody SupplierStockDTO dto){
		log.info("SCM消息系统StockSync接口服务接收到调用方数据为======<<"+dto.toString()+">>======, 系统即将开始发送消息！");
		long start = System.currentTimeMillis();
		String messageId = dto.getMessageId();
		String syncType = dto.getSyncType();
		Integer priority = dto.getPriority();
		List<SupplierStockDetailDTO> detailSyscDtos = dto.getStockDetailSyncDtos();
		List<SupplierStockDetailSync> detailSyncs = null;
		if (CollectionUtils.isNotEmpty(detailSyscDtos)) {
			detailSyncs = new ArrayList<>();
			for (SupplierStockDetailDTO supplierStockDetailSync : detailSyscDtos) {
				detailSyncs.add(new SupplierStockDetailSync(supplierStockDetailSync.getSupplierNo(), supplierStockDetailSync.getSkuNo()));
			}
		}
		SupplierStockSync message = new SupplierStockSync(messageId, syncType, detailSyncs,sdf.format(new Date()));
		APIRsponse response = null;
		try {
			if (priority == null) {//默认优先级最低
				priority = 0;
			}
			supplierStockService.sendMessageToRabbitMQ(message, priority);
			response = new APIRsponse(Code.OK.getCode(), Code.OK.getMessage());
			log.info("SCM消息系统已将编号为======<<"+messageId+">>======的消息成功发送至RabbitMQ！耗时："+(System.currentTimeMillis() - start)+" milliseconds");
		} catch (Throwable e) {
			response = new APIRsponse(Code.NO.getCode(), Code.NO.getMessage());
			log.error("SCM消息系统StockSync接口服务发送编号为======<<"+messageId+">>======的消息失败，异常为 :"+e.getMessage(), e);
			e.printStackTrace();
		}
		return response;
	}
}