/**
 * 
 */
package com.shangpin.igo.ebay.test;

import org.junit.Test;

import com.ebay.sdk.ApiCall;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;
import com.ebay.sdk.SdkSoapException;
import com.ebay.soap.eBLBaseComponents.BidActionCodeType;
import com.ebay.soap.eBLBaseComponents.OfferType;
import com.ebay.soap.eBLBaseComponents.PlaceOfferRequestType;
import com.shangpin.iog.ebay.conf.EbayConf;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年7月3日
 */
public class PlaceOffer {
	String spIp="www.shangpin.com";
	@Test
	public void testOrder() throws ApiException, SdkSoapException, SdkException{
		PlaceOfferRequestType req = new PlaceOfferRequestType();
		String itemId="323232323";
		req.setItemID(itemId);
		OfferType offer = new OfferType();
		offer.setAction(BidActionCodeType.PURCHASE);
		
		req.setOffer(offer);
		req.setEndUserIP(spIp);
		ApiContext api=EbayConf.getApiContext();
		ApiCall call = new ApiCall(api);
		call.execute(req);
	}
}
