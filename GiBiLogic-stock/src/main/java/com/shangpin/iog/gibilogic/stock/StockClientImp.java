package com.shangpin.iog.gibilogic.stock;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.gibilogic.stock.util.StockUtil;

@Component("gibilogicStock")
public class StockClientImp extends AbsUpdateProductStock {
	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		
		Map<String, String> stockMap = new StockUtil().getStockMap();

		Iterator<String> it = skuNo.iterator();
		String skuId ="";
		Map<String, String> skustock = new HashMap<String, String>();
		while (it.hasNext()) {
			skuId = it.next();
			if (stockMap.containsKey(skuId)) {
				skustock.put(skuId, stockMap.get(skuId));
			} else {
				skustock.put(skuId, "0");
			}
		}

		return skustock;
	}

}
