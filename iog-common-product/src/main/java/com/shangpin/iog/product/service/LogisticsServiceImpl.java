package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
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
import java.util.*;

/**
 * Created by lizhongren on 2016/1/11.
 */
@Service
public class LogisticsServiceImpl implements LogisticsService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    LogisticsMapper logisticsDAO;

    @Override
    public void save(OrderDTO orderDTO,String logisticsCompany,String trackNumber,String shippedTime) throws ServiceException {
        try {
            LogisticsDTO logisticsDTO = new LogisticsDTO();
            logisticsDTO.setLogisticsCompany(logisticsCompany);
            logisticsDTO.setTrackNumber(trackNumber);
            logisticsDTO.setShippedTime(shippedTime);
            logisticsDTO.setCreateTime(new Date());
            logisticsDTO.setSupplierId(orderDTO.getSupplierId());
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
    public List<String> findNotConfirmSupplierLogisticsNumber(String supplierId, Date searchDate) {
        String dateTime = DateTimeUtil.convertFormat(searchDate,"yyyy-MM-dd HH:mm:ss");
        return logisticsDAO.findNotConfirmSupplierLogisticsNumber(supplierId,dateTime) ;
    }

    @Override
    public LogisticsDTO findPurchaseDetailNoBySupplierIdAndTrackNumber(String supplierId,String trackNmuber) {

        List<LogisticsDTO> logisticsDTOList= logisticsDAO.findPurchaseDetailNoBySupplierAndTrackNumber(supplierId,trackNmuber) ;
        LogisticsDTO  returnDto = new LogisticsDTO();
        returnDto.setTrackNumber(trackNmuber);
        String purchaseDetailNo="";
        List<String> purchaseDetailList = new ArrayList<>();
        for(LogisticsDTO dto:logisticsDTOList){
            returnDto.setLogisticsCompany(dto.getLogisticsCompany());
            returnDto.setPurchaseNo(dto.getPurchaseNo());
            purchaseDetailNo=dto.getPurchaseDetailNo();
            String[] purchaseDetailNoArray = purchaseDetailNo.split(";");
            if(null!=purchaseDetailNoArray){
                for(String purchaseDetailNum:purchaseDetailNoArray){
                    purchaseDetailList.add(purchaseDetailNum);

                }
            }


        }
        returnDto.setPurchaseDetailList(purchaseDetailList);

        return returnDto;
    }


    @Override
    @Transactional(rollbackFor = {ServiceException.class})
    public void updateInvoice(String supplierId,String trackNum ,String spInvoice,Date updateTime) throws ServiceException {

        Map<String,Object> map = new HashMap<>();
        map.put("supplierId",supplierId);
        map.put("trackNumber",trackNum);
        map.put("spInvoice",spInvoice);
        map.put("updateTime",updateTime);
        try {
            logisticsDAO.updateMulti(map);
        } catch (Exception e) {
            logger.error("供货商:"+ supplierId + " 的物流单号:" + trackNum + " 更新尚品的物流单号:" + spInvoice + "失败" );
            throw  new ServiceMessageException("更新物流信息失败");
        }


    }
}
