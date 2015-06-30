/**
 * 
 */
package com.shangpin.iog.ebay;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
	 * @throws ApiException 
	 * @throws SdkSoapException
	 * @throws SdkException
	 */
	public void getSellerList(String userId,Date endStart,Date endEnd) throws ApiException, SdkSoapException, SdkException{
		ApiContext api = EbayConf.getApiContext();
		ApiCall call = new ApiCall(api);
		GetSellerListRequestType req = new GetSellerListRequestType();
		req.setUserID(userId);//pumaboxstore buydig inzara.store happynewbaby2011
		req.setEndTimeFrom(getCalendar(endStart));
		req.setEndTimeTo(getCalendar(endEnd));
		
		int page=1;
		PaginationType pg =new PaginationType();
		pg.setPageNumber(page);pg.setEntriesPerPage(pageSize);
		req.setPagination(pg);
		req.setIncludeVariations(true);
		/*req.setDetailLevel(new DetailLevelCodeType[]{
				DetailLevelCodeType.ITEM_RETURN_ATTRIBUTES,
				DetailLevelCodeType.ITEM_RETURN_CATEGORIES,
				DetailLevelCodeType.RETURN_HEADERS,
				DetailLevelCodeType.RETURN_ALL
				});*/
		req.setGranularityLevel(GranularityLevelCodeType.FINE);
		boolean hasMore=false;
		Map<String, List<? extends Object>> skuAndSpu=new HashMap<String, List<?>>();
		do{
			GetSellerListResponseType resp = (GetSellerListResponseType) call.execute(req);
			if(AckCodeType.FAILURE.equals(resp.getAck())){
				hasMore=resp.isHasMoreItems();
				ItemType[] tps = resp.getItemArray().getItem();
				Map<String, List<? extends Object>> kpp=TradeItemConvert.convert2SKuAndSpu(tps,resp.getSeller());
				if(skuAndSpu==null){
					skuAndSpu=kpp;
				}else{
					skuAndSpu.get("sku").addAll(kpp.get("sku"));
					skuAndSpu.get("spu").addAll(kpp.get("spu"));
					skuAndSpu.get("pic").addAll(kpp.get("pic"));
				}
				pg.setPageNumber(page++);
			}
		}while(hasMore);
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
