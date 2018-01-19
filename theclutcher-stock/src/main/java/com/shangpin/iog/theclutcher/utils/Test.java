package com.shangpin.iog.theclutcher.utils;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

public class Test
{
	
	public static void main(String[] args) {
		readLine(getJson());
	}

  public static String getJson() {
    String app_secret = "hM&L1dqGA5YGjGK%fU8*715D$g&z$B";
    Map<String,String> params = new LinkedHashMap<String,String>();
    String app_key = "SHNGPN-001";
    params.put("app_key", app_key);
    params.put("request", "");
    String time = new StringBuilder().append(getUTCTime().getTime()).append("").toString();
    time = time.substring(0, 10);
    params.put("time_stamp", time);
    String charset = "UTF-8";

    String json = null;
    try {
      String md5_sign = new StringBuilder().append("app_key=").append(ToolsUtils.urlEncode(app_key, charset)).append((params.containsKey("request")) ? new StringBuilder().append("&request=").append(ToolsUtils.urlEncode((String)params.get("request"), charset)).toString() : "").append("&time_stamp=").append(ToolsUtils.urlEncode((String)params.get("time_stamp"), charset)).append("_").append(app_secret).toString();
      String md5_result = MD5.encrypt32(md5_sign);
      params.put("sign", md5_result);
      Map<String,String> headerMap = new LinkedHashMap<String,String>();
      headerMap.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
      json = HttpUtil45.operateData("get", null, "http://ws.theclutcher.com/shangpin.svc/stock", new OutTimeConfig(1000*60*20, 1000*60*20,1000*60*20), params, null, headerMap, null, null);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    return json;
  }

  private static Date getUTCTime() {
    Calendar cal = Calendar.getInstance();
    cal.setTimeZone(TimeZone.getTimeZone("GMT"));
    System.out.println(cal.getTime());

    int zoneOffset = cal.get(15);

    int dstOffset = cal.get(16);

    cal.add(14, -(zoneOffset + dstOffset));
    return cal.getTime();
  }

  public static long GetTicks()
  {
    SimpleDateFormat s = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
    String date = s.format(getUTCTime());
    String[] ds = date.split("/");

    Calendar calStart = Calendar.getInstance();
    calStart.set(1, 1, 3, 0, 0, 0);

    Calendar calEnd = Calendar.getInstance();
    calEnd.set(Integer.parseInt(ds[0]), Integer.parseInt(ds[1]), Integer.parseInt(ds[2]), Integer.parseInt(ds[3]), Integer.parseInt(ds[4]), Integer.parseInt(ds[5]));

    long epochStart = calStart.getTime().getTime();

    long epochEnd = calEnd.getTime().getTime();

    long all = epochEnd - epochStart;

    long ticks = all / 1000L * 1000000L * 10L;

    return ticks;
  }

  private static void readLine(String content)
  {
    File file = new File("D://clutcher0000.json");
    FileWriter fwriter = null;
    try {
      fwriter = new FileWriter(file);
      fwriter.write(content);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        fwriter.flush();
        fwriter.close();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
}