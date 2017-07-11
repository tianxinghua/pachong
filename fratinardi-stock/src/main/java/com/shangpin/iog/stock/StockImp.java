package com.shangpin.iog.stock;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.stock.util.DownloadAndReadCSV;

@Component("fratinardiStock")
public class StockImp extends AbsUpdateProductStock{

	private static Logger logInfo  = Logger.getLogger("info");
	
	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		
		Map<String, String> skustock = new HashMap<>();
		Map<String,String> stockMap = new HashMap<>();
		JSONArray list = DownloadAndReadCSV.readLocalCSV();
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> ite = list.iterator();
		while(ite.hasNext()){
			JSONObject product =ite.next();
			String stock = product.getString("qty");
			String supplierSkuNo = product.getString("sku No");
			if(StringUtils.isNotBlank(supplierSkuNo)&&StringUtils.isNotBlank(stock)){
				stockMap.put(supplierSkuNo.replaceAll("\"", ""), stock.replaceAll("\"", ""));
			}
		}
		
		for (String skuno : skuNo) {
            if(stockMap.containsKey(skuno)){
            	try {
            		logInfo.info("sku="+skuno+"---containskey");
                    skustock.put(skuno, stockMap.get(skuno));
				} catch (Exception e) {
					skustock.put(skuno, "0");
				}
            } else{
				logInfo.info("sku="+skuno+"---not  containskey");
                skustock.put(skuno, "0");
            }
        }
		return skustock;
	}
}
