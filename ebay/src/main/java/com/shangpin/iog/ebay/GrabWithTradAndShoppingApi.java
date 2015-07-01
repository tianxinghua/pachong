/**
 *
 */
package com.shangpin.iog.ebay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;
import com.ebay.sdk.SdkSoapException;
import com.ebay.soap.eBLBaseComponents.AckCodeType;
import com.ebay.soap.eBLBaseComponents.GetSellerListResponseType;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.shangpin.ebay.shoping.GetMultipleItemsResponseType;
import com.shangpin.ebay.shoping.SimpleItemType;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.ebay.convert.ShopingItemConvert;
import com.shangpin.iog.ebay.service.GrabEbayApiService;
import com.shangpin.iog.service.ProductFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description
 * @author 陈小峰
 * <br/>2015年6月30日
 */
@Component
public class GrabWithTradAndShoppingApi {
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
            System.out.println("nihao");
			if(!AckCodeType.FAILURE.equals(resp.getAck())){
                System.out.println("nihaoaaaaaa");
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
				Map<String, ? extends Collection<?>> kpp=ShopingItemConvert.convert2kpp(itemTypes,userId);
				System.out.println(kpp.size()+"nihao");
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
	 * @param date
	 * @return
	 */
	private Calendar getCalendar(Date date) {
		Calendar ca=Calendar.getInstance();
		ca.setTime(date);
		return ca;
	}

}
