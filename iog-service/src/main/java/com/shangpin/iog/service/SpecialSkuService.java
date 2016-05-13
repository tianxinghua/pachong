package com.shangpin.iog.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.dto.SpecialSkuDTO;
import com.shangpin.iog.dto.StockUpdateDTO;


/**
 * @author monkey
 *
 */
public interface SpecialSkuService {
	public void saveDTO(List<SpecialSkuDTO> stockUpdateDTO) throws ServiceMessageException;

	public Map<String, String> findListSkuBySupplierId(String supplierId);

	public void deleteSkuBySupplierId(List<SpecialSkuDTO> list);
}
