package com.shangpin.iog.ebay;

import com.ebay.sdk.ApiContext;
import com.ebay.sdk.call.GetItemCall;
import com.ebay.sdk.call.GetSellerListCall;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.ebay.conf.EbayConf;

import java.util.List;

/**
 * Created by huxia on 2015/6/23.
 */
public class EbayGrabProductService {
    List<SkuDTO> grabProduct(String seller){

        return null;
    }

    /*public int getStock(String itemId) throws Exception {
        ApiContext contxt=EbayConf.getApiContext();
        GetItemCall call = new GetItemCall(contxt);
        ItemType o = call.getItem(itemId);
        return o.getQuantity();
    }

    public ItemType[] getItems() throws Exception {
        ApiContext contxt=EbayConf.getApiContext();
        GetSellerListCall call = new GetSellerListCall(contxt);
        ItemType[] array= call.getSellerList();
        return array;
    }*/

}
