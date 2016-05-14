package com.shangpin.iog.product.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.dto.SpecialSkuDTO;
import com.shangpin.iog.service.SpecialSkuService;
@Service
public class SpecialSkuServiceImpl implements SpecialSkuService{
	 @Autowired
	 com.shangpin.iog.product.dao.SpecialSkuMapper specialSkuMapper;
	 public static String  REPEAT_MESSAGE="数据插入失败键重复";
	@Override
	public void saveDTO(List<SpecialSkuDTO> stockUpdateDTO) throws ServiceMessageException{
		
		 try {
			 specialSkuMapper.saveList(stockUpdateDTO);
	        } catch (Exception e) {
	        	if(e instanceof DuplicateKeyException)
	        		throw new ServiceMessageException(REPEAT_MESSAGE);
	            throw new ServiceMessageException("数据插入失败"+e.getMessage());
	        }
		
		
	}
	public Map<String, String> findListSkuBySupplierId(String supplierId){
		
		List<SpecialSkuDTO> list = specialSkuMapper.findListSkuBySupplierId(supplierId);
		Map<String,String> map = new HashMap<String,String>();
		if(list!=null){
			for(SpecialSkuDTO s : list){
				map.put(s.getSupplierSkuId(),"1");
			}
		}
		 return map;
		
	}
	
	public void deleteSkuBySupplierId(List<SpecialSkuDTO> list){
		
		if(list!=null){
			for(SpecialSkuDTO dto : list){
				specialSkuMapper.delete(dto);
			}
		}
		 
		
	}
}
