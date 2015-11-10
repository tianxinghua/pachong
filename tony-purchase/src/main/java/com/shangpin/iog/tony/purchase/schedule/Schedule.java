package com.shangpin.iog.tony.purchase.schedule;

import com.google.gson.Gson;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.tony.purchase.order.OrderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by sunny on 2015/9/16.
 */
@Component
public class Schedule {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private static ResourceBundle bdl=null;
    private static String url="https://api-sandbox.gilt.com/global/orders";
    private static String supplierId;
    private static String key ;
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        key = bdl.getString("key");
    }

    @Autowired
    OrderImpl orderService;
    //已支付订单推送
    @Scheduled(cron="0 0/2 * * * ? ")
    public void checkoutOrderFromWMS(){
        try {
            orderService.startWMS();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //已支付订单推送
    @Scheduled(cron="0 0/2 * * * ? ")
    public void confirmOrder(){
        try {
            orderService.confirmOrder();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args){
       /* Gson gson =new Gson();
        OutTimeConfig timeConfig = new OutTimeConfig(1000*5,1000*5,1000*5);
        Map<String,String> param =new HashMap<>();
        String str="";
        //String result = HttpUtil45.post(url+"2e701f4e-4438-4f62-bd5c-da3d486525b8",param,timeConfig);
        String result= null;
        String result1=null;
        try {
            //result = HttpUtil45.operateData("", "jsonget", url + "2e701f4e-4438-4f62-bd5c-da3d486525b8", timeConfig, null, "", key, "");
            result1=HttpUtil45.get(url+"1adc7865-7aa0-4afb-b22c-1e99736ab0ad",timeConfig,param,key,"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        OrderDTO dto=gson.fromJson(result,OrderDTO.class);
        System.out.println("返回结果 1："+result1);*/
        OutTimeConfig timeConfig = new OutTimeConfig(1000*5,1000*5,1000*5);
        Gson gson =new Gson();
        Map<String,String> paraMap =new HashMap<>();
        paraMap.put("status","cancelled");
        String json = "{\"status\" : \"confirmed\"}";
       // String result=HttpUtil45.get(url +"/1adc7865-7aa0-4afb-b22c-1e99736ab0ad", timeConfig, param, key, "");
        try {
//            String str = HttpUtil45.operateData("delete", "", url + "/1adc7865-7aa0-4afb-b22c-1e99736ab0ad", timeConfig, paraMap,"", key,"");
            String str = HttpUtil45.get(url,timeConfig,null,key,"");
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //OrderDTO dto = gson.fromJson(result, OrderDTO.class);
        //System.out.println(dto.getId());
    }
}
