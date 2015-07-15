/**
 * 
 */
package com.shangpin.iog.ebay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;
import com.ebay.sdk.SdkSoapException;
import com.ebay.soap.eBLBaseComponents.AckCodeType;
import com.ebay.soap.eBLBaseComponents.GetSellerListResponseType;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.ListingStatusCodeType;
import com.shangpin.ebay.finding.AckValue;
import com.shangpin.ebay.finding.FindItemsIneBayStoresResponse;
import com.shangpin.ebay.finding.SearchItem;
import com.shangpin.ebay.shoping.GetMultipleItemsResponseType;
import com.shangpin.ebay.shoping.SimpleItemType;
import com.shangpin.ebay.shoping.VariationType;
import com.shangpin.ebay.shoping.VariationsType;
import com.shangpin.iog.ebay.convert.ShopingItemConvert;
import com.shangpin.iog.ebay.service.GrabEbayApiService;

/**
 * ebay数据抓取服务，数据库存抓取服务
 * @description 
 * @author 陈小峰
 * <br/>2015年6月30日
 */
@Component
public class V1GrabService {
	static Logger logger = LoggerFactory.getLogger(V1GrabService.class);
	static int pageSize=100;
	/**
	 * 抓取ebay商户的数据
	 * @param userId 商户id
	 * @param endStart 产品的结束时间 开始日期
	 * @param endEnd 产品的结束时间 终止日期
	 * @return
	 * @throws ApiException
	 * @return  封装好的sku,spu,pic，各键代表对应的数据集合
	 * @throws ApiException 
	 * @throws SdkSoapException
	 * @throws SdkException
	 */
	@SuppressWarnings({ "rawtypes" })
	public Map<String, ? extends Collection> getSellerList(String userId,Date endStart,Date endEnd) throws ApiException, SdkSoapException, SdkException{
		int page=1;
		boolean hasMore=false;
		Map<String, ? extends Collection> skuSpuAndPic=null;
		GetSellerListResponseType resp =null;
		do{//分页循环取
			resp = GrabEbayApiService.tradeSellerList(userId, 
					getCalendar(endStart), getCalendar(endEnd),page,pageSize);
			if(!AckCodeType.FAILURE.equals(resp.getAck())){//失败的则不处理
				hasMore=resp.isHasMoreItems();
				ItemType[] tps = resp.getItemArray().getItem();
				List<String> itemIds = new ArrayList<>(tps.length);//1.得到id
				for (ItemType itemType : tps) {
					boolean active=ListingStatusCodeType.ACTIVE.equals(itemType.getSellingStatus().getListingStatus());
					if(!active)
						continue;
					itemIds.add(itemType.getItemID());
				}
				try {
					skuSpuAndPic = getItemDetail(userId, skuSpuAndPic, itemIds);
					//skuSpuAndPic = findDetailKPP(userId, skuSpuAndPic, itemIds);
				} catch (XmlException e) {
					logger.error("查询item明细异常，supplierId:"+userId,e);
					page++;
					continue ;
				}
				page++;
			}else{
				logger.warn("获取商户{}产品失败，错误码：{}，错误信息{}:",userId,
						resp.getErrors(0).getErrorCode(),
						resp.getErrors(0).getLongMessage());
				hasMore=false;
			}
		}while(hasMore);
		
		return skuSpuAndPic;
	}

