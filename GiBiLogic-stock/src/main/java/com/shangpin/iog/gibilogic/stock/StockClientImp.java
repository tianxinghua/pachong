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
	
	private static Logger log = Logger.getLogger("info");
	
	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		
		Map<String, String> stockMap = new StockUtil().getStockMap();
		log.info("抓取的供应商的总体数据大小是============"+stockMap.size()); 
		log.info("sop获取到的数据的大小是============"+skuNo.size()); 
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
	
//	public static void main(String[] args) {
//		try {
//			new StockClientImp().grabStock(null);
//		} catch (ServiceException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

}
