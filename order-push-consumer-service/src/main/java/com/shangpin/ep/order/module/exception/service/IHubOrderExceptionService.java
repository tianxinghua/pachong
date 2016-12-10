package com.shangpin.ep.order.module.exception.service;

import com.shangpin.ep.order.conf.stream.sink.message.SupplierOrderSync;
import com.shangpin.ep.order.module.exception.bean.HubOrderExceptionWithBLOBs;

/**
 * <p>Title:IHubOrderExceptionService.java </p>
 * <p>Description: HUB订单系统中异常订单消息业务逻辑处理接口规范</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午1:54:41
 */
public interface IHubOrderExceptionService {
	
    /**
     * 保存各个供货商的订单和订单明细信息

     * @param supplierOrderMsg :key :供货商Id List 订单明细  拆单后的
     * @return
     */
    public boolean saveHubOrderException(HubOrderExceptionWithBLOBs supplierOrderMsg) throws Exception;

    /**
     * 保存各个供货商的订单和订单明细信息
     * @param content 消息体
     * @param reason  原因
     * @return
     * @throws Exception
     */
    public boolean saveHubOrderException(String content,String reason) throws Exception;

    /**
     *  保存订单的具体信息以及错误原因
     * @param orderSync   ：消息具体信息
     * @param reason     ： 原因
     * @return
     * @throws Exception
     */
    public boolean saveHubOrderException(SupplierOrderSync orderSync,String reason) throws Exception;
}
