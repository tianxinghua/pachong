package com.shangpin.iog.generater.strategy.sepStrategy;

import java.util.List;

/**
 * 分割合并策略接口
  * @author Administrator
 *
 */
public abstract class ISepStrategy {
	protected String strategy;
	
	public ISepStrategy(String strategy) {
		super();
		this.strategy = strategy;
	}

	/**
	 * 两个值的分隔合并  front%;%0%- 如 值a;b 值c 处理后 为a-c  
	 * @param strategy  默认策略不处理   ""        前%sep%分割后第几个%合并sep	  f%;%0%-     后%sep%分割后第几个%合并sep   b%-%1%-    单%sep%分割后第几个%合并sep s%-%1%""
	 * @param dataList
	 * @return
	 */
	public abstract String merge(List<String> dataList);

	public String getStrategy() {
		return strategy;
	}
}
