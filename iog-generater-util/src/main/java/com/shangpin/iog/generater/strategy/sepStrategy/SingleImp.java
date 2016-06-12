package com.shangpin.iog.generater.strategy.sepStrategy;

import java.util.List;

public class SingleImp extends ISepStrategy{

	public SingleImp(String strategy) {
		super(strategy);
	}

	@Override
	public String merge(List<String> dataList) {
		String[] split = strategy.split("%");
		return dataList.get(0).split(split[1])[Integer.valueOf(split[2])];
	}

}
