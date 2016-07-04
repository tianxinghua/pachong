package com.shangpin.iog.generater.strategy.sepStrategy;

import java.util.List;

/**
 * 默认单个
 * @author Administrator
 *
 */
public class DefaultImp extends ISepStrategy{
	
	public DefaultImp(String strategy) {
		super(strategy);
	}

	@Override
	public String merge(List<String> dataList) {
		return dataList.get(0);
	}
}
