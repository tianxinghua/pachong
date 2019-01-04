package com.shangpin.iog.generater.strategy.sepStrategy;

import java.util.List;

public class BackImp extends ISepStrategy{

	public BackImp(String strategy) {
		super(strategy);
	}

	/**
	 * 处理2个字段，适用于处理后一个字段，比如字段1值是a，字段2值是b-c，现在要得到a_c，则策略是：
	 * back%-%1%_
	 */
	@Override
	public String merge(List<String> dataList) {
		String[] split = strategy.split("%");
		return dataList.get(0)+split[3]+dataList.get(1).split(split[1])[Integer.valueOf(split[2])];
	}


}
