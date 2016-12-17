package com.shangpin.supplier.product.consumer.supplier.common.util;

import org.springframework.util.StringUtils;

public class StringUtil {

	public static String verifyPrice(String price){
		if(StringUtils.isEmpty(price)){
			return "0.00";
		}else{
			return price.replaceAll(",", ".");
		}
	}
	
	public static int verifyStock(String stock){
		if(StringUtils.isEmpty(stock)){
			return 0;
		}else{
			return Integer.parseInt(stock);
		}
	}
}
