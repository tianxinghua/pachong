package com.shangpin.iog.oupaiguoji.dto;

import com.google.gson.Gson;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.oupaiguoji.util.SignUtils;

import javax.xml.bind.JAXBException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by loyalty on 15/6/5.
 */
public class Test {
    public static void main(String[] args){

        try {
            Gson gson  = new Gson();
            //
            //http://opentest.guanyierp.com/rest/core   http://demo.guanyierp.com/rest/erp_open

            /***
             * 9824848  19:09:16
             3）接口地址：需根据客户所在环境选择调用地址
             > www.guanyierp.com 对应 http://api.guanyierp.com/rest/erp_open
             > v2.guanyierp.com 对应 http://v2.api.guanyierp.com/rest/erp_open
             > demo.guanyierp.com 对应 http://demo.guanyierp.com/rest/erp_open
             */
            String url = "http://api.guanyierp.com/rest/erp_open";

            String requestContent = "{\n" +
                    "    \"appkey\": \"101439\",\n" +
                    "    \"sessionkey\": \"978666cca9dd4e4388db5a1159a03b6c\"\n" +
                    "    \"method\": \"gy.erp.items.get\",\n" +
                    "    \"page_no\": \"1\",\n" +
                    "    \"page_size\": \"10\",\n" +



                    "}";

            String sign= SignUtils.sign(requestContent,"92cde6beaff84d8e817d6c6d937a7722");


            Map<String,String> paraMap = new HashMap<>();
            paraMap.put("appkey","101439");
            paraMap.put("sessionkey","978666cca9dd4e4388db5a1159a03b6c");
            paraMap.put("method","gy.erp.items.get");
            paraMap.put("page_no","1");
            paraMap.put("page_size","10");
            paraMap.put("sign",sign);



            String kk=    HttpUtils.post(url,paraMap);
            System.out.println("kk  =" + kk);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
