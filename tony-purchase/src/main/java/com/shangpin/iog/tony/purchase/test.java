package com.shangpin.iog.tony.purchase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/9/28.
 */
public class test {

    public static void main(String[] args) throws  Exception{
        Date date=new Date();
        DateFormat format=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String time=format.format(date);
        System.out.println(time);
    }
}
