package com.shangpin.ephub.product.business.common.util;


import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateTimeUtil {
  /**
   * Return current datetime string.
   * @return current datetime, pattern: "yyyy-MM-dd HH:mm:ss".
   */
  public static String getDateTime() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String dt = sdf.format(new Date());
    return dt;
  }

  /**
   * Return current datetime string.
   * @param pattern format pattern
   * @return current datetime
   */
  public static String getDateTime(String pattern) {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    String dt = sdf.format(new Date());
    return dt;
  }

  /**
   * Return short format datetime string.
   * @param date java.util.Date
   * @return short format datetime
   */
  public static String strForDate(Date date) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    return sdf.format(date);
  }

  /**
   * Return short format datetime string.
   * @param date java.util.Date
   * @return short format datetime
   */
  public static String strForDateNew(Date date) {
	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	  return sdf.format(date);
  }

  /**
   * 生日日期格式化
   * @param date
   * @return MMdd
   */
  public static String birthdayFmt(Date date){
	  SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
	    return sdf.format(date);
  }

  /**
   * 获取日期格式如：2010年1月1日
   * @return
   */
  public static String getDateToString(){
	  StringBuffer str = new StringBuffer();
	  str.append(getCurrentYear()+"年"+getCurrentMonth()+"月"+getCurrentDay(getShortCurrentDate()+"日"));
	  return str.toString();
  }

  /**
   * Return short format datetime string.
   * @param date java.util.Date
   * @return short format datetime
   */
  public static String shortFmt(Date date) {
  	if(date==null){
  	 return "";
  	}
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    return sdf.format(date);
  }

  public static String shortMonthFmt(Date date) {
	  	if(date==null){
	  	 return "";
	  	}
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
	    return sdf.format(date);
  }

  public static String LongFmt(Date date) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    return sdf.format(date);
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
  public static Date parse(String param) {
    Date date = null;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    try {
      date = sdf.parse(param);
    }
    catch (ParseException ex) {
    }
    return date;
  }

  /**
   * Parse a datetime string.
   * @param param datetime string, pattern: "yyyy-MM-dd HH:mm:ss".
   * @return java.util.Date parsed as ‘yyyy-MM-dd’ 
   */
  public static Date getShortDate(String param) {
    Date date = null;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    try {
      date = sdf.parse(param);
    }
    catch (ParseException ex) {
    }
    return date;
  }
  /**
   * 获取格林尼治时间1970-1-1 00:00:00到date的天数<br/>
   * 如：date=1970-01-01 xx:xx:xx，则是0
   * @param date 任意日期 ,如果为null则为当前时间
   * @return 1970-1-1 00:00:00到date的天数<br/>
   */
  public static int getDays(Date date){
		Calendar c = Calendar.getInstance();
		if(date!=null) c.setTime(date);
		Date x = DateUtils.truncate(c, Calendar.DATE).getTime();
		long m = (x.getTime() / 1000) / 60;
		long h = m / 60;
		Long d1 = h / 24+(h%24>0?1:0);
		return d1.intValue();
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



  /**
   * 返回当前年份
   * @return String
   */
  public static String getCurrentYear() {
    Calendar cal = Calendar.getInstance();
    return String.valueOf(cal.get(Calendar.YEAR));
  }

  /**
   * 返回当前月份
   * @return String
   */
  public static String getCurrentMonth() {
    Calendar cal = Calendar.getInstance();
    int nMonth = cal.get(Calendar.MONTH) + 1;
    if (nMonth < 10) {
      return "0" + String.valueOf(nMonth);
    }
    return String.valueOf(nMonth);
  }
  /**
   * 返回上一月
   * @return String
   */
  public static String getPreviousMonth() {
    Calendar cal = Calendar.getInstance();
    int nMonth = cal.get(Calendar.MONTH);
    if (nMonth < 10) {
      if(nMonth==0){                 //如果是1月，则返回上年12月
        return "12";
      }
      return "0" + String.valueOf(nMonth);
    }
    return String.valueOf(nMonth);
  }


  /**
   * 返回下一月
   * @return String
   */
  public static String getNextMonth() {
    Calendar cal = Calendar.getInstance();
    int nMonth = cal.get(Calendar.MONTH) + 2;
    if (nMonth < 10) {
      return "0" + String.valueOf(nMonth);
    }
    return String.valueOf(nMonth);
  }

  /**
   * 返回下一年份
   * @return String
   */
  public static String getNextYear() {
    Calendar cal = Calendar.getInstance();
    return String.valueOf(cal.get(Calendar.YEAR) + 1);
  }

  /**返回上一年份 */
  public static String getLastYear() {
    Calendar cal = Calendar.getInstance();
    return String.valueOf(cal.get(Calendar.YEAR) - 1);
  }

  /**
   * 获得日期字符串
   * @return
   */
  public static String getDateString() {
    Date dToday = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat(
        "yyyyMMddHHmmss");
    return formatter.format(dToday);
  }

  /**
   * 解析日期类型,获取当前月份天数
   * @param strDate String:2005-05-05
   * @return int
   * @author:gjw
   * @date:2005-07-11
   */
  public static int getDayCount(String strDate) {
    int nYear = 0;
    int nMonth = 0;
    int[] nDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}; //默认非润年
    if (strDate.length() > 7) {
      nYear = Integer.parseInt(strDate.substring(0, 4));
      nMonth = Integer.parseInt(strDate.substring(5, 7));
    }
    if ( (nYear % 4 == 0 && nYear % 100 != 0) || (nYear % 400 == 0)) { //判断是否是闰年
      nDays[1] = 29;
    }
    for (int i = 0; i < nDays.length; i++) {
      if (nMonth == i + 1) {
        return nDays[i];
      }
    }
    return 0;
  }

  /**
   * 获取当前天数
   * @param strDate String:2005-05-05
   * @return int
   * @author:gjw
   * @date:2005-07-11
   */
  public static int getCurrentDay(String strDate) {
    int nDay = 0;
    if (strDate.length() >=10) {
      nDay = Integer.parseInt(strDate.substring(8, 10));
    }
    return nDay;
  }

  /**
   * 返回当前日期 
   * @return  String yyyy-MM-dd解释的字符串
   * @author  hechangcheng
   */
  public static String getShortCurrentDate() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String dt = sdf.format(new Date());
    return dt;
  }

  /**返回比当前日期早七天的日期 */
  public static String getDay() {
      Date ndate = new Date();
      /**七天共 604800000 毫秒*/
      long currentdate = ndate.getTime() - 604800000 ;
      Date dd = new Date(currentdate);
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      String dt = sdf.format(dd);
      return dt ;
  }
 public static String getbeforeDay(int n){
    Date ndate =new Date();
    //n天共n*24*60*60*1000毫秒
    long currentdate=ndate.getTime()-n*24*60*60*1000;
    Date dd = new Date(currentdate);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String dt = sdf.format(dd);
    return dt ;
 }

 //dateStr的格式:yyyymmdd
 public static String getbeforeDay(String dateStr ,int n){
	    Date ndate =getDateSFomate(dateStr);
	    //n天共n*24*60*60*1000毫秒
	    long currentdate=ndate.getTime()-n*24*60*60*1000;
	    Date dd = new Date(currentdate);
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    String dt = sdf.format(dd);
	    return dt ;
	 }
  /**
   * 返回比当前日早一天的日期
   * @return String
   */
  public static String getPreviouseDate(){
      Date ndate = new Date();
      /**一天共86400000 毫秒*/
      long currentdate = ndate.getTime() - 86400000 ;
      Date dd = new Date(currentdate);
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      String dt = sdf.format(dd);
      return dt ;

  }

