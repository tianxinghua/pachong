package com.shangpin.igo.ebay.test;

import com.ebay.sdk.ApiContext;
import com.shangpin.iog.ebay.EbayGrabProductService;
import com.shangpin.iog.ebay.conf.EbayConf;

/**
 * Created by huxia on 2015/6/25.
 */
public class GetItemTest {
    public static void main(String args[]) throws Exception {
        EbayGrabProductService ebayGrabProductService = new EbayGrabProductService();
        String itemId = "171401792453";
        ebayGrabProductService.getStock(itemId);

    }
    public static void testApiContext(){
        ApiContext api= EbayConf.getApiContext();
    }
}
