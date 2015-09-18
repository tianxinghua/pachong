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
        Map<String,String> param = new HashMap<>();
        //param.put("merchantId","55f707f6b49dbbe14ec6354d");
        //param.put("token","d355cd8701b2ebc54d6c8811e03a3229");
        param.put("merchantId","55f707f6b49dbbe14ec6354d");
        param.put("token","d355cd8701b2ebc54d6c8811e03a3229");
        String s = HttpUtil45.post("http://www.cs4b.eu/ws/getItem", param, new OutTimeConfig());
        System.out.println("444444444"+s);
    }
}
