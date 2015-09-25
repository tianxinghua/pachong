package com.shangpin.iog.gilt.schedule;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.gilt.dto.OrderDTO;
import com.shangpin.iog.gilt.order.OrderServiceImpl;
import com.shangpin.iog.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

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
    OrderServiceImpl orderService;

    //下单
    @Scheduled(cron="0 0/3 * * * ? ")
    public void putOrder(){

        //拉取数据
        System.out.println("-------gilt start---------");
        try {
            orderService.purchaseOrder();
            System.out.println("成功插入数据库");
            System.out.println("-------gilt end---------");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //确认支付
//    @Scheduled(cron="0 0/3 * * * ? ")
    public  void confirmOrder(){
        try {
            orderService.confirmOrder();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //确认发货
  //  @Scheduled(cron="0 0/3 * * * ? ")
    public void deliveryOrder(){
        System.out.println("-------deliveryOrder start---------");
        try {
            orderService.deliveryOrder();
            System.out.println("-------deliveryOrder end---------");
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
