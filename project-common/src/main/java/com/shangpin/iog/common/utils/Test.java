package com.shangpin.iog.common.utils;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * Created by lizhongren on 2016/1/29.
 */
public class Test {

    private static LoggerUtil logger = LoggerUtil.getLogger("info");
    public static void main(String[] args) throws Exception{

//        int i = 10/2;
//        logger.info("i="+i);
//
//        try {
//            int j=2/0;
//        } catch (Exception e) {
//            logger.info("exception ="+e.getMessage());
//        }
//           System.out.println( "201602161167427".compareTo("201602161167427"));
//        HttpUtil45.get("http://www.pos123.us/api/v2/products/.json?limit=50&page=2",new OutTimeConfig(),
//                null,"RIX5NkHDIM25yUFZmDlVSdWEE7V3aSYv","");


//        String skuData = HttpUtil45.postAuth("http://79.62.242.6:8088/ws_sito/ws_sito_p15.asmx/GetAllAvailabilityMarketplace", null,
//                new OutTimeConfig(1000*60*10,1000*60*60,1000*60*60),"shangpin","Daniello0203");
//        System.out.println("skuData　＝"+ skuData);

//       Date date =  DateTimeUtil.convertFormat(DateTimeUtil.shortFmt(new Date()) +" 00:00:00","yyyy-MM-dd HH:mm:ss");
//       System.out.println("  == " + date.toString() );
    	String json = "{\"supplier\":\"2015111001657\",\"startDate\":\"2016-08-26 00:00:10\",\"endDate\":\"\",\"pageIndex\":\"\",\"pageSize\":\"\",\"supplierName\":\"efashion\",\"flag\":\"same\",\"bu\":\"-1\"}";
    	String sss =  HttpUtil45.operateData("post", "json", "http://iog.shangpin.com/iog/download/csv", new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10), null, json, "", "");
    	System.out.println(sss);
    }
}
