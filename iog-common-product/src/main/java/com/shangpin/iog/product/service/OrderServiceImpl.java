package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.product.dao.OrderMapper;
import com.shangpin.iog.service.OrderService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by loyalty on 15/9/11.
 */
@Service
public class OrderServiceImpl implements OrderService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private  final  static String UPDATE_ERROR = "更新订单状态失败";

    private  final  static  String UPDATE_EXCEPTON_MSG_ERROR="更新订单异常信息时失败";

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
    public void update(OrderDTO orderDTO) throws ServiceException {
        try {
            orderDAO.update(orderDTO);
        } catch (SQLException e) {
            logger.error("订单"+ orderDTO.getSpOrderId() +"更新失败："+ e.getMessage());
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
	public List<OrderDTO>  getOrderBySupplierIdAndOrderStatus(String supplierId,
			String status, String date) throws ServiceException {
		
		 return  orderDAO.findBySupplierIdAndStatusAndDate(supplierId, status,date);
	}
    @Override
    public List<OrderDTO> getOrderBySupplierIdAndOrderStatus(String supplierId, String status) throws ServiceException {
        return  orderDAO.findBySupplierIdAndStatus(supplierId, status);
    }

    @Override
    public List<OrderDTO> getExceptionOrder() throws ServiceException {
    	return orderDAO.findExceptionOrder();
    }

    @Override
    public void updateOrderMsg(Map<String, String> statusMap) throws ServiceException {
        judgeMapParam(statusMap);
        try {
            orderDAO.updateOrderMsg(statusMap);
        } catch (Exception e) {
            logger.error("更改订单 ： " + statusMap.get("uuid") + "失败" );
            e.printStackTrace();
        }
    }

    @Override
    public void updateOrderStatus(Map<String, String> statusMap) throws ServiceException {
        judgeMapParam(statusMap);
        try {
            orderDAO.updateOrderStatus(statusMap);
        } catch (Exception e) {
            logger.error(UPDATE_ERROR);
            e.printStackTrace();
        }


    }

    @Override
    public void updateExceptionMsg(Map<String, String> exceptionMap) throws ServiceException {
        judgeMapParam(exceptionMap);
        try {
            orderDAO.updateOrderExceptionMsg(exceptionMap);
        } catch (Exception e) {
            logger.error(UPDATE_EXCEPTON_MSG_ERROR);
            e.printStackTrace();
        }
    }

    @Override
    public void updateDeliveryNo(Map<String, String> exceptionMap) throws ServiceException {
        judgeMapParam(exceptionMap);

        try {
            orderDAO.updateDeliveryNo(exceptionMap);
        } catch (Exception e) {
            logger.error(exceptionMap.get("uuid")+"更改发货单失败");
            e.printStackTrace();
        }
    }

    private void judgeMapParam(Map<String, String> exceptionMap) throws ServiceMessageException {
        if(null==exceptionMap||exceptionMap.size()==0) throw new ServiceMessageException("参数传入错误");

    }

    @Override
    public String getUuIdBySpOrderId(String spOrderId) throws ServiceException {
        String uuid="";
        try{
            OrderDTO dto = orderDAO.findBySpOrderId(spOrderId);
            uuid=dto.getUuId();
        }catch (Exception e){

        }
        return uuid;
    }

    @Override
    public OrderDTO getOrderByPurchaseNo(String purchaseNo) throws ServiceException {
        if(StringUtils.isBlank(purchaseNo)) throw new ServiceMessageException("采购单参数为空");
        return orderDAO.findByPurchaseNo(purchaseNo);
    }

    @Override
    public OrderDTO getOrderByOrderNo(String orderNo) throws ServiceException {
        if(StringUtils.isBlank(orderNo)) throw new ServiceMessageException("订单编号参数为空");
        return orderDAO.findBySpOrderId(orderNo);
    }

    @Override
    public OrderDTO getOrderByUuId(String uuid) throws ServiceException {
        if(StringUtils.isBlank(uuid)) throw new ServiceMessageException("唯一编号参数为空");
        return orderDAO.findByUuId(uuid);
    }



}
