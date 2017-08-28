package com.shangpin.ep.order.module.orderapiservice.impl;


import java.rmi.RemoteException;
import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ep.order.common.HandleException;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.LogLeve;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;

import Magento.PortType;
import Magento.PortTypeProxy;


@Component("clothingOrderService")
public class ClothingOrderServiceImpl implements IOrderService {

	
	@Autowired
    LogCommon logCommon;    

    @Autowired
    HandleException handleException;

    @Autowired
    ParisiOrderUtil parisiOrderUtil;
    
    @Autowired
    SupplierProperties supplierProperties;
    
    private PortType proxy = new PortTypeProxy();

	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");
	
    private  String strPassword = "05TBbJHa^vgL4b!";
    private  String strKey = "Shangpin";
    
    @PostConstruct
    public void init(){
    	//线上
//    	strPassword = supplierProperties.getClothing().getStrPassword();
//    	strKey = supplierProperties.getClothing().getStrKey();
    }
	/**
	 * 锁库存
	 */
    @Override
   	public void handleSupplierOrder(OrderDTO orderDTO) {
    	
		try {
			String seesionId = proxy.login(strKey, strPassword);
			boolean isflg = proxy.salesOrderHold(seesionId, orderDTO.getSpOrderId());
			
			orderDTO.setLogContent("handleSupplierOrder下单返回结果==" + isflg + ",推送参数：" + "seesionId:"+ seesionId + "spOrderId:"+orderDTO.getSpOrderId());
			System.out.println(isflg);
			
	   		orderDTO.setLockStockTime(new Date());
	   		orderDTO.setPushStatus(PushStatus.LOCK_PLACED);
	   		orderDTO.setLogContent("");
	   		logCommon.loggerOrder(orderDTO, LogLeve.INFO);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
   	}

   	/**
   	 * 推送订单
   	 */
   	@Override
   	public void handleConfirmOrder(OrderDTO orderDTO) {
   		try {
   			String seesionId = proxy.login(strKey, strPassword);
   			boolean isflg = proxy.salesOrderAddComment(seesionId, orderDTO.getSpOrderId(), "processing", "", "");
   			orderDTO.setLogContent("handleConfirmOrder下单返回结果==" + isflg + ",推送参数：" + "seesionId:"+ seesionId + "spOrderId:"+orderDTO.getSpOrderId());
   		} catch (Exception e) {
			e.printStackTrace();
		}
   	}

   	@Override
   	public void handleCancelOrder(OrderDTO deleteOrder) {
   		try {
	   		String seesionId = proxy.login(strKey, strPassword);
			boolean isflg = proxy.salesOrderUnhold(seesionId, deleteOrder.getSpOrderId());
			deleteOrder.setLogContent("handleCancelOrder退单返回结果==" + isflg + ",推送参数：" + "seesionId:"+ seesionId + "spOrderId:"+deleteOrder.getSpOrderId());
	   		
	   		deleteOrder.setPushStatus(PushStatus.LOCK_CANCELLED);
	   		deleteOrder.setCancelTime(new Date());
	   		deleteOrder.setLogContent("------取消锁库结束-------");
	   		
	   		logCommon.loggerOrder(deleteOrder, LogTypeStatus.LOCK_CANCELLED_LOG);
   		} catch (Exception e) {
			e.printStackTrace();
		}
   	}

   	@Override
   	public void handleRefundlOrder(OrderDTO deleteOrder) {
   		try {
   			String seesionId = proxy.login(strKey, strPassword);
   			boolean isflg = proxy.salesOrderCancel(seesionId, deleteOrder.getSpOrderId());
   			deleteOrder.setLogContent("handleRefundlOrder退单返回结果==" + isflg + ",推送参数：" + "seesionId:"+ seesionId + "spOrderId:"+deleteOrder.getSpOrderId());
   		} catch (Exception e) {
			e.printStackTrace();
		}
   	}


   	public String handleException(String url,OrderDTO orderDTO,Map<String, String> map,Throwable e){		
   		handleException.handleException(orderDTO, e); 
   		return null;
   	}
	
	public static void main(String[] args){
		ClothingOrderServiceImpl serviceImpl = new ClothingOrderServiceImpl();
    	OrderDTO orderDTO = new OrderDTO();
    	orderDTO.setDetail("37372-18-6-9 mth:1");
    	orderDTO.setSpOrderId("0123456789");
    	orderDTO.setPurchaseNo("9876543210");
    	serviceImpl.handleSupplierOrder(orderDTO);
    	serviceImpl.handleCancelOrder(orderDTO);
    }
	
}
