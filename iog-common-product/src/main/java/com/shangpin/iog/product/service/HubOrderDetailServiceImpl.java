package com.shangpin.iog.product.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.dto.HubOrderDetailDTO;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.OrderDetailDTO;
import com.shangpin.iog.product.dao.HubOrderDetailMapper;
import com.shangpin.iog.product.dao.OrderDetailMapper;
import com.shangpin.iog.service.HubOrderDetailService;
import com.shangpin.iog.service.OrderDetailService;

@Service
public class HubOrderDetailServiceImpl implements HubOrderDetailService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final static String UPDATE_ERROR = "更新订单状态失败";
    private final static String UPDATE_EXCEPTON_MSG_ERROR = "更新订单异常信息时失败";

    @Autowired
    HubOrderDetailMapper orderDetailDao;

    @Override
    public List<HubOrderDetailDTO> getOrderBySupplierIdAndTime(String supplier,
                                                               Date startDate, Date endDate,String CGD,String spSkuId,String supplierSkuId,String orderStatus,String pushStatus, Integer pageIndex, Integer pageSize) {

        return orderDetailDao.getOrderBySupplierIdAndTime(supplier, startDate,
                endDate,CGD,spSkuId,supplierSkuId,orderStatus,pushStatus, new RowBounds(pageIndex, pageSize));
    }

    @Override
    public int getOrderTotalBySupplierIdAndTime(String supplier, String startTime,
                                                String endTime,String CGD,String spSkuId,String supplierSkuId,String orderStatus,String pushStatus){
        return orderDetailDao.getOrderTotalBySupplierIdAndTime(supplier,startTime,endTime,CGD,spSkuId,supplierSkuId,orderStatus,pushStatus);
    }
}
