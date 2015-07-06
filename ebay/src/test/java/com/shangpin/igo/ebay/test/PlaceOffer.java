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
import com.ebay.soap.eBLBaseComponents.AddItemRequestType;
import com.ebay.soap.eBLBaseComponents.AmountType;
import com.ebay.soap.eBLBaseComponents.BidActionCodeType;
import com.ebay.soap.eBLBaseComponents.CurrencyCodeType;
import com.ebay.soap.eBLBaseComponents.NameValueListArrayType;
import com.ebay.soap.eBLBaseComponents.NameValueListType;
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
		//TODO 添加购买的产品id,价格....
		String itemId="231520688479";
		req.setItemID(itemId);
		OfferType offer = new OfferType();
		offer.setAction(BidActionCodeType.PURCHASE);
		AmountType bid = new AmountType();
		bid.setCurrencyID(CurrencyCodeType.USD);bid.setValue(12.34D);
		offer.setMaxBid(bid);
		offer.setQuantity(1);
		NameValueListArrayType variation = new NameValueListArrayType();
		NameValueListType nv1=new NameValueListType();nv1.setName("Color");nv1.setValue(new String[]{"pink"});
		NameValueListType nv2=new NameValueListType();nv2.setName("Size");nv1.setValue(new String[]{"xs"});
		NameValueListType[] nvs = new NameValueListType[]{
				nv1,nv2
		};
		variation.setNameValueList(nvs);
		req.setVariationSpecifics(variation);
		req.setOffer(offer);
		req.setEndUserIP(spIp);
		
		ApiContext api=EbayConf.getApiContext();
		ApiCall call = new ApiCall(api);
		call.execute(req);
	}
	
	@Test
	public void testAddItem() throws ApiException, SdkSoapException, SdkException{
		AddItemRequestType req=new AddItemRequestType();
		//TODO  添加商品内容....
		
		ApiContext api=EbayConf.getApiContext();
		ApiCall call = new ApiCall(api);
		call.execute(req);
	}
}

