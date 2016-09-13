package com.shangpin.iog.common.utils;

public class JavaUtils {

	public static String getBASE64(String s) { 
		if (s == null) return null; 
		return (new sun.misc.BASE64Encoder()).encode( s.getBytes() ); 
	} 
	
	public static String getFromBASE64(String s) { 
		if (s == null) return null; 
		sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder(); 
		try { 
			byte[] b = decoder.decodeBuffer(s); 
			return new String(b); 
		} catch (Exception e) { 
			return null; 
		} 
	}
}
