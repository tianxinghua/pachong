package com.shangpin.ep.order.push.create;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.stream.sink.message.SupplierOrderSync;
import com.shangpin.ep.order.enumeration.LogLeve;
import com.shangpin.ep.order.enumeration.OrderStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
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
 * <p>Title:CreateOrderPusher.java </p>
 * <p>Description: 下单推送器</p>
 * <p>Company: www.shangpin.com</p>
 * @author yanxiaobin
 * @date 2016年11月20日 下午9:07:13
 */
@Service
@Slf4j
public class CreateOrderPusher extends AbstractPusher{
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
	 * 推送订单  现在所有的都是一个订单一个件商品
	 * 整体思路 更新数据库挂了 ，发邮件 直接抛出异常
	 * @param message 消息
	 * @param headers 消息头
	 */
	public void push(SupplierOrderSync message, Map<String, Object> headers) throws Exception {

		try {
			//记录日志
			LogCommon.recordLog("createOrder message origin content :"+message.toString() +
					"　message-header content :" +headers.toString(),LogLeve.INFO);



			if (orderCommonUtil.isExceptionMq(headers)) { //异常的消息体
				//异常处理
				retryHandle(message, headers);
            } else { //正常的消息体
				createOrderAndPush(message, headers);
			}

		} catch (Exception e) {

			handleMessageException.handleException(message,headers,e);
			//内部延迟短些 推送的延迟产些 以秒为单位（1000代表一秒）
			int delay = supplierProperties.getSupplier().getDelayTime();
			this.reTry(message,delay*60,(Integer) headers.get(GlobalConstant.MESSAGE_HEADER_ORDER_STATUS_KEY),(Integer) headers.get(GlobalConstant.MESSAGE_HEADER_PUSH_STATUS_KEY),
					(null==headers.get(GlobalConstant.MESSAGE_HEADER_X_RETRIES_KEY)?0:(Integer)headers.get(GlobalConstant.MESSAGE_HEADER_X_RETRIES_KEY)),null);

		}
	}



	/**
	 * 异常处理逻辑
	 * @param message
	 * @param headers
	 * @throws Exception
     */
	private void retryHandle(SupplierOrderSync message, Map<String, Object> headers) throws Exception {
		Map<String, List<OrderDTO>> supplierOrderListMap = orderCommonUtil.getOrderDTO(message,headers);
		Set<String> keySet =  supplierOrderListMap.keySet();
		for(String supplierId:keySet) {
            List<OrderDTO> orderDTOs = supplierOrderListMap.get(supplierId);
            for(OrderDTO orderDTO:orderDTOs){

				//总控订单状态
				if(!isNeedContinueHandle(orderDTO)){
					continue;
				}
                //异常处理方法
				//先设置订单状态
				orderCommonUtil.setStatusIntoHeader(headers,orderDTO);
                orderCreateExceptionHandle(message,headers,orderDTO);
            }
        }
	}

	/**
	 * 正常消息处理
	 * @param message
	 * @param headers
	 * @throws Exception
     */
	private void createOrderAndPush(SupplierOrderSync message, Map<String, Object> headers) throws Exception {
		Map<String, List<OrderDTO>> supplierOrderListMap = orderCommonUtil.getOrderDTO(message,headers);
		Set<String>  keySet =  supplierOrderListMap.keySet();

		//保存订单和订单明细信息    日志在内部处理
		hubOrderService.saveOrderAndOrderDetails(supplierOrderListMap);
		//每个供货商的订单循环推送
		for(String supplierId:keySet) {
			List<OrderDTO> orderDTOs = supplierOrderListMap.get(supplierId);
			for(OrderDTO orderDTO:orderDTOs){
				//先设置订单状态
				orderCommonUtil.setStatusIntoHeader(headers,orderDTO);
				orderLock(message,headers,supplierId, orderDTO);
			}
		}
	}

