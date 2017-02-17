package com.shangpin.supplier.product.consumer.supplier.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.shangpin.supplier.product.consumer.supplier.common.dto.Color;

public class StringUtil {

	/**
	 * 校验价格
	 * @param price
	 * @return
	 */
	public static String verifyPrice(String price){
		if(StringUtils.isEmpty(price)){
			return "0.00";
		}else{
			return price.replaceAll(",", ".");
		}
	}
	/**
	 * 校验库存
	 * @param stock
	 * @return
	 */
	public static int verifyStock(String stock){
		
		if(StringUtils.isEmpty(stock)){
			return 0;
		}else{
			if(stock.contains(".")){
				stock = stock.substring(0,stock.indexOf(".")); 
			}
			if(Integer.parseInt(stock.trim())<0){
				return 0;
			}
			return Integer.parseInt(stock);
		}
	}
	/**
	 * 将颜色码和颜色值混合的字段拆分
	 * @param color
	 * @return
	 */
	public static Color splitColor(String color) {
		Color colorDto = new Color();
		if(color.contains(" ")){
			String str = color.substring(0, color.indexOf(" "));
			String regex = "\\d+";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(str);
			while (m.find()) {
				color = color.substring(color.indexOf(" ")).trim();
				colorDto.setColorCode(str);
				colorDto.setColorValue(color);
				return colorDto;
			}
		}
		colorDto.setColorValue(color); 
		return colorDto;
	}
}
