package com.shangpin.iog.onsite.base.common;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/18.
 */
public class Test {

    /**
     * test
     * @param args
     */
    public static void main(String[] args) {
        //Map<String,String> param = new HashMap<>();
       // param.put("merchantId","55f707f6b49dbbe14ec6354d");
       // param.put("token","d355cd8701b2ebc54d6c8811e03a3229");
       // String s = HttpUtil45.post("http://www.cs4b.eu/ws/getItem", param, new OutTimeConfig());
       // System.out.println("444444444"+s);
       String kk = HttpUtil45.get("http://webserv.havok.it/stock/v1/style.php?k=033bd94b1168d7e4f0d644c3c95e35bf&f=shangpin",new OutTimeConfig(1000*60,1000*60,1000*60),
                null);
        System.out.println("kk =" + kk);
    }
}
