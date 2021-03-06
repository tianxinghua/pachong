package com.shangpin.iog.product.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.iog.dto.StockUpdateDTO;
import com.shangpin.iog.product.dao.StockUpdateMapper;
import com.shangpin.iog.service.UpdateStockService;

@Service("updateStockServiceImpl")
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
	public void updateStatus(StockUpdateDTO stockUpdateDTO) throws SQLException {
		updateStockDao.updateStockStatus(stockUpdateDTO);
	}

	@Override
	public List<StockUpdateDTO> getAll() throws SQLException {
		return updateStockDao.getAllData();
	}

	@Override
	public void saveDTO(StockUpdateDTO stockUpdateDTO) throws SQLException {
		updateStockDao.saveStockUpdateDTO(stockUpdateDTO);
	}

	@Override
	public void saveOrUpdateDTO(StockUpdateDTO stockUpdateDTO)
			throws SQLException {
		List<StockUpdateDTO> allData = updateStockDao.getAllData();
		boolean flag = false;
		for (StockUpdateDTO data : allData) {
			if (data.getSupplierId().equals(stockUpdateDTO.getSupplierId())) {
				flag = true;
			}
		}
		if (flag) {
			updateStockDao.updateStockStatus(stockUpdateDTO);
		}else{
			updateStockDao.saveStockUpdateDTO(stockUpdateDTO);
		}
	}

	@Override
	public void saveStockUpdateDTO(StockUpdateDTO stockUpdateDTO)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public StockUpdateDTO findStockUpdateBySUpplierId(String supplierId)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}
