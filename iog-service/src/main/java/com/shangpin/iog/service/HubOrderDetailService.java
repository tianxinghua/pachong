package com.shangpin.iog.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.HubOrderDetailDTO;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.OrderDetailDTO;

public interface HubOrderDetailService {
 /**
     * 查询订单
     * @param supplier
     * @param startDate
     * @param endDate
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public List<HubOrderDetailDTO> getOrderBySupplierIdAndTime(String supplier, Date startDate,
			Date endDate,String CGD,String spSkuId,String supplierSkuId,String orderStatus,String pushStatus,Integer pageIndex, Integer pageSize);
    

	public int getOrderTotalBySupplierIdAndTime(String supplier, String startTime,
			String endTime,String CGD,String spSkuId,String supplierSkuId,String orderStatus,String pushStatus);

}
