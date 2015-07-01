package com.shangpin.iog.ebay.service;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ebay.sdk.ApiCall;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;
import com.ebay.sdk.SdkSoapException;
import com.ebay.soap.eBLBaseComponents.DetailLevelCodeType;
import com.ebay.soap.eBLBaseComponents.GetItemRequestType;
import com.ebay.soap.eBLBaseComponents.GetItemResponseType;
import com.ebay.soap.eBLBaseComponents.GetSellerListRequestType;
import com.ebay.soap.eBLBaseComponents.GetSellerListResponseType;
import com.ebay.soap.eBLBaseComponents.PaginationType;
import com.shangpin.ebay.shoping.GetMultipleItemsResponseDocument;
import com.shangpin.ebay.shoping.GetMultipleItemsResponseType;
import com.shangpin.ebay.shoping.GetSingleItemResponseDocument;
import com.shangpin.ebay.shoping.GetSingleItemResponseType;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.ebay.conf.EbayConf;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月30日
 */
public class GrabEbayApiService {
	static Logger log = LoggerFactory.getLogger(GrabEbayApiService.class);
	/**
	 * 获取ebay商铺销售的产品<br/>
	 * 只获取itemId,seller信息，通过itemId再去调用{@link #tradeGetItem(String)}完善信息
	 * @see #shoppingSingleItem(String) 获取单个item
	 * @see #shoppingGetMultipleItems(List) 获取多个item
	 * @param userId 商铺id
	 * @param endStart item的结束时间 开始
	 * @param endTime item的结束时间 结束
	 * @param page 页码 1开始
	 * @param pageSize 页大小
	 * @return
	 * @throws ApiException
	 * @throws SdkSoapException
	 * @throws SdkException
	 */
	public static GetSellerListResponseType tradeSellerList(String userId,Calendar endStart,Calendar endTime,int page,int pageSize) throws ApiException, SdkSoapException, SdkException{
		ApiContext api = EbayConf.getApiContext();
		ApiCall call = new ApiCall(api);
		GetSellerListRequestType req = new GetSellerListRequestType();
		req.setUserID(userId);//pumaboxstore buydig inzara.store happynewbaby2011 guess_outlet
		req.setEndTimeFrom(endStart);
		req.setEndTimeTo(endTime);
		PaginationType pg =new PaginationType();
		pg.setPageNumber(page);pg.setEntriesPerPage(pageSize);
		req.setPagination(pg);
		req.setIncludeVariations(true);
		req.setDetailLevel(new DetailLevelCodeType[]{
				DetailLevelCodeType.ITEM_RETURN_DESCRIPTION
				});
		//req.setGranularityLevel(GranularityLevelCodeType.CUSTOM_CODE);
		GetSellerListResponseType resp = (GetSellerListResponseType) call.execute(req);
		return resp;
	}
	/**
	 * 获取单个item<br/>
	 * 
	 * @see #shoppingSingleItem(String)
	 * @param itemId ebay 的itemID
	 * @return
	 * @throws ApiException
	 * @throws SdkSoapException
	 * @throws SdkException
	 */
	public static GetItemResponseType tradeGetItem(String itemId) throws ApiException, SdkSoapException, SdkException{
		ApiContext api = EbayConf.getApiContext();
		ApiCall call = new ApiCall(api);
		GetItemRequestType req=new GetItemRequestType();
		req.setIncludeItemSpecifics(true);
		req.setItemID(itemId);
		req.setDetailLevel(new DetailLevelCodeType[]{DetailLevelCodeType.ITEM_RETURN_DESCRIPTION});
		GetItemResponseType resp=(GetItemResponseType)call.execute(req);
		return resp;
	}
	/**
	 * 通过shopping接口获取单个item<br/>
	 * 查询item的变体，属性非常适合，当然包含每个变体的图片<br/>
	 * ItemSpecifics、Variations，Pictures，VariationSpecificsSet
	 * @see #shoppingGetMultipleItems(List) 获取多个item
	 * @param itemId ebay的itemId
	 * @return
	 */
	public static GetSingleItemResponseType shoppingSingleItem(String itemId){
		String url=EbayConf.getShopingCallUrl("GetSingleItem");
		url+="&ItemID="+itemId+"&IncludeSelector=Variations,ItemSpecifics";
		String xml=HttpUtils.get(url);
		try {
			GetSingleItemResponseDocument doc=GetSingleItemResponseDocument.Factory.parse(xml);
			GetSingleItemResponseType rt=doc.getGetSingleItemResponse();
			return rt;
		} catch (XmlException e) {
			log.error("getSingleItemError",e);
		}
		return null;
	}
	/**
	 * Variations,ItemSpecifics,Quantity
	 * @param itemIds ebay的itemId集合
	 * @return
	 */
	public static GetMultipleItemsResponseType shoppingGetMultipleItems(Collection<String> itemIds){
		return shopingGetMultipleItem(itemIds,"Details,Variations,ItemSpecifics");
	}
	/**
	 * 获取库存信息，不包括itemSpecifics
	 * @see #shoppingGetMultipleItems(List) 获取详细信息
	 * @param itemIds
	 * @return
	 */
	public static GetMultipleItemsResponseType shoppingGetMultipleItems4Stock(Collection<String> itemIds){
		return shopingGetMultipleItem(itemIds,"Details,Variations");
	}
	/**
	 * @param itemIds
	 * @return
	 */
	private static GetMultipleItemsResponseType shopingGetMultipleItem(
			Collection<String> itemIds,String includeSelector) {
		String url=EbayConf.getShopingCallUrl("GetMultipleItems");
		StringBuffer sb = new StringBuffer(url);
		sb.append("&ItemID=");
		for (Iterator<String> iterator = itemIds.iterator(); iterator.hasNext();) {
			String itemId = iterator.next();
			sb.append(itemId).append(",");
		}
		sb.append("&IncludeSelector="+includeSelector);
		String xml=HttpUtils.get(url);
		System.out.println(xml);
		try {
			GetMultipleItemsResponseDocument doc=GetMultipleItemsResponseDocument.Factory.parse(xml);
			GetMultipleItemsResponseType rt=doc.getGetMultipleItemsResponse();
			return rt;
		} catch (XmlException e) {
			log.error("getMultipleItem error",e);
		}
		return null;
	}
	
	
}
