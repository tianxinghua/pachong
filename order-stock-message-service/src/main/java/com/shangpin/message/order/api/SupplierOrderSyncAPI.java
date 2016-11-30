/**
 * @项目名称: scm-message-service
 * @文件名称: SupplierOrderSyncAPI.java
 * @Date: 2016年10月25日
 * @Copyright: 2016 www.shangpin.com Inc. All rights reserved.
 * 注意：本内容仅限于xxx公司内部传阅，禁止外泄以及用于其他的商业目的.
*/
package com.shangpin.message.order.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.message.common.e.Code;
import com.shangpin.message.common.response.APIRsponse;
import com.shangpin.message.conf.stream.source.order.message.SupplierOrderSync;
import com.shangpin.message.order.dto.SupplierOrderDTO;
import com.shangpin.message.order.mongodb.SupplierOrderMongoDB;
import com.shangpin.message.order.mongodb.entity.SupplierOrderEntity;
import com.shangpin.message.order.service.SupplierOrderService;

import lombok.extern.slf4j.Slf4j;

/**
 *供应商订单同步接口服务
 *@Project: scm-message-service
 *@Author: yanxiaobin
 *@Date: 2016年10月25日
 *@Copyright: 2016 www.shangpin.com Inc. All rights reserved.
 */
@Slf4j
@RestController
@RequestMapping(value = "/message/api")
public class SupplierOrderSyncAPI {

	@Autowired
	private SupplierOrderService supplierOrderService;
	@Autowired
	private SupplierOrderMongoDB supplierOrderMongoDB;
	/**
	 * 推送同步供应商订单
	 * @param dto 数据传输对象
	 * @return APIRsponse 响应数据
	 */
	@RequestMapping(value = "/order-sync")
	public APIRsponse stockSync(@RequestBody SupplierOrderDTO dto){
		log.info("SCM消息系统OrderSync接口服务接收到调用方数据为======<<"+dto.toString()+">>======系统即将开始发送消息！");
		long start = System.currentTimeMillis();
		APIRsponse response = null;
		boolean flag = false;
		List<SupplierOrderSync> supplierOrders = null;
		try {
			supplierOrders = supplierOrderService.splitOrder(dto);//拆单
			//保存mongo
			flag = supplierOrderService.sendOrder(supplierOrders);
			if (flag) {
				response = new APIRsponse(Code.OK.getCode(), Code.OK.getMessage());
				log.info("SCM消息系统OrderSync接口服务已成功发送编号为======<<"+dto.getMessageId()+">>======的消息至RabbitMQ集群！耗时："+(System.currentTimeMillis() - start)+" milliseconds");
			} else {
				throw new RuntimeException("消息编号为======<<"+dto.toString()+">>======的消息发往Message Broker失败");
			}
		} catch (Exception e) {
			response = new APIRsponse(Code.NO.getCode(), e.getMessage());
			log.error("SCM消息系统OrderSync接口服务发送编号为======<<"+dto.getMessageId()+">>======的消息失败，异常为 :"+e.getMessage(), e);
			e.printStackTrace();
		} finally {
			SupplierOrderEntity supplierOrderEntity = null;
			try {
				supplierOrderEntity = new SupplierOrderEntity(dto.getMessageId(), dto, supplierOrders, flag);
				supplierOrderMongoDB.save(supplierOrderEntity);
			} catch (Exception e) {
				log.error("SCM消息系统OrderSync接口服务往Mongodb保存消息数据======<<"+supplierOrderEntity.toString()+">>======时发生异常，mongodb不可用，异常为 :"+e.getMessage(), e);
				e.printStackTrace();
			}
		}
		return response;
	}
}