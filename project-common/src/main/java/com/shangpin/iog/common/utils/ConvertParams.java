package com.shangpin.iog.common.utils;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class ConvertParams {

	public static String convertDate(String str){
		if(StringUtils.isBlank(str)){
			return "";
		}else{
			StringBuffer sb=new StringBuffer(str);
			sb.insert(4,"-");
			sb.insert(7,"-");
			sb.insert(10, " ");
			if(str.length()>=10){
				sb.insert(13,":");
			}
			if(str.length()>=12){
				sb.insert(16,":");
			}
			return sb.toString();
		}
	}
	public static List<String> paramList(String str1,String str2) {
		Integer i1=Integer.valueOf(str1.replace("-",""));
		Integer i2=Integer.valueOf(str2.replace("-",""));
		List<String> list=new ArrayList<String>();
		for (int i = i1; i <= i2; i++) {
			list.add(String.valueOf(i));
		}
		return list;
	}

	public static String paramString(String str1, String str2) {
		Integer i1 = Integer.valueOf(str1.replace("-", ""));
		Integer i2 = Integer.valueOf(str2.replace("-", ""));

		StringBuffer res = new StringBuffer();

		for (int i = i1; i <= i2; i++) {
			String str = String.valueOf(i);
			//str = str.substring(str.length() - 2, str.length());
			String day=str.substring(str.length() - 2, str.length());
			String month=str.substring(str.length() - 4, str.length()-2);
			if (Integer.valueOf(day) > 0 && Integer.valueOf(day) < 32 && Integer.valueOf(month)>0 && Integer.valueOf(month)<13) {
				if (i == i1) {
					res.append("'").append(String.valueOf(i)).append("'");
				} else {
					res.append(",").append("'").append(String.valueOf(i)).append("'");
				}
			}
		}
		return res.toString();
	}

}
