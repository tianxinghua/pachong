package com.shangpin.igo.ebay.test;

import com.shangpin.iog.common.utils.httpclient.HttpUtils;

/**
 * Created by huxia on 2015/6/24.
 */
public class SingleItemTest {
    public  static String getProductInEbay(){
        String url ="http://open.api.ebay.com/shopping?" +
                "callname=GetSingleItem&" +
                "responseencoding=XML&" +
                "appid=vanskydba-8e2b-46af-adc1-58cae63bf2e&" +
                "siteid=0&" +
                "version=905&" +
                "ItemID=161730285167&IncludeSelector=ItemSpecifics,Variations";

        String result = HttpUtils.get(url);

        return result;
    }

    public static void main(String args[]) throws Exception {

        SingleItemTest singleItemTest = new SingleItemTest();
        System.out.println(singleItemTest.getProductInEbay());
    }
}
