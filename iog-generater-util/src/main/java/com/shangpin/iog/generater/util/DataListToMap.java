package com.shangpin.iog.generater.util;

import java.util.List;
import java.util.Map;

import com.shangpin.iog.generater.dto.ProductDTO;
import com.shangpin.iog.generater.strategy.toMapStrategy.ToMapStrategyContext;
import com.shangpin.iog.generater.strategy.toMapStrategy.TransStrategy;

public class DataListToMap {
	public static Map<String, Object> toMap(String condition, List<ProductDTO> productList, String supplierId, String[] strategys){
		TransStrategy strategy = ToMapStrategyContext.checkToMap(condition);
		Map<String, Object> map = strategy.toMap(productList, supplierId, strategys);
		return map;
	}
}