	/**
	 * 根据itemId获取item及变种的库存<br/>
	 * @param itemIds ebay的itemId
	 * @return skuId:stock的键值对
	 * @see ShopingItemConvert#getSkuId(SimpleItemType, VariationType) 产品skuId
	 */
	public Map<String,Integer> getStock(Collection<String> itemIds){
		GetMultipleItemsResponseType resp;
		try {
			resp = GrabEbayApiService.shoppingGetMultipleItems4Stock(itemIds);
		} catch (XmlException e) {
			logger.error("getMultipleItem error",e);
			return null;
		}
		if(AckCodeType.FAILURE.value().equals(resp.getAck().toString())){
			logger.warn("获取库存失败，错误码：{}，错误信息{}:",resp.getErrorsArray(0).getErrorCode(),resp.getErrorsArray(0).getLongMessage());
			return null;
		}
		SimpleItemType[] sits=resp.getItemArray();
		Map<String,Integer> rtnMap=new HashMap<>(sits.length);
		for (SimpleItemType sit : sits) {
			VariationsType vta=sit.getVariations();
			//如果结束了那么库存为null
			boolean isEnd=false;
			if(sit.getListingStatus()!=null && !ListingStatusCodeType.ACTIVE.value().equalsIgnoreCase(sit.getListingStatus().toString())){
				isEnd=true;
			}
			if(vta!=null && vta.getVariationArray()!=null){
				VariationType[] vts=vta.getVariationArray();
				for (VariationType vt : vts) {
					int quantity=vt.getQuantity()-vt.getSellingStatus().getQuantitySold();
					rtnMap.put(ShopingItemConvert.getSkuId(sit,vt),isEnd?0:quantity);
				}
			}else{
				int quantity=sit.getQuantity()-sit.getQuantitySold();
				rtnMap.put(ShopingItemConvert.getSkuId(sit,null), isEnd?0:quantity);
			}
		}
		return rtnMap;
	}
	
	/**
	 * 通过find接口查询供应商店铺销售的item
	 * @param storeName 店铺名字
	 * @param brandName 品牌名字
	 * @return  封装好的sku,spu,pic，各键代表对应的数据集合
	 */
	@SuppressWarnings({ "rawtypes" })
	public Map<String, ? extends Collection> findStoreBrand(String storeName,
			String brand){
		int page=1;
		Map<String, ? extends Collection> skuSpuAndPic=null;//new HashMap<>();
		boolean hasMore=false;
		FindItemsIneBayStoresResponse resp =null;
		do{//分页循环取
			try {
				resp = GrabEbayApiService.findItemsIneBayStores(storeName,brand,page,pageSize);
				if(!AckValue.SUCCESS.equals(resp.getAck()) && !AckValue.WARNING.equals(resp.getAck())){//返回错误了则直接返回
					logger.warn("find store:{},brand:{}失败，错误码：{}，错误信息{}:",storeName,brand,
							resp.getErrorMessage().getErrorArray(0).getErrorId(),
							resp.getErrorMessage().getErrorArray(0).getMessage());
					hasMore=false;
					return skuSpuAndPic;
				}
			}catch(Exception e){
				logger.error("findStoreBrand异常，storeName:"+storeName+",brand:"+brand,e);
				hasMore=false;
				return skuSpuAndPic;
			}
			//TODO 如果没找到数据，那么返回null
			if(resp.getPaginationOutput().getTotalEntries()==0){
				logger.warn("没有找到storeName:{},brand:{}的产品,page:{}",storeName,brand,page);
				return skuSpuAndPic;
			}
			int totalPage=resp.getPaginationOutput().getTotalPages();
			if(totalPage>resp.getPaginationOutput().getPageNumber()){
				page++;
				hasMore=true;
			}else{
				hasMore=false;
			}
			//TODO 查询结果resp.getSearchResult().getCount(),还是resp.getPaginationOutput().getTotalEntries())值得商榷
			if(page==2)
				logger.debug(
						"search store:{},brand:{} Result,resultCount:{},totalPage:{},totalCount:{}",
						storeName, brand,resp.getSearchResult().getCount(), 
						totalPage, resp.getPaginationOutput().getTotalEntries());
			SearchItem[] items=resp.getSearchResult().getItemArray();
			boolean isActive=false;
			List<String> itemIds=new ArrayList<>();
			//1得到id
			for (SearchItem item : items) {
				isActive=ListingStatusCodeType.ACTIVE.value().equalsIgnoreCase(item.getSellingStatus().getSellingState());
				if(isActive){
					itemIds.add(item.getItemId());
				}
			}
			try{
				skuSpuAndPic = getItemDetail(storeName, skuSpuAndPic, itemIds);
				//TODO 此处应该过滤目标品牌的
				//filterBrand()
			}catch(Exception e){
				logger.error("查询店铺、品牌，getMultipleItem异常，storeName:"+storeName+",brand:"+brand,e);
			}
		}while(hasMore);
		
		return skuSpuAndPic;
	}

