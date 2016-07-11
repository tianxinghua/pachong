package com.shangpin.iog.generater.strategy.sepStrategy;

import java.util.List;

import org.apache.commons.lang.StringUtils;

public class SepStrategyContext {
	//具体操作
	public ISepStrategy[] operate(String[] strategy){
		ISepStrategy[] s = new ISepStrategy[strategy.length];
		for (int i = 0; i < strategy.length; i++) {
			s[i]=checkCondition(strategy[i]);
		}
		return s;
	}
	//选择
	private ISepStrategy checkCondition(String condition){
		if (StringUtils.isBlank(condition)) {
			return new DefaultImp(condition);
		}else if(condition.contains("front")){
			return new FrontImp(condition);
		}else if(condition.contains("back")){
			return new BackImp(condition);
		}else if(condition.contains("sin")){
			return new SingleImp(condition);
		}else if(condition.contains("sas")){
			return new SaSImp(condition);
		}else if(condition.contains("more")){
			return new MoreImp(condition);
		}else{
			return new DefaultImp(condition);
		}
	}
	
}
