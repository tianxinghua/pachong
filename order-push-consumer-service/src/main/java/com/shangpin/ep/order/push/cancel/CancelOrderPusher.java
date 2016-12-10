package com.shangpin.ep.order.push.cancel;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.push.common.HandleMessageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.stream.sink.message.SupplierOrderSync;
import com.shangpin.ep.order.enumeration.HandleStep;
import com.shangpin.ep.order.enumeration.LogLeve;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.enumeration.OrderStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.exception.ServiceException;
import com.shangpin.ep.order.exception.ServiceMessageException;
import com.shangpin.ep.order.module.log.service.IHubOrderLogService;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.order.service.IHubOrderService;
import com.shangpin.ep.order.module.order.service.impl.OrderCommonUtil;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.orderapiservice.impl.OrderHandleSearch;
import com.shangpin.ep.order.push.common.AbstractPusher;
import com.shangpin.ep.order.push.common.GlobalConstant;

/**
 * <p>Title:CancelOrderPusher.java </p>
 * <p>Description: 取消订单推送器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月20日 下午9:08:35
 */
@Service
public class CancelOrderPusher extends AbstractPusher{
	
	@Autowired
	OrderCommonUtil orderCommonUtil;

	@Autowired
	IHubOrderService hubOrderService;

	@Autowired
	OrderHandleSearch orderHandleSearch;

	@Autowired
	IHubOrderLogService orderLogService;


	@Autowired
	HandleMessageException handleMessageException;


	@Autowired
	SupplierProperties supplierProperties;
	
	/**
	 * 推送取消订单
	 * @param message 消息
	 * @param headers 消息头
	 */
	public void push(SupplierOrderSync message, Map<String, Object> headers) throws Exception {
		//记录日志
		LogCommon.recordLog("cancel message origin content :"+message.toString() +
				"　message-header content :" +headers.toString(), LogLeve.INFO);
		try {
			handleCancel(message, headers);
		} catch (Exception e) {
			handleMessageException.handleException(message,headers,e);
			//内部延迟短些 推送的延迟产些 以秒为单位（1000代表一秒）
			int delay = supplierProperties.getSupplier().getDelayTime();
			this.reTry(message,delay*60,(Integer) headers.get(GlobalConstant.MESSAGE_HEADER_ORDER_STATUS_KEY),(Integer) headers.get(GlobalConstant.MESSAGE_HEADER_PUSH_STATUS_KEY),
					(null==headers.get(GlobalConstant.MESSAGE_HEADER_X_RETRIES_KEY)?0:(Integer)headers.get(GlobalConstant.MESSAGE_HEADER_X_RETRIES_KEY)),null);

		}
		
	}

