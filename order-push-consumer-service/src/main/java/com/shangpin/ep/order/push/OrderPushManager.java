package com.shangpin.ep.order.push;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ep.order.conf.stream.sink.message.SupplierOrderSync;
import com.shangpin.ep.order.exception.EpOrderServiceRuntimeException;
import com.shangpin.ep.order.push.cancel.CancelOrderPusher;
import com.shangpin.ep.order.push.common.GlobalConstant;
import com.shangpin.ep.order.push.create.CreateOrderPusher;
import com.shangpin.ep.order.push.payed.PayedOrderPusher;
import com.shangpin.ep.order.push.refund.RefundOrderPusher;
import com.shangpin.ep.order.push.repurchase.RePurchaseOrderPusher;
import com.shangpin.ep.order.push.shipped.ShippedConsumer;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:OrderPushManager.java </p>
 * <p>Description: 订单推送管理器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月10日 上午10:53:13
 */
@Component
@Slf4j
public class OrderPushManager {

	@Autowired
	private CreateOrderPusher createOrderPusher;
	@Autowired
	private CancelOrderPusher cancelOrderPusher;
	@Autowired
	private PayedOrderPusher payedOrderPusher;
	@Autowired
	private RefundOrderPusher refundOrderPusher;
	@Autowired
	private RePurchaseOrderPusher rePurchaseOrderPusher;
	@Autowired
	private ShippedConsumer shippedConsumer;
	
	/**
	 * 订单推送处理：
	 * 1.该方法的所有处理逻辑不允许出现任何异常，否则系统会辨认为系统漏洞
	 * 2.该类只负责消息类型的路由功能，至于具体业务逻辑的处理将会路由到具体的实现类中去！
	 * @param message 监听器接收到的消息
	 * @param headers 监听器接收到的消息头
	 */
	public void orderProcess(SupplierOrderSync message, Map<String, Object> headers) throws Exception {
		Map<String,Object>  copyHeaders =  this.getBusinesssMap(headers);
		log.info("EpOrderSystem 接收到的消息体数据为：{}",message.toString());
		long start = System.currentTimeMillis();
		String syncType = message.getSyncType();
        if (syncType == null) {// 消息类型未提供，系统无法处理这种脏数据
        	throw new EpOrderServiceRuntimeException("EpOrderSystem接收到异常消息数据（没有消息类型）："+message.toString());
        } else if (syncType.equals(GlobalConstant.SYNC_TYPE_CREATE_ORDER)) {//下单
            doCreateOrder(message, copyHeaders);
        } else if (syncType.equals(GlobalConstant.SYNC_TYPE_CANCEL_ORDER)) {//取消
            doCancelOrder(message, copyHeaders);
        } else if (syncType.equals(GlobalConstant.SYNC_TYPE_PAYED_ORDER)) {//支付
            doPayedOrder(message, copyHeaders);
        } else if (syncType.equals(GlobalConstant.SYNC_TYPE_REFUNDED)) {//退款
            doRefundedOrder(message, copyHeaders);
        } else if (syncType.equals(GlobalConstant.SYNC_TYPE_REPURCHASE_ORDER)) {//重采
            doRePurchaseSupplierOrder(message, copyHeaders);
        } else if (syncType.equals(GlobalConstant.SYNC_TYPE_SHIPPED)) {//发货
            doShippedOrder(message, copyHeaders);
        } else {// 消息类型未支持，系统无法处理这种脏数据
        	throw new EpOrderServiceRuntimeException("EpOrderSystem接收到异常消息数据（无效消息类型）："+message.toString());
        }
        log.info("EpOrderSystem 成功处理编号为{}的消息完毕，耗时{}milliseconds",message.getMessageId(),System.currentTimeMillis()-start);
	}
	/**
	 * 下单：考虑到后期业务逻辑的扩张性，所以将整个消息以及所有的消息头传递给具体的业务逻辑处理实现类的处理方法中，以便具体的业务实现解耦！
	 * @param message 系统接收到的下单消息事件
	 * @param headers 系统接收到的该消息的所有消息头
	 */
	protected void doCreateOrder(SupplierOrderSync message, Map<String, Object> headers) throws Exception{
		createOrderPusher.push(message,headers);
	}
	/**
	 * 未支付取消订单：考虑到后期业务逻辑的扩张性，所以将整个消息以及所有的消息头传递给具体的业务逻辑处理实现类的处理方法中，以便具体的业务实现解耦！
	 * @param message 系统接收到的取消订单消息事件
	 * @param headers 系统接收到的该消息的所有消息头
	 */
	protected void doCancelOrder(SupplierOrderSync message, Map<String, Object> headers) throws Exception {
		cancelOrderPusher.push(message,headers);
	}
	/**
	 * 支付：考虑到后期业务逻辑的扩张性，所以将整个消息以及所有的消息头传递给具体的业务逻辑处理实现类的处理方法中，以便具体的业务实现解耦！
	 * @param message 系统接收到的订单支付消息事件
	 * @param headers 系统接收到的该消息的所有消息头
	 */
	protected void doPayedOrder(SupplierOrderSync message, Map<String, Object> headers) throws Exception {
		payedOrderPusher.push(message,headers);
	}
	/**
	 * 退款：考虑到后期业务逻辑的扩张性，所以将整个消息以及所有的消息头传递给具体的业务逻辑处理实现类的处理方法中，以便具体的业务实现解耦！
	 * @param message 系统接收到的退款消息事件
	 * @param headers 系统接收到的该消息的所有消息头
	 */
	protected void doRefundedOrder(SupplierOrderSync message, Map<String, Object> headers) throws Exception{
		refundOrderPusher.push(message,headers);
	}
	/**
	 * 重采：考虑到后期业务逻辑的扩张性，所以将整个消息以及所有的消息头传递给具体的业务逻辑处理实现类的处理方法中，以便具体的业务实现解耦！
	 * @param message 系统接收到的重采消息事件
	 * @param headers 系统接收到的该消息的所有消息头
	 */
	protected void doRePurchaseSupplierOrder(SupplierOrderSync message, Map<String, Object> headers) throws Exception {
		rePurchaseOrderPusher.push(message, headers);
	}
	/**
	 * 发货：考虑到后期业务逻辑的扩张性，所以将整个消息以及所有的消息头传递给具体的业务逻辑处理实现类的处理方法中，以便具体的业务实现解耦！
	 * @param message 系统接收到的发货消息事件
	 * @param headers 系统接收到的该消息的所有消息头
	 */
	protected void doShippedOrder(SupplierOrderSync message, Map<String, Object> headers) throws Exception {
		shippedConsumer.push(message,headers);
	}

	/**
	 * 来的消息不能复制 创建新的MAP
	 * @param headers
	 * @return
	 */
	private Map<String,Object> getBusinesssMap(Map<String, Object> headers){
		Map<String,Object> result = new HashMap<>();
		if(MapUtils.isEmpty(headers)) return result;
		for(Iterator<Map.Entry<String,Object>> iterator = headers.entrySet().iterator(); iterator.hasNext();){
			Map.Entry<String,Object> mapEntry = iterator.next();
			result.put(mapEntry.getKey(),mapEntry.getValue());
		}
		return result;
	}

}
