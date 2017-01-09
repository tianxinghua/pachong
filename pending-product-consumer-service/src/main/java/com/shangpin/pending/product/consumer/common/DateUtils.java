package com.shangpin.pending.product.consumer.common;

import java.util.Calendar;

/**
 * Created by loyalty on 16/12/15.
 */
public class DateUtils {

    public static int  getCurrentMin(){
        Calendar calendar = Calendar.getInstance();
        return  calendar.get(Calendar.MINUTE);

    }
}