	/***
	 * 业务处理
	 * @param message
	 * @param headers

	 * @throws ServiceException
     */
	private void handleCancel(SupplierOrderSync message, Map<String, Object> headers) throws Exception {
		//获取拆单后的信息
		Map<String, List<OrderDTO>> supplierOrderListMap = orderCommonUtil.getOrderDTO(message,headers);
		Set<String> supplierIdSet = supplierOrderListMap.keySet();
		//是否订单对接
		boolean isOrderApi = true;
		if (null != supplierOrderListMap && supplierOrderListMap.size() > 0) {
			for(String supplierId:supplierIdSet){
	            IOrderService iOrderService = orderHandleSearch.getHander(supplierId);
				if(null==iOrderService){
					isOrderApi = false;
				}
	            List<OrderDTO> orderDTOs = supplierOrderListMap.get(supplierId);
	            
	            if(orderDTOs!=null&&orderDTOs.size()>0){
	            	 for(OrderDTO orderDTO:orderDTOs){
	            		
	            		 //如果订单已支付，则不处理
	            		 if(orderDTO.getOrderStatus()!=null&&orderDTO.getOrderStatus().getIndex()==OrderStatus.PAYED.getIndex()){
	            			 continue;
	            		 }
	            		//处理订单状态
	     				if(isNeedHandle(orderDTO,HandleStep.HANDLE_ORDER)){
	     					handleOrderStatus(orderDTO,headers);
	     				}
	     				if(isOrderApi){//订单对接
	     					//处理推送
	     					handlePush(iOrderService, orderDTO,headers);

	     				}
	 	            }
	            }else{
	            	LogCommon.recordLog("取消订单" + message.getOrderNo() + "在数据库中未查询到此订单信息");
//	    			orderCommonUtil.sendMail("取消订单失败","取消订单" + message.getOrderNo() + "在数据库中未查询到此订单信息");
	    			throw new ServiceMessageException("取消订单" + message.getOrderNo() + "在数据库中未查询到此订单信息");
	            }
	        }
		}else {
			LogCommon.recordLog("取消订单" + message.getOrderNo() + "消息体转换成订单对象后为空");
//			orderCommonUtil.sendMail("取消订单失败","取消订单时候" + message.getOrderNo() + "消息体转换成订单对象后为空");
			throw new ServiceMessageException("取消订单" + message.getOrderNo() + "消息体转换成订单对象后为空");
		}
	
	}
	/**
	 * 处理推送
	 * 需要注意 如果推送状态为空  则是新产生的订单  说明处理顺序已经混乱 需要重新锁库

	 * @param iOrderService   :具体推送的实现类
	 * @param orderDTO
	 * @param headers
	 * @throws ServiceException
     */
	private void handlePush(IOrderService iOrderService, OrderDTO orderDTO, Map<String, Object> headers) throws  Exception{

		orderDTO.setErrorType(null);
	    if(isNeedHandle(orderDTO,HandleStep.HANDLE_PUSH)){
			iOrderService.handleCancelOrder(orderDTO);
		}
		this.handlePushStauts(orderDTO,headers);
		//推送发生错误 需要重新发送
		if(null!=orderDTO.getErrorType()){
			throw new Exception("取消推送出错，需要重试.错误原因: " + orderDTO.getLogContent());
		}

	}
	private void handlePushStauts(OrderDTO orderDTO, Map<String, Object> headers) throws Exception {
		//更新推送状态
		hubOrderService.updateHubOrderDetailPushStatus(orderDTO);
		orderCommonUtil.setStatusIntoHeader(headers, orderDTO);
		LogCommon.loggerOrder(orderDTO,LogLeve.INFO);

	}
	/**
	 * 判断是否需要操作下一步 ture :需要
	 * @param orderDTO
	 * @param handleStep
	 * @return
	 * @throws Exception
     */
	private boolean isNeedHandle(OrderDTO orderDTO, HandleStep handleStep) throws Exception {
		boolean isNeed = true;
		
		if(null==orderDTO.getExceptionOrderStatus()) {//新的消息 需要处理
			return isNeed;
		}
		
	    if(handleStep.getIndex()==HandleStep.HANDLE_ORDER.getIndex()){ //订单处理
			if(orderDTO.getOrderStatus().getIndex()!=orderDTO.getExceptionOrderStatus().getIndex()){// 修改订单状态时失败
				if(orderDTO.getOrderStatus().getIndex()!=OrderStatus.NO_PAY.getIndex()&&
						orderDTO.getOrderStatus().getIndex()!=OrderStatus.PAYED.getIndex()){
					//不能再做支付
					throw new ServiceMessageException("业务发生混乱,不能做处理");
				}

			}else{
				if(orderDTO.getOrderStatus().getIndex()==OrderStatus.NO_PAY_CANCELLED.getIndex()){ //已支付 不做处理
					isNeed = false;
				}
			}
		}else if((handleStep.getIndex()==HandleStep.HANDLE_PUSH.getIndex())){//推送处理
			if(orderDTO.getPushStatus().getIndex()!=orderDTO.getExceptionPushStatus().getIndex()){ //推送状态不等
				if(orderDTO.getExceptionPushStatus().getIndex()!=PushStatus.LOCK_CANCELLED_ERROR.getIndex()){//更新数据库即可
					isNeed = false;
				}
			}else{
				if(orderDTO.getPushStatus().getIndex()==PushStatus.LOCK_CANCELLED_ERROR.getIndex()){ //推送异常 重新推送

				}else{ //不是推送出错
					isNeed = false;
				}

			}
		}
		return isNeed;
	}
	/**
	 * 处理订单状态
	 * @param orderDTO
	 * @param headers
	 * @throws Exception
     */
	private void handleOrderStatus(OrderDTO orderDTO, Map<String, Object> headers) throws Exception {
		orderDTO.setCancelTime(new Date());
		orderDTO.setOrderStatus(OrderStatus.NO_PAY_CANCELLED);
		hubOrderService.updateHubOrderDetailOrderStatus(orderDTO);
		//设置订单及推送的状态
		orderCommonUtil.setStatusIntoHeader(headers,orderDTO);

		LogCommon.loggerOrder(orderDTO,LogLeve.INFO);
	}


	



}
