package com.shangpin.iog.tony.purchase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.tony.purchase.common.Constant;
import com.shangpin.iog.tony.purchase.schedule.AppContext;

/**
 * Created by Administrator on 2015/9/28.
 */
public class Startup {
	   private static   Logger logger = LoggerFactory.getLogger(Startup.class);
       private static ApplicationContext factory;
       private static void loadSpringContext()
       {
           factory = new AnnotationConfigApplicationContext(AppContext.class);
       }

    public static void main(String[] args) throws  Exception{
    	
//    	String json = "{\"merchantId\":\"56e95bb7f868647d13873d98\",\"token\":\"ec1ebaa88fbcb0e5fa02ad42bdcc5be4\",\"shopOrderId\":\"\",\"status\":\"PENDING\",\"statusDate\":\"2016/06/15 10:36:01\",\"orderDate\":\"2016/06/15 10:36:01\","
//    			+ "\"items\":[{\"sku\":\"\",\"qty\":1,\"price\":65.0,\"cur\":1}],\"orderTotalPrice\":65.0,\"shippingInfo\":{\"fees\":\"0.0\",\"address\":{\"firstname\":\"Filippo \",\"lastname\":\"Troina\",\"companyname\":\"Genertec Italia S.r.l.\",\"street\":\"VIA G.LEOPARDI 27\",\"hn\":\"22075 \",\"zip\":\"22075\",\"city\":\"LURATE CACCIVIO \",\"province\":\"como\",\"state\":\"Italy\"}},\"billingInfo\":{\"total\":40.0,\"paymentMethod\":7,\"address\":{\"firstname\":\"Filippo \",\"lastname\":\"Troina\",\"companyname\":\"Genertec Italia S.r.l.\",\"street\":\"VIA G.LEOPARDI 27\",\"hn\":\"22075 \",\"zip\":\"22075\",\"city\":\"LURATE CACCIVIO \",\"province\":\"como\",\"state\":\"Italy\"}}}";
//    	
//    	 String rtnData = HttpUtil45.operateData("post", "json",  Constant.url+"createOrder", null, null, json, "", "");
//         //{"error":"发生异常错误"}
//         logger.info("锁库存推送订单返回结果=="+rtnData);
//         System.out.println("锁库存推送订单返回结果=="+rtnData);
    	
    	loadSpringContext();
         logger.info(" schedule start  ");
    }
}
