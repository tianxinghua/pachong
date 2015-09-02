package com.shangpin.iog.ebay;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.VariationType;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;

/**
 * Created by huxia on 2015/7/2.
 */
public class GrabStockWithFindingApi extends AbsUpdateProductStock {
	static final Log log = LogFactory.getLog(GrabStockWithFindingApi.class);

	/**
	 * 抓取供应商库存数据
	 * 
	 * @param skuNo
	 *            供应商的每个产品的唯一编号：sku
	 * @see #grabProduct(String, String, String) 抓取主站SKU
	 * @return 每个sku对应的库存数
	 * @throws ServiceException
	 */
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws Exception {
		Map<String, String> skuStock = new HashMap<String, String>();
		FetchEbayProduct product = new FetchEbayProduct();
		ItemType item = null;
		String skuid = null;
		for (String skuId : skuNo) {
			skuid = skuId.split("#")[1];
			//item = product.testGetItem(skuId.split("#")[0]);
			if (item.getListingDetails().getEndTime().getTime()
					.after(Calendar.getInstance().getTime())) {
				if (item.getVariations() != null) {
					VariationType[] variationTypes = item.getVariations()
							.getVariation();
					if (variationTypes != null) {
						for (VariationType var : variationTypes) {
							if (skuid.equals(var.getSKU())) {
								skuStock.put(skuId, String.valueOf((var.getQuantity() - var
										.getSellingStatus().getQuantitySold())));
								break;
							}
						}
					}
				} else {
					skuStock.put(skuId, String.valueOf((item.getQuantity()-item.getSellingStatus().getQuantitySold())));
				}
			}
		}
		return skuStock;
	}
	//TODO 尽量放测试代码里面，如果写在main/java里面，提交的时候注释掉
	/*public static void main(String args[]) throws Exception {
		System.out.println("dghfgh");
		GrabStockWithFindingApi grabStockWithFindingApi = new GrabStockWithFindingApi();
		Collection<String> sku = new HashSet<String>();
		sku.add("231359724482#A4150dh_XL@ZBU7");
		Map<String, Integer> skuStock;
		skuStock = grabStockWithFindingApi.grabStock(sku);
		System.out.println(skuStock.get("231359724482#A4150dh_XL@ZBU7"));
		System.out.println("dgryt");
		// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		*/
	    /*
		 log.info("eaby更新数据库开始");
		 grabStockWithFindingApi.updateProductStock("ebay#inzara.store",
		 "2015-01-14 15:11","2015-07-11 15:11"); log.info("eaby更新数据库结束");
		 /*
	}*/
}
