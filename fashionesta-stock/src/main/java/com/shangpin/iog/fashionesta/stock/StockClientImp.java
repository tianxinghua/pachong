package com.shangpin.iog.fashionesta.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.fashionesta.stock.dto.Item;
import com.shangpin.iog.fashionesta.stock.dto.Product;
import com.shangpin.iog.fashionesta.stock.utils.DownloadAndReadCSV;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by monkey on 2015/9/28.
 */
public class StockClientImp extends AbsUpdateProductStock {

	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");

	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String url;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("param");
		supplierId = bdl.getString("supplierId");
		url = bdl.getString("url");
	}

	@Autowired
	DownloadAndReadCSV csvUtil;

	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {

		Map<String, String> skustock = new HashMap<>(skuNo.size());
		try {
			List<Product> list = csvUtil.readLocalCSV();
			Map<String,String> supplierStockMap = new HashMap<>();
			for (Product product : list) {
                List<Item> items = product.getItems();
                if (items.size()>0) {
                    for (Item item : items) {

                        supplierStockMap.put(item.getItemCode(),item.getStock());
                    }
                }
            }


			Iterator<String> it = skuNo.iterator();
			String skuId ="";
			while (it.hasNext()) {
                skuId = it.next();
                if(supplierStockMap.containsKey(skuId)){
                    skustock.put(skuId,supplierStockMap.get(skuId));
                }else{
                    skustock.put(skuId,"0");
                }

            }
		} catch (Exception e) {
			loggerError.error("获取库存失败。"+e.getMessage());
			throw e;
		}
		return skustock;
	}

	public static void main(String[] args) throws Exception {
		AbsUpdateProductStock stockImp = new StockClientImp();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		logger.info("更新数据库开始");
		try {
			stockImp.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
		} catch (Exception e) {
			logger.info("更新库存数据库出错"+e.toString());
		}
		logger.info("更新数据库结束");
		System.exit(0);
	}


}
