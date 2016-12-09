package com.shangpin.ep.order.common;

import com.shangpin.ep.order.enumeration.LogLeve;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ep.order.enumeration.ErrorStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;

/** 
 * 异常处理类
 * @author lubaijiang
 */

@Component
public class HandleException {
	
	@Autowired
	LogCommon logCommon;
	
	/**
	 * 处理异常
	 * @param order
	 * @param e
	 */
	public void handleException(OrderDTO order,Throwable e){
		if(e.getMessage().equals("状态码:404") || e.getMessage().equals("状态码:500") || e.getMessage().equals("状态码:505")){
			order.setErrorType(ErrorStatus.API_ERROR);
			order.setDescription(e.getMessage());
		}else if(e.getMessage().contains("状态码:")){
			order.setErrorType(ErrorStatus.NETWORK_ERROR);
			order.setDescription(e.getMessage());
		}else{
			order.setErrorType(ErrorStatus.OTHER_ERROR);
			order.setDescription(e.getMessage());
		}
		//记录日志
		logCommon.loggerOrder(order, LogLeve.ERROR);
		
	}

	
}
