package com.shangpin.iog.product.service;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.StockUpdateLimitDTO;
import com.shangpin.iog.product.dao.StockUpdateLimitMapper;
import com.shangpin.iog.service.StockUpdateLimitService;

@Service
public class StockUpdateLimitServiceImpl implements StockUpdateLimitService{

	@Autowired
	StockUpdateLimitMapper stockUpdateLimitDAO;
	
	@Override
	public void save(StockUpdateLimitDTO hubSku) throws ServiceException {
		
		try {
			stockUpdateLimitDAO.save(hubSku);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateLimitNum(StockUpdateLimitDTO skuDTO)
			throws ServiceException {
		if(skuDTO.getUpdateTime()==null) skuDTO.setUpdateTime(new Date());
		stockUpdateLimitDAO.updateLimitNum(skuDTO);
		
	}

	@Override
	public StockUpdateLimitDTO findBySupplierId(String supplierId) {
		
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = sim.format(new Date());
		
		return stockUpdateLimitDAO.findBySupplierId(supplierId,nowDate);
	}

}
