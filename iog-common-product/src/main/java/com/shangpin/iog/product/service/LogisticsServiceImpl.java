package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.dto.LogisticsDTO;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.product.dao.LogisticsMapper;
import com.shangpin.iog.service.LogisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by lizhongren on 2016/1/11.
 */
@Service
public class LogisticsServiceImpl implements LogisticsService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    LogisticsMapper logisticsDAO;

    @Override
    public void save(OrderDTO orderDTO,String logisticsCompany,String trackNumber,String shipDate) throws ServiceException {
        try {
            LogisticsDTO logisticsDTO = new LogisticsDTO();
            logisticsDTO.setLogisticsCompany(logisticsCompany);
            logisticsDTO.setTrackNumber(trackNumber);
            logisticsDTO.setShippedDate(shipDate);
            logisticsDTO.setCreateDate(new Date());
            logisticsDTO.setSupplierId(orderDTO.getSupplierNo());
            logisticsDTO.setPurchaseNo(orderDTO.getSpPurchaseNo());
            logisticsDTO.setPurchaseDetailNo(orderDTO.getSpPurchaseDetailNo());
            logisticsDTO.setOrderNo(orderDTO.getSpOrderId());

            logisticsDAO.save(logisticsDTO);
        } catch (SQLException e) {
            logger.error("采购单："+ orderDTO.getSpPurchaseNo()+"，保存失败："+ e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    @Transactional(rollbackFor = {ServiceException.class})
    public void updateInvoice(String supplierId,Date  searchDate) throws ServiceException {
        String dateTime = DateTimeUtil.convertFormat(searchDate,"yyyy-MM-hh HH:mm:ss");
        List<String> trackNumberList = logisticsDAO.findNotConfirmSupplierLogisticsNumber(supplierId,dateTime) ;
        for(String trackNmuber: trackNumberList){
            List<LogisticsDTO> logisticsDTOList = logisticsDAO.findPurchaseDetailNoByTrackNumber(trackNmuber);
        }


    }
}
