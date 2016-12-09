package com.shangpin.ep.order.push.payed;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.stream.sink.message.SupplierOrderSync;
import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.HandleStep;
import com.shangpin.ep.order.enumeration.LogLeve;
import com.shangpin.ep.order.enumeration.OrderStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.exception.ServiceException;
import com.shangpin.ep.order.exception.ServiceMessageException;
import com.shangpin.ep.order.module.log.service.IHubOrderLogService;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.order.service.IHubOrderService;
import com.shangpin.ep.order.module.order.service.impl.OpenApiService;
import com.shangpin.ep.order.module.order.service.impl.OrderCommonUtil;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.orderapiservice.impl.OrderHandleSearch;
import com.shangpin.ep.order.module.supplier.bean.SupplierDTO;
import com.shangpin.ep.order.push.common.AbstractPusher;
import com.shangpin.ep.order.push.common.GlobalConstant;
import com.shangpin.ep.order.push.common.HandleMessageException;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:PayedOrderPusher.java </p>
 * <p>Description: 订单支付推送器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月20日 下午9:09:33
 */
@Service
@Slf4j
public class PayedOrderPusher extends AbstractPusher{
	@Autowired
	OrderCommonUtil orderCommonUtil;

	@Autowired
	IHubOrderService hubOrderService;

	@Autowired
	OrderHandleSearch orderHandleSearch;

	@Autowired
	IHubOrderLogService orderLogService;

	@Autowired
	OpenApiService openApiService;

	@Autowired
	HandleMessageException handleMessageException;

	@Autowired
	SupplierProperties supplierProperties;

	/**
	 * 推送订单支付
	 * @param message 消息
	 * @param headers 消息头
	 */
	public void push(SupplierOrderSync message, Map<String, Object> headers) throws Exception {


		try {
			//记录日志
			LogCommon.recordLog("payed message origin content :"+message.toString()+
					"　message-header content :" +headers.toString(), LogLeve.INFO);
			//获取拆单后的信息
			Map<String, List<OrderDTO>> supplierOrderListMap = orderCommonUtil.getOrderDTO(message,headers);
			Set<String> supplierIdSet = supplierOrderListMap.keySet();

			for(String supplierId:supplierIdSet){
				List<OrderDTO> orderDTOs = supplierOrderListMap.get(supplierId);
				for(OrderDTO orderDTO:orderDTOs){
					//总控订单状态
					if(!isNeedContinueHandle(orderDTO)){
						continue;
					}
					//先设置订单状态
					orderCommonUtil.setStatusIntoHeader(headers,orderDTO);
					//处理订单状态
					if(isNeedHandleOrderStatus(orderDTO)){
						handleOrderStatus(orderDTO,headers);
					}
					//  锁库时 推送状态已经为 NOSTOCK   需要设置采购异常
					if(isNeedHandlePurchaseException(orderDTO)){
						setPurchaseException(headers,orderDTO);
						continue;//采购异常不需要继续向下
					}

					//处理推送
                    handlePush( orderDTO,headers);


				}

			}
		} catch (Exception e) {

			handleMessageException.handleException(message,headers,e);
			//内部延迟短些 推送的延迟产些 以秒为单位（1000代表一秒）
			int delay = supplierProperties.getSupplier().getDelayTime();
			this.reTry(message,delay*60,(Integer) headers.get(GlobalConstant.MESSAGE_HEADER_ORDER_STATUS_KEY),(Integer) headers.get(GlobalConstant.MESSAGE_HEADER_PUSH_STATUS_KEY),
					(null==headers.get(GlobalConstant.MESSAGE_HEADER_X_RETRIES_KEY)?0:(Integer)headers.get(GlobalConstant.MESSAGE_HEADER_X_RETRIES_KEY)),null);

		}
	}