	private boolean isNeedContinueHandle(OrderDTO orderDTO){
		if(orderDTO.getOrderStatus().getIndex()==OrderStatus.NO_PAY_CANCELLED.getIndex()||
				orderDTO.getOrderStatus().getIndex()==OrderStatus.PAYED.getIndex()||
				orderDTO.getOrderStatus().getIndex()==OrderStatus.SHIPPED.getIndex()||
				orderDTO.getOrderStatus().getIndex()==OrderStatus.REFUNDED.getIndex()||
				orderDTO.getOrderStatus().getIndex()==OrderStatus.PURCHASE_EXCEPTION.getIndex()||
				orderDTO.getOrderStatus().getIndex()==OrderStatus.PURCHASE_EXCEPTION_FAKE.getIndex()){
			return false;
		}
		return true;
	}


	/**
	 * 锁库逻辑
	 * @param supplierId
	 * @param orderDTO
     */
	private void orderLock(SupplierOrderSync message,Map<String, Object> headers,String supplierId, OrderDTO orderDTO) throws Exception {
		IOrderService iOrderService = orderHandleSearch.getHander(supplierId);
		if(null!=iOrderService){
			orderDTO.setErrorType(null);
			orderDTO.setDescription("");

			iOrderService.handleSupplierOrder(orderDTO);

			//更新推送状态
			hubOrderService.updateHubOrderDetailPushStatus(orderDTO);
			orderLogService.saveOrderLog(orderDTO);
			//记录处理后的日志
			LogCommon.loggerOrder(orderDTO,LogLeve.INFO);

			//推送发生错误 需要重新发送
			if(null!=orderDTO.getErrorType()){
				orderCommonUtil.setStatusIntoHeader(headers, orderDTO);
				throw new Exception("锁库推送发生错误,需要重新发送.错误原因： " + orderDTO.getLogContent());
			}else{
				if(null!=orderDTO.getPushStatus()){

					if(orderDTO.getPushStatus().getIndex()==PushStatus.NO_STOCK.getIndex()){//无库  更新库存为0
						//设置库存为0
						SupplierDTO supplierDTO=  orderCommonUtil.getSupplier(orderDTO.getSupplierNo());

						openApiService.setSkuQuantity(supplierDTO.getOpenApiKey(),supplierDTO.getOpenApiKey(),orderDTO.getSpSkuNo(),0);


					}
				}
			}
		}


	}



	/**
	 * 异常处理逻辑
	 * @param message
	 * @param headers
	 * @param orderDTO
	 * @throws Exception
     */
	private void orderCreateExceptionHandle(SupplierOrderSync message, Map<String, Object> headers,OrderDTO orderDTO) throws Exception {
		if(null==orderDTO.getOrderStatus()){//尚未存入到数据库中，重新调用正常逻辑
			createOrderAndPush(message, headers);
        }else{
            if(orderDTO.getOrderStatus().getIndex()!= OrderStatus.NO_PAY.getIndex()){//订单状态已不是最初的未支付状态 ，后续不再处理
               throw new ServiceMessageException("订单状态已不是最初的未支付状态 ，后续不再处理");
            }else if(orderDTO.getOrderStatus().getIndex()!= orderDTO.getExceptionOrderStatus().getIndex()){
                //如果发生 则数据已经不正常
                throw new ServiceMessageException("订单："+ orderDTO.toString()  + "数据逻辑不正确");
            }
        }
		//如果走到此步 说明必定是推送时发生错误
		if(null==orderDTO.getPushStatus()){//数据库更新失败

		   if(null!=orderDTO.getExceptionPushStatus()&&orderDTO.getExceptionPushStatus().getIndex()== PushStatus.LOCK_PLACED_ERROR.getIndex()){//锁库失败 重新推送
			   orderLock(message,headers,orderDTO.getSupplierId(),orderDTO);
		   }else{//更新数据库
			   orderDTO.setPushStatus(orderDTO.getExceptionPushStatus());
			   orderDTO.setLockStockTime(new Date());
			   hubOrderService.updateHubOrderDetailPushStatus(orderDTO);
		   }
        }else{ //推送失败  重新推送
			orderLock(message,headers,orderDTO.getSupplierId(),orderDTO);
		}
	}


}
