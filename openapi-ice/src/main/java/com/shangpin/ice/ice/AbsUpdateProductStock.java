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
	 * @param stocks ice与本地sku编号键值对
	 * @return
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
					stocks.put(ice.SkuNo,ice.SupplierSkuNo);
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
		//ice的skuid与本地库拉到的skuId的关系，value是本地库中skuId,key是ice中的skuNo
		final Map<String,String> iceAndLocalSku=new HashMap<String, String>();
		final Collection<String> skuNoSet=grabProduct(supplier, start, end,iceAndLocalSku);
logger.warn("待更新库存数据总数："+skuNoSet.size());
		final List<Integer> totoalFailCnt=Collections.synchronizedList(new ArrayList<Integer>());
		if(useThread){
			int poolCnt=skuNoSet.size()/getSkuCount4Thread();
			ExecutorService exe=Executors.newFixedThreadPool(poolCnt/4+1);//相当于跑4遍
			final List<Collection<String>> subSkuNos=subCollection(skuNoSet);
			logger.warn("线程池数："+(poolCnt/4+1)+",sku子集合数："+subSkuNos.size());
			for(int i = 0 ; i <subSkuNos.size();i++){
				exe.execute(new UpdateThread(subSkuNos.get(i),supplier,iceAndLocalSku,totoalFailCnt));
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
			return updateStock(supplier, iceAndLocalSku, skuNoSet);
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
	 * @param iceAndLocalSku key是ice的skuNo,value是本地skuId
	 * @param skuNoSet
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 * @throws ApiException
	 */
	private int updateStock(String supplier,
			Map<String, String> iceAndLocalSku,
			Collection<String> skuNoSet) throws ServiceException, Exception,
			ApiException {
		//拉库存
		Map<String, Integer> supplierStock=grabStock(skuNoSet);
		logger.warn("拉取库存完毕,supplier Stock："+supplierStock.size());
		Set<String> iceSkuSet=iceAndLocalSku.keySet();
		Map<String, Integer> iceStock=new HashMap<String, Integer>(supplierStock.size());
		for (String iceSKU : iceSkuSet) {
			String skuId=iceAndLocalSku.get(iceSKU);//本地skuId
			Integer stock=supplierStock.get(skuId);//本地skuId库存
			if(stock==null){
				stock=0;
			}
			iceStock.put(iceSKU, stock);//供应商skuId
		}
		/*Set<String> skuSet=supplierStock.keySet();
		for (Iterator<String> iterator = skuSet.iterator(); iterator.hasNext();) {
			String skuId = iterator.next();
			Integer stock=supplierStock.get(skuId);//库存
			String skuNo=skuRelation4iceAndSupplier.get(skuId);//skuNo
			if(skuNo!=null)
				iceStock.put(skuNo, stock);
		}*/
		
		OpenApiServantPrx servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
		int failCount=0;
		Iterator<Entry<String, Integer>> iter=iceStock.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Integer> entry = iter.next();
			//logger.warn("更新库存ice sku,stock="+entry.getKey()+":"+ entry.getValue());
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
		/*if(supplierStock!=null && supplierStock.size()>0){
		}*/
		return failCount;
	}
	
	class UpdateThread extends Thread{

		private Collection<String> skuNos;
		private Map<String, String> iceAndLocalSku;
		private String supplier;
		private List<Integer> totoalFailCnt;
		/**
		 * @param iceAndLocalSku 
		 * @param totoalFailCnt 
		 * @param collection
		 */
		public UpdateThread(Collection<String> skuNos,String supplier, Map<String, String> iceAndLocalSku, List<Integer> totoalFailCnt ) {
			this.skuNos=skuNos;
			this.supplier=supplier;
			this.iceAndLocalSku=iceAndLocalSku;
			this.totoalFailCnt=totoalFailCnt;
		}
		@Override
		public void run() {
			int size=skuNos.size();
			try {
				logger.warn(Thread.currentThread().getName()+"处理开始，数："+size);
				int failCnt = updateStock(supplier, iceAndLocalSku,
						skuNos);
				totoalFailCnt.add(failCnt);
				logger.warn(Thread.currentThread().getName()+"完成，失败数:"+failCnt);
			} catch (Exception e) {
				logger.warn(Thread.currentThread().getName()+"处理出错",e);
			}
		}
		
	}
}