	private void handlePushStauts(OrderDTO orderDTO, Map<String, Object> headers) throws Exception {
		//更新推送状态
		hubOrderService.updateHubOrderDetailPushStatus(orderDTO);
		orderCommonUtil.setStatusIntoHeader(headers, orderDTO);
		//记录处理后的日志
		LogCommon.loggerOrder(orderDTO,LogLeve.INFO);
	}




	/**
	 * 处理推送
	 * 需要注意 如果推送状态为空  则是新产生的订单  说明处理顺序已经混乱 需要重新锁库

	 * @param orderDTO
	 * @param headers
	 * @throws ServiceException
     */
	private void handlePush(OrderDTO orderDTO, Map<String, Object> headers) throws  Exception{
		//先设置错误信息为空

		IOrderService iOrderService = orderHandleSearch.getHander(orderDTO.getSupplierId());
		if(null==iOrderService){//无订单对接的不处理推送
			return ;
		}
		orderCommonUtil.setStatusIntoHeader(headers,orderDTO);

		if(isNeedHandleLock(orderDTO)){//需要锁库  (下单消息还未处理 支付消息已经来了 )  或者同样的订单取消后，再次支付  或者支付了 锁库异常
			orderDTO.setErrorType(null);
			orderDTO.setDescription("");
			iOrderService.handleSupplierOrder(orderDTO);
			orderCommonUtil.setStatusIntoHeader(headers,orderDTO);
			this.handlePushStauts(orderDTO, headers);
			//推送发生错误 需要重新发送
			if(null!=orderDTO.getErrorType()){
				throw new Exception("订单确认推送出错，需要重试.错误原因： " +  orderDTO.getLogContent());
			}
		}else{
			if(isNeedHandleLockStatus(orderDTO)){
				this.handlePushStauts(orderDTO, headers);
			}
		}

	    if(isNeedHandleConfirm(orderDTO)){
			orderDTO.setErrorType(null);
			orderDTO.setDescription("");
			iOrderService.handleConfirmOrder(orderDTO);
			orderCommonUtil.setStatusIntoHeader(headers,orderDTO);
			this.handlePushStauts(orderDTO, headers);
		}else{
			if(isNeedHandleConfrimStatus(orderDTO)){
				this.handlePushStauts(orderDTO, headers);
			}
		}

		//推送发生错误 需要重新发送
		if(null!=orderDTO.getErrorType()){
			throw new Exception("订单确认推送出错，需要重试.错误原因：" + orderDTO.getLogContent() );
		}
		//是否需要设置采购异常
		if(isNeedHandlePurchaseException(orderDTO)){
			setPurchaseException(headers,orderDTO);

		}


	}

	/**
	 * 处理订单状态
	 * @param orderDTO
	 * @param headers
	 * @throws Exception
     */
	private void handleOrderStatus(OrderDTO orderDTO, Map<String, Object> headers) throws Exception {
		if(null==orderDTO.getPayTime()){
			orderDTO.setPayTime(new Date());
		}
		orderDTO.setOrderStatus(OrderStatus.PAYED);
		if(null==orderDTO.getId()){//下单消息尚未处理  支付已经过来 此时抛弃下单消息 直接可支付支付

			hubOrderService.saveOrderAndOrderDetails(orderDTO);
			//设置订单及推送的状态
			orderCommonUtil.setStatusIntoHeader(headers,orderDTO);
			//记录处理后的日志
			LogCommon.loggerOrder(orderDTO,LogLeve.INFO);

		}
		orderDTO.setUpdateTime(new Date());
		hubOrderService.updateHubOrderDetailOrderStatus(orderDTO);
		//设置订单及推送的状态
		orderCommonUtil.setStatusIntoHeader(headers,orderDTO);
		//记录处理后的日志
		LogCommon.loggerOrder(orderDTO,LogLeve.INFO);


	}




