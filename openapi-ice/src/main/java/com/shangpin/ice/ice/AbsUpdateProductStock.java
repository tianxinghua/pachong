package com.shangpin.ice.ice;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ShangPin.SOP.Api.ApiException;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuIce;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPage;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPageQuery;
import ShangPin.SOP.Entity.Api.Product.SopSkuIce;
import ShangPin.SOP.Entity.Api.Product.SopSkuInventoryIce;
import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetail;
import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetailPage;
import ShangPin.SOP.Entity.DTO.PurchaseOrderDetilApiDto;
import ShangPin.SOP.Entity.DTO.PurchaseOrderInfoApiDto;
import ShangPin.SOP.Entity.Where.OpenApi.Purchase.PurchaseOrderQueryDto;
import ShangPin.SOP.Servant.OpenApiServantPrx;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.common.utils.SendMail;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.dto.SkuRelationDTO;
import com.shangpin.iog.dto.SpecialSkuDTO;
import com.shangpin.iog.dto.StockUpdateDTO;
import com.shangpin.iog.dto.StockUpdateLimitDTO;
import com.shangpin.iog.service.SkuPriceService;
import com.shangpin.iog.service.SkuRelationService;
import com.shangpin.iog.service.SpecialSkuService;
import com.shangpin.iog.service.StockUpdateLimitService;
import com.shangpin.iog.service.UpdateStockService;

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
	private static LoggerUtil loggerError = LoggerUtil.getLogger("error");

	private boolean useThread=false;
	private int skuCount4Thread=100;
	private static ReentrantLock lock = new ReentrantLock();

	//  true :已供应商提供的SKU为主 不更新未提供的库存
	//  false:已尚品的SKU为主 未查到的一律赋值为0
	public static boolean supplierSkuIdMain=false;

	public static boolean ORDER= false;

	private String  splitSing="|||||";

	public static Map<String,String>   sopMarketPriceMap = null;

	private static ResourceBundle bdl = null;
	private static  String email = null;
	private static String expStartTime = null;
	private static String expEndTime = null;
	static {
		if(null==bdl){
			bdl=ResourceBundle.getBundle("openice");
		}
		email = bdl.getString("email");
		expStartTime = bdl.getString("expStartTime");
		expEndTime = bdl.getString("expEndTime");
	}
	
	private static ResourceBundle bd = null;
//	private static String spe_supplier = null;
//	private static Map<String,String> speMap = new HashMap<String,String>();	
	private static String startTime = null;
	private static String endTime = null;
	
	static {
	        try {
	            if(null==bd){
	                bd=ResourceBundle.getBundle("special");
	            }	           
	            startTime = bd.getString("startTime");
	            endTime = bd.getString("endTime");
	            
	        }catch (Exception e) {
	            loggerError.error("读取special.properties失败 "+e.toString()); 
	        }
	 }


	private  void  getSopMarketPriceMap(String supplierId) throws ServiceException {
//		try{
//			lock.lock();
//			if(null==sopMarketPriceMap){//第一次初始化
//				sopMarketPriceMap =skuPriceService.getSkuPriceMap(supplierId);
//			}
//
//		}finally{
//			if(lock.isLocked())	lock.unlock();
//		}
		sopMarketPriceMap = new HashMap<>();

	}
	//
	@Autowired
	public SkuPriceService skuPriceService;

	@Autowired
	SkuRelationService skuRelationService;

	@Autowired
	UpdateStockService updateStockService;

	@Autowired
	SpecialSkuService specialSkuService;

	@Autowired
	StockUpdateLimitService stockUpdateLimitService;


	/**
	 * 抓取供应商库存数据 
	 * @param skuNo 供应商的每个产品的唯一编号：sku
	 * @return 每个sku对应的库存数
	 * @throws ServiceException
	 */
	public abstract Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception;



	/**
	 * 更新市场价进入中间库存
	 * @param supplierPriceMap
	 * @throws ServiceException
	 */