public static Date getDateSFomate(String param) {
   Date date = null;
   SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
   try {
    date = sdf.parse(param);
   }
   catch (ParseException ex) {
   }
    return date;
 }








	 /**
	  * 当年
	  * @return
	  */
	public static int currentYear(){
			return Calendar.getInstance().get(Calendar.YEAR);
		}

	/**
	 * 当月
	 * @return
	 */
	public static int currentMonth(){
			return Calendar.getInstance().get(Calendar.MONTH) + 1;
		}

/**
 * 返回当前月最后一天的日期
 * @return date
 * parameter yyyy-mm-dd
 * wlb
 */
public static Date getLastDay(String dateDay)
{
	Calendar c   =   Calendar.getInstance();
	Date date = null;
	dateDay=dateDay+" 00:00:00";
	c.setTime(getDateTimeFormate(dateDay));
	c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
	date=c.getTime();
	return date;
}
/**
 * 返回当前月第一天的日期
 * @return date
 * parameter yyyy-mm-dd
 * wlb
 */
public static Date getFirstDay(String dateDay)
{
	Calendar c   =   Calendar.getInstance();
	Date date = null;
	dateDay=dateDay+" 00:00:00";
	c.setTime(getDateTimeFormate(dateDay));
	c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
	date=c.getTime();
	return date;
}
public static String getCurrentFirstDay()
{
	String dateDay=getDateTime();
	Calendar c   =   Calendar.getInstance();
	Date date = null;
	c.setTime(getDateTimeFormate(dateDay));
	c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
	date=c.getTime();
	return shortFmt(date);
}

