package com.shangpin.iog.generater.util;

import java.util.List;

import com.shangpin.iog.generater.dto.ProductDTO;
import com.shangpin.iog.generater.strategy.toMapStrategy.ToMapStrategyContext;
import com.shangpin.iog.generater.strategy.toMapStrategy.TransStrategy;

public class DataListToMap {
	public static void toMap(String condition, List<ProductDTO> productList, String supplierId, String[] strategys){
		TransStrategy strategy = ToMapStrategyContext.checkToMap(condition);
		strategy.toMap(productList, supplierId, strategys);
	}
}