	/**
	 * 循环调用获取item的变体明细
	 * @param supplierKey
	 * @param skuSpuAndPic
	 * @param itemIds
	 * @return
	 * @throws XmlException
	 */
	@SuppressWarnings("rawtypes")
	private Map<String, ? extends Collection> getItemDetail(String supplierKey,
			Map<String, ? extends Collection> skuSpuAndPic, List<String> itemIds)
			throws XmlException {
		int idLen=itemIds.size();
		if(idLen>20){
			int p1=0;int p2=20;
			do{
				p2=p1+20;if(p2>idLen) p2=idLen;
				skuSpuAndPic=findDetailKPP(supplierKey,skuSpuAndPic,itemIds.subList(p1, p2));
				p1=p2;
			}while(p1<idLen);
		}else{
			skuSpuAndPic=findDetailKPP(supplierKey,skuSpuAndPic,itemIds);					
		}
		return skuSpuAndPic;
	}
	/**
	 * @param date
	 * @return
	 */
	private Calendar getCalendar(Date date) {
		Calendar ca=Calendar.getInstance();
		ca.setTime(date);
		return ca;
	}
	
	/**
	 * 根据itemIds获取sku，spu，pic信息
	 * @param supplierKey 供应商id，（商铺id，用户id）
	 * @param skuSpuAndPic 
	 * @param itemIds item id
	 * @return
	 * @throws XmlException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map<String, ? extends Collection> findDetailKPP(String supplierKey,
			Map<String, ? extends Collection> skuSpuAndPic, List<String> itemIds) throws XmlException {
		//2.得到item
		GetMultipleItemsResponseType multResp=null;
		try{
			multResp= GrabEbayApiService.shoppingGetMultipleItems(itemIds);
		}catch(Exception e){
			logger.error(supplierKey,e);
			return skuSpuAndPic;
		}
		if(AckValue.FAILURE.equals(multResp.getAck())||
				AckValue.PARTIAL_FAILURE.equals(multResp.getAck())){
			logger.warn("GetMultipleItems Fail::supplierKey{},itemIds len:{}，错误码：{}，错误信息{}:",supplierKey,itemIds.size(),
					multResp.getErrorsArray(0).getErrorCode(),
					multResp.getErrorsArray(0).getLongMessage()
					);
			return skuSpuAndPic;
		}
		//3.转换sku,spu
		SimpleItemType[] itemTypes=multResp.getItemArray();
		Map<String, Collection> kpp=ShopingItemConvert.convert2kpp(itemTypes,supplierKey);
		if(skuSpuAndPic==null){
			skuSpuAndPic=kpp;
		}else{
			skuSpuAndPic.get("sku").addAll(kpp.get("sku"));
			skuSpuAndPic.get("spu").addAll(kpp.get("spu"));
			skuSpuAndPic.get("pic").addAll(kpp.get("pic"));
		}
		return skuSpuAndPic;
	}
	/*public static void main(String[] args) {
		List<String> itemIds=new ArrayList<>();
		itemIds.add("251485222300");
		itemIds.add("251674833689");//过期的
		Calendar t1 = Calendar.getInstance();
		t1.setTime(new Date());
		Calendar t2 = Calendar.getInstance();t2.set(Calendar.MONTH, 8);
		V1GrabService srv=new V1GrabService();
		srv.getStock(itemIds);
		//tradeSellerList("pumaboxstore", t1, t2, 1, 8);
		//tradeGetItem("251485222300");
	}*/
}
