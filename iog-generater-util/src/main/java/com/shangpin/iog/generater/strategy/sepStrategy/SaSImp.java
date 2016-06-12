package com.shangpin.iog.generater.strategy.sepStrategy;

import java.util.List;

public class SaSImp extends ISepStrategy{

	public SaSImp(String strategy) {
		super(strategy);
	}

	@Override
	public String merge(List<String> dataList) {
		String str = "";
		for (int i = 0; i < dataList.size()-1; i++) {
			str+=dataList.get(i)+strategy.split("%")[1];
		}
		str+=dataList.get(dataList.size()-1);
		return str;
	}

}
