package com.shangpin.iog.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.LogisticsDTO;
import com.shangpin.iog.dto.OrderDTO;

import java.util.Date;

/**
 * Created by lizhongren on 2016/1/11.
 * 物流业务
 */
public interface LogisticsService {
    /**
     * 物流中心录入数据
     * @param orderDTO 订单信息
     * @param logisticsCompany 物流公司
     * @param trackNumber  物流单号
     * @param shipDate     发货时间
     * @throws ServiceException
     */
    public  void save(OrderDTO orderDTO,String logisticsCompany,String trackNumber,String shipDate) throws ServiceException;

    /**
     * 更新订单的尚品发货单号
     * @param supplierId  供货商ID
     * @param searchDate   查询时间
     * @throws ServiceException
     */
    public void updateInvoice(String supplierId ,Date  searchDate) throws ServiceException;
}
