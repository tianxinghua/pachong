package com.shangpin.iog.generater.strategy.sepStrategy;

import java.util.List;

public class SingleImp extends ISepStrategy{

	public SingleImp(String strategy) {
		super(strategy);
	}

	/**
	 * 拆分某一个字段，比如字段a值是XXL|3
	 * 现在要从a中摘出尺码的部分，则策略是：sin%|%0 
	 */
	@Override
	public String merge(List<String> dataList) {
		String[] split = strategy.split("%");
		return dataList.get(0).split(split[1])[Integer.valueOf(split[2])];
	}

}
