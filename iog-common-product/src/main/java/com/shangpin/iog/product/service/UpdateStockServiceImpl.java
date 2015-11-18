package com.shangpin.iog.product.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.iog.dto.StockUpdateDTO;
import com.shangpin.iog.product.dao.StockUpdateMapper;
import com.shangpin.iog.service.UpdateStockService;

@Service
public class UpdateStockServiceImpl implements UpdateStockService{
	@Autowired
	StockUpdateMapper updateStockDao;
	
	@Override
	public void updateTime(String supplierId) throws SQLException {
		StockUpdateDTO suDTO = new StockUpdateDTO();
		suDTO.setSupplierId(supplierId);
		suDTO.setUpdateTime(new Date());
		updateStockDao.updateStockTime(suDTO);
	}

	@Override
	public List<StockUpdateDTO> getAll() throws SQLException {
		return updateStockDao.getAllData();
	}
}
