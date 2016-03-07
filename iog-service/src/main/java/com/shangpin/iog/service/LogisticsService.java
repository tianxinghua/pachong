package com.shangpin.iog.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.LogisticsDTO;
import com.shangpin.iog.dto.OrderDTO;

import java.util.Date;
import java.util.List;

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
     * 获取小于指定查询时间尚未推送给SOP的供货商的发货单号信息
     * @param supplierId    供货商编号
     * @param searchDate    查询日期
     * @return
     */
    public List<String>   findNotConfirmSupplierLogisticsNumber(String supplierId,Date searchDate);

    /**
     * 返回拼装的物流信息 包含 供货商使用的物流公司、物流单号和 尚品采购够单明细信息列表
     * @param supplierId   供货商号
     * @param trackNmuber  供货商物流单号
     * @return
     */
    public LogisticsDTO findPurchaseDetailNoBySupplierIdAndTrackNumber(String supplierId,String trackNmuber);

    /**
     * 更新尚品发货单号
     * @param supplierId 供货商编号
     * @param trackNum   供货商的发货单号
     * @param spInvoice  尚品的发货单号
     * @param updateTime  查询时间
     * @throws ServiceException
     */
    public void updateInvoice(String supplierId,String trackNum ,String spInvoice,Date updateTime) throws ServiceException;
}
