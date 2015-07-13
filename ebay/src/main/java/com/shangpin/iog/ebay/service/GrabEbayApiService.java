package com.shangpin.iog.ebay.service;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import com.shangpin.ebay.finding.FindItemsIneBayStoresResponse;
import com.shangpin.ebay.finding.FindItemsIneBayStoresResponseDocument;
import com.shangpin.ebay.shoping.GetMultipleItemsResponseDocument;
import com.shangpin.ebay.shoping.GetMultipleItemsResponseType;
import com.shangpin.ebay.shoping.GetSingleItemResponseDocument;
import com.shangpin.ebay.shoping.GetSingleItemResponseType;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.ebay.conf.EbayConf;
import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月30日
 */
public class GrabEbayApiService {
	static Logger log = LoggerFactory.getLogger(GrabEbayApiService.class);
	/**
	 * 获取ebay商铺销售的产品<br/>
	 * 只获取itemId,item的ListingDetails信息,seller信息，通过itemId再去调用{@link #tradeGetItem(String)}完善信息<br/>
	 * 注意开始结束时间不能超过120天
	 * @param userId 商铺id
	 * @param endStart item的结束时间 开始
	 * @param endTime item的结束时间 结束
	 * @param page 页码 1开始
	 * @param pageSize 页大小
	 * @return
	 * @see #shoppingSingleItem(String) 获取单个item
	 * @see #shoppingGetMultipleItems(List) 获取多个item
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
		/*标准输入参数
		 * call.setDetailLevel(new DetailLevelCodeType[]{ 
				DetailLevelCodeType.ITEM_RETURN_DESCRIPTION
				});*/
		//req.setGranularityLevel(GranularityLevelCodeType.CUSTOM_CODE);
		GetSellerListResponseType resp = (GetSellerListResponseType) call.execute(req);
		return resp;
	}
	/**
	 * 获取单个item，获取的数据比较多，有些不需要<br/>
	 * shopping接口获取单个item更好{@link #shoppingSingleItem(String) 获取单个item}
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
		//标准输入参数
		call.setDetailLevel(new DetailLevelCodeType[]{DetailLevelCodeType.ITEM_RETURN_DESCRIPTION});
		GetItemResponseType resp=(GetItemResponseType)call.execute(req);
		return resp;
	}
	/**
	 * 通过shopping接口获取单个item<br/>
	 * 查询item的变体，属性非常适合，当然包含每个变体的图片<br/>
	 * ItemSpecifics、Variations，Pictures，VariationSpecificsSet
	 * @see #shoppingGetMultipleItems(List) 获取多个item
	 * @see #shoppingSingleItemVariation(String, String) 获取指定item变体 
	 * @param itemId ebay的itemId
	 * @return
	 */
	public static GetSingleItemResponseType shoppingSingleItem(String itemId){
		String url=EbayConf.getShopingCallUrl("GetSingleItem");
		url+="&ItemID="+itemId+"&IncludeSelector=Variations,ItemSpecifics";
		String xml=HttpUtil45.get(url,null,null);
		log.debug("itemId:{},结果：{}",itemId,xml);
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
	 * Variations,ItemSpecifics,Quantity<br/>
	 * <b/>请根据ListingStatus来判断产品是否下架,状态Active才是销售中的</b>
	 * @param itemIds ebay的itemId集合
	 * @return
	 */
	public static GetMultipleItemsResponseType shoppingGetMultipleItems(Collection<String> itemIds){
		return shopingGetMultipleItem(itemIds,"Details,Variations,ItemSpecifics");
	}
	/**
	 * 获取库存信息，不包括itemSpecifics<br/>
	 * <b>注意：请根据ListingStatus来判断产品是否下架,状态Active才是销售中的</b>
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
		
		String xml=HttpUtil45.get(sb.toString(),null,null);
		log.debug("url:{},结果：{}",sb.toString(),xml);
		try {
			GetMultipleItemsResponseDocument doc=GetMultipleItemsResponseDocument.Factory.parse(xml);
			GetMultipleItemsResponseType rt=doc.getGetMultipleItemsResponse();
			return rt;
		} catch (XmlException e) {
			log.error("getMultipleItem error",e);
		}
		return null;
	}
	/**
	 * 只获取Variations，下单的时候有用<br/>
	 * 通过skuId获取变种数据，在下单的时候需要用到返回的数据<br/>
	 * 注意：返回的数据只有对应sku变种的Variations信息，item其他的ItemSpecifics没有
	 * @see #shoppingSingleItem(String) 拉产品获取单个item
	 * @param itemId 产品ItemId
	 * @param variationSKU 变体的sku nullable，如果为空，则获取itemId所有的变种信息
	 * @return
	 */
	public GetSingleItemResponseType shoppingSingleItemVariation(String itemId,String variationSKU){
		String url=EbayConf.getShopingCallUrl("GetSingleItem");
		url+="&ItemID="+itemId+"&IncludeSelector=Variations";
		if(StringUtils.isNotBlank(variationSKU)){
			url+="&VariationSKU="+variationSKU;
		}
		String xml=HttpUtil45.get(url, null, null);
		log.debug("itemId:{},结果：{}",itemId,xml);
		try {
			GetSingleItemResponseDocument doc=GetSingleItemResponseDocument.Factory.parse(xml);
			GetSingleItemResponseType rt=doc.getGetSingleItemResponse();
			return rt;
		} catch (XmlException e) {
			log.error("",e);
		}
		return null;
	}
	/**
	 * 调用find接口，查询店铺关键词的item
	 * @param storeName 店铺名
	 * @param keywords item的关键词
	 * @param page 页码
	 * @param pageSize 分页大小
	 * @return
	 * @throws XmlException
	 */
	public static FindItemsIneBayStoresResponse findItemsIneBayStores(
			String storeName, String keywords, int page, int pageSize) throws XmlException {
		String url = EbayConf.getFindCallUrl("findItemsIneBayStores");
		url += "&storeName=%s&paginationInput.entriesPerPage=%d&paginationInput.pageNumber=%d&keywords=%s";
		String filter="&outputSelector[0]=searchResult.item.sellingStatus.sellingState&outputSelector[1]=searchResult.item.itemId";
		url+=filter;
		url = String.format(url, storeName, page,pageSize,keywords);
		String xml=HttpUtil45.get(url, null, null);
		log.debug("查询商铺：{}，关键词：{},结果：{}",storeName,keywords,xml);
		try{
			FindItemsIneBayStoresResponseDocument doc = FindItemsIneBayStoresResponseDocument.Factory.parse(xml);
			FindItemsIneBayStoresResponse rt = doc.getFindItemsIneBayStoresResponse();			
			return rt;
		}catch(Exception e){
			log.error("findItemsIneBayStores 错误，storeName:"+storeName+",keyWords:"+keywords,e);
		}
		return null;
	}
	
	/*public static void main(String[] args) {
		List<String> itemIds=new ArrayList<>();
		itemIds.add("251485222300");
		itemIds.add("251674833689");//过期的
		Calendar t1 = Calendar.getInstance();
		t1.setTime(new Date());
		Calendar t2 = Calendar.getInstance();t2.set(Calendar.MONTH, 8);
		try {
			//tradeSellerList("pumaboxstore", t1, t2, 1, 8);
			//tradeGetItem("251485222300");
			FindItemsIneBayStoresResponse resp=findItemsIneBayStores("Toms-Home-Treasures","dalia",1,10);
			if(AckValue.SUCCESS.equals(resp.getAck())){
				System.out.println("success");
			}
			System.out.println(resp.xmlText());
		} catch (XmlException e) {
			e.printStackTrace();
		}
		//shoppingGetMultipleItems4Stock(itemIds);
	}*/
}