//	public  void updateMarketPrice(Map<String,String> supplierPriceMap,String supplieId) throws ServiceException{
//		Set<Entry<String, String>> supplierPriceSet = supplierPriceMap.entrySet();
//		String supplierSku="",price = "",newPrice="",oldPrice="";
//		for(Entry<String, String> entry:supplierPriceSet){
//			supplierSku =entry.getKey();
//			price = entry.getValue();
//			newPrice= price.substring(0,price.indexOf(","));
//			oldPrice=price.substring(price.indexOf(",")+1);
//			SkuPriceDTO dto = new SkuPriceDTO();
//			dto.setMarketPrice(newPrice);
//			dto.setSkuId(supplierSku);
//			dto.setSupplierId(supplieId);
////			skuPriceService.updatePrice(dto);
//		}
//
//	}

	/**
	 * 抓取主站商品SKU信息，等待更新库存<br/>
	 * @param supplier 供应商id
	 * @param start 主站数据开始时间
	 * @param end 主站数据结束时间
	 * @param stocks 本地sku编号与ice的sku键值对
	 * @return 供应商skuNo
	 * @throws Exception
	 */
	private Collection<String> grabProduct(String supplier,String start,String end,Map<String,String> stocks) throws Exception{
		loggerInfo.info("抓取主站商品SKU信息开始");
		int pageIndex=1,pageSize=100;
		OpenApiServantPrx servant = null;
		try {
			servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
			
			setStockNotUpdateBySop(supplier,servant);
			
		} catch (Exception e) {
			loggerError.error("ICE 代理失败");
//			e.printStackTrace();
			throw e;
		}
		boolean hasNext=true;
		Set<String> skuIds = new HashSet<String>();

		//获取已有的SPSKUID
		loggerInfo.info("从关系表中获取已有的spSku"); 
		Map<String,String> map = new HashMap<>();
		if(null!=skuRelationService){
			List<SkuRelationDTO> skuRelationDTOList = skuRelationService.findListBySupplierId(supplier);

			for(SkuRelationDTO skuRelationDTO:skuRelationDTOList){
				map.put(skuRelationDTO.getSopSkuId(),null);
			}
		}
		loggerInfo.info("从关系表中获取已有的spSku结束"); 

		Date date  = new Date();
		boolean sendMail = true;
		while(hasNext){
			long startDate = System.currentTimeMillis();
			SopProductSkuPageQuery query = new SopProductSkuPageQuery(start,end,pageIndex,pageSize);
			List<SopProductSkuIce> skus = null;

			//如果异常次数超过5次就跳出
			for(int i=0;i<5;i++){
				startDate = System.currentTimeMillis();
				try {
					SopProductSkuPage products = servant.FindCommodityInfoPage(supplier, query);
					long fetchTime = System.currentTimeMillis() - startDate;
					logger.warn("通过openAPI 获取第 "+ pageIndex +"页产品信息，信息耗时" + fetchTime);
					loggerInfo.info("通过openAPI 获取第 "+ pageIndex +"页产品信息，信息耗时" + fetchTime);
					if(fetchTime>10000&&sendMail){
						try {
							SendMail.sendGroupMail("smtp.shangpin.com", "chengxu@shangpin.com",
									"shangpin001", email, "库存更新OPENICE超时",
									"通过openAPI获取供应商"+supplier+"一页产品信息耗时" + fetchTime,
									"text/html;charset=utf-8");
							sendMail = false;
						} catch (Exception e) {
							
						}
					}
					skus = products.SopProductSkuIces;
					if(skus!=null){
						i=5;
					}
				} catch (ApiException e1) {
					loggerError.error("openAPI 获取产品信息出错 -ApiException -  "+e1.Message);
				}  catch (Exception e1) {
					logger.error("openAPI 获取产品信息出错",e1);
					loggerError.error("openAPI 获取产品信息出错"+e1.getMessage());
				}
			}


			for (SopProductSkuIce sku : skus) {
				List<SopSkuIce> skuIces = sku.SopSkuIces;
				for (SopSkuIce ice : skuIces) {
					if(null!=ice.SkuNo&&!"".equals(ice.SkuNo)&&null!=ice.SupplierSkuNo&&!"".equals(ice.SupplierSkuNo)){
						if(1!=ice.IsDeleted){
							if (null!=skuRelationService&&!map.containsKey(ice.SkuNo)){ //海外库保留尚品SKU和供货商SKU对照关系
								try {
									SkuRelationDTO skuRelationDto = skuRelationService.getSkuRelationBySupplierIdAndSkuId(supplier, ice.SkuNo);
									if(skuRelationDto!=null){
										skuRelationDto.setSopSkuId(ice.SkuNo);
										skuRelationDto.setSupplierSkuId(ice.SupplierSkuNo); 
										skuRelationService.updateSupplierSkuNo(skuRelationDto);
									}else{
										SkuRelationDTO skuRelationDTO = new SkuRelationDTO();
										skuRelationDTO.setSupplierId(supplier);
										skuRelationDTO.setSupplierSkuId(ice.SupplierSkuNo);
										skuRelationDTO.setSopSkuId(ice.SkuNo);
										skuRelationDTO.setCreateTime(date);
										try {
											long startDateSave = System.currentTimeMillis();
											skuRelationService.saveSkuRelateion(skuRelationDTO);
											logger.warn("保存SKU对应关系耗时 " + (System.currentTimeMillis() - startDate));
											loggerInfo.info("保存SKU对应关系耗时 " + (System.currentTimeMillis() - startDate));
										} catch (ServiceException e) {
											logger.error(skuRelationDTO.toString() + "保存失败");
										}
									}
								} catch (Exception e2) {
									logger.error(ice.toString() + "更新失败");
								}
							}
							skuIds.add(ice.SupplierSkuNo);
							stocks.put(ice.SupplierSkuNo,ice.SkuNo);
						}
					}

				}
			}
			pageIndex++;
			hasNext=(pageSize==skus.size());

		}
		logger.warn("获取icesku 结束");

		return skuIds;
	}



	/**
	 * 更新主站库存
	 * @param supplier 供应商id
	 * @param start 主站产品数据时间开始 yyyy-MM-dd HH:mm
	 * @param end 主站产品数据时间结束 yyyy
	 *            +   -MM-dd HH:mm
	 * @return 更新失败数
	 * @throws Exception
	 */
	public int updateProductStock(final String supplier,String start,String end) throws Exception{
		loggerInfo.info("进入更新库存程序");
		//初始化 sopMarketPriceMap
		getSopMarketPriceMap(supplier);

		//ice的skuid与本地库拉到的skuId的关系，value是ice中skuId,key是本地中的skuId

		final Map<String,String> localAndIceSku=new HashMap<String, String>();
		final Map<String,String> sopSkuAndSupplierSku =new HashMap<>();
		final Collection<String> skuNoSet=grabProduct(supplier, start, end,localAndIceSku);
		if(skuNoSet.size()==0){

		}
		loggerInfo.info("sku总数："+skuNoSet.size());
		//logger.warn("需要更新ice,supplier sku关系是："+JSON.serialize(localAndIceSku));
		final List<Integer> totoalFailCnt=Collections.synchronizedList(new ArrayList<Integer>());		
		
		//将供货商添加到UPDATE_STOCK表中
		saveStockUpdateDTO(supplier);
		
		if(useThread){
			int poolCnt=skuNoSet.size()/getSkuCount4Thread();
			ExecutorService exe=Executors.newFixedThreadPool(poolCnt/4+1);//相当于跑4遍
			final List<Collection<String>> subSkuNos=subCollection(skuNoSet);
			logger.warn("线程池数："+(poolCnt/4+1)+",sku子集合数："+subSkuNos.size());
			for(int i = 0 ; i <subSkuNos.size();i++){
				Map<String,String> sopPriceMap = new HashMap<>();
				exe.execute(new UpdateThread(subSkuNos.get(i),supplier,localAndIceSku,totoalFailCnt,sopPriceMap));
			}
			exe.shutdown();
			while (!exe.awaitTermination(60, TimeUnit.SECONDS)) {

			}			
			boolean isOk = false;
			for(int k=0;k<totoalFailCnt.size();k++){				
				if(totoalFailCnt.get(k) == 0){
					loggerInfo.info("---------------第==="+k+"===组更新成功------------"); 
					isOk = true;
					break;
				}
			}	
			loggerInfo.info("isOk============="+isOk); 
			if(isOk){//多线程更新库存,有一个更新成功,则视为更新库存成功.
				this.updateStockTimeAndState(supplier);
			}
			
			int fct=0;
			for(int k=0;k<totoalFailCnt.size();k++){
				fct+=totoalFailCnt.get(k);				
			}
			loggerInfo.info("更新库存失败的数量==========="+fct);
			return fct;
		}else{
			Map<String,String> sopPriceMap = new HashMap<>();
			int i= updateStock(supplier, localAndIceSku, skuNoSet,sopPriceMap);
			loggerInfo.info("更新库存失败的数量==========="+i);			
			if(i>=0){//待更新的库存失败数小于0时，不更新
				this.updateStockTimeAndState(supplier);
			}

			return i;
		}
	}

	/**
	 * 更新库存时间
	 * @param supplier
	 */
	private void updateStockTime(String supplier){
		try {
//			if(null!=updateStockService){
//				updateStockService.updateTime(supplier);
				loggerInfo.info("=========="+supplier+"开始更新库存时间========"); 
				StockUpdateDTO stockUpdateDTO = new StockUpdateDTO();
				stockUpdateDTO.setSupplierId(supplier);
				stockUpdateDTO.setUpdateTime(new Date());

				updateStockService.updateStatus(stockUpdateDTO);
//			}
		} catch (Exception e) {
			loggerError.error("更新库存更新时间业务失败======"+e);
		}
	}

	private void updateStockTimeAndState(String supplier){
		try {
//			if(null!=updateStockService){
//				updateStockService.updateTime(supplier);
			loggerInfo.info("=========="+supplier+"开始更新库存时间========");
			StockUpdateDTO stockUpdateDTO = new StockUpdateDTO();
			stockUpdateDTO.setSupplierId(supplier);
			stockUpdateDTO.setUpdateTime(new Date());
			//
			stockUpdateDTO.setSpare(String.valueOf(stockUpdateDTO.getUpdateTime().getTime()));
			updateStockService.updateStatus(stockUpdateDTO);
//			}
		} catch (Exception e) {
			loggerError.error("更新库存更新时间业务失败======"+e);
		}
	}

	
	/**
	 * 将供货商添加到UPDATE_STOCK表中
	 * @param supplier
	 */
	private void saveStockUpdateDTO(String supplier){
		try {
//			if(null!=updateStockService){
//				updateStockService.updateTime(supplier);
//				loggerInfo.info("=========="+supplier+"开始更新库存时间========"); 
				StockUpdateDTO stockUpdateDTO = new StockUpdateDTO();
				stockUpdateDTO.setSupplierId(supplier);
				stockUpdateDTO.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2015-01-01 00:00:00"));
				updateStockService.saveStockUpdateDTO(stockUpdateDTO);  
//			}
		} catch (Exception e) {
			loggerError.error("添加供货商"+supplier+"到UPDATE_STOCK表时出错"); 
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
							Collection<String> skuNoSet,Map<String,String> sopPriceMap) throws ServiceException, Exception,
			ApiException {
		Map<String, Integer> iceStock = grab4Icestock(skuNoSet,localAndIceSkuId,sopPriceMap,supplier);
		int failCount = updateIceStock(supplier, iceStock,sopPriceMap);
		return failCount;
	}
	/**
	 * 更新ice的库存
	 * @param supplier 供应商
	 * @param iceStock ice的skuNo,与对应的库存关系
	 * @return 更新失败的数目
	 * @throws Exception
	 */
	private int updateIceStock(String supplier, Map<String, Integer> iceStock,Map<String,String> sopPriceMap)
			throws Exception {



		//拉取的供货商的库存集合为空时，不往下执行
		if(!this.supplierSkuIdMain && iceStock.size() ==0){
			
			return -1;
//			StockUpdateDTO stockUpdateDTO = updateStockService.findStockUpdateBySUpplierId(supplier);
//			if(null !=stockUpdateDTO && null !=stockUpdateDTO.getUpdateTime()){
//				long diff = new Date().getTime()-stockUpdateDTO.getUpdateTime().getTime();
//	    		long hours = diff / (1000 * 60 * 60);
//	    		if(hours < 5){
//	    			return -1;
//	    		}
//			}
		}else{
			//更新库存时间
			updateStockTime(supplier);
		}

		//获取允许更新的数量
		int updateTimes=0;
		int updateCount=0;
		updateTimes = handleUpdateTimes(supplier, updateTimes);

		OpenApiServantPrx servant = null;
		try {
			servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
		} catch (Exception e) {
			loggerError.error("Ice 代理失败");
//			e.printStackTrace();
			throw e;
		}
		//logger.warn("{}---更新ice--,数量：{}",Thread.currentThread().getName(),iceStock.size());
		//获取尚品库存
		Set<String> skuNoShangpinSet = iceStock.keySet();
		int skuNum = 1;
		List<String> skuNoShangpinList = new ArrayList<>();

		//更新库存
		int failCount=0;
		for(Iterator<String> itor =skuNoShangpinSet.iterator();itor.hasNext();){
			if(skuNum%100==0){
				//调用接口 查找库存
				Map<String,Integer> toUpdateIce=new HashMap<>();
				removeNoChangeStockRecord(supplier, iceStock, servant, skuNoShangpinList,toUpdateIce);

				Iterator<Entry<String, Integer>> iter=toUpdateIce.entrySet().iterator();
				loggerInfo.info("待更新的数据总和：--------"+toUpdateIce.size());
				updateCount = updateCount+toUpdateIce.size();
				failCount = updateStock(supplier, servant, failCount, iter);

				skuNoShangpinList = new ArrayList<>();
			}
			skuNoShangpinList.add(itor.next());
			skuNum++;
		}
		//排除最后一次
		Map<String,Integer> toUpdateIce=new HashMap<>();
		removeNoChangeStockRecord(supplier, iceStock, servant, skuNoShangpinList,toUpdateIce);

		Iterator<Entry<String, Integer>> iter=toUpdateIce.entrySet().iterator();
		loggerInfo.info("待更新的数据总和：--------"+toUpdateIce.size());
		updateCount = updateCount+toUpdateIce.size();
		failCount = updateStock(supplier, servant, failCount, iter);
		loggerInfo.info("更新库存 失败的数量：" + failCount);

		//更新可更新的数量
		updateUpdateTime(supplier, updateTimes, updateCount);
		return failCount;
	}

	private int updateStock(String supplier, OpenApiServantPrx servant, int failCount, Iterator<Entry<String, Integer>> iter) {
		while (iter.hasNext()) {
            Entry<String, Integer> entry = iter.next();
            Boolean result =true;
            int stock = 0;
            for(int i=0;i<2;i++){  //发生错误 允许再执行一次
                try{
                    if(entry.getValue()>=0){
                        stock = entry.getValue();
                    }
                    result = servant.UpdateStock(supplier, entry.getKey(), stock);
                    loggerInfo.info("待更新的数据：--------"+entry.getKey()+":"+stock+" ,"+ result);
                }catch(ApiException e){
                    result=false;
                    loggerError.error("更新sku错误："+entry.getKey()+":"+stock+"---"+e.Message);
                } catch(Exception e){
                    logger.error("更新sku错误："+entry.getKey()+":"+stock,e);
                    loggerError.error("更新sku错误："+entry.getKey()+":"+stock,e);
                }

                if(result){
                    i=2;
                }
            }

            if(!result){
                failCount++;
                logger.warn("更新iceSKU：{}，库存量：{}失败",entry.getKey(),stock);
            }
        }
		return failCount;
	}

	private void updateUpdateTime(String supplier, int updateTimes, int updateCount) {
		if(null!=stockUpdateLimitService){
			StockUpdateLimitDTO updateDate =new StockUpdateLimitDTO();
			updateDate.setSupplierId(supplier);

			updateDate.setUpdateTime(new Date());
			updateDate.setLimitNum(updateTimes-updateCount);
			try {
				stockUpdateLimitService.updateLimitNum(updateDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private int handleUpdateTimes(String supplier, int updateTimes) {
		if(null!=stockUpdateLimitService){
			StockUpdateLimitDTO limitDTO =  stockUpdateLimitService.findBySupplierId(supplier);
			if(null!=limitDTO){
				updateTimes = limitDTO.getLimitNum();
			}else{
				updateTimes = getUpdateTimes(supplier, updateTimes);

			}
			//发邮件或者退出不更新
			if(updateTimes<0){
				Thread t = new Thread(new StockLimitMail(supplier));
				t.start();
			}
		}
		return updateTimes;
	}

	private int getUpdateTimes(String supplier, int updateTimes) {
		//插入新的记录
		StockUpdateLimitDTO saveDto =new StockUpdateLimitDTO();
		saveDto.setSupplierId(supplier);
		saveDto.setCreateTime(DateTimeUtil.convertFormat(new Date(),"yyyy-MM-dd"));
		saveDto.setUpdateTime(new Date());
		saveDto.setLimitNum(500000);

		try {
            stockUpdateLimitService.save(saveDto);
            updateTimes = saveDto.getLimitNum();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return updateTimes;
	}

	/**
	 * 移除库存没有变化的商品 不做更新
	 * @param supplier
	 * @param iceStock
	 * @param servant
	 * @param skuNoShangpinList
	 * @param toUpdateIce
	 * @throws ApiException
	 */
	private void removeNoChangeStockRecord(String supplier, Map<String, Integer> iceStock, OpenApiServantPrx servant, List<String> skuNoShangpinList, Map<String, Integer> toUpdateIce) throws ApiException {
		if(CollectionUtils.isEmpty(skuNoShangpinList)) return ;
		SopSkuInventoryIce[] skuIceArray = null;

		for(int i=0;i<2;i++){
			try{
				skuIceArray =servant.FindStockInfo(supplier, skuNoShangpinList);
			}catch(ApiException e){
				loggerError.error("removeNoChangeStockRecord查询库存出错=======",e);
			}catch(Exception e){
				loggerError.error("removeNoChangeStockRecord查询库存出错=======",e);
			}
			if(null!=skuIceArray){
				i=2;
			}
		}

		//查找未维护库存的SKU
		if(null!=skuIceArray&&skuIceArray.length!=skuNoShangpinList.size()){
			Map<String,String> sopSkuMap = new HashMap();
			for(SopSkuInventoryIce skuIce:skuIceArray){
				sopSkuMap.put(skuIce.SkuNo,"");
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
		Date nowTime = new Date();
		loggerInfo.info("nowTime============="+com.shangpin.iog.common.utils.DateTimeUtil.convertFormat(nowTime,"yyyy-MM-dd HH:mm:ss")); 
		long theStart = 0;
		long theEnd = 0;
		if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
			loggerInfo.info("在"+startTime+"到"+endTime+"时间段内，只更新供应商库存小于尚品库存的sku"); 
			String startStr = com.shangpin.iog.common.utils.DateTimeUtil.convertFormat(nowTime, "yyyy-MM-dd")+" "+startTime;
			long start = com.shangpin.iog.common.utils.DateTimeUtil.convertFormat(startStr,"yyyy-MM-dd HH:mm:ss").getTime();
			String endString = com.shangpin.iog.common.utils.DateTimeUtil.convertFormat(nowTime, "yyyy-MM-dd")+" "+endTime;
			long end = com.shangpin.iog.common.utils.DateTimeUtil.convertFormat(endString,"yyyy-MM-dd HH:mm:ss").getTime();
			if(end < start){
				theStart = end;
				theEnd = start;
			}else{
				theStart = end - 1000*60*60*24;
				theEnd = start;
			}
		}
		if(null!=skuIceArray){
			// get update supplier date
			boolean isNeedHandle = isNeedUpdateStock(supplier);
			for(SopSkuInventoryIce skuIce:skuIceArray){
				if(iceStock.containsKey(skuIce.SkuNo)){
					loggerInfo.info("sop skuNo ：--------" + skuIce.SkuNo + " suppliersku: " + skuIce.SupplierSkuNo +" supplier quantity =" + iceStock.get(skuIce.SkuNo) + " shangpin quantity = " + skuIce.InventoryQuantity );
					if( iceStock.get(skuIce.SkuNo)!=skuIce.InventoryQuantity){
						if( nowTime.getTime() > theStart && nowTime.getTime() < theEnd){
							//全量更新
							toUpdateIce.put(skuIce.SkuNo, iceStock.get(skuIce.SkuNo));
						}else{
							//只更新小于尚品的库存
							if(iceStock.get(skuIce.SkuNo) < skuIce.InventoryQuantity){
								toUpdateIce.put(skuIce.SkuNo, iceStock.get(skuIce.SkuNo));
							}else{
							    if(isNeedHandle){
									toUpdateIce.put(skuIce.SkuNo, iceStock.get(skuIce.SkuNo));
								}else{
                                    loggerInfo.info(">>>>>>该时段，供应商库存大于尚品库存，不更新>>>>>sop skuNo: " + skuIce.SkuNo + " suppliersku: " + skuIce.SupplierSkuNo +" supplier quantity =" + iceStock.get(skuIce.SkuNo) + " shangpin quantity = " + skuIce.InventoryQuantity );
								}
							}
							
						}
					}
					
				}else{
					logger.error(" iceStock not contains  "+"sop skuNo ：--------"+skuIce.SkuNo +" suppliersku: "+ skuIce.SupplierSkuNo );
				}
			}
		}else{//查询现有库存失败 更新查找到的库存
			for(String spSku:skuNoShangpinList){
				if(iceStock.containsKey(spSku)){
					loggerError.error("ICE服务查询库存失败的记录 skuNO="+spSku );
					toUpdateIce.put(spSku, iceStock.get(spSku));

				}
			}
		}

	}

	/**
	 * 超过两个小时未更新的 可以更新库存
	 * @param supplier
	 * @return
     */
	private boolean isNeedUpdateStock(String supplier) {
		boolean result = false;

		try {
            StockUpdateDTO stockUpdateDTO = updateStockService.findStockUpdateBySUpplierId(supplier);
			if(null!=stockUpdateDTO){
				Date now  = new Date();
				if(StringUtils.isNotBlank(stockUpdateDTO.getSpare())){

					Long  updateStockTime = Long.valueOf(stockUpdateDTO.getSpare());
					if(now.getTime() - updateStockTime > 1000*60*60*2){
						loggerInfo.info("########供应商已超过2小时没更新库存，现在全量更新。#########");
						result = true;
					}
				}

			}


        } catch (SQLException e) {
            e.printStackTrace();
        }
		return result;
	}


	/**
	 * 拉取供应商库存，并返回ice中对应的sku的库存
	 * @param skuNos 供应商sku编号，从ice中获取到的
	 * @param localAndIceSkuId 供应商sku编号与icesku编号的关系
	 * @return 供应商sku对应的icesku编号的库存，key是ice的sku编号，值是库存
	 */
	private Map<String, Integer> grab4Icestock(Collection<String> skuNos,Map<String, String> localAndIceSkuId,
											   Map<String,String> sopPriceMap, final String supplierId) {
		Map<String, Integer> iceStock=new HashMap<>();
		Map<String,String> map = null;
		try {
			Map<String, String> supplierStock= null;  //
			try {
				supplierStock = grabStock(skuNos);
				
				//根据supplierId获取预售的sku集合
				map = specialSkuService.findListSkuBySupplierId(supplierId);
				
				if(null == supplierStock || supplierStock.size()==0){
					loggerError.error("=======抓取供货商信息返回的supplierStock为null=========");
					return iceStock;
//					StockUpdateDTO stockUpdateDTO = updateStockService.findStockUpdateBySUpplierId(supplierId);
//					if(null !=stockUpdateDTO && null !=stockUpdateDTO.getUpdateTime()){
//						long diff = new Date().getTime()-stockUpdateDTO.getUpdateTime().getTime();
//			    		long hours = diff / (1000 * 60 * 60);
//			    		//待更新的库存为0时查询超时时间，如果超时时间大于某一个特定值，则继续往下执行，避免超卖
//			    		if(hours < 5){
//			    			return iceStock;
//			    		}else{
//			    			loggerError.error("该供货商出错已经超过"+hours+"小时，所有库存将会被置为0，避免超卖");
//			    		}
//					}					
				}else{//判断supplierStock的值是否全为0
					if(!this.supplierSkuIdMain){
						boolean isNUll = true;
						for (Map.Entry<String, String> entry : supplierStock
								.entrySet()) {
							if(org.apache.commons.lang.StringUtils.isNotBlank(entry.getValue()) && !"0".equals(entry.getValue())){
								isNUll = false;
								break;
							}
						}

						if(isNUll){//supplierStock的值全为0,则返回空的map
							loggerError.error("========抓取供货商那边返回的map里的所有的value都是0========");
							return iceStock;
//							StockUpdateDTO stockUpdateDTO = updateStockService.findStockUpdateBySUpplierId(supplierId);
//							if(null !=stockUpdateDTO && null !=stockUpdateDTO.getUpdateTime()){
//								long diff = new Date().getTime()-stockUpdateDTO.getUpdateTime().getTime();
//					    		long hours = diff / (1000 * 60 * 60);
//					    		//待更新的库存为0时查询超时时间，如果超时时间大于某一个特定值，则继续往下执行，避免超卖
//					    		if(hours < 5){
//					    			return iceStock;
//					    		}else{
//					    			loggerError.error("该供货商出错已经超过"+hours+"小时，所有库存将会被置为0，避免超卖");
//					    		}
//							}	
						}
					}					
				}
			} catch (Exception e) {    //获取库存信息时失败
				loggerError.error("获取库存信息时发生异常"+e.getMessage());
				return iceStock;
			}

			int stockResult=0;
			//获取采购单信息   key为尚品的SKU
			Map<String,Integer> sopPurchaseMap = new HashMap<>();
			if(!ORDER){
//				sopPurchaseMap = this.getSopPuchase(supplierId);
			}

			String result = "",stockTemp="",priceResult="";
			boolean sendMail=true;
//			loggerInfo.info("供货商skuid和sop skuid关系map==========="+localAndIceSkuId.toString());
			String iceSku="";
			for (String skuNo : skuNos) {
				if(map.size()>0){
					if(map.containsKey(skuNo)){
						continue;
					}
				}
				stockTemp ="";
				result =  supplierStock.get(skuNo);
				if(!StringUtils.isEmpty(result)){
					if(result.indexOf("|")>=0){//有价格变动的
						stockTemp=  result.substring(0,result.indexOf("|"));
						if(StringUtils.isEmpty(stockTemp)){
							stockResult=0;
						}else{
							try {
								stockResult = Integer.valueOf(stockTemp);
							} catch (NumberFormatException e) {
								stockResult=0;
							}
						}
						priceResult=result.substring(result.indexOf("|")+1);

						sopPriceMap.put(skuNo,priceResult) ;
					}else{ //无价格变动

						try {
							stockResult = (new BigDecimal(result)).intValue();
						} catch (NumberFormatException e) {
							stockResult=0;
						}
					}


				}else{
					stockResult=0;
				}

				if(stockResult>0){ //判断是否发邮件
					sendMail = false;
				}

				iceSku=localAndIceSkuId.get(skuNo);//根据供货商的sku 获取尚品的SKU

				if(!ORDER){

					if(sopPurchaseMap.containsKey(iceSku)){
						
						loggerInfo.info("sku ：" + skuNo +"原库存："+stockResult);
						stockResult =  stockResult - sopPurchaseMap.get(iceSku);
						loggerInfo.info("sku ：" + skuNo +" 采购单含有数量 : " + sopPurchaseMap.get(iceSku)+" 最终库存 ：" + stockResult);
						if(stockResult<0) stockResult=0;

					}
				}


				if(this.supplierSkuIdMain){  // 已供应商提供的SKU为主 不更新未提供的库存

					if(supplierStock.containsKey(skuNo)){
						iceStock.put(iceSku, stockResult);
					}

				}else{

					if(!StringUtils.isEmpty(iceSku))	iceStock.put(iceSku, stockResult);

				}

				//转化 把SOP SKUID 和 SUPPLIER SKUID 都加入 以便换算
				if(sopPriceMap.containsKey(skuNo)){
					sopPriceMap.put(iceSku,skuNo+splitSing+sopPriceMap.get(skuNo));
					sopPriceMap.remove(skuNo);
				}

			}
			if(sendMail){ //发送邮件

				Thread t = new Thread(new MailThread(supplierId));
				t.start();
			}

		} catch (Exception e1) {
			logger.error("抓取库存失败:", e1);
		}
		loggerInfo.info("iceStock size =" + iceStock.size());
		return iceStock;
	}






	/**
	 * 通过获取采购单，得到每个供货商SKUID对应的未处理的采购单
	 * @param supplierId
	 * @return
	 */

	private Map<String,Integer> getSopPuchase(String supplierId) throws  Exception{


		int pageIndex=1,pageSize=20;
		OpenApiServantPrx servant = null;
		try {
			servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
		} catch (Exception e) {
			loggerError.error("Ice 代理失败");
//			e.printStackTrace();
			throw e;

		}
		boolean hasNext=true;
		logger.warn("获取ice采购单 开始");
		Set<String> skuIds = new HashSet<String>();
		Map<String,Integer> sopPurchaseMap = new HashMap<>();
		String supplierSkuNo = "";

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date endDate = new Date();
		String endTime = format.format(endDate);

		String startTime = format.format(getAppointDayFromSpecifiedDay(endDate, -10, "D"));
		List<java.lang.Integer> statusList = new ArrayList<>();
		statusList.add(1);
		statusList.add(2);
		while(hasNext){

			boolean fetchSuccess=true;
			PurchaseOrderInfoApiDto purchaseOrderInfoApiDto = null;
			for(int i=0;i<2;i++){  //允许调用失败后，再次调用一次
				try {

					PurchaseOrderQueryDto orderQueryDto = new PurchaseOrderQueryDto(startTime,endTime,statusList
							,pageIndex,pageSize);

					purchaseOrderInfoApiDto = servant.FindPurchaseOrderDetailCountPaged(supplierId, orderQueryDto);
					if(null==purchaseOrderInfoApiDto){
						fetchSuccess=false;
					}
				} catch (ApiException e) {
					fetchSuccess=false;
					loggerError.error("获取采购单失败"+e.Message);
				}  catch (Exception e) {
					fetchSuccess=false;
					loggerError.error("获取采购单失败",e);
				}
				if(fetchSuccess){
					i=2;
				}else{
					loggerError.error("获取采购单失败");
				}
			}
			List<PurchaseOrderDetilApiDto>  detilApiDtos = null;
			if(null!=purchaseOrderInfoApiDto){
				detilApiDtos =  purchaseOrderInfoApiDto.PurchaseOrderDetailList;
				for (PurchaseOrderDetilApiDto orderDetail : detilApiDtos) {
					sopPurchaseMap.put(orderDetail.SkuNo,orderDetail.Count);
				}
			}else{
				loggerError.error("两次获取采购单均失败");
				throw new ServiceMessageException("获取采购单信息失败");
//				return sopPurchaseMap;
			}


			pageIndex++;
			hasNext=(pageSize==detilApiDtos.size());

		}
		logger.warn("获取ice采购单 结束");
		loggerInfo.info("获取采购单数量："+sopPurchaseMap.size());
		return sopPurchaseMap;


	}

	/**
	 * 获取采购异常的商品  存入不再更新库存的记录表中
	 * @param supplierId
	 * @param servant
     */
	private void setStockNotUpdateBySop(String supplierId,OpenApiServantPrx servant){

		loggerInfo.info("获取采购异常的商品开始"); 
		List<PurchaseOrderDetail> orderDetails = null;
		boolean hasNext=true;
		String endTime = "";
		String startTime = "";
		if(org.apache.commons.lang.StringUtils.isNotBlank(expStartTime) && org.apache.commons.lang.StringUtils.isNotBlank(expEndTime)){
			startTime = expStartTime;
			endTime = expEndTime;
		}else{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date endDate = new Date();
			endTime = format.format(endDate);
			startTime = format.format(getAppointDayFromSpecifiedDay(endDate, -1, "D"));
		}
		
		List<java.lang.Integer> statusList = new ArrayList<>();
		statusList.add(7);
		int pageIndex=1,pageSize=20;

		while(hasNext){
			PurchaseOrderQueryDto orderQueryDto = new PurchaseOrderQueryDto(startTime,endTime,statusList
					,pageIndex,pageSize);
			try {
				PurchaseOrderDetailPage orderDetailPage=
						servant.FindPurchaseOrderDetailPaged(supplierId, orderQueryDto);
				orderDetails = orderDetailPage.PurchaseOrderDetails;
				if(null!=orderDetails){
					loggerInfo.info("采购异常数量为: " + orderDetails.size());
				}else{
					loggerInfo.info(startTime +"-到-" + endTime +" 无采购异常数据");
				}
				for (PurchaseOrderDetail orderDetail : orderDetails) {


					if(7!=orderDetail.GiveupType){
						SpecialSkuDTO spec = new SpecialSkuDTO();
						String supplierSkuNo  = orderDetail.SupplierSkuNo;
						spec.setSupplierId(supplierId);
						spec.setSupplierSkuId(supplierSkuNo);
						try {
							loggerInfo.info("采购异常的信息："+spec.toString());
							specialSkuService.saveDTO(spec);
							//直接调用库存更新  库存为0
							try {
								servant.UpdateStock(supplierId, orderDetail.SkuNo, 0);
							} catch (Exception e) {
								loggerError.error("采购异常的商品 "+ orderDetail.SkuNo + " 库存更新失败。");
							}
						} catch (ServiceMessageException e) {
							e.printStackTrace();
						}

					}else{
						logger.info("异常采购信息："+ orderDetail.SopPurchaseOrderNo + " 因质量问题采购异常，可继续更新库存");
					}

				}
			} catch (Exception e) {
				if(orderDetails==null){
					orderDetails = new ArrayList<PurchaseOrderDetail>();
				}
				loggerError.error("获取采购异常错误："+ e.getMessage());
				e.printStackTrace();
			}
			pageIndex++;
			hasNext=(pageSize==orderDetails.size());
		}
		loggerInfo.info("获取采购异常的商品结束"); 

	}

	//时间处理
	private  Date getAppointDayFromSpecifiedDay(Date today,int num,String type){
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

	//发邮件
	class MailThread implements  Runnable{

		String supplier = "";

		public MailThread(String  supplierId){
			this.supplier = supplierId;
		}

		@Override
		public void run() {
			try {
				SendMail.sendGroupMail("smtp.shangpin.com", "chengxu@shangpin.com",
						"shangpin001", email, "海外对接供货商无法链接",
						"门户编号：" + supplier + "，链接异常。请手工拉取库存",
						"text/html;charset=utf-8");
			} catch (Exception e) {
//				e.printStackTrace();
			}
		}
	}


	class StockLimitMail implements  Runnable{

		String supplier = "";

		public StockLimitMail(String  supplierId){
			this.supplier = supplierId;
		}

		@Override
		public void run() {
			try {
				SendMail.sendGroupMail("smtp.shangpin.com", "chengxu@shangpin.com",
						"shangpin001", email, "海外对接供货商更新超出限制",
						"门户编号：" + supplier + "，供货商更新超出限制",
						"text/html;charset=utf-8");
			} catch (Exception e) {
//				e.printStackTrace();
			}
		}
	}


	class UpdateThread extends Thread{
		final Logger logger = LoggerFactory.getLogger(UpdateThread.class);
		private Collection<String> skuNos;
		private Map<String, String> localAndIceSkuId;
		private String supplier;
		private List<Integer> totoalFailCnt;
		private Map<String,String> sopPriceMap;

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


		public UpdateThread(Collection<String> skuNos,String supplier, Map<String, String> localAndIceSku, List<Integer> totoalFailCnt ,
							Map<String,String> sopPriceMap	) {
			this.skuNos=skuNos;
			this.supplier=supplier;
			this.localAndIceSkuId=localAndIceSku;
			this.totoalFailCnt=totoalFailCnt;
			this.sopPriceMap=sopPriceMap;
		}
		@Override
		public void run() {
			Map<String, Integer> iceStock = grab4Icestock(skuNos,localAndIceSkuId,sopPriceMap,supplier);
			try {
				logger.warn(Thread.currentThread().getName()+"ice更新处理开始，数："+iceStock.size());
				int failCnt = updateIceStock(supplier, iceStock,sopPriceMap);
				totoalFailCnt.add(failCnt);
				logger.warn(Thread.currentThread().getName()+"ice更新完成，失败数:"+failCnt);
			} catch (Exception e) {
				logger.warn(Thread.currentThread().getName() + "处理出错", e);
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
	
//	public static void main(String[] args) {
//		Date nowTime =com.shangpin.iog.common.utils.DateTimeUtil.convertFormat("2016-05-09 05:00:00", "yyyy-MM-dd HH:mm:ss");
//		loggerInfo.info("nowTime============="+com.shangpin.iog.common.utils.DateTimeUtil.convertFormat(nowTime,"yyyy-MM-dd HH:mm:ss")); 
//		long theStart = 0;
//		long theEnd = 0;
//		if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
//			String startStr = com.shangpin.iog.common.utils.DateTimeUtil.convertFormat(nowTime, "yyyy-MM-dd")+" "+startTime;
//			long start = com.shangpin.iog.common.utils.DateTimeUtil.convertFormat(startStr,"yyyy-MM-dd HH:mm:ss").getTime();
//			String endString = com.shangpin.iog.common.utils.DateTimeUtil.convertFormat(nowTime, "yyyy-MM-dd")+" "+endTime;
//			long end = com.shangpin.iog.common.utils.DateTimeUtil.convertFormat(endString,"yyyy-MM-dd HH:mm:ss").getTime();
//			if(end < start){
//				theStart = end;
//				theEnd = start;
//			}else{
//				theStart = end - 1000*60*60*24;
//				theEnd = start;
//			}
//		}
//		System.out.println("start==="+theStart);
//		System.out.println("end==="+theEnd);
//		System.out.println("now===="+nowTime.getTime());
//		if(nowTime.getTime() > theStart && nowTime.getTime() < theEnd){
////			System.out.println("需要比较库存，只更新库存小于供应商的产品"); 
//			System.out.println("这个时间段内需要全量更新");
//		}
//	}
		
}
