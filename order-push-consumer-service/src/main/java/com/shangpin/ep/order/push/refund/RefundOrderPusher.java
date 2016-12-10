package com.shangpin.ep.order.push.refund;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.*;
import com.shangpin.ep.order.push.common.HandleMessageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.mail.message.ShangpinMail;
import com.shangpin.ep.order.conf.mail.sender.ShangpinMailSender;
import com.shangpin.ep.order.conf.stream.sink.message.SupplierOrderSync;
import com.shangpin.ep.order.exception.ServiceException;
import com.shangpin.ep.order.exception.ServiceMessageException;
import com.shangpin.ep.order.module.exception.bean.HubOrderExceptionWithBLOBs;
import com.shangpin.ep.order.module.exception.service.impl.HubOrderExceptionService;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.order.service.impl.HubOrderService;
import com.shangpin.ep.order.module.order.service.impl.OrderCommonUtil;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.orderapiservice.impl.OrderHandleSearch;
import com.shangpin.ep.order.push.common.AbstractPusher;
import com.shangpin.ep.order.push.common.GlobalConstant;

/**
 * <p>Title:RefundOrderPusher.java </p>
 * <p>Description: 订单退款推送器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月22日 上午10:30:23
 */
@Service
public class RefundOrderPusher extends AbstractPusher{
	
    @Autowired
    OrderCommonUtil orderCommonUtil;
    
    @Autowired
    OrderHandleSearch orderHandleSearch;

    @Autowired
    HubOrderService hubOrderService;
    @Autowired
    HubOrderExceptionService hubOrderExceptionService;
    @Autowired
    ShangpinMailSender mailSender;

	@Autowired
	SupplierProperties supplierProperties;


	@Autowired
	HandleMessageException handleMessageException;
	/**
	 * 退款订单推送器
	 * @param message 消息
	 * @param headers 消息头
	 */
	@SuppressWarnings("all")
	public void push(SupplierOrderSync message, Map<String, Object> headers) throws Exception {
		
		//记录日志
		LogCommon.recordLog("refund message origin content :"+message.toString()+
				"　message-header content :" +headers.toString(), LogLeve.INFO);
		try {
			handleRefunded(message, headers);
		} catch (Exception e) {
//
			handleMessageException.handleException(message,headers,e);
			//内部延迟短些 推送的延迟产些 以秒为单位（1000代表一秒）
			int delay = supplierProperties.getSupplier().getDelayTime();
			this.reTry(message,delay*60,(Integer) headers.get(GlobalConstant.MESSAGE_HEADER_ORDER_STATUS_KEY),(Integer) headers.get(GlobalConstant.MESSAGE_HEADER_PUSH_STATUS_KEY),
					(null==headers.get(GlobalConstant.MESSAGE_HEADER_X_RETRIES_KEY)?0:(Integer)headers.get(GlobalConstant.MESSAGE_HEADER_X_RETRIES_KEY)),null);

		}
	}

	/**
	 * 正常消息处理
	 * @param message
	 * @param headers
	 * @throws Exception
     */
	private void handleRefunded(SupplierOrderSync message, Map<String, Object> headers) throws Exception {
		
		Map<String, List<OrderDTO>> supplierOrderListMap = orderCommonUtil.getOrderDTO(message, headers);
		//是否订单对接
		boolean isOrderApi = true;
		
		if (null != supplierOrderListMap && supplierOrderListMap.size() > 0) {
			
			Set<String> supplierIdSet = supplierOrderListMap.keySet();
			//按供货商循环推送
			for (String supplierId : supplierIdSet) {
				isOrderApi = true;
				IOrderService iOrderService = orderHandleSearch.getHander(supplierId);
				if(null==iOrderService){
					isOrderApi = false;
				}
				List<OrderDTO> orderDetails = supplierOrderListMap.get(supplierId);
				// 正常的消息体
				if(null!=orderDetails&&orderDetails.size()>0){
					for(OrderDTO orderDTO:orderDetails){
						//未找到的退款订单 插入到数据库中
						if(null!=orderDTO.getBusinessOperate()&&orderDTO.getBusinessOperate().getIndex()== OrderBusinessOperateType.OPERATE_CREATE_NEW_ORDER.getIndex()){
							  this.saveOrderMsg(orderDTO,headers,isOrderApi);
						}
						loopHandleOrderDTO(iOrderService,orderDTO,headers,isOrderApi);
					}
				}
			}
		} else {
			throw new ServiceMessageException("退款失败","退款订单" + message.getOrderNo() + "消息体转换成订单对象后为空");
		}
	}
	
