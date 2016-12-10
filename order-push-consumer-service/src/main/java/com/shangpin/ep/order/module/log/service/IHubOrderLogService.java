package com.shangpin.ep.order.module.log.service;

import com.shangpin.ep.order.exception.ServiceException;
import com.shangpin.ep.order.module.log.bean.HubOrderLog;
import com.shangpin.ep.order.module.order.bean.OrderDTO;

/**
 * <p>Title:IHubOrderLogService.java </p>
 * <p>Description: HUB订单系统订单明细历史日志记录接口规范</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午1:56:56
 */
public interface IHubOrderLogService {
    /**
     * 保存订单状态变化日志
     * @param orderLog
     * @throws ServiceException
     */
    public void saveOrderLog(HubOrderLog orderLog) throws ServiceException;

    /**
     * 保存订单状态变化日志
     * @param orderDTO
     * @throws ServiceException
     */
    public void saveOrderLog(OrderDTO orderDTO) throws ServiceException;

}
