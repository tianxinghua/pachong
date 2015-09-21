package com.shangpin.iog.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.ReturnOrderDTO;

import java.util.Map;

/**
 * Created by sunny on 2015/9/19.
 */
public interface ReturnOrderService {
    /**
     * 保存退单信息
     * @param returnOrderDTO 退单DTO
     * @throws ServiceException
     */
    public void saveOrder(ReturnOrderDTO returnOrderDTO ) throws ServiceException;
    /**
     * 修改退单状态
     * @param statusMap 状态信息  ORDERID(UUID),STATUS
     * @throws ServiceException
     */
    public void updateReturnOrderStatus(Map<String,String> statusMap) throws ServiceException;
}