	private void loopHandleOrderDTO(IOrderService iOrderService, OrderDTO orderDTO, Map<String, Object> headers,boolean isOrderApi) throws Exception{
		
		//退款操作，先判断订单状态是否已推送成功
		if(OrderStatus.NO_PAY.getIndex()==orderDTO.getOrderStatus().getIndex()||(null!=orderDTO.getPushStatus()&&PushStatus.ORDER_CONFIRMED_ERROR.getIndex()==orderDTO.getPushStatus().getIndex())){
			//未支付或者支付了推送供应商失败，都直接更新订单状态和推送状态
			handleOrderStatus(orderDTO,headers);
			orderDTO.setPushStatus(PushStatus.NO_REFUNDED_API);
			handlePushStauts(orderDTO,headers);
		}else if(OrderStatus.REFUNDED.getIndex()==orderDTO.getOrderStatus().getIndex()){
			// 订单对接
			if (isOrderApi) {
				// 处理推送
				handlePush(iOrderService, orderDTO, headers);
			}
		} else if (OrderStatus.PAYED.getIndex()==orderDTO.getOrderStatus().getIndex()) {
			if(isNeedHandle(orderDTO,HandleStep.HANDLE_ORDER)){
				handleOrderStatus(orderDTO,headers);
			}
			if(isOrderApi){//订单对接
				//处理推送
				handlePush(iOrderService, orderDTO,headers);
			}
		} else {
			// 发送邮件，计入人工干预的表中
			handleOrderStatus(orderDTO,headers);
			orderDTO.setPushStatus(PushStatus.NO_REFUNDED_API);
			handlePushStauts(orderDTO,headers);
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
			iOrderService.handleRefundlOrder(orderDTO);
		}
		this.handlePushStauts(orderDTO,headers);
		//推送发生错误 需要重新发送
		if(null!=orderDTO.getErrorType()){
			throw new Exception("退款推送出错，需要重试.错误原因： " + orderDTO.getLogContent());
		}

	}
	
	private void handlePushStauts(OrderDTO orderDTO, Map<String, Object> headers) throws Exception {
		//更新推送状态
		hubOrderService.updateHubOrderDetailPushStatus(orderDTO);
		orderCommonUtil.setStatusIntoHeader(headers, orderDTO);
		//TODO 增加订单状发生变化的日志
		LogCommon.loggerOrder(orderDTO,LogLeve.INFO);
	}
	/**
	 * 处理订单状态
	 * @param orderDTO
	 * @param headers
	 * @throws Exception
     */
	private void handleOrderStatus(OrderDTO orderDTO, Map<String, Object> headers) throws Exception {
		orderDTO.setRefundTime(new Date());
		orderDTO.setOrderStatus(OrderStatus.REFUNDED);
		hubOrderService.updateHubOrderDetailOrderStatus(orderDTO);
		//设置订单及推送的状态
		orderCommonUtil.setStatusIntoHeader(headers,orderDTO);

		//记录处理后的日志
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
				if(orderDTO.getOrderStatus().getIndex()==OrderStatus.REFUNDED.getIndex()){ //已支付 不做处理
					isNeed = false;
				}
			}
		}else if((handleStep.getIndex()==HandleStep.HANDLE_PUSH.getIndex())){//推送处理
			if(orderDTO.getPushStatus().getIndex()!=orderDTO.getExceptionPushStatus().getIndex()){ //推送状态不等
				if(orderDTO.getExceptionPushStatus().getIndex()!=PushStatus.REFUNDED_ERROR.getIndex()){//更新数据库即可
					isNeed = false;
				}
			}else{
				if(orderDTO.getPushStatus().getIndex()==PushStatus.REFUNDED_ERROR.getIndex()){ //推送异常 重新推送

				}else{ //不是推送出错
					isNeed = false;
				}

			}
		}
		return isNeed;
	}

	/**
	 * 保存订单信息
	 * @param orderDTO
	 * @param headers
	 * @throws Exception
     */
    private void saveOrderMsg(OrderDTO orderDTO,Map<String, Object> headers,boolean isOrderApi) throws Exception{
		Date date = new Date();
		if(null==orderDTO.getPayTime()){
			orderDTO.setPayTime(new Date());
		}
		orderDTO.setOrderStatus(OrderStatus.PAYED);
		if(isOrderApi){
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED);
			orderDTO.setLockStockTime(date);
			orderDTO.setConfirmTime(date);
		}
		orderDTO.setUpdateTime(new Date());

		hubOrderService.saveOrderAndOrderDetails(orderDTO);
		//设置订单及推送的状态
		orderCommonUtil.setStatusIntoHeader(headers,orderDTO);
		//记录处理后的日志
		LogCommon.loggerOrder(orderDTO,LogLeve.INFO);


	}

	


}