public static String getCurrentLastDay()
{
	String dateDay=getDateTime();
	Calendar c   =   Calendar.getInstance();
	Date date = null;
	c.setTime(getDateTimeFormate(dateDay));
	c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
	date=c.getTime();
	return shortFmt(date);
}
public static Date getFirstDay(Date dateDay)
{
	return getFirstDay(shortFmt(dateDay));
}

    /**
     * 获取指定日期的月的最后一天
      * @param date
     * @return
     */
    public static Date getLastDay(Date date){
        return getLastDay(shortFmt(date));
    }


/**
 * 根据年月获得，指定年月的最后一天
 * @param yy_mm 例2007-11
 * @return 返回指定参数＋最后一天＝20071130
 * @author LiangYanPeng
 */
public static String getLastDayByYYMM(String yy_mm) {
	return DateTimeUtil.strForDate(DateTimeUtil.getLastDay(yy_mm + "-01"));
}



    /**
     * 获取指定日期的指定数字的相对日期
     * @param today  指定日期
     * @param num    指定数字
     * @param  type  指定类型 为空时默认为天  D 天  H  小时  M 分   S 秒
     * @return：获取当前日期的指定数字的相对日期
     */
    public static Date getAppointDayFromSpecifiedDay(Date today,int num,String type){
        Calendar c   =   Calendar.getInstance();
        c.setTime(today);

        if("Y".equals(type)){
            c.add(Calendar.YEAR, num);
        }else if("M".equals(type)){
            c.add(Calendar.MONTH, num);
        }else if(null==type||"".equals(type)||"D".equals(type))
            c.add(Calendar.DAY_OF_YEAR, num);
        else if("H".equals(type))
            c.add(Calendar.HOUR_OF_DAY,num);
        else if("m".equals(type))
            c.add(Calendar.MINUTE,num);
        else if("S".equals(type))
            c.add(Calendar.SECOND,num);
        return c.getTime();
    }



/**
 * 获取当前日期的前一天
 * @return：Date
 */
public static Date getPreviousDay() {
	Date date = new Date();
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.add(Calendar.DAY_OF_MONTH, -1);
	date = calendar.getTime();
	return date;



}

/**
 * 获取该日期的零点
 * @param  date
 * @return：Date
 */
public static Date getZeroClockOfOneDay(Date date) {
	Calendar cal = Calendar.getInstance();
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.MILLISECOND, 0);
	cal.add(Calendar.DAY_OF_MONTH, -1);
	return cal.getTime();
}

 /**
 * 获取往前第一周的日期
 * @param  date
 * @return：Date
 */
public static Date getPreviousOneWeek(Date date) {
	Calendar cal = Calendar.getInstance();
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.MILLISECOND, 0);
	cal.add(Calendar.DAY_OF_MONTH, -7);
	return cal.getTime();
}

 /**
 * 获取往前第二周的日期
 * @param  date
 * @return：Date
 */
public static Date getPreviousTwoWeek(Date date) {
	Calendar cal = Calendar.getInstance();
	cal.set(Calendar.HOUR_OF_DAY, 0);
	cal.set(Calendar.SECOND, 0);
	cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.MILLISECOND, 0);
	cal.add(Calendar.DAY_OF_MONTH, -14);
	return cal.getTime();
}

