package com.shangpin.supplier.product.consumer.util;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * <p>Title:DateTimeUtil </p>
 * <p>Description: 日期工具类 </p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2017年1月3日 下午2:51:54
 *
 */
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
}
