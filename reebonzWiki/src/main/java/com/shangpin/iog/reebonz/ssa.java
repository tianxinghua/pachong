package com.shangpin.iog.reebonz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ssa   
{   
        public static void main (String args[])   
        {   
                try   
                {   
                	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                	Date sss = new Date();
                	
                	//2007-10-23T17:15:44.000Z
                        String ts = "2015-11-15T06:00:00Z";   
                        String ss = getString(ts);
                        Date d = sf.parse(ss);
                        
                        boolean a = d.before(sss);
                        System.out.println(a);
                }   
                catch(Exception pe)   
                {   
                       
                }   
        }   
  
  
        private static String getString(String ts) throws ParseException   
        {   
        	 ts = ts.replace("Z", " UTC");   
             SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");  
              Date dt = sdf.parse(ts);   
              TimeZone tz = sdf.getTimeZone();   
              Calendar c = sdf.getCalendar();   
                StringBuffer result = new StringBuffer();   
                result.append(c.get(Calendar.YEAR));   
                result.append("-");   
                result.append((c.get(Calendar.MONTH) + 1));   
                result.append("-");   
                result.append(c.get(Calendar.DAY_OF_MONTH));   
                result.append(" ");   
                result.append(c.get(Calendar.HOUR_OF_DAY));   
                result.append(":");   
                result.append(c.get(Calendar.MINUTE));   
                result.append(":");   
                result.append(c.get(Calendar.SECOND));   
                return result.toString();   
        }   
  
  
  
}   
  