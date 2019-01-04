package com.shangpin.iog.generater.strategy.sepStrategy;

import java.util.List;

/**
 * 合并多个列
 * @author sunny
 *
 */
public class MoreImp extends ISepStrategy{

	public MoreImp(String strategy) {
		super(strategy);
	}

	/**
	 * more% %0%;
	 */
	@Override
	public String merge(List<String> dataList) {
		String[] split = strategy.split("%");
		String str = "";
		for (String string : dataList) {
			str +=string+split[3];
		}
		return str;
	}


}
