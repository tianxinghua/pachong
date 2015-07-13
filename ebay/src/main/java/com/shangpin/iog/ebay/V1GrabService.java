/**
 * 
 */
package com.shangpin.iog.ebay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;
import com.ebay.sdk.SdkSoapException;
import com.ebay.soap.eBLBaseComponents.AckCodeType;
import com.ebay.soap.eBLBaseComponents.GetSellerListResponseType;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.shangpin.ebay.shoping.GetMultipleItemsResponseType;
import com.shangpin.ebay.shoping.SimpleItemType;
import com.shangpin.ebay.shoping.VariationType;
import com.shangpin.ebay.shoping.VariationsType;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.ebay.convert.ShopingItemConvert;
import com.shangpin.iog.ebay.service.GrabEbayApiService;
import com.shangpin.iog.service.ProductFetchService;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月30日
 */
@Component
public class V1GrabService {
	static Logger logger = LoggerFactory.getLogger(V1GrabService.class);
	@Autowired
	ProductFetchService productFetchService;
	static int pageSize=200;
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, ? extends Collection> getSellerList(String userId,Date endStart,Date endEnd) throws ApiException, SdkSoapException, SdkException{
		int page=1;
		boolean hasMore=false;
		Map<String, ? extends Collection> skuSpuAndPic=null;
		do{
			GetSellerListResponseType resp = GrabEbayApiService.tradeSellerList(userId, 
					getCalendar(endStart), getCalendar(endEnd),page,pageSize);
			if(AckCodeType.FAILURE.equals(resp.getAck())){
				hasMore=resp.isHasMoreItems();
				ItemType[] tps = resp.getItemArray().getItem();
				List<String> itemIds = new ArrayList<>(tps.length);//1.得到id
				for (ItemType itemType : tps) {
					itemIds.add(itemType.getItemID());
				}
				//2.得到item
				GetMultipleItemsResponseType multResp=GrabEbayApiService.shoppingGetMultipleItems(itemIds);
				//3.转换sku,spu
				SimpleItemType[] itemTypes=multResp.getItemArray();
				//Map<String, ? extends Collection<?>> kpp=TradeItemConvert.convert2SKuAndSpu(tps,userId);
				Map<String, Collection> kpp=ShopingItemConvert.convert2kpp(itemTypes,userId);
				if(skuSpuAndPic==null){
					skuSpuAndPic=kpp;
				}else{
					skuSpuAndPic.get("sku").addAll(kpp.get("sku"));
					skuSpuAndPic.get("spu").addAll(kpp.get("spu"));
					skuSpuAndPic.get("pic").addAll(kpp.get("pic"));
				}
				page++;
			}
		}while(hasMore);
		
		return skuSpuAndPic;
	}

    public void FetchAndSave() throws SdkException, ServiceException {
		Date date=Calendar.getInstance().getTime();
		Date date2=null;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 1);
		date2=c.getTime();
		Map<String, ? extends Collection> skuSpuAndPic=getSellerList("inzara.store",date,date2);
		//Collection<SkuDTO>  skus= skuSpuAndPic.get("sku");
		//System.out.println(skus.size()+"nihaoma");
//		for(SkuDTO sku:skus) {
//			productFetchService.saveSKU(sku);
//		}
//		Collection<SpuDTO> spuDTOs = skuSpuAndPic.get("spu");
//		for(SpuDTO spu:spuDTOs){
//			productFetchService.saveSPU(spu);
//		}
//		Collection<ProductPictureDTO> picUrl = skuSpuAndPic.get("pic");
//		for(ProductPictureDTO picurl:picUrl){
//			productFetchService.savePictureForMongo(picurl);
//		}
	}
	/**
	 * 根据itemId获取item及变种的库存<br/>
	 * @param itemIds ebay的itemId
	 * @return skuId:stock的键值对
	 * @see ShopingItemConvert#getSkuId(SimpleItemType, VariationType) 产品skuId
	 */
	public Map<String,Integer> getStock(Collection<String> itemIds){
		GetMultipleItemsResponseType resp=GrabEbayApiService.shoppingGetMultipleItems4Stock(itemIds);
		if(AckCodeType.FAILURE.value().equals(resp.getAck().toString())){
			logger.warn("获取库存失败，错误码：{}，错误信息{}:",resp.getErrorsArray(0).getErrorCode(),resp.getErrorsArray(0).getLongMessage());
			return null;
		}
		SimpleItemType[] sits=resp.getItemArray();
		Map<String,Integer> rtnMap=new HashMap<>(sits.length);
		for (SimpleItemType sit : sits) {
			VariationsType vta=sit.getVariations();
			if(vta!=null && vta.getVariationArray()!=null){
				VariationType[] vts=vta.getVariationArray();
				for (VariationType vt : vts) {
					int quantity=vt.getQuantity()-vt.getSellingStatus().getQuantitySold();
					rtnMap.put(ShopingItemConvert.getSkuId(sit,vt),quantity);
				}
			}else{
				int quantity=sit.getQuantity()-sit.getQuantitySold();
				rtnMap.put(ShopingItemConvert.getSkuId(sit,null), quantity);
			}
		}
		return null;
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
	
}
