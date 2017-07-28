package com.shangpin.asynchronous.task.consumer.productexport.type.commited.enumeration;

import java.util.HashMap;
import java.util.Map;

public class SpuState {

	/**
	 * 状态  0:未寄出 1：已加入发货单  2：已发货  3:不处理
	 */
	public static Map<Integer,String> spuStates = new HashMap<Integer,String>(){/**
		 * 
		 */
		private static final long serialVersionUID = 2025581465149620371L;

	{
		put(0,"未寄出");
		put(1,"已加入发货单");
		put(2,"已发货");
		put(3,"不处理"); 
	}};
}
