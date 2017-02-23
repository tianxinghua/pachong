package com.shangpin.ep.order.module.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ep.order.enumeration.OrderStatus;
import com.shangpin.ep.order.module.order.bean.HubOrderDetail;
import com.shangpin.ep.order.module.order.bean.HubOrderDetailCriteria;
import com.shangpin.ep.order.module.order.bean.HubOrderDetailCriteria.Criteria;
import com.shangpin.ep.order.module.order.mapper.HubOrderDetailMapper;
import com.shangpin.ep.order.module.order.service.IHubOrderDetailService;
/**
 * HubOrderDetail(订单明细表)对应的逻辑实现类
 * <p>Title: HubOrderDetailService</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年2月23日 上午10:55:58
 *
 */
@Service
public class HubOrderDetailService implements IHubOrderDetailService {
	
	@Autowired
    private HubOrderDetailMapper orderDetailDao;

	@Override
	public List<HubOrderDetail> findHubOrderDetails(String supplierId, OrderStatus orderStatus, Date startTime,
			Date endTime) {
		HubOrderDetailCriteria detailCriteria = new HubOrderDetailCriteria();
		Criteria criteria = detailCriteria.createCriteria().andSupplierIdEqualTo(supplierId);
		if(null != orderStatus){
			criteria.andOrderStatusEqualTo(orderStatus.getIndex());
		}
		if(null != startTime){
			criteria.andPayTimeGreaterThanOrEqualTo(startTime);
		}
		if(null != endTime){
			criteria.andPayTimeLessThan(endTime);
		}
		return orderDetailDao.selectByExample(detailCriteria);
	}

}