	private boolean isNeedContinueHandle(OrderDTO orderDTO){
		 if(orderDTO.getOrderStatus().getIndex()==OrderStatus.SHIPPED.getIndex()||
				 orderDTO.getOrderStatus().getIndex()==OrderStatus.REFUNDED.getIndex()||
				 orderDTO.getOrderStatus().getIndex()==OrderStatus.PURCHASE_EXCEPTION.getIndex()||
				 orderDTO.getOrderStatus().getIndex()==OrderStatus.PURCHASE_EXCEPTION_FAKE.getIndex()){
			 return false;
		 }
		return true;
	}

	private boolean isNeedHandleOrderStatus(OrderDTO orderDTO) {
		Boolean isNeed = true;
		if(null==orderDTO.getExceptionOrderStatus()) {//新的消息 需要处理

		}else{
			if(orderDTO.getOrderStatus().getIndex()!=orderDTO.getExceptionOrderStatus().getIndex()){// 修改订单状态时失败
				if(orderDTO.getExceptionOrderStatus().getIndex()==OrderStatus.PURCHASE_EXCEPTION_ERROR.getIndex()){//采购异常时失败
					isNeed=false;
				}
			}else{
				if(orderDTO.getOrderStatus().getIndex()==OrderStatus.PAYED.getIndex()){ //已支付 不做处理
					isNeed = false;
				}
			}
		}
		return  isNeed;

	}

	private boolean isNeedHandleLock(OrderDTO orderDTO){
		Boolean isNeed = false;
		if(null==orderDTO.getPushStatus()){ //需要锁库  (下单消息还未处理 支付消息已经来了 )
			if(null==orderDTO.getExceptionPushStatus()) {//新的消息 需要处理
				isNeed = true;
			}else {
				if (orderDTO.getExceptionPushStatus().getIndex() == PushStatus.LOCK_PLACED_ERROR.getIndex()) {//锁库失败  保存数据库失败  继续锁库
					isNeed = true;
				}
			}
		}else{//订单推送有状态
			if(null==orderDTO.getExceptionPushStatus()) {//正常逻辑
				// 订单取消后，再次支付   或者支付时 发现原来的锁库异常 需要继续推送
				if (orderDTO.getPushStatus().getIndex() == PushStatus.LOCK_CANCELLED.getIndex() ||
						orderDTO.getPushStatus().getIndex() == PushStatus.NO_LOCK_CANCELLED_API.getIndex()||
						orderDTO.getPushStatus().getIndex() == PushStatus.LOCK_PLACED_ERROR.getIndex()) {
					isNeed = true;
				}
			}else{ //有异常
				if (orderDTO.getExceptionPushStatus().getIndex() == PushStatus.LOCK_PLACED_ERROR.getIndex()) {//锁库失败 继续锁库
					isNeed = true;
				}

			}
		}

		return isNeed;
	}

	/**
	 * 单独更新订单的锁库推送状态 说明有异常发生 只能是数据库更新时挂了
	 * @param orderDTO
	 * @return
     */
	private boolean isNeedHandleLockStatus(OrderDTO orderDTO){
		Boolean isNeed = false;
		if(null==orderDTO.getPushStatus()){ //无状态
			if(null!=orderDTO.getExceptionPushStatus()) { //有异常
				orderDTO.setPushStatus(orderDTO.getExceptionPushStatus());
				isNeed = true;
			}
		}else{//订单推送有状态
			if(null!=orderDTO.getExceptionPushStatus()) {//
				if (orderDTO.getPushStatus().getIndex() != orderDTO.getExceptionPushStatus().getIndex()) {//状态不相等
					if(orderDTO.getExceptionPushStatus().getIndex()==PushStatus.LOCK_PLACED_ERROR.getIndex()||
							orderDTO.getExceptionPushStatus().getIndex()==PushStatus.LOCK_PLACED.getIndex()){
						orderDTO.setPushStatus(orderDTO.getExceptionPushStatus());
						isNeed = true;
					}
				}
			}
		}

		return isNeed;
	}




