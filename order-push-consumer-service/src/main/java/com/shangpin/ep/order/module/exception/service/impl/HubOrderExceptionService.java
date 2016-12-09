package com.shangpin.ep.order.module.exception.service.impl;

import com.shangpin.ep.order.conf.stream.sink.message.SupplierOrderDetailSync;
import com.shangpin.ep.order.conf.stream.sink.message.SupplierOrderSync;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ep.order.module.exception.bean.HubOrderExceptionWithBLOBs;
import com.shangpin.ep.order.module.exception.mapper.HubOrderExceptionMapper;
import com.shangpin.ep.order.module.exception.service.IHubOrderExceptionService;

import java.util.Date;
import java.util.List;

/**
 * <p>Title:HubOrderExceptionService.java </p>
 * <p>Description: HUB订单系统中异常订单消息业务逻辑处理接口规范实现类</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午1:56:04
 */
@Service
public class HubOrderExceptionService implements IHubOrderExceptionService {
    @Autowired
    HubOrderExceptionMapper hubOrderExceptionMapper;

	@Override
	public boolean saveHubOrderException(HubOrderExceptionWithBLOBs supplierOrderMsg) throws Exception {
		
		hubOrderExceptionMapper.insert(supplierOrderMsg);
		return true;
	}

	@Override
	public boolean saveHubOrderException(String content, String reason) throws Exception {
		HubOrderExceptionWithBLOBs hubOrderExceptionWithBLOBs = new HubOrderExceptionWithBLOBs();
		hubOrderExceptionWithBLOBs.setCreateTime(new Date());
		hubOrderExceptionWithBLOBs.setContent(content);
		hubOrderExceptionWithBLOBs.setExceptionReason(reason);
		return saveHubOrderException(hubOrderExceptionWithBLOBs);
	}

	@Override
	public boolean saveHubOrderException(SupplierOrderSync orderSync, String reason) throws Exception {
		List<SupplierOrderDetailSync> orderDetailSyncs = orderSync.getSyncDetailDto();
		String supplierNo="";
		if(null!=orderDetailSyncs){
			for(SupplierOrderDetailSync orderDetailSync :orderDetailSyncs){
				supplierNo = orderDetailSync.getSupplierNo();
			}
		}
		HubOrderExceptionWithBLOBs hubOrderExceptionWithBLOBs = new HubOrderExceptionWithBLOBs();
		hubOrderExceptionWithBLOBs.setSupplierNo(supplierNo);
		hubOrderExceptionWithBLOBs.setCreateTime(new Date());
		hubOrderExceptionWithBLOBs.setContent(orderSync.toString());
		hubOrderExceptionWithBLOBs.setExceptionReason(reason);
		return saveHubOrderException(hubOrderExceptionWithBLOBs);
	}


}
