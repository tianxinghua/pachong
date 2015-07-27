package com.shangpin.iog.ebay;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年7月24日
 */
public class V1UpdateStock  extends AbsUpdateProductStock{

	V1GrabService grabSrv = new V1GrabService();
	@Override
	public Map<String, Integer> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		Collection<String> itemIds=new HashSet<>(skuNo.size());
		for (Iterator<String> iterator = skuNo.iterator(); iterator.hasNext();) {
			String skuId = iterator.next();
			itemIds.add(skuId.split("#")[0]);
		}
		return grabSrv.getStock(itemIds);
	}
}
