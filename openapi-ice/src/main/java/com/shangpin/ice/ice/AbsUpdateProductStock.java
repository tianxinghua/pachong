package com.shangpin.ice.ice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ShangPin.SOP.Api.ApiException;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuIce;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPage;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPageQuery;
import ShangPin.SOP.Entity.Api.Product.SopSkuIce;
import ShangPin.SOP.Entity.Api.Product.SopSkuInventoryIce;
import ShangPin.SOP.Servant.OpenApiServantPrx;

import com.shangpin.framework.ServiceException;

/**
 * 更新主站库存的抽象类<br/>
 * 各供应商模块，只要实现{@link #grabStock(Collection) "根据sku拿库存"}就行
 * @description 
 * @author 陈小峰
 * <br/>2015年6月10日
 */
public abstract class AbsUpdateProductStock {
	static Logger logger = LoggerFactory.getLogger(AbsUpdateProductStock.class);
	private boolean useThread=false;
	private int skuCount4Thread=100;
	
	/**
	 * 抓取供应商库存数据 
	 * @param skuNo 供应商的每个产品的唯一编号：sku
	 * @see #grabProduct(String, String, String) 抓取主站SKU
	 * @return 每个sku对应的库存数
	 * @throws ServiceException 
	 */
	public abstract Map<String,Integer> grabStock(Collection<String> skuNo) throws ServiceException, Exception;
	/**
	 * 抓取主站商品SKU信息，等待更新库存<br/>
	 * @see #updateProduct(String, Map) 更新库存
	 * @param supplier 供应商id
	 * @param start 主站数据开始时间
	 * @param end 主站数据结束时间
	 * @param stocks 本地sku编号与ice的sku键值对
	 * @return 供应商skuNo
	 * @throws Exception
	 */
	private Collection<String> grabProduct(String supplier,String start,String end,Map<String,String> stocks) throws Exception{
		int pageIndex=1,pageSize=100;
		OpenApiServantPrx servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
		boolean hasNext=true;
		Set<String> skuIds = new HashSet<String>();
		while(hasNext){
			SopProductSkuPageQuery query = new SopProductSkuPageQuery(start,end,pageIndex,pageSize);
			SopProductSkuPage products = servant.FindCommodityInfoPage(supplier, query);
			List<SopProductSkuIce> skus = products.SopProductSkuIces;
			for (SopProductSkuIce sku : skus) {
				List<SopSkuIce> skuIces = sku.SopSkuIces;
				for (SopSkuIce ice : skuIces) {
					skuIds.add(ice.SupplierSkuNo);
					stocks.put(ice.SupplierSkuNo,ice.SkuNo);
				}
			}
			pageIndex++;
			hasNext=(pageSize==skus.size());
		}
		return skuIds;
	}
	
