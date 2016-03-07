package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.dto.SupplierDTO;
import com.shangpin.iog.dto.SupplierStockDTO;
import com.shangpin.iog.product.dao.SupplierMapper;
import com.shangpin.iog.product.dao.SupplierStockMapper;
import com.shangpin.iog.service.SupplierService;
import com.shangpin.iog.service.SupplierStockService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by loyalty on 15/6/9.
 */
@Service
public class SupplierStockServiceImpl implements SupplierStockService {

	public static String  REPEAT_MESSAGE="数据插入失败键重复";
    @Autowired
    SupplierStockMapper supplierStockDAO;

	@Override
	public void updateStock(SupplierStockDTO stockDTO) throws ServiceMessageException {
		try {
			stockDTO.setOptTime(new Date());
            supplierStockDAO.updateStock(stockDTO);
        } catch ( Exception e) {
            throw new ServiceMessageException("数据更新失败"+e.getMessage());
        }
	}

	@Override
	public void saveStock(SupplierStockDTO stockDTO) throws ServiceMessageException {
		  try {
			  supplierStockDAO.save(stockDTO);
	        } catch (Exception e) {
	        	if(e instanceof DuplicateKeyException)
	        		throw new ServiceMessageException(REPEAT_MESSAGE);
	            throw new ServiceMessageException("数据插入失败"+e.getMessage());
	        }
	}


	@Override
	public SupplierStockDTO findSingleStock(String skuNo,
			String supplierId) throws ServiceMessageException {
		SupplierStockDTO list = null;
		try{
			Map<String,String> map = new HashMap<String,String>();
			map.put("supplierId", supplierId);
			map.put("skuNo", skuNo);
			list = supplierStockDAO.findBySkuAndSupplier(map);
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceMessageException("数据查询失败"+e.getMessage());
		}
		return list;
	}

	@Override
	public List<SupplierStockDTO> findAll() throws ServiceMessageException {
		List<SupplierStockDTO> list = null;
		try{
			list = supplierStockDAO.findAllSupplierStock();
		}catch(Exception e){
			throw new ServiceMessageException("数据查询失败"+e.getMessage());
		}
		return list;
	}

	@Override
	public List<SupplierStockDTO> findBySupplierId(String supplierId) throws ServiceMessageException {
		List<SupplierStockDTO> list = new ArrayList<>();
		try{
			Map<String,String> map= new HashMap<>();
			map.put("supplierId",supplierId);
			list = supplierStockDAO.findByMap(map);
		}catch (Exception e){
			throw new ServiceMessageException("数据查询失败"+e.getMessage());
		}
		return list;
	}


}
