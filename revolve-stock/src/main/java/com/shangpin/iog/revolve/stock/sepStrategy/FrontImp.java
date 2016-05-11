package com.shangpin.iog.revolve.stock.sepStrategy;

import java.util.List;

public class FrontImp extends ISepStrategy{

	public FrontImp(String strategy) {
		super(strategy);
	}

	@Override
	public String merge(List<String> dataList) {
		String[] split = strategy.split("%");
		return dataList.get(0).split(split[1])[Integer.valueOf(split[2])]+split[3]+dataList.get(1);
	}


}
