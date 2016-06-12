package com.shangpin.iog.generater.strategy.toMapStrategy;

public class ToMapStrategyContext {
	
	public static TransStrategy checkToMap(String condition){
		if ("".equals(condition)) {
			return new DefaultImp();
		}else if(condition.contains("sas")){
			return new SasImp();
		}else{
			return new DefaultImp();
		}
	}
}