	private boolean isNeedHandleConfirm(OrderDTO orderDTO){
		boolean isNeed = false;
		if(null==orderDTO.getExceptionPushStatus()) {//正常逻辑
			isNeed = true;
		}else{ //有异常
			if(orderDTO.getPushStatus().getIndex()==orderDTO.getExceptionPushStatus().getIndex()) {
				if (orderDTO.getPushStatus().getIndex() == PushStatus.ORDER_CONFIRMED_ERROR.getIndex()) { //推送异常 重新推送
					isNeed = true;
				}
			}else{
				//更新库存失败 不需要做处理

			}

		}
		return isNeed;
	}


	/*** 单独更新订单的锁库推送状态 说明有异常发生 只能是数据库更新时挂了
	* @param orderDTO
	* @return
			*/
	private boolean isNeedHandleConfrimStatus(OrderDTO orderDTO){
		Boolean isNeed = false;
		if(null!=orderDTO.getExceptionPushStatus()) {//
			if (orderDTO.getPushStatus().getIndex() != orderDTO.getExceptionPushStatus().getIndex()) {//状态不相等
				if(orderDTO.getPushStatus().getIndex()==PushStatus.ORDER_CONFIRMED.getIndex()){//人工修改了推送状态

				}else{
					orderDTO.setPushStatus(orderDTO.getExceptionPushStatus());
					isNeed = true;
				}


			}
		}
		return isNeed;
	}

	private  boolean isNeedHandlePurchaseException(OrderDTO orderDTO){
		Boolean isNeed = true;
		if(orderDTO.getOrderStatus().getIndex()==OrderStatus.PAYED.getIndex()){ //已支付
			if(null==orderDTO.getPushStatus()){ //推送状态没有 说明是重采的 或者是下单流程尚未处理 支付流程先处理
				isNeed = false;
			}else{
				if(orderDTO.getPushStatus().getIndex()!=PushStatus.NO_STOCK.getIndex()) {
					isNeed = false;
				}
			}
		}
		return  isNeed;
	}


	/**
	 * 设置采购异常 返回true 不在继续执行
	 * @param orderDTO
	 * @return
     */
	private boolean setPurchaseException(Map<String, Object> headers,OrderDTO orderDTO) throws Exception{
		boolean result = false;

		//如果推送状态是NO_STOCK 需要采购异常
		Date date = new Date();
		orderDTO.setUpdateTime(date);
		if(orderDTO.getPushStatus().getIndex()==PushStatus.NO_STOCK.getIndex()){

			orderDTO.setPayTime(date);
			//设置库存为0
			SupplierDTO supplierDTO=  orderCommonUtil.getSupplier(orderDTO.getSupplierNo());

			openApiService.setSkuQuantity(supplierDTO.getOpenApiKey(),supplierDTO.getOpenApiSecret(),orderDTO.getSpSkuNo(),0);
			//设置采购异常
			if(null!=supplierDTO.getIsPurchaseException()&&supplierDTO.getIsPurchaseException()){
				try {
					openApiService.setPurchaseException(supplierDTO.getOpenApiKey(),supplierDTO.getOpenApiSecret(),orderDTO.getPurchaseNo());
				} catch (Exception e) {
					if(e instanceof  ServiceMessageException){//采购单尚未生成
						orderDTO.setOrderStatus(OrderStatus.PURCHASE_EXCEPTION_ERROR);
						orderCommonUtil.setStatusIntoHeader(headers,orderDTO);
						throw new Exception(e.getMessage());

					}else{
						throw e;
					}
				}
				orderDTO.setOrderStatus(OrderStatus.PURCHASE_EXCEPTION);
			}else{
				orderDTO.setOrderStatus(OrderStatus.PURCHASE_EXCEPTION_FAKE);
			}

			//设置订单及推送的状态
			orderCommonUtil.setStatusIntoHeader(headers,orderDTO);
			//更新状态
			hubOrderService.updateHubOrderDetailOrderStatus(orderDTO);

			//增加订单状发生变化的日志
			orderLogService.saveOrderLog(orderDTO);
			result = true;
		}
		return result;
//		return false;
	}


}
