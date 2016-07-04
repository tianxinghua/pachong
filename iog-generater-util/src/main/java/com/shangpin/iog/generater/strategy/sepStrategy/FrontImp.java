package com.shangpin.iog.generater.strategy.sepStrategy;

import java.util.List;

public class FrontImp extends ISepStrategy{

	public FrontImp(String strategy) {
		super(strategy);
	}

	/**
	 * 处理2个字段，适用于要处理前一个字段，比如字段1值是a|b,字段2值是c，现在要得到a-c则策略是：
	 * front%|%0%-
	 * @param dataList 需要合并的两列的值，如["a|b","c"]
	 */
	@Override
	public String merge(List<String> dataList) {
		String[] split = strategy.split("%");
		String[] tmp = dataList.get(0).split(split[1]);//(a|b).split("|") == [a,b]
		String first = tmp[Integer.valueOf(split[2])];//tmp[0] == a
		String middle = split[3];//-
		String end = dataList.get(1);//c		
		//return dataList.get(0).split(split[1])[Integer.valueOf(split[2])]+split[3]+dataList.get(1);
		return first+middle+end;		
		
	}


}
