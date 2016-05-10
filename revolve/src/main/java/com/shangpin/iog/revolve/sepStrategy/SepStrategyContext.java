package com.shangpin.iog.revolve.sepStrategy;

import java.util.List;

public class SepStrategyContext {
	//选择
	private ISepStrategy checkCondition(String condition){
		if (condition.equals("")) {
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
	//具体操作
	public ISepStrategy[] operate(String[] strategy){
		ISepStrategy[] s = new ISepStrategy[strategy.length];
		for (int i = 0; i < strategy.length; i++) {
			s[i]=checkCondition(strategy[i]);
		}
		return s;
	}
	
}
