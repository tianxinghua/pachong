package com.shangpin.ice.ice;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by loyalty on 15/9/26.
 */
public class DateTimeUtil {

    //按给定的格式转化给定的日期
    public static String convertFormat(Date date ,String format){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return (sdf.format(date));

    }
}
