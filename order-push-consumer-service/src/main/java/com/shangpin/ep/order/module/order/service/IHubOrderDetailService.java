package com.shangpin.ep.order.module.order.service;

import java.util.Date;
import java.util.List;

import com.shangpin.ep.order.enumeration.OrderStatus;
import com.shangpin.ep.order.module.order.bean.HubOrderDetail;

/**
 * HubOrderDetail(订单明细表)对应的逻辑类
 * <p>Title: IHubOrderDetailService</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年2月23日 上午10:13:31
 *
 */
public interface IHubOrderDetailService {

	/**
	 * 根据条件查询订单明细集合
	 * @param supplierId 供应商门户编号
	 * @param orderStatus 订单状态
	 * @param startTime 开始支付时间(包含)
	 * @param endTime 结束支付时间(不包含)
	 * @return
	 */
	public List<HubOrderDetail> findHubOrderDetails(String supplierId, OrderStatus orderStatus, Date startTime, Date endTime);
}
