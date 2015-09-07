package com.shangpin.iog.ebay;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.shangpin.sop.AbsUpdateProductStock;
import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.framework.ServiceException;

/**
 * @description
 * @author 陈小峰
 * <br/>2015年7月24日
 */
public class V1UpdateStock  extends AbsUpdateProductStock {
	static Logger logger = LoggerFactory.getLogger(V1UpdateStock.class);
	V1GrabService grabSrv = new V1GrabService();
	ThreadLocal<Set<String>> errItemId=new ThreadLocal<Set<String>>();
	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		Set<String> itemIds=new HashSet<>(skuNo.size());
		for (Iterator<String> iterator = skuNo.iterator(); iterator.hasNext();) {
			String skuId = iterator.next();
			itemIds.add(skuId.split("#")[0]);
		}
		Map<String, String> rtn = grabItmStock(itemIds);
		int retry=0;
		while(getErrItemId()!=null && getErrItemId().size()>0 && retry<10){//失败的重取，errItemId得清空
			retry++;
			logger.info("拉取失败的重拉第{}次，大小：{}",retry,getErrItemId().size());
			Set<String> itms=getErrItemId();
			errItemId.set(new HashSet<String>());
			combine(rtn, grabItmStock(itms));
		}
		return rtn;
	}

	/**
	 * @param itemIds
	 * @param rtn
	 * @return
	 * @throws XmlException
	 */
	private Map<String, String> grabItmStock(Set<String> itemIds){
		Map<String, String> rtn = new HashMap<String, String>();
		if(itemIds.size()>20){
			List<String> pageItem = new ArrayList<>(20);
			for (String id : itemIds) {
				if(pageItem.size()==20){
					try {
						combine(rtn,grabSrv.getStock(pageItem));
					} catch (XmlException e) {
						logger.info("拉取有失败的，加入重试库a");
						addErrorItem(pageItem);
					}//每20个获取一遍
					pageItem=new ArrayList<>(20);
				}
				pageItem.add(id);
			}
			if(pageItem.size()>0){
				try {
					combine(rtn,grabSrv.getStock(pageItem));
				} catch (XmlException e) {
					logger.info("拉取有失败的，加入重试库b");
					addErrorItem(pageItem);
				}//每20个获取一遍
			}
		}else{
			try {
				rtn=grabSrv.getStock(itemIds);
			} catch (XmlException e) {
				logger.info("拉取有失败的，加入重试库c");
				addErrorItem(itemIds);
			}
		}
		return rtn;
	}


	/**
	 * @param rtn
	 * @param stock
	 */
	private static void combine(Map<String, String> rtn, Map<String, String> stock)  {
		Set<Entry<String, String>> entrySet = stock.entrySet();
		for (Entry<String, String> entry : entrySet) {
			rtn.put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * @param itemIds
	 */
	private void addErrorItem(Collection<String> itemIds) {
		if(errItemId.get()==null)
			errItemId.set(new HashSet<String>());
		errItemId.get().addAll(itemIds);
	}
	/**
	 * 添加获取错误的item
	 * @param itemId
	 */
	public void addErrorItem(String itemId){
		if(errItemId.get()==null)
			errItemId.set(new HashSet<String>());
		errItemId.get().add(itemId);
	}
	public Set<String> getErrItemId() {
		return errItemId.get();
	}
}
