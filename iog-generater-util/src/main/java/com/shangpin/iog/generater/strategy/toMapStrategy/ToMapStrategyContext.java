package com.shangpin.iog.generater.strategy.toMapStrategy;

/**
 * 分派策略的类
 * @author sunny
 *
 */
public class ToMapStrategyContext {
	
	public static TransStrategy checkToMap(String condition){
		if ("".equals(condition)) {
			return new DefaultImp();
		}else if(condition.contains("sas")){
			return new SasImp();
		}else if(condition.contains("spuWithSku")){
			return new SpuWithSkuImp();
		}else{
			return new DefaultImp();
		}
	}
}
