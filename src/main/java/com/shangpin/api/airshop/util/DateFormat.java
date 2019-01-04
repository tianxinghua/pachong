package com.shangpin.api.airshop.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**时间格式化
 * @author Administrator
 *
 */
public class DateFormat {
	
	/**时间 格式 转换
	 * @param time
	 * @param oldfromateString
	 * @param newfromateString
	 * @return
	 */
	public static String TimeFormatChangeToString(String time,String oldfromateString,String newfromateString){
		if (time==null||time.equals("")) {
			return "";
		}
		SimpleDateFormat oldformat = new SimpleDateFormat(oldfromateString);
		SimpleDateFormat newformat = new SimpleDateFormat(newfromateString);
		
		try {
			return newformat.format(oldformat.parse(time));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return "";
		}
		
	}
	
	/**时间格式化
	 * @param time
	 * @param newfromateString
	 * @return
	 */
	public static String TimeFormatToString(Date time,String newfromateString){
		
		SimpleDateFormat newformat = new SimpleDateFormat(newfromateString);
		return newformat.format(time);
		
	}
}
