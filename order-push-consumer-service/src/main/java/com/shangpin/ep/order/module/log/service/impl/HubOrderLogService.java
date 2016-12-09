package com.shangpin.ep.order.module.log.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ep.order.enumeration.ExceptionType;
import com.shangpin.ep.order.exception.ServiceException;
import com.shangpin.ep.order.exception.ServiceMessageException;
import com.shangpin.ep.order.module.log.bean.HubOrderLog;
import com.shangpin.ep.order.module.log.mapper.HubOrderLogMapper;
import com.shangpin.ep.order.module.log.service.IHubOrderLogService;
import com.shangpin.ep.order.module.order.bean.OrderDTO;

/**
 * <p>Title:HubOrderLogService.java </p>
 * <p>Description: HUB订单系统订单明细历史日志记录接口规范实现</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午1:58:12
 */
@Service
public class HubOrderLogService implements IHubOrderLogService {
    @Autowired
    HubOrderLogMapper hubOrderLogDAO;

    @Override
    public void saveOrderLog(HubOrderLog orderLog) throws ServiceException {
        try {
            hubOrderLogDAO.insert(orderLog);
        } catch (Exception e) {
            throw new ServiceMessageException(ExceptionType.DATABASE_HANDLE_EXCEPTION.toString(),"保存订单状态日志失败");
        }
    }

    @Override
    public void saveOrderLog(OrderDTO orderDTO) throws ServiceException {
        HubOrderLog hubOrderLog = new HubOrderLog();
        hubOrderLog.setOrderDetailId(orderDTO.getId().intValue());
        if(null!=orderDTO.getOrderStatus()){

            hubOrderLog.setOrderStatus(orderDTO.getOrderStatus().getIndex());
        }
        if(null!=orderDTO.getPushStatus()){

            hubOrderLog.setPushStatus(orderDTO.getPushStatus().getIndex());
        }
        if(null!=orderDTO.getBusinessType()){  //业务类型
            hubOrderLog.setOperateType(orderDTO.getBusinessType().getIndex());
        }

        hubOrderLog.setCreateTime(new Date());

        hubOrderLogDAO.insert(hubOrderLog);
    }


}
