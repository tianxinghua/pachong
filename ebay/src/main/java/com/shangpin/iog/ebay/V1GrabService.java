/**
 *
 */
package com.shangpin.iog.ebay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.ebay.convert.ShopingItemConvert;
import com.shangpin.iog.ebay.page.PageGrabService;
import com.shangpin.iog.ebay.service.GrabEbayApiService;

/**
 * ebay数据抓取服务，数据库存抓取服务
 * @description
 * @author 陈小峰
 * @see PageGrabService 
 * @date 2015年6月30日
 */
@Deprecated
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
		Map<String,  ? extends Collection> skuSpuAndPic=null;
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
					skuSpuAndPic = getMoreDetail(userId, itemIds);
					//skuSpuAndPic = findDetailKPP(userId, skuSpuAndPic, itemIds);
				} catch (XmlException e) {
					logger.error("查询item明细异常，supplierId:"+userId,e);
					page++;
					continue ;
				}
				page++;
			}else{
				/*logger.warn("获取商户{}产品失败，错误码：{}，错误信息{}:",userId,
						resp.getErrors(0).getErrorCode(),
						resp.getErrors(0).getLongMessage());*/
				hasMore=false;
			}
		}while(hasMore);

		return skuSpuAndPic;
	}

	/**
	 * 根据itemId获取item及变种的库存<br/>
	 * @param itemIds ebay的itemId
	 * @return skuId:stock的键值对
	 * @throws XmlException
	 * @see ShopingItemConvert#getSkuId(SimpleItemType, VariationType) 产品skuId
	 */
	public Map<String,Integer> getStock(Collection<String> itemIds) throws XmlException{
		GetMultipleItemsResponseType resp;
		try {
			resp = GrabEbayApiService.shoppingGetMultipleItems4Stock(itemIds);
		} catch (XmlException e) {
			logger.error("getMultipleItem 错误:",e);
			throw e;
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
	 * @param date
	 * @return
	 *//*
	private Calendar getCalendar(Date date) {
		Calendar ca=Calendar.getInstance();
		ca.setTime(date);
		return ca;
	}*/
	/**
	 * 根据itemIds获取sku，spu，pic信息
	 * @param supplierKey 供应商id，（商铺id，用户id）
	 * @param skuSpuAndPic
	 * @param itemIds item id
	 * @return
	 * @throws XmlException
	 */
	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map<String, ? extends Collection> findDetailKPP(String supplierKey,Map<String, ? extends Collection> skuSpuAndPic, List<String> itemIds) throws XmlException {
		//2.得到item
		GetMultipleItemsResponseType multResp= GrabEbayApiService.shoppingGetMultipleItems(itemIds);
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
	}*/
	/**
	 * 通过find接口查询供应商店铺销售的item
	 * @param storeName 店铺名字
	 * @param brand 品牌名字
	 * @return  封装好的sku,spu,pic，各键代表对应的数据集合
	 */
	@SuppressWarnings({ "rawtypes" })
	public Map<String, ? extends Collection> findStoreBrand(String storeName,
															String brand){
		int page=1;
		Map<String, Collection> skuSpuAndPic = initResultMap();
		boolean hasMore=false;
		FindItemsIneBayStoresResponse resp =null;
		ExecutorService exe = null;List<Future<Map<String, Collection>>> fu = null;
		do{//分页循环取
			try {
				resp = GrabEbayApiService.findItemsIneBayStores(storeName,brand,page,pageSize);
				if(!AckValue.SUCCESS.equals(resp.getAck()) && !AckValue.WARNING.equals(resp.getAck())){//返回错误了则直接返回
					/*logger.warn("find store:{},brand:{}失败，错误码：{}，错误信息{}:",storeName,brand,
							resp.getErrorMessage().getErrorArray(0).getErrorId(),
							resp.getErrorMessage().getErrorArray(0).getMessage());*/
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
				//logger.warn("没有找到storeName:{},brand:{}的产品,page:{}",storeName,brand,page);
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
			if(page==2){
				logger.info(
						"search store:{},brand:{} Result,resultCount:{},totalPage:{},totalCount:{}",
						storeName, brand,resp.getSearchResult().getCount(),
						totalPage, resp.getPaginationOutput().getTotalEntries());
				if(totalPage>4){
					logger.warn("store:{},brand:{}的item超过了500个，总共：{}，获取sku,spu,pic需要时间",storeName,brand,resp.getPaginationOutput().getTotalEntries());
				}
			}
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
				if(totalPage>4){//考虑通过线程去处理了
					if(exe==null){
						exe = Executors.newFixedThreadPool(totalPage);//每页一个线程去跑
						fu=new ArrayList<>();
					}
					Future<Map<String, Collection>> rs=exe.submit(new GetDetailThread(storeName,itemIds));
					fu.add(rs);
				}else{
					combine(getMoreDetail(storeName,itemIds),skuSpuAndPic);
				}
				//TODO 此处应该过滤目标品牌的
				//filterBrand()
			}catch(Exception e){
				logger.error("查询店铺、品牌，getMultipleItem异常，storeName:"+storeName+",brand:"+brand,e);
			}
		}while(hasMore);
		if(exe!=null){
			exe.shutdown();
			try{
				while(!exe.awaitTermination(1, TimeUnit.MINUTES)){//1分钟查询一次有无完成
					ThreadPoolExecutor pool = (ThreadPoolExecutor)exe;
					logger.info("item detail pool,总数：{}，当前活动线程数：{}",pool.getTaskCount(),pool.getActiveCount());
				}
				//线程完毕之后开始得到结果合并
				for (Future<Map<String, Collection>> future : fu) {
					combine(future.get(),skuSpuAndPic);
				}
			}catch(Exception e){
				logger.warn("获取itemIds的明细信息异常",e);
			}
		}
		return skuSpuAndPic;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static Map<String, Collection> initResultMap() {
		Map<String, Collection> skuSpuAndPic=new HashMap<>();
		skuSpuAndPic.put("sku", new HashSet<SkuDTO>());
		skuSpuAndPic.put("spu", new HashSet<SpuDTO>());
		skuSpuAndPic.put("pic", new HashSet<ProductPictureDTO>());
		return skuSpuAndPic;
	}
	@SuppressWarnings("rawtypes")
	static class GetDetailThread implements Callable<Map<String,Collection>>{

		private String storeName;
		private List<String> itemIds;

		/**
		 * @param storeName
		 * @param itemIds
		 */
		public GetDetailThread(String storeName, List<String> itemIds) {
			this.storeName=storeName;
			this.itemIds=itemIds;
		}

		@Override
		public Map<String, Collection> call() throws Exception {
			logger.info("获取itemIds明细线程启动,itemId size:"+itemIds.size());
			return (Map<String, Collection>) getMoreDetail(storeName,itemIds);
		}

	}
	/**
	 * 循环调用获取item的变体明细
	 * @param supplierKey
	 * @param itemIds
	 * @return
	 * @throws XmlException
	 */
	@SuppressWarnings("rawtypes")
	private static Map<String, ? extends Collection> getMoreDetail(String supplierKey,List<String> itemIds)
			throws XmlException {
		int idLen=itemIds.size();
		Map<String, ? extends Collection> kpp=initResultMap();
		if(idLen>20){
			int p1=0;int p2=20;
			do{
				p2=p1+20;if(p2>idLen) p2=idLen;
				combine(findDetailKPP(supplierKey,itemIds.subList(p1, p2)),kpp);
				p1=p2;
			}while(p1<idLen);
		}else{
			combine(findDetailKPP(supplierKey,itemIds),kpp);
		}
		return kpp;
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
	 * 将src中的sku,spu,pic合并到target的sku,spu,pic中
	 * @param src 需要合并的
	 * @param target 合并到目的map
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void combine(Map<String, ? extends Collection> src,Map<String, ? extends Collection> target){
		if(src==null){
			return ;
		}else{
			if(src.get("sku")!=null)
				target.get("sku").addAll(src.get("sku"));
			if(src.get("spu")!=null)
				target.get("spu").addAll(src.get("spu"));
			if(src.get("pic")!=null)
				target.get("pic").addAll(src.get("pic"));
		}
	}
	/**
	 * 根据itemIds获取sku，spu，pic信息
	 * @param supplierKey 供应商id，（商铺id，用户id）
	 * @param itemIds item id
	 * @return
	 * @throws XmlException
	 */
	@SuppressWarnings({ "rawtypes"})
	public static Map<String, ? extends Collection> findDetailKPP(String supplierKey,List<String> itemIds) throws XmlException {
		//2.得到item
		GetMultipleItemsResponseType multResp=null;
		try{
			multResp= GrabEbayApiService.shoppingGetMultipleItems(itemIds);
		}catch(Exception e){
			logger.error(supplierKey,e);
			return null;
		}
		if(multResp==null){
			return null;
		}
		if(AckValue.FAILURE.equals(multResp.getAck())||
				AckValue.PARTIAL_FAILURE.equals(multResp.getAck())){
			/*logger.warn("GetMultipleItems Fail::supplierKey{},itemIds len:{}，错误码：{}，错误信息{}:",supplierKey,itemIds.size(),
					multResp.getErrorsArray(0).getErrorCode(),
					multResp.getErrorsArray(0).getLongMessage()
					);*/
			return null;
		}
		//3.转换sku,spu
		SimpleItemType[] itemTypes=multResp.getItemArray();
		Map<String, Collection> kpp=ShopingItemConvert.convert2kpp(itemTypes,supplierKey);
		return kpp;
	}
	/*@SuppressWarnings({ "rawtypes", "static-access" })
	public static void main(String[] args) {
		List<String> itemIds=new ArrayList<>();
		itemIds.add("331533278186");
		itemIds.add("251674833689");//过期的
		Calendar t1 = Calendar.getInstance();
		t1.setTime(new Date());
		Calendar t2 = Calendar.getInstance();t2.set(Calendar.MONTH, 8);
		
		List<String> list=new ArrayList<>();
		try(BufferedReader br = new BufferedReader(new FileReader(new File("D:/tmp/spu.txt")))){
			String tmp=null;
			while((tmp=br.readLine())!=null){
				list.add(tmp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		V1GrabService srv=new V1GrabService();
		int idx=0;
		Map<String, ? extends Collection> kpp=initResultMap();
		try {
			kpp=srv.getMoreDetail("ebay",list.subList(idx, idx+20));
		} catch (XmlException e) {
			e.printStackTrace();
		}
		Gson g = new Gson();
		System.out.println(kpp.get("spu").size());
		System.out.println(kpp.get("sku").size());
		System.out.println(kpp.get("pic").size());
		System.out.println(g.toJson(kpp));
		while(idx<size){
			try {
				idx=idx+20;
			} catch (XmlException e) {
				e.printStackTrace();
			}
		}
		if(idx-size>0){
			try {
				srv.getMoreDetail("ebay", list.subList(idx-20, size));
			} catch (XmlException e) {
				e.printStackTrace();
			}
		}
		//srv.getStock(itemIds);
		//tradeSellerList("pumaboxstore", t1, t2, 1, 8);
		//tradeGetItem("251485222300");
	}*/
}
