package com.shangpin.sop;


import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.SkuRelationDTO;
import com.shangpin.iog.service.SkuRelationService;
import com.shangpin.iog.service.UpdateStockService;
import com.shangpin.openapi.api.sdk.client.SpClient;
import com.shangpin.openapi.api.sdk.model.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 更新主站库存的抽象类<br/>
 * 各供应商模块，只要实现{@link #grabStock(Collection) "根据sku拿库存"}就行
 * @description 
 * @author 陈小峰
 * <br/>2015年6月10日
 */
public abstract class AbsUpdateProductStock {
	static Logger logger = LoggerFactory.getLogger(AbsUpdateProductStock.class);

	private static org.apache.log4j.Logger loggerInfo = org.apache.log4j.Logger.getLogger("info");
	private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger.getLogger("error");
	private boolean useThread=false;
	private int skuCount4Thread=100;

    //  true :已供应商提供的SKU为主 不更新未提供的库存
    //  false:已尚品的SKU为主 未查到的一律赋值为0
    public static boolean supplierSkuIdMain=false;

	public  static boolean ORDER=false;


	@Autowired
	SkuRelationService skuRelationService;

	@Autowired
	UpdateStockService updateStockService;
	
	/**
	 * 抓取供应商库存数据 
	 * @param skuNo 供应商的每个产品的唯一编号：sku
	 * @return 每个sku对应的库存数
	 * @throws ServiceException 
	 */
	public abstract Map<String,Integer> grabStock(Collection<String> skuNo) throws ServiceException, Exception;
	/**
	 * 抓取主站商品SKU信息，等待更新库存<br/>
	 * @param host 服务地址
	 * @param app_key 分配给供应商的KEY
	 * @param app_secret 分配给供应商的SECRET
	 * @param start 主站数据开始时间
	 * @param end 主站数据结束时间
	 * @param stocks 本地sku编号与ice的sku键值对
	 * @return 供应商skuNo
	 * @throws Exception
	 */
	private Collection<String> grabProduct(String host,String app_key,String app_secret,String start,String end,Map<String,String> stocks) throws Exception{
		int pageIndex=1,pageSize=100;
		ApiResponse<SopProductSkuPage>  result =   null;
		SopProductSkuPageQuery   request = null;

		boolean hasNext=true;
		logger.warn("获取sku 开始");
		Set<String> skuIds = new HashSet<String>();

		while(hasNext){

			List<SopProductSku> skus = null;
			try {
				request  = new SopProductSkuPageQuery();
				request.setPageIndex(pageIndex);
				request.setPageSize(pageSize);
				request.setStartTime(start);
				request.setEndTime(end);
				Date timestamp = new Date();  //
				result =SpClient.FindCommodityByPage( host,  app_key,  app_secret,  timestamp,  request);
				SopProductSkuPage products = result.getResponse();
				skus = products.getSopProductSkuIces();
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (SopProductSku sku : skus) {
				List<SopSku> skuIces = sku.getSopSkuIces();
				for (SopSku ice : skuIces) {

					if(null!=ice.getSkuNo()&&!"".equals(ice.getSkuNo())&&null!=ice.getSupplierSkuNo()&&!"".equals(ice.getSupplierSkuNo())){
						if(1!=ice.getIsDeleted()){
							skuIds.add(ice.getSupplierSkuNo());
							stocks.put(ice.getSupplierSkuNo(),ice.getSkuNo());
						}
					}

				}
			}
			pageIndex++;
			hasNext=(pageSize==skus.size());

		}
		logger.warn("获取sku 结束");
	    loggerInfo.info("获取SKU数量为："+skuIds.size());
		return skuIds;
	}


	/**
	 * 抓取主站商品SKU信息，等待更新库存<br/>

	 * @param host 服务地址
	 * @param app_key 分配给供应商的KEY
	 * @param app_secret 分配给供应商的SECRET
	 * @param supplierId 供货商ID
	 * @param start 主站数据开始时间
	 * @param end 主站数据结束时间
	 * @param stocks 本地sku编号与ice的sku键值对
	 * @return 供应商skuNo
	 * @throws Exception
	 */
	private Collection<String> grabProduct(String host,String app_key,String app_secret,String supplierId,String start,String end,Map<String,String> stocks) throws Exception{
		int pageIndex=1,pageSize=100;
		ApiResponse<SopProductSkuPage>  result =   null;
		SopProductSkuPageQuery   request = null;

		boolean hasNext=true;
		logger.warn("获取sku 开始");
		Set<String> skuIds = new HashSet<String>();

		//获取已有的SPSKUID
		Map<String,String> map = new HashMap<>();
		if(null!=skuRelationService){
			List<SkuRelationDTO> skuRelationDTOList = skuRelationService.findListBySupplierId(supplierId);

			for(SkuRelationDTO skuRelationDTO:skuRelationDTOList){
				map.put(skuRelationDTO.getSopSkuId(),null);
			}
		}

		Date date  = new Date();
		while(hasNext){

			List<SopProductSku> skus = null;
			try {
				request  = new SopProductSkuPageQuery();
				request.setPageIndex(pageIndex);
				request.setPageSize(pageSize);
				request.setStartTime(start);
				request.setEndTime(end);
				Date timestamp = new Date();  //
				result =SpClient.FindCommodityByPage( host,  app_key,  app_secret,  timestamp,  request);
				SopProductSkuPage products = result.getResponse();
				skus = products.getSopProductSkuIces();
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (SopProductSku sku : skus) {
				List<SopSku> skuIces = sku.getSopSkuIces();
				for (SopSku ice : skuIces) {

					if (null!=skuRelationService&&!map.containsKey(ice.getSkuNo())){ //海外库保留尚品SKU和供货商SKU对照关系
						SkuRelationDTO skuRelationDTO = new SkuRelationDTO();
						skuRelationDTO.setSupplierId(supplierId);
						skuRelationDTO.setSupplierSkuId(ice.getSupplierSkuNo());
						skuRelationDTO.setSopSkuId(ice.getSkuNo());
						skuRelationDTO.setCreateTime(date);
						try {
							skuRelationService.saveSkuRelateion(skuRelationDTO);
						} catch (ServiceException e) {
							logger.error(skuRelationDTO.toString() + "保存失败");
						}
					}


					if(null!=ice.getSkuNo()&&!"".equals(ice.getSkuNo())&&null!=ice.getSupplierSkuNo()&&!"".equals(ice.getSupplierSkuNo())){
						if(1!=ice.getIsDeleted()){
							skuIds.add(ice.getSupplierSkuNo());
							stocks.put(ice.getSupplierSkuNo(),ice.getSkuNo());
						}
					}

				}
			}
			pageIndex++;
			hasNext=(pageSize==skus.size());

		}
		logger.warn("获取sku 结束");
		loggerInfo.info("获取SKU数量为："+skuIds.size());
		return skuIds;
	}
	
	/**
	 * 更新主站库存
	 * 客户端调用的接口
	 * @param host     服务地址
	 * @param app_key 提供给供应商的KEY
	 * @param app_secret 提供给供应商的SECRET
	 * @param start 主站产品数据时间开始 yyyy-MM-dd HH:mm
	 * @param end 主站产品数据时间结束 yyyy
	 *            +   -MM-dd HH:mm

	 * @return 更新失败数
	 * @throws Exception 
	 */
	public int updateProductStock(final  String host,final String app_key, final String app_secret,final String start,final String end) throws Exception{
		//ice的skuid与本地库拉到的skuId的关系，value是ice中skuId,key是本地中的skuId
		final Map<String,String> localAndIceSku=new HashMap<String, String>();
		final Collection<String> skuNoSet=grabProduct(host,app_key,app_secret, start, end,localAndIceSku);
		logger.warn("待更新库存数据总数："+skuNoSet.size());
		//logger.warn("需要更新ice,supplier sku关系是："+JSON.serialize(localAndIceSku));
		final List<Integer> totoalFailCnt=Collections.synchronizedList(new ArrayList<Integer>());
		if(useThread){
			int poolCnt=skuNoSet.size()/getSkuCount4Thread();
			ExecutorService exe=Executors.newFixedThreadPool(poolCnt/4+1);//相当于跑4遍
			final List<Collection<String>> subSkuNos=subCollection(skuNoSet);
			logger.warn("线程池数："+(poolCnt/4+1)+",sku子集合数："+subSkuNos.size());
			for(int i = 0 ; i <subSkuNos.size();i++){
				exe.execute(new UpdateThread(subSkuNos.get(i),host,app_key,app_secret,localAndIceSku,totoalFailCnt));
			}
			exe.shutdown();
			while (!exe.awaitTermination(60, TimeUnit.SECONDS)) {
				
			}
			int fct=0;
			for(int k=0;k<totoalFailCnt.size();k++){
				fct+=totoalFailCnt.get(k);
			}
			this.updateStockTime(app_key);
			return fct;
		}else{
			return updateStock(host,app_key,app_secret, localAndIceSku, skuNoSet);
		}
	}



	/**
	 * 更新主站库存
	 * 客户端调用的接口
	 * @param host     服务地址
	 * @param app_key 提供给供应商的KEY
	 * @param app_secret 提供给供应商的SECRET
	 * @param supplierId  供货商ID 主要是为了存储SKUID对应关系中的供货商ID
	 * @param start 主站产品数据时间开始 yyyy-MM-dd HH:mm
	 * @param end 主站产品数据时间结束 yyyy
	 *            +   -MM-dd HH:mm
	 *
	 * @return 更新失败数
	 * @throws Exception
	 */
	public int updateProductStock(final  String host,final String app_key, final String app_secret,final String supplierId,final String start,final String end) throws Exception{
		//ice的skuid与本地库拉到的skuId的关系，value是ice中skuId,key是本地中的skuId
		final Map<String,String> localAndIceSku=new HashMap<String, String>();
		final Collection<String> skuNoSet=grabProduct(host,app_key,app_secret,supplierId, start, end,localAndIceSku);
		logger.warn("待更新库存数据总数："+skuNoSet.size());
		//logger.warn("需要更新ice,supplier sku关系是："+JSON.serialize(localAndIceSku));
		final List<Integer> totoalFailCnt=Collections.synchronizedList(new ArrayList<Integer>());
		if(useThread){
			int poolCnt=skuNoSet.size()/getSkuCount4Thread();
			ExecutorService exe=Executors.newFixedThreadPool(poolCnt/4+1);//相当于跑4遍
			final List<Collection<String>> subSkuNos=subCollection(skuNoSet);
			logger.warn("线程池数："+(poolCnt/4+1)+",sku子集合数："+subSkuNos.size());
			for(int i = 0 ; i <subSkuNos.size();i++){
				exe.execute(new UpdateThread(subSkuNos.get(i),host,app_key,app_secret,localAndIceSku,totoalFailCnt));
			}
			exe.shutdown();
			while (!exe.awaitTermination(60, TimeUnit.SECONDS)) {

			}
			int fct=0;
			for(int k=0;k<totoalFailCnt.size();k++){
				fct+=totoalFailCnt.get(k);
			}
			this.updateStockTime(app_key);
			return fct;
		}else{
			return updateStock(host,app_key,app_secret, localAndIceSku, skuNoSet);
		}
	}

	/**
	 * @param skuNoSet
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
	 * 更新库存  ，返回失败的数量 。
	 * 其中需要转换供货商的SKUID 为尚品的SKUID ,
	 * @param host
	 * @param app_key
	 * @param app_secret
	 * @param localAndIceSkuId value是ice的skuNo,key是供应商skuId
	 * @param skuNoSet 供应商sku编号
	 * @return
	 * @throws ServiceException
	 * @throws Exception

	 */
	private int updateStock(String host,String app_key,String app_secret,
			Map<String, String> localAndIceSkuId,
			Collection<String> skuNoSet) throws ServiceException, Exception
			 {
		Map<String, Integer> iceStock = grab4Icestock(host,app_key,app_secret,skuNoSet,localAndIceSkuId);
		int failCount = updateIceStock(host,app_key,app_secret, iceStock);

		this.updateStockTime(app_key);
		return failCount;
	}
	/**
	 * 更新ice的库存
	 * @param host     服务地址
	 * @param app_key 提供给供应商的KEY
	 * @param app_secret 提供给供应商的SECRET
	 * @param iceStock  尚品skuNo与对应的库存关系
	 * @return 更新失败的数目
	 * @throws Exception
	 */
	private int updateIceStock(String host,String app_key,String app_secret, Map<String, Integer> iceStock)
			throws Exception {
		//logger.warn("{}---更新ice--,数量：{}",Thread.currentThread().getName(),iceStock.size());
		//获取尚品库存
		Set<String> skuNoShangpinSet = iceStock.keySet();
		int skuNum = 1;
		List<String> skuNoShangpinList = new ArrayList<>();
		Map<String,Integer> toUpdateIce=new HashMap<>();
		for(Iterator<String> itor =skuNoShangpinSet.iterator();itor.hasNext();){
			if(skuNum%100==0){
				//调用接口 查找库存
				removeNoChangeStockRecord(host,app_key,app_secret, iceStock, skuNoShangpinList,toUpdateIce);
				skuNoShangpinList = new ArrayList<>();
			}
			skuNoShangpinList.add(itor.next());
			skuNum++;
		}
		//排除最后一次
		removeNoChangeStockRecord(host,app_key,app_secret, iceStock, skuNoShangpinList,toUpdateIce);
		
		
		int failCount=0;
		Iterator<Entry<String, Integer>> iter=toUpdateIce.entrySet().iterator();

		logger.warn("待更新的数据总和：--------"+toUpdateIce.size());
		loggerInfo.info("待更新的数据总和：--------"+toUpdateIce.size());
		ApiResponse<Boolean>  result =null;
		StockInfo request_body = null;

		while (null!=iter&&iter.hasNext()) {

			Entry<String, Integer> entry = iter.next();


			try{
				logger.warn("待更新的数据：--------"+entry.getKey()+":"+entry.getValue());
				request_body = new StockInfo();
				request_body.setSkuNo(entry.getKey());
				request_body.setInventoryQuantity(entry.getValue()<0?0:entry.getValue());
				 result = SpClient.UpdateStock(host, app_key, app_secret, new Date(), request_body);
			}catch(Exception e){
				loggerInfo.info("更新sku错误： " +e.getMessage());
				logger.error("更新sku错误："+entry.getKey()+":"+entry.getValue(),e);
				loggerError.error("更新sku错误："+entry.getKey()+":"+entry.getValue()+" " + e.getMessage());
			}
			if(null!=result&&!result.getResponse()){
				failCount++;
				logger.warn("更新iceSKU：{}，库存量：{}失败",entry.getKey(),entry.getValue());
				loggerError.error(entry.getKey() + ":" + entry.getValue() +"更新库存失败");
			}				
		}
		loggerInfo.info("更新库存结束");
		return failCount;
	}



	/**
	 * 更新库存时间
	 * @param supplier
	 */
	private void updateStockTime(String supplier){
		try {
			if(null!=updateStockService){
				loggerInfo.info("key="+supplier+"记录更新时间开始");
				updateStockService.updateTime(supplier);
			}
		} catch (Exception e) {
			loggerError.error("更新库存更新时间业务失败");
		}
	}
	/**
	 * 移除库存没有变化的商品 不做更新
	 * 如果尚未维护库存只有商品 则默认添加查询到的商品库存
	 * @param host     服务地址
	 * @param app_key 提供给供应商的KEY
	 * @param app_secret 提供给供应商的SECRET
	 * @param iceStock
	 * @param skuNoShangpinList
	 * @param toUpdateIce 
	 * @throws ServiceException
	 */
	private void removeNoChangeStockRecord(String host,String app_key,String app_secret, Map<String, Integer> iceStock,  List<String> skuNoShangpinList, Map<String, Integer> toUpdateIce) throws ServiceException {
		if(CollectionUtils.isEmpty(skuNoShangpinList)) return ;
		SopSkuInventory[] skuArray = null;
		ApiResponse<List<SopSkuInventory>> result = null;
		Date timestamp = new Date();//时间戳
		try {
			 String[] skuNoShangpinArray = new String[skuNoShangpinList.size()];
			 result = SpClient.FindStock(host, app_key, app_secret, timestamp, (String[]) skuNoShangpinList.toArray(skuNoShangpinArray));
			 if(null==result) return ;
			 if(null==result.getResponse()) return ;
			skuArray =(SopSkuInventory[]) result.getResponse().toArray(new SopSkuInventory[0]);

		} catch (Exception e) {
			e.printStackTrace();
		}
		//查找未维护库存的SKU
        if(null!=skuArray&&skuArray.length!=skuNoShangpinList.size()){
            Map<String,String> sopSkuMap = new HashMap();
            for(SopSkuInventory skuIce:skuArray){
                sopSkuMap.put(skuIce.getSkuNo(),"");
            }
            String sopSku="";
           for(Iterator<String> itor =  skuNoShangpinList.iterator();itor.hasNext();){
               sopSku = itor.next();

                  if(!sopSkuMap.containsKey(sopSku)){
                      if(iceStock.containsKey(sopSku)){
//						  logger.warn("skuNo ：--------"+sopSku +"supplier quantity =" + iceStock.get(sopSku) + " shangpin quantity = null" );
//						  System.out.println("skuNo ：--------"+sopSku +"supplier quantity =" + iceStock.get(sopSku) + " shangpin quantity = null");
								  toUpdateIce.put(sopSku, iceStock.get(sopSku));
                      }
                  }
           }
        }

		//排除无用的库存
		if(null!=skuArray){
			for(SopSkuInventory skuIce:skuArray){
				if(iceStock.containsKey(skuIce.getSkuNo())){
					logger.warn("skuNo ：--------"+skuIce.getSkuNo()+ " supplierIdsku :" + skuIce.getSupplierSkuNo()  +" supplier quantity =" + iceStock.get(skuIce.getSkuNo()) + " shangpin quantity = "+ skuIce.getInventoryQuantity() );
					loggerInfo.info("skuNo ：--------" + skuIce.getSkuNo() + " supplierIdsku :" + skuIce.getSupplierSkuNo() + " supplier quantity =" + iceStock.get(skuIce.getSkuNo()) + " shangpin quantity = " + skuIce.getInventoryQuantity());
//				System.out.println("skuNo ：--------"+skuIce.SkuNo +"supplier quantity =" + iceStock.get(skuIce.SkuNo) + " shangpin quantity = "+ skuIce.InventoryQuantity);
					if(!iceStock.get(skuIce.getSkuNo()).toString().equals(skuIce.getInventoryQuantity())){
						toUpdateIce.put(skuIce.getSkuNo(), iceStock.get(skuIce.getSkuNo()));
					}
				} else {
					loggerError.error("  skuNo ：--------" + skuIce.getSkuNo() + " supplierIdsku :" + skuIce.getSupplierSkuNo() + " supplier quantity =" + iceStock.get(skuIce.getSkuNo()) +" not handle " );
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
	private Map<String, Integer> grab4Icestock(String host,String app_key,
											   String app_secret,Collection<String> skuNos,Map<String, String> localAndIceSkuId) {
		Map<String, Integer> iceStock=new HashMap<>();

		Map<String,Integer>  sopPurchaseMap = new HashMap<>();


		try {


			Map<String, Integer> supplierStock= null;  //
			try {
				supplierStock = grabStock(skuNos);
				if(supplierStock.size()==0){
					loggerError.error("获取库存信息时发生异常，程序退出");
					System.exit(0);
				}
			} catch (Exception e) {    //获取库存信息时失败 直接退出
				loggerError.error("获取库存信息时发生异常，程序退出");
				System.exit(0);
			}


		   //获取采购单信息
			try {
				if(!ORDER)	sopPurchaseMap = this.getPurchaseOrder(host,app_key,app_secret);
			} catch (Exception e) {
				e.printStackTrace();
			}

			for (String skuNo : skuNos) {
				Integer stock=supplierStock.get(skuNo);

				if(!ORDER){
					if(sopPurchaseMap.containsKey(skuNo)){
						if(stock==null)
							stock=0;
						logger.error("供货商库存：" + stock + "采购单："+skuNo +" ; 数量 : " + sopPurchaseMap.get(skuNo));
						loggerInfo.info("供货商库存：" + stock +"采购单：" + skuNo + " ; 数量 : " + sopPurchaseMap.get(skuNo));
						stock =  stock - sopPurchaseMap.get(skuNo);
						logger.error("最终库存 ：" + stock);
						loggerInfo.info("最终库存 ：" + stock);
						if(stock<0) stock=0;

					}
				}

				String iceSku=localAndIceSkuId.get(skuNo);
				if(this.supplierSkuIdMain){  // 已供应商提供的SKU为主 不更新未提供的库存
					if(supplierStock.containsKey(skuNo)){
						if(null!=stock){
							iceStock.put(iceSku, stock);
						}
					}


				}else{
					if(stock==null)
						stock=0;
					if(!StringUtils.isEmpty(iceSku))
						iceStock.put(iceSku, stock);
				}

			}


        } catch (Exception e1) {
			logger.error("抓取库存失败:", e1);
		}

		return iceStock;
	}

	class UpdateThread extends Thread{
		final Logger logger = LoggerFactory.getLogger(UpdateThread.class);
		private Collection<String> skuNos;
		private String supplierId;//
		private String host;//服务地址
		private String app_key;//
		private String app_secret;//
		private Map<String, String> localAndIceSkuId;
		private List<Integer> totoalFailCnt;
		/**
		 * @param localAndIceSku 所有主站skuId,供应商skuNo关系,key：供应商skuId,value:ice的skuNo
		 * @param totoalFailCnt 
		 * @param skuNos 供应商skuNo集合
		 */
		public UpdateThread(Collection<String> skuNos,String host,String app_key,String app_secret, Map<String, String> localAndIceSku, List<Integer> totoalFailCnt ) {
			this.skuNos=skuNos;
			this.host=host;
			this.app_key=app_key;
			this.app_secret = app_secret;
			this.localAndIceSkuId=localAndIceSku;
			this.totoalFailCnt=totoalFailCnt;
		}
		@Override
		public void run() {
			Map<String, Integer> iceStock = grab4Icestock(host,app_key,app_secret,skuNos,localAndIceSkuId);
			try {
				logger.warn(Thread.currentThread().getName()+"ice更新处理开始，数："+iceStock.size());
				int failCnt = updateIceStock(host,app_key,app_secret, iceStock);
				totoalFailCnt.add(failCnt);
				logger.warn(Thread.currentThread().getName()+"ice更新完成，失败数:"+failCnt);
			} catch (Exception e) {
				logger.warn(Thread.currentThread().getName() + "处理出错", e);
			}
		}
		
	}



	public static Date getAppointDayFromSpecifiedDay(Date today,int num,String type){
		Calendar c   =   Calendar.getInstance();
		c.setTime(today);

		if("Y".equals(type)){
			c.add(Calendar.YEAR, num);
		}else if("M".equals(type)){
			c.add(Calendar.MONTH, num);
		}else if(null==type||"".equals(type)||"D".equals(type))
			c.add(Calendar.DAY_OF_YEAR, num);
		else if("H".equals(type))
			c.add(Calendar.HOUR_OF_DAY,num);
		else if("m".equals(type))
			c.add(Calendar.MINUTE,num);
		else if("S".equals(type))
			c.add(Calendar.SECOND,num);
		return c.getTime();
	}

	/**
	 * 获取采购单
	 * @param host
	 * @param app_key
	 * @param app_secret
	 * @return
	 * @throws Exception
	 */
	public Map<String,Integer> getPurchaseOrder(String host,String app_key,
												String app_secret) throws Exception{
		int pageIndex=1,pageSize=20;
		boolean hasNext=true;
		logger.warn("获取SOP采购单 开始");
		Set<String> skuIds = new HashSet<String>();
		Map<String,Integer>  purchaseOrderMap = new HashMap<>();
		String supplierSkuNo = "";
		String startTime="" , endTime="";

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date endDate = new Date();
		endTime = format.format(endDate);

		startTime = format.format(this.getAppointDayFromSpecifiedDay(endDate,-2,"M"));

		List<Integer> detailStatus = new ArrayList<>();
		ApiResponse<PurchaseOrderDetailPage> response = null;
		while(hasNext){
			List<PurchaseOrderDetail> orderDetails = null;
			try {
				PurchaseOrderQueryDto queryDto = new PurchaseOrderQueryDto();

				queryDto.setUpdateTimeBegin(startTime);
				queryDto.setUpdateTimeEnd(endTime);
				queryDto.setPageIndex(pageIndex);
				queryDto.setPageSize(pageSize);
				detailStatus.add(1);
				detailStatus.add(2);
				queryDto.setDetailStatus(detailStatus);


				response =  SpClient.FindPOrderByPage(host, app_key, app_secret, new Date(), queryDto);

				PurchaseOrderDetailPage orderDetailPage = response.getResponse();

				orderDetails=orderDetailPage.getPurchaseOrderDetails();

				for (PurchaseOrderDetail orderDetail : orderDetails) {
					supplierSkuNo  = orderDetail.getSupplierSkuNo();
					if(purchaseOrderMap.containsKey(supplierSkuNo)){

						purchaseOrderMap.put(supplierSkuNo, purchaseOrderMap.get(supplierSkuNo)+ 1);
						;
					}else{

						purchaseOrderMap.put(supplierSkuNo,1);
					}


				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			pageIndex++;
			hasNext=(pageSize==orderDetails.size());

		}

		logger.warn("获取SOP采购单 结束");

		return purchaseOrderMap;

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
