package com.shangpin.iog.ebay;

import ShangPin.SOP.Api.ApiException;
import com.ebay.sdk.SdkException;

/**
 * Created by huxia on 2015/6/30.
 */
public class Test {
    public static void main(String args[]) throws SdkException, ApiException {
        String id = "231562522334";
        FetchEbayProduct fetchEbayProduct = new FetchEbayProduct();
        fetchEbayProduct.FetchSkuAndSave(id);
    }
}
