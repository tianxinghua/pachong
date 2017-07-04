package com.shangpin.ephub.product.business.utils.time;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateTimeUtil {
	
	private static String pattern = "yyyy-MM-dd";
	/**
	 * 将时间字符串格式化为yyyy-MM-dd
	 * @param param
	 * @return
	 * @throws ParseException
	 */
	public static Date parse(String param) throws ParseException {
	   SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	   return sdf.parse(param);
	}
	/**
	 * 将日期转为字符串 yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String format(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	public static int  getCurrentMin(){
        Calendar calendar = Calendar.getInstance();
        return  calendar.get(Calendar.MINUTE);
    }
  
	public static String getTime(Date date) {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    return sdf.format(date);
	}
  

  /**
    * Parse a datetime string.
    * @param param datetime string, pattern: "yyyy-MM-dd HH:mm:ss".
    * @return java.util.Date
    */
   public static Date getDateTimeFormate(String param) {
     Date date = null;
     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     try {
       date = sdf.parse(param);
     }
     catch (ParseException ex) {
     }
     return date;
  }

    //按给定的格式转化给定的日期
    public static String convertFormat(Date date ,String format){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return (sdf.format(date));

    }

    //按给定的格式转化给定的日期
    public static Date convertFormat(String date,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
           return  sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转化给定的日期格式
     * @param date
     * @param format
     * @return
     */
    public static Date convertDateFormat(Date date,String format){
      return  convertFormat(convertFormat(date,format),format);

    }
    /**
     * 将Date类型转换为yyyy-MM-dd格式
     * @param date
     * @return
     */
    public static Date convertDayDate(Date date){
    	return  convertFormat(convertFormat(date,pattern),pattern);
    }
}
