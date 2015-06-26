package com.shangpin.iog.ebay;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ebay.sdk.ApiContext;
import com.ebay.sdk.TimeFilter;
import com.ebay.sdk.call.GetItemCall;
import com.ebay.sdk.call.GetSellerListCall;
import com.ebay.soap.eBLBaseComponents.DetailLevelCodeType;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.NameValueListType;
import com.ebay.soap.eBLBaseComponents.PaginationType;
import com.ebay.soap.eBLBaseComponents.VariationType;
import com.ebay.soap.eBLBaseComponents.VariationsType;
import com.shangpin.ebay.finding.FindItemsIneBayStoresResponse;
import com.shangpin.iog.ebay.conf.EbayConf;

/**
 * Created by huxia on 2015/6/23.
 */
public class EbayGrabProductService {
    static int pageSize=200;
	/**
	 * 获取ebay指定商家的sku,spu,picture信息<br/>
	 * @param seller 卖家id
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return sku,spu,pic分别对应sku，spk，picture数据
	 */
	public Map<String,List<?>> grabProduct(String seller,Date start,Date end){
		ApiContext context=EbayConf.getApiContext();
		GetSellerListCall call = new GetSellerListCall(context);
		call.setDetailLevel(new DetailLevelCodeType[]{DetailLevelCodeType.ITEM_RETURN_ATTRIBUTES,DetailLevelCodeType.ITEM_RETURN_CATEGORIES});
		PaginationType pg=new PaginationType();
		//TODO 动态指定分页
		pg.setEntriesPerPage(pageSize);pg.setPageNumber(1);
		call.setPagination(pg);
		Calendar timeFrom=Calendar.getInstance();timeFrom.setTime(start);
		Calendar timeTo=Calendar.getInstance();timeFrom.setTime(end);
		call.setEndTimeFilter(new TimeFilter(timeFrom, timeTo));
		ItemType[] items=call.getReturnedItems();//获取item
		for (ItemType item : items) {
			item.getItemID();//itemId
			VariationsType vt=item.getVariations();//获取item的变种
			VariationType[] vtps=vt.getVariation();
			for (VariationType vtp : vtps) {
				vtp.getSKU();
			}
			NameValueListType[] nvs=vt.getVariationSpecificsSet().getNameValueList();
			for (NameValueListType nv : nvs) {
				nv.getName();//属性名
				nv.getValue();//属性值
			}
		}
        return null;
    }
	
    public void getSku(List<FindItemsIneBayStoresResponse> response){
    	
    }
    
    public void getSpu(List<FindItemsIneBayStoresResponse> response){
    	
    }
    
    
    
    public int getStock(String itemId) throws Exception {
        ApiContext contxt=EbayConf.getApiContext();
        GetItemCall call = new GetItemCall(contxt);
        ItemType o = call.getItem(itemId);
        return o.getQuantity();
    }
   

}
