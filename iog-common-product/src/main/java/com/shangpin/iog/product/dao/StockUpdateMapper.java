package com.shangpin.iog.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.StockUpdateDTO;
import com.shangpin.iog.dto.TokenDTO;

@Mapper
public interface StockUpdateMapper extends IBaseDao<TokenDTO> {
	
	public void updateStockStatus(StockUpdateDTO stockUpdateDTO);
	
	public List<StockUpdateDTO> getAllData();
	
	public void saveStockUpdateDTO(StockUpdateDTO stockUpdateDTO);
	
	public void updateStockTime(StockUpdateDTO stockUpdateDTO);
	
	public StockUpdateDTO findStockUpdateBySUpplierId(@Param("supplierId") String supplierId);
	
}
