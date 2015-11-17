package com.shangpin.iog.product.dao;

import java.util.List;

import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.StockUpdateDTO;
import com.shangpin.iog.dto.TokenDTO;

@Mapper
public interface StockUpdateMapper extends IBaseDao<TokenDTO> {
	
	public void updateStockTime(StockUpdateDTO stockUpdateDTO);
	
	public List<StockUpdateDTO> getAllData();
}
