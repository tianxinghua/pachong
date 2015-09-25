package com.shangpin.iog.ostore;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lizhongren on 2015/8/10.
 */
public class Test {

    public  static void main(String[] args){
        Map<String,String> paraMap = new HashMap<>();
        paraMap.put("DeveloperKey","537c99a8-e3d6-4788-9296-029420540832");
        paraMap.put("Password","L1zhongren!");
        String  kk =  HttpUtil45.post("http://api.channeladvisor.com/webservices/GetAuthorizationList",paraMap,new OutTimeConfig());
        System.out.println("kk="+kk);
    }
}