/**
 * 获取往前第三周的日期
 * @param  date
 * @return：Date
 */
public static Date getPreviousThreeWeek(Date date) {
	Calendar cal = Calendar.getInstance();
	cal.set(Calendar.HOUR_OF_DAY, 0);
	cal.set(Calendar.SECOND, 0);
	cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.MILLISECOND, 0);
	cal.add(Calendar.DAY_OF_MONTH, -21);
	return cal.getTime();
}

/**
 * 获取往前第四周的日期
 * @param  date
 * @return：Date
 */
public static Date getPreviousFourWeek(Date date) {
	Calendar cal = Calendar.getInstance();
	cal.set(Calendar.HOUR_OF_DAY, 0);
	cal.set(Calendar.SECOND, 0);
	cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.MILLISECOND, 0);
	cal.add(Calendar.DAY_OF_MONTH, -28);
	return cal.getTime();
}
    /**
     * 获取当前时间下一年的当月最后一天的日期
     * @param  date
     * @return：Date
     */
    public static String getNextYearAndLastMonthDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR,1);
        date =cal.getTime();
        return getLastDayByYYMM(shortMonthFmt(date));
    }
    
    public static Date parse(String dateStr,String pattern){
    	Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
          date = sdf.parse(dateStr);
        }
        catch (ParseException ex) {
        }
        return date;
    }
    /**
     * 返回1970年的1月1号
     * @return
     */
	public static Date initDate() {
		Calendar d = Calendar.getInstance();
		d.setTimeInMillis(0);
		return d.getTime();
	}
	/**
	 * 获取某天，最后一秒的时间 23:59
	 * @return
	 */
	public static Date getFinalMinuteTime(Date date){
		Calendar d = Calendar.getInstance();
		d.setTime(date);
		d.set(Calendar.HOUR, 23);
		d.set(Calendar.MINUTE,59);
		d.set(Calendar.SECOND,59);
		return d.getTime();
	}
	
	public static void main(String[] args) {
		
		System.out.println(DateTimeUtil.getTime(DateTimeUtil.getZeroClockOfOneDay(new Date())));
		System.out.println(DateTimeUtil.getTime(DateTimeUtil.getFinalMinuteTime(new Date())));
		System.out.println(DateTimeUtil.getTime(DateTimeUtil.getAnyYear(2300)));
		
	}

	/**
	 * 获取任意一年的最后一天一刻，
	 * @param year 
	 * @return
	 */
	public static Date getAnyYear(int year) {
		Calendar d = Calendar.getInstance();
		d.set(Calendar.YEAR, year);
		d.set(year, 11, 31, 23, 59, 59);
		return d.getTime();
	}
    //获取日期差
    public static int getDateifference(Date startDate,Date endDate){

        Date tempStartDate = DateTimeUtil.getShortDate(DateTimeUtil.shortFmt(startDate));
        Date tempEndDate = DateTimeUtil.getShortDate(DateTimeUtil.shortFmt(endDate));

        Calendar calendar=Calendar.getInstance();
        calendar.setTime(tempEndDate);
        long  endTime = calendar.getTimeInMillis();
        calendar.setTime(tempStartDate);

        long l=endTime-calendar.getTimeInMillis();

        return new  Long(l/(1000*60*60*24)).intValue();

    }

    /**
     * 返回时间差 毫秒级
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    public static long getTimeDifference(Date startDate,Date endDate){
        Date tempStartDate =  DateTimeUtil.convertDateFormat(startDate, "yyyy-MM-dd HH:mm:ss");
        Date tempEndDate =  DateTimeUtil.convertDateFormat(endDate, "yyyy-MM-dd HH:mm:ss");

        Calendar calendar=Calendar.getInstance();
        calendar.setTime(tempEndDate);
        long  endTime = calendar.getTimeInMillis();
        calendar.setTime(tempStartDate);

        return endTime-calendar.getTimeInMillis();
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
      return   convertFormat(convertFormat(date,format),format);

    }
}
