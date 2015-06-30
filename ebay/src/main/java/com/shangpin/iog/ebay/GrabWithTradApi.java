/**
 * 
 */
package com.shangpin.iog.ebay;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ebay.sdk.ApiCall;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;
import com.ebay.sdk.SdkSoapException;
import com.ebay.soap.eBLBaseComponents.AckCodeType;
import com.ebay.soap.eBLBaseComponents.GetSellerListRequestType;
import com.ebay.soap.eBLBaseComponents.GetSellerListResponseType;
import com.ebay.soap.eBLBaseComponents.GranularityLevelCodeType;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.PaginationType;
import com.shangpin.iog.ebay.conf.EbayConf;
import com.shangpin.iog.ebay.convert.TradeItemConvert;
import com.shangpin.iog.ebay.service.GrabEbayApiService;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月30日
 */
public class GrabWithTradApi {
	
	static int pageSize=300;
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
			if(AckCodeType.FAILURE.equals(resp.getAck())){
				hasMore=resp.isHasMoreItems();
				ItemType[] tps = resp.getItemArray().getItem();
				Map<String, ? extends Collection<?>> kpp=TradeItemConvert.convert2SKuAndSpu(tps,userId);
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