	/**
	 * 更新主站库存
	 * @param supplier 供应商id
	 * @param start 主站产品数据时间开始 yyyy-MM-dd HH:mm
	 * @param end 主站产品数据时间结束 yyyy
	 *            +   -MM-dd HH:mm
	 * @see {@link #grabStock(List) 抓取库存 }
	 * @return 更新失败数
	 * @throws Exception 
	 */
	public int updateProductStock(final String supplier,String start,String end) throws Exception{
		//ice的skuid与本地库拉到的skuId的关系，value是ice中skuId,key是本地中的skuId
		final Map<String,String> localAndIceSku=new HashMap<String, String>();
		final Collection<String> skuNoSet=grabProduct(supplier, start, end,localAndIceSku);
		logger.warn("待更新库存数据总数："+skuNoSet.size());
		final List<Integer> totoalFailCnt=Collections.synchronizedList(new ArrayList<Integer>());
		if(useThread){
			int poolCnt=skuNoSet.size()/getSkuCount4Thread();
			ExecutorService exe=Executors.newFixedThreadPool(poolCnt/4+1);//相当于跑4遍
			final List<Collection<String>> subSkuNos=subCollection(skuNoSet);
			logger.warn("线程池数："+(poolCnt/4+1)+",sku子集合数："+subSkuNos.size());
			for(int i = 0 ; i <subSkuNos.size();i++){
				exe.execute(new UpdateThread(subSkuNos.get(i),supplier,localAndIceSku,totoalFailCnt));
			}
			exe.shutdown();
			while (!exe.awaitTermination(10, TimeUnit.SECONDS)) {
				
			}
			int fct=0;
			for(int k=0;k<totoalFailCnt.size();k++){
				fct+=totoalFailCnt.get(k);
			}
			return fct;
		}else{
			return updateStock(supplier, localAndIceSku, skuNoSet);
		}
	}
	/**
	 * @param skuNoSet
	 * @param thd
	 * @return
	 */
	private List<Collection<String>> subCollection(Collection<String> skuNoSet) {
		int thcnt = getSkuCount4Thread();
		List<Collection<String>> list=new ArrayList<>();
		int count=0;int currentSet=0;
		for (Iterator<String> iterator = skuNoSet.iterator(); iterator
				.hasNext();) {
			String skuNo = iterator.next();
			if(count==thcnt)
				count=0;
			if(count==0){
				Collection<String> e = new ArrayList<>();
				list.add(e);				
				currentSet++;
			}
			list.get(currentSet-1).add(skuNo);
			count++;
		}
		return list;
	}
	/**
	 * @param supplier
	 * @param localAndIceSkuId value是ice的skuNo,key是供应商skuId
	 * @param skuNoSet 供应商sku编号
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 * @throws ApiException
	 */
	private int updateStock(String supplier,
			Map<String, String> localAndIceSkuId,
			Collection<String> skuNoSet) throws ServiceException, Exception,
			ApiException {
		Map<String, Integer> iceStock = grab4Icestock(skuNoSet,localAndIceSkuId);
		int failCount = updateIceStock(supplier, iceStock);
		return failCount;
	}
	/**
	 * 更新ice的库存
	 * @param supplier 供应商
	 * @param iceStock ice的skuNo,与对应的库存关系
	 * @return 更新失败的数目
	 * @throws Exception
	 */
	private int updateIceStock(String supplier, Map<String, Integer> iceStock)
			throws Exception {
		OpenApiServantPrx servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
		
		//获取尚品库存
		Set<String> skuNoShangpinSet = iceStock.keySet();
		int skuNum = 1;
		List<String> skuNoShangpinList = new ArrayList<>();
		for(Iterator<String> itor =skuNoShangpinSet.iterator();itor.hasNext();){
			if(skuNum%200==0){
				//调用接口 查找库存
				removeNoChangeStockRecord(supplier, iceStock, servant, skuNoShangpinList);
				skuNoShangpinList = new ArrayList<>();
			}
			skuNoShangpinList.add(itor.next());
			skuNum++;
		}
		//排除最后一次
		removeNoChangeStockRecord(supplier, iceStock, servant, skuNoShangpinList);
		
		
		int failCount=0;
		Iterator<Entry<String, Integer>> iter=iceStock.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Integer> entry = iter.next();
			Boolean result =true;
			try{
				result = servant.UpdateStock(supplier, entry.getKey(), entry.getValue());				
			}catch(Exception e){
				result=false;
				logger.error("更新sku错误："+entry.getKey()+":"+entry.getValue(),e);
			}
			if(!result){
				failCount++;
				logger.warn("更新iceSKU：{}，库存量：{}失败",entry.getKey(),entry.getValue());
			}				
		}
		return failCount;
	}
	/**
	 * 移除库存没有变化的商品 不做更新
	 * @param supplier
	 * @param iceStock
	 * @param servant
	 * @param skuNoShangpinList
	 * @throws ApiException
	 */
	private void removeNoChangeStockRecord(String supplier, Map<String, Integer> iceStock, OpenApiServantPrx servant, List<String> skuNoShangpinList) throws ApiException {
		SopSkuInventoryIce[] skuIceArray =servant.FindStockInfo(supplier, skuNoShangpinList);
		//排除无用的库存
		for(SopSkuInventoryIce skuIce:skuIceArray){
	        if(iceStock.containsKey(skuIce.SkuNo)){
	            if(iceStock.get(skuIce.SkuNo)==skuIce.InventoryQuantity){
	                iceStock.remove(skuIce.SkuNo);
	            }
	        }
		}
    }
	/**
	 * 拉取供应商库存，并返回ice中对应的sku的库存
	 * @param skuNos 供应商sku编号，从ice中获取到的
	 * @param localAndIceSkuId 供应商sku编号与icesku编号的关系
	 * @return 供应商sku对应的icesku编号的库存，key是ice的sku编号，值是库存
	 */
	private Map<String, Integer> grab4Icestock(Collection<String> skuNos,Map<String, String> localAndIceSkuId) {
		Map<String, Integer> iceStock=new HashMap<>();
		try {
			Map<String, Integer> supplierStock=grabStock(skuNos);
			for (String skuNo : skuNos) {
				Integer stock=supplierStock.get(skuNo);
				String iceSku=localAndIceSkuId.get(skuNo);
				if(stock==null)
					stock=0;
				iceStock.put(iceSku, stock);
			}
		} catch (Exception e1) {
			logger.error("抓取库存失败:",e1);
		}
		return iceStock;
	}

	class UpdateThread extends Thread{

		private Collection<String> skuNos;
		private Map<String, String> localAndIceSkuId;
		private String supplier;
		private List<Integer> totoalFailCnt;
		/**
		 * @param localAndIceSku 所有主站skuId,供应商skuNo关系,key：供应商skuId,value:ice的skuNo
		 * @param totoalFailCnt 
		 * @param skuNos 供应商skuNo集合
		 */
		public UpdateThread(Collection<String> skuNos,String supplier, Map<String, String> localAndIceSku, List<Integer> totoalFailCnt ) {
			this.skuNos=skuNos;
			this.supplier=supplier;
			this.localAndIceSkuId=localAndIceSku;
			this.totoalFailCnt=totoalFailCnt;
		}
		@Override
		public void run() {
			int size=skuNos.size();
			Map<String, Integer> iceStock = grab4Icestock(skuNos,localAndIceSkuId);
			try {
				logger.warn(Thread.currentThread().getName()+"处理开始，数："+size);
				int failCnt = updateIceStock(supplier, iceStock);
				totoalFailCnt.add(failCnt);
				logger.warn(Thread.currentThread().getName()+"完成，失败数:"+failCnt);
			} catch (Exception e) {
				logger.warn(Thread.currentThread().getName()+"处理出错",e);
			}
		}
		
	}
	
	
	/**
	 * 多少个sku启动一个线程,默认100
	 * @return
	 */
	public int getSkuCount4Thread() {
		if(skuCount4Thread<=0)
			return 100;
		return skuCount4Thread;
	}
	/**
	 * 多少个sku启动一个线程进行数据拉取，默认100
	 * @param skuCount4Thread
	 */
	public void setSkuCount4Thread(int skuCount4Thread) {
		this.skuCount4Thread = skuCount4Thread;
	}
	/**
	 * 是否使用多线程
	 * @return
	 */
	public boolean isUseThread() {
		return useThread;
	}
	/**
	 * 是否使用多线程
	 * @param useThread
	 */
	public void setUseThread(boolean useThread) {
		this.useThread = useThread;
	}
}
