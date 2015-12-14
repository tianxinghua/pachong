package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.framework.page.Page;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.product.dao.OrderMapper;
import com.shangpin.iog.service.OrderService;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
    
    private static String splitSign = ",";

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
    public boolean saveOrderWithResult(OrderDTO orderDTO) throws ServiceException {
        try {
            orderDAO.save(orderDTO);
            return true;
        } catch (Exception e) {
            logger.error("订单保存失败："+ e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public boolean checkOrderByOrderIdSupplier(OrderDTO orderDTO){
    	
    	boolean flag = false;
    	 try {
    		 OrderDTO order = orderDAO.checkOrderByOrderIdSupplier(orderDTO.getSupplierId(),orderDTO.getSpOrderId());
    		 if(order!=null){
    			 flag = true;
    		 }
         } catch (Exception e) {
             logger.error("订单保存失败："+ e.getMessage());
             e.printStackTrace();
         }
         return flag;
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

	@Override
	public List<OrderDTO> getOrderBySupplierIdAndTime(String supplier, Date startDate,
			Date endDate,Integer pageIndex, Integer pageSize) {
		
		return orderDAO.getOrderBySupplierIdAndTime(supplier, startDate, endDate, new RowBounds(pageIndex,pageSize));
	}
	
	public List<OrderDTO> getOrderBySupplierIdAndTime(String supplier, Date startDate, Date endDate){
		return orderDAO.getOrderBySupplierIdAndTime(supplier, startDate, endDate);
	}
	
	public Page<OrderDTO> getOrderBySupplierIdAndTime(String supplier, Date startDate,
			Date endDate,Integer pageIndex, Integer pageSize,String flag) throws ServiceMessageException{
		List<OrderDTO> orderList = new ArrayList<>();
		Page<OrderDTO> page = null;
		try{
			if(pageIndex != null && pageSize != null && pageIndex != -1 && pageSize != -1){
				
				orderList = this.getOrderBySupplierIdAndTime(supplier, startDate, endDate, pageIndex, pageSize);
				page = new Page<>(pageIndex, pageSize);
				
			}else{
				
				orderList = this.getOrderBySupplierIdAndTime(supplier, startDate, endDate);
				page = new Page<>(1, orderList.size());
				
			}	
			
			page.setItems(orderList);
			
		}catch(Exception ex){
			logger.error(ex.getMessage());
			ex.printStackTrace();
			throw new ServiceMessageException("数据导出失败");
		}
		
		return page;
	}

	
	@Override
	public StringBuffer exportOrder(String supplier, Date startDate,
			Date endDate, int pageIndex, int pageSize, String flag) {		
		
		Map<String, String> nameMap = new HashMap<String, String>();
    	nameMap.put("placed", "下订单成功");
    	nameMap.put("payed", "支付");
    	nameMap.put("cancelled", "取消成功");
    	nameMap.put("confirmed", "支付成功");
    	nameMap.put("nohandle", "超时不处理");
    	nameMap.put("waitplaced", "待下订单");
    	nameMap.put("waitcancel", "待取消");
    	nameMap.put("refunded", "退款成功");
    	nameMap.put("waitrefund", "待退款");
    	nameMap.put("purexpsuc", "采购异常Suc");
    	nameMap.put("purexperr", "采购异常Err");
		StringBuffer buffer = new StringBuffer("SupplierId 供货商" + splitSign
				+ "SpOrderId 尚品订单编号" + splitSign
				+ "SpPurchaseNo 采购单编号" + splitSign +"订单状态"+ splitSign+ "SpPurchaseDetailNo 采购单明细" + splitSign
				+ "Detail 供货商skuId:数量" + splitSign + "Memo 尚品skuId:数量" + splitSign
				+ "CreateTime" + splitSign + "UpdateTime" + splitSign
				+ "UuId").append("\r\n");
		Page<OrderDTO> page = null;
		try{
			
			page = this.getOrderBySupplierIdAndTime(supplier, startDate, endDate, pageIndex, pageSize, flag);
			String detail = "" ,memo = "",supplierName = "";
			if(page.getItems().size()>0){
				for(OrderDTO order :page.getItems()){
					
					if(StringUtils.isNotBlank(order.getSupplierName())){
						supplierName = order.getSupplierName();
					}else{
						supplierName = order.getSupplierId();
					}
					buffer.append(supplierName).append(splitSign);
					buffer.append(order.getSpOrderId()).append(splitSign);
					buffer.append(order.getSpPurchaseNo()).append(splitSign);
					buffer.append(nameMap.get(order.getStatus().toLowerCase())).append(splitSign);
					buffer.append(order.getSpPurchaseDetailNo()).append(splitSign);
					if(StringUtils.isNotBlank(order.getDetail())){
						detail = order.getDetail().replaceAll(splitSign, "");
					}
					buffer.append(detail).append(splitSign);
					if(StringUtils.isNotBlank(order.getMemo())){
						memo = order.getMemo().replaceAll(splitSign, "");
					}
					buffer.append(memo).append(splitSign);
					buffer.append(order.getCreateTime()).append(splitSign);
					buffer.append(order.getUpdateTime()).append(splitSign);
					buffer.append(order.getUuId());
					buffer.append("\r\n");
				}
			}
			
		}catch(Exception ex){
			logger.error(ex.getMessage());
			ex.printStackTrace();
		}
		
		return buffer;
	}



}
