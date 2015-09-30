package com.shangpin.iog.fashionesta.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.fashionesta.stock.dto.Item;
import com.shangpin.iog.fashionesta.stock.dto.Product;
import com.shangpin.iog.fashionesta.stock.utils.DownloadAndReadCSV;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by monkey on 2015/9/28.
 */
public class StockClientImp extends AbsUpdateProductStock {
	@Autowired
	DownloadAndReadCSV csvUtil;

	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		ResourceBundle bundle = ResourceBundle.getBundle("param");
		String supplierId = bundle.getString("supplier");
		Map<String, String> skustock = new HashMap<>(skuNo.size());
		List<Product> list = csvUtil.readLocalCSV();
		Iterator<String> it = skuNo.iterator();
		while (it.hasNext()) {
			String skuId = it.next();
			for (Product product : list) {
				List<Item> items = product.getItems();
				if (items.size()>0) {
					for (Item item : items) {
						if (skuId.equals(item.getItemCode())) {
							skustock.put(skuId, item.getStock());
						}
					}
				}
			}
		}
		return skustock;
	}
}
