package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.HubOrderDetail;
import com.shangpin.iog.dto.OrderDetailDTO;
import com.shangpin.iog.product.dao.HubOrderDetailMapper;
import com.shangpin.iog.product.dao.OrderDetailMapper;
import com.shangpin.iog.service.HubOrderDetailService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lizhongren on 2016/12/7.
 */
@Service
public class HubOrderDetailServiceImpl implements HubOrderDetailService {

    @Autowired
    HubOrderDetailMapper orderDetailDao;

    @Override
    public List<OrderDetailDTO> getOrderDetailBySpOrderDetailNo(String orderDetailNo) throws ServiceException {
        return null;
    }

    @Override
    public List<OrderDetailDTO> getOrderDetailBySupplierIdAndOrderStatus(String supplierId, String status) throws ServiceException {
        return null;
    }

    @Override
    public List<OrderDetailDTO> getOrderBySupplierIdAndOrderStatus(String supplierId, String status, String date) throws ServiceException {
        return null;
    }

    @Override
    public List<OrderDetailDTO> getOrderBySupplierIdAndOrderStatusAndExceptionStatus(String supplierId, String status, String excState, String date, int interval) throws ServiceException {
        return null;
    }

    @Override
    public List<OrderDetailDTO> getOrderBySupplierIdAndOrderStatusAndTime(String supplierId, String status, String startTime, String endTime) throws ServiceException {
        return null;
    }

    @Override
    public List<OrderDetailDTO> getOrderBySupplierIdAndOrderStatusAndUpdateTime(String supplierId, String status, String startTime, String endTime) throws ServiceException {
        return null;
    }

    @Override
    public List<OrderDetailDTO> getOrderBySupplierIdAndTime(String supplier, Date startDate, Date endDate, String CGD, String spSkuId, String supplierSkuId, String status, Integer pageIndex, Integer pageSize) {
        List<HubOrderDetail> hubOrderDetails =  orderDetailDao.getOrderBySupplierIdAndTime(supplier, startDate,
                endDate,CGD,spSkuId,supplierSkuId,status, new RowBounds(pageIndex, pageSize));
        List<OrderDetailDTO>  result = new ArrayList<>();
        changeOrderDTO(hubOrderDetails, result);
        return result ;
    }

    @Override
    public int getOrderTotalBySupplierIdAndTime(String supplier, String startTime, String endTime, String CGD, String spSkuId, String supplierSkuId, String status) {
        return orderDetailDao.getOrderTotalBySupplierIdAndTime(supplier,startTime,endTime,CGD,spSkuId,supplierSkuId,status);
    }


    private void changeOrderDTO(List<HubOrderDetail> hubOrderDetails, List<OrderDetailDTO> result) {
        for(HubOrderDetail hubOrderDetail:hubOrderDetails){
            OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
            BeanUtils.copyProperties(hubOrderDetail,orderDetailDTO);
            result.add(orderDetailDTO);
        }
    }

    @Override
    public List<OrderDetailDTO> getOrderBySupplierIdAndTime(String supplier, Date startDate, Date endDate) {
        return null;
    }

    @Override
    public List<OrderDetailDTO> getDetailDTOByEpMasterOrderNo(String epMasterOrderNo) throws ServiceException {
        return null;
    }
}
