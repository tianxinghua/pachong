package com.shangpin.iog.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.OrderDetailDTO;

@Mapper
public interface OrderDetailMapper extends IBaseDao<OrderDTO>{
	
	public void saveOrderDetailDTO(OrderDetailDTO orderDetailDTO);

	public void update(OrderDetailDTO orderDetailDTO);
	
	public List<OrderDetailDTO> findSubOrderListByMOrderNo(@Param("spMorderNo") String masterOrderNo);
	
	public OrderDetailDTO findSubOrderByPurchaseNo(@Param("purchaseNo") String spPurchaseNo);
	
}
