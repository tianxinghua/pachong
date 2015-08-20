/**
 * 
 */
package com.shangpin.iog.ebay.page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.soap.eBLBaseComponents.AckCodeType;
import com.ebay.soap.eBLBaseComponents.ListingStatusCodeType;
import com.shangpin.ebay.finding.AckValue;
import com.shangpin.ebay.finding.FindItemsIneBayStoresResponse;
import com.shangpin.ebay.finding.SearchItem;
import com.shangpin.ebay.shoping.GetMultipleItemsResponseType;
import com.shangpin.ebay.shoping.SimpleItemType;
import com.shangpin.ebay.shoping.VariationType;
import com.shangpin.ebay.shoping.VariationsType;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.json.JsonUtil;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.ebay.convert.ShopingItemConvert;
import com.shangpin.iog.ebay.service.GrabEbayApiService;
import com.shangpin.iog.service.ProductFetchService;

/**
 * ebay数据抓取服务，数据库存抓取服务
 * @description 
 * @author 陈小峰
 * <br/>2015年6月30日
 */
@Component
public class PageGrabService {
	static Logger logger = LoggerFactory.getLogger(PageGrabService.class);
	static int pageSize=100;
	public static ProductFetchService fetchSrv;
	@Autowired
	public void setFetchSrv(ProductFetchService fetchSrv) {
		PageGrabService.fetchSrv = fetchSrv;
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
	 * @param brand 品牌名字
	 * @return  封装好的sku,spu,pic，各键代表对应的数据集合
	 */
	@SuppressWarnings("rawtypes")
	public void findStoreBrand(String storeName,
			String brand){
		int page=1;
		boolean hasMore=false;
		FindItemsIneBayStoresResponse resp =null;
		ExecutorService exe = null;
		do{//分页循环取
			try {
				resp = GrabEbayApiService.findItemsIneBayStores(storeName,brand,page,pageSize);
				if(!AckValue.SUCCESS.equals(resp.getAck()) && !AckValue.WARNING.equals(resp.getAck())){//返回错误了则直接返回
					hasMore=false;
					return ;
				}
			}catch(XmlException e){
				//TODO 考虑重试！
				logger.error("findStoreBrand xml转换异常，storeName:"+storeName+",brand:"+brand,e);
				hasMore=false;
				return ;
			}catch(Exception e){
				logger.error("findStoreBrand未知异常，storeName:"+storeName+",brand:"+brand,e);				
				hasMore=false;
				return ;
			}
			//TODO 如果没找到数据，那么返回null
			if(resp.getPaginationOutput().getTotalEntries()==0){
				//logger.warn("没有找到storeName:{},brand:{}的产品,page:{}",storeName,brand,page);
				return ;
			}
			int totalPage=resp.getPaginationOutput().getTotalPages();
			if(totalPage>resp.getPaginationOutput().getPageNumber()){
				page++;
				hasMore=true;
			}else{
				hasMore=false;
			}
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
			//2.正式拉取并保存每一页
			if(totalPage>4){//考虑通过线程去处理了
				if(exe==null){
					exe = Executors.newFixedThreadPool(totalPage);//每页一个线程去跑
				}
				exe.execute(new GetDetailThread(storeName,itemIds,page));
			}else{
				Map<String, ? extends Collection> kpp;
				try {
					kpp = getMoreDetail(storeName,itemIds);
					saveData(kpp);
				} catch (XmlException e) {
					logger.error("{}获取品牌{}detail，第{}页错误:{}",storeName,brand,page,e.getMessage());
				}
			}
			//保存
		}while(hasMore);
		
		return ;
	}

	/**
	 * 保存每页的结果
	 * @param skuSpuAndPic
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void saveData(Map<String, ? extends Collection> skuSpuAndPic) {
		Collection<SkuDTO> skus = skuSpuAndPic.get("sku");
		Collection<String> skuIds = null;
		logger.info("线程{}抓取到sku数{}",Thread.currentThread().getName(),skus.size());
		skuIds = saveSku(skus);
		Collection<ProductPictureDTO> picUrls = skuSpuAndPic.get("pic");
		logger.info("线程{}抓取到pic数{}",Thread.currentThread().getName(),picUrls.size());
		savePic(picUrls,skuIds);
		Collection<SpuDTO> spuDTOs = skuSpuAndPic.get("spu");
		logger.info("线程{}抓取到spu数{}",Thread.currentThread().getName(),spuDTOs.size());
		saveSpu(spuDTOs);
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
	static class GetDetailThread implements  Runnable{

		private String storeName;
		private List<String> itemIds;
		private int page;

		/**
		 * @param storeName
		 * @param itemIds
		 * @param page 
		 */
		public GetDetailThread(String storeName, List<String> itemIds, int page) {
			this.storeName=storeName;
			this.itemIds=itemIds;
			this.page=page;
		}

		/*@Override
		public Map<String, Collection> call() throws Exception {
			logger.info("获取itemIds明细线程启动,itemId size:"+itemIds.size());
			return (Map<String, Collection>) getMoreDetail(storeName,itemIds);
		}
*/
		@Override
		public void run() {
			try {
				Map<String, ? extends Collection> kpp = getMoreDetail(storeName,itemIds);
				saveData(kpp);
			} catch (XmlException e) {
				logger.error("{}线程获取detail第{}页错误:{}",storeName,page,e.getMessage());
			}
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
			throw new XmlException(e.getMessage());
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


	/**
	 * @param picUrls
	 */
	private static void savePic(Collection<ProductPictureDTO> picUrls,Collection<String> skuIds) {
		logger.info("pic数：{}", picUrls.size());
		int failCnt = 0;
		for (ProductPictureDTO picurl : picUrls) {
			for(String  skuid:skuIds) {
				if(!picurl.getSkuId().equals(skuid)) {
					try {
						fetchSrv.savePictureForMongo(picurl);
					} catch (ServiceException e) {
						logger.error("保存图片{}失败,error:{}", JsonUtil.getJsonString4JavaPOJO(picurl), e.getMessage());
						failCnt++;
					}
				}
			}
		}
		logger.info("保存pic数：{}成功，失败数：{}", picUrls.size(),failCnt);
	}

	/**
	 * @param spuDTOs
	 */
	private static void saveSpu(Collection<SpuDTO> spuDTOs) {
		//logger.info("spu数：{}", spuDTOs.size());
		int failCnt = 0;
		for (SpuDTO spu : spuDTOs) {
			try {
				if(StringUtils.isNotBlank(spu.getMaterial())) {
					fetchSrv.saveSPU(spu);
				}
			} catch (ServiceException e) {
				logger.error("保存spu:{}失败,error:{}",JsonUtil.getJsonString4JavaPOJO(spu), e.getMessage());
				failCnt++;
			}
		}
		logger.info("保存spu数：{}成功，失败数：{}", spuDTOs.size(),failCnt);
	}

	/**
	 * @param skus
	 */
	private static Collection<String> saveSku(Collection<SkuDTO> skus) {
		//logger.info("sku数：{}", skus.size());
		int failCnt = 0;
		Collection<String> skuIds = new HashSet<>();
		for (SkuDTO sku : skus) {
			try {
				fetchSrv.saveSKU(sku);
			} catch (ServiceException e) {
				skuIds.add(sku.getSkuId());
				if(!"数据插入失败键重复".equals(e.getMessage()))
					logger.error("保存sku:{}失败,error:{}", JsonUtil.getJsonString4JavaPOJO(sku),e.getMessage());
					failCnt++;
			}
		}
		logger.info("保存sku数：{}成功，失败数：{}", skus.size(),failCnt);
		return skuIds;
	}
	
}
