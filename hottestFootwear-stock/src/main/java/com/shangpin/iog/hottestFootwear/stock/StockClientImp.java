package com.shangpin.iog.hottestFootwear.stock;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.hottestFootwear.dto.CsvDTO;
import com.shangpin.iog.hottestFootwear.utils.CSVUtil;

/**
 * Created by monkey on 2015/10/20.
 */
@Component("hottestFootwear")
public class StockClientImp extends AbsUpdateProductStock {

	private static Logger logger = Logger.getLogger("info");
	 private static ApplicationContext factory;
	    private static void loadSpringContext()
	    {
	        factory = new AnnotationConfigApplicationContext(AppContext.class);
	    }
	private static ResourceBundle bdl = null;
	private static String supplierId;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("param");
		supplierId = bdl.getString("supplierId");
	}




	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		String skuId ="";
		
		Map<Integer, Integer> colNum = new HashMap<Integer, Integer>();
		//参数附上对应的value
		colNum.put(0, 0);colNum.put(1, 3);
		List<CsvDTO> skuLists = CSVUtil.readLocalCSV(CsvDTO.class, ",", colNum);
		Map<String,String> stockMap = new HashMap<String, String>();
		Map<String,String> returnMap = new HashMap<String, String>();
		for (CsvDTO csvDTO : skuLists) {
			stockMap.put(csvDTO.getSku(), csvDTO.getQty());
		}
		Iterator<String> it = skuNo.iterator();
		while (it.hasNext()) {
			skuId = it.next();
			if (stockMap.containsKey(skuId)) {
				returnMap.put(skuId, stockMap.get(skuId));
			}else{
				returnMap.put(skuId, "0");
			}
		}
		return returnMap;
	}

	public static void main(String[] args) throws Exception {
		//加载spring
        loadSpringContext();
        StockClientImp stockImp =(StockClientImp)factory.getBean("hottestFootwear");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		logger.info("更新数据库开始");
		try {
			stockImp.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
//			stockImp.grabStock(null);
		} catch (Exception e) {
			logger.info("更新库存数据库出错"+e.toString());
		}
		logger.info("更新数据库结束");
		System.exit(0);
	}


}
