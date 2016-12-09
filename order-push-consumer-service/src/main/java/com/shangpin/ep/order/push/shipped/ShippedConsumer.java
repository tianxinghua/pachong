package com.shangpin.ep.order.push.shipped;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.LogLeve;
import com.shangpin.ep.order.push.common.HandleMessageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.stream.sink.message.SupplierOrderSync;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.enumeration.OrderStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.exception.ServiceException;
import com.shangpin.ep.order.exception.ServiceMessageException;
import com.shangpin.ep.order.module.log.service.IHubOrderLogService;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.order.service.IHubOrderService;
import com.shangpin.ep.order.module.order.service.impl.OrderCommonUtil;
import com.shangpin.ep.order.module.orderapiservice.impl.OrderHandleSearch;
import com.shangpin.ep.order.push.common.AbstractPusher;
import com.shangpin.ep.order.push.common.GlobalConstant;

/**
 * <p>Title:ShippedConsumer.java </p>
 * <p>Description: 发货类型消息消费处理</p>
 * <p>Company: www.shangpin.com</p>
 * @author yanxiaobin
 * @date 2016年11月22日 上午9:58:03
 */
@Service
public class ShippedConsumer  extends AbstractPusher{

	@Autowired
	OrderCommonUtil orderCommonUtil;

	@Autowired
	IHubOrderService hubOrderService;

	@Autowired
	OrderHandleSearch orderHandleSearch;

	@Autowired
	IHubOrderLogService orderLogService;

	@Autowired
	SupplierProperties supplierProperties;


	@Autowired
	HandleMessageException handleMessageException;

	/**
	 * 发货类型消息消费处理
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void push(SupplierOrderSync message, Map<String, Object> headers) throws Exception {

		try {
			//记录日志
			LogCommon.recordLog(" shipped message origin content :"+message.toString()+
					"　message-header content :" +headers.toString(), LogLeve.INFO);
			handleShip(message, headers);
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
	private void handleShip(SupplierOrderSync message, Map<String, Object> headers) throws Exception {
		//获取拆单后的信息
		Map<String, List<OrderDTO>> supplierOrderListMap = orderCommonUtil.getOrderDTO(message,headers);
		Set<String> supplierIdSet = supplierOrderListMap.keySet();

		if(supplierOrderListMap!=null&&supplierOrderListMap.size()>0){
			for(String supplierId:supplierIdSet){
				List<OrderDTO> orderDTOs = supplierOrderListMap.get(supplierId);
				if(orderDTOs!=null&&orderDTOs.size()>0){
					for(OrderDTO orderDTO:orderDTOs){
						//处理订单状态
						handleOrderStatus(orderDTO,headers);
					}
				}
			}
		}
	}

	private void handleOrderStatus(OrderDTO orderDTO, Map<String, Object> headers) throws Exception {


		orderDTO.setShipTime(new Date());
		orderDTO.setOrderStatus(OrderStatus.SHIPPED);
		hubOrderService.updateHubOrderDetailOrderStatus(orderDTO);
		LogCommon.loggerOrder(orderDTO, LogTypeStatus.SHIPPED);

	}

}
