package com.shangpin.iog.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.OrderTimeUpdateDTO;
import com.shangpin.iog.dto.StockUpdateDTO;
import com.shangpin.iog.dto.TokenDTO;

@Mapper
public interface OrderTimeUpdateMapper extends IBaseDao<OrderTimeUpdateDTO> {
	
	public void updateSupplierOrderTime(OrderTimeUpdateDTO stockUpdateDTO);
	
	
	public void savesupplierOrderTime(OrderTimeUpdateDTO stockUpdateDTO);
	

	public OrderTimeUpdateDTO findSupplierOrderById(@Param("supplierId") String supplierId);
	
}
