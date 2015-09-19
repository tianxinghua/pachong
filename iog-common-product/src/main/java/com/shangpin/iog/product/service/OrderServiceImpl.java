package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.product.dao.OrderMapper;
import com.shangpin.iog.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by loyalty on 15/9/11.
 */
@Service
public class OrderServiceImpl implements OrderService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private  final  static String UPDATE_ERROR = "更新订单状态失败";

    @Autowired
    OrderMapper orderDAO;

    @Override
    public void saveOrder(OrderDTO orderDTO) throws ServiceException {
        try {
            orderDAO.save(orderDTO);
        } catch (SQLException e) {
            logger.error("订单保存失败："+ e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public List<String> getOrderIdBySupplierIdAndOrderStatus(String supplierId, String status) throws ServiceException {
        List<String>  uuIdList = new ArrayList<>();
        try {
            List<OrderDTO> orderDTOList = orderDAO.findBySupplierIdAndStatus(supplierId, status);
            for(OrderDTO dto:orderDTOList){
                uuIdList.add(dto.getUuId());
            }
        } catch (Exception e) {
            logger.error("查询订单失败：供货商："+supplierId +  " 订单状态：" + status + " | " + e.getMessage());
            e.printStackTrace();
        }

        return uuIdList;
    }

    @Override
    public void updateOrderStatus(Map<String, String> statusMap) throws ServiceException {

        try {
            orderDAO.updateOrderStatus(statusMap);
        } catch (Exception e) {
            logger.error(UPDATE_ERROR);
            e.printStackTrace();
        }


    }
}
