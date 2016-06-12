package com.shangpin.iog.generater.strategy.toMapStrategy;

import java.util.List;
import java.util.Map;

import com.shangpin.iog.generater.dto.ProductDTO;

/**
 * 转化为map的策略接口
 * @author Administrator
 *
 */
public  interface TransStrategy {
	
	 Map<String,Object> toMap(List<ProductDTO> productList,String supplierId,String[] strategys);
		
}
