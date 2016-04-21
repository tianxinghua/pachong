package com.shangpin.iog.spinnaker;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.shangpin.iog.spinnaker.dto.OrderInfoDTO;
import com.shangpin.iog.spinnaker.dto.ResponseObject;

import java.util.List;

/**
 * Created by lizhongren on 2016/2/18.
 */
public class Test {

    public static void  main(String[] args){

        Gson gson = new Gson();
        String rtnData  ="[{\"Id_b2b_order\":1090,\"Order_No\":\"null\",\"Purchase_No\":\"CGD2016021400485\",\"producer_id\":\"56257\",\"Size\":\"37+0017\",\"Color\":\"M400 ROSA CHIARO\",\"Barcode\":null,\"Qty_Ord\":\"1\",\"Qty_Conf\":\"1\",\"Qty_Shipped\":\"1\",\"IT_Time_Order\":\"9:35:2\",\"Date_Order\":\"2016-02-14T00:00:00\",\"Date_Confirmed\":\"2016-02-17T00:00:00\",\"Date_Shipped\":\"2016-02-17T00:00:00\",\"Trk_Number\":\"26 2395 6075\",\"logistics_company\":\"DHL\",\"Status\":\"FA\"}]";


        List<OrderInfoDTO> responseObjectList = gson.fromJson(rtnData, new TypeToken<List<OrderInfoDTO>>() {}.getType());
        if(responseObjectList.size()>0){
            OrderInfoDTO responseObject = responseObjectList.get(0);
            if("SH".equals(responseObject.getStatus())||"FA".equals(responseObject.getStatus())){
                //String deliverNo =  "DHL" + ";" + "0123456789" + ";" + "2016-01-13 12:00:00";
                String deliverNo =  responseObject.getLogistics_company() +";"+ responseObject.getTrk_Number() +";"+ responseObject.getDate_Shipped();
                System.out.println("deliverNo ="+deliverNo);
            }
        }
    }
}
