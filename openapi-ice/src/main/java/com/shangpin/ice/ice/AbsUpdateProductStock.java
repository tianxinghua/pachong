package com.shangpin.ice.ice;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import IceUtilInternal.StringUtil;
import ShangPin.SOP.Entity.Api.Product.*;
import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetail;
import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetailPage;
import ShangPin.SOP.Entity.Where.OpenApi.Purchase.PurchaseOrderQueryDto;

import com.shangpin.iog.common.utils.SendMail;
import com.shangpin.iog.dto.SkuRelationDTO;
import com.shangpin.iog.dto.StockUpdateDTO;
import com.shangpin.iog.service.SkuPriceService;
import com.shangpin.iog.service.SkuRelationService;
import com.shangpin.iog.service.UpdateStockService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import ShangPin.SOP.Api.ApiException;
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

	private static org.apache.log4j.Logger loggerInfo = org.apache.log4j.Logger.getLogger("info");
	private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger.getLogger("error");

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
	static {
		if(null==bdl){
			bdl=ResourceBundle.getBundle("openice");
		}
		email = bdl.getString("email");
	}


	private  void  getSopMarketPriceMap(String supplierId) throws ServiceException {
		//TODO 测试
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



	/**
	 * 抓取供应商库存数据 
	 * @param skuNo 供应商的每个产品的唯一编号：sku
	 * @see #grabProduct(String, String, String) 抓取主站SKU
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
		OpenApiServantPrx servant = null;
		try {
			servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
		} catch (Exception e) {
			loggerError.error("ICE 代理失败");
			e.printStackTrace();
			throw e;
		}
		boolean hasNext=true;
		logger.warn("获取icesku 开始");
		Set<String> skuIds = new HashSet<String>();

		//获取已有的SPSKUID
		Map<String,String> map = new HashMap<>();
		if(null!=skuRelationService){
			List<SkuRelationDTO> skuRelationDTOList = skuRelationService.findListBySupplierId(supplier);

			for(SkuRelationDTO skuRelationDTO:skuRelationDTOList){
				map.put(skuRelationDTO.getSopSkuId(),null);
			}
		}

		Date date  = new Date();
		while(hasNext){
			List<SopProductSkuIce> skus = null;
			try {
				SopProductSkuPageQuery query = new SopProductSkuPageQuery(start,end,pageIndex,pageSize);
				SopProductSkuPage products = servant.FindCommodityInfoPage(supplier, query);
				skus = products.SopProductSkuIces;
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (SopProductSkuIce sku : skus) {
				List<SopSkuIce> skuIces = sku.SopSkuIces;
				for (SopSkuIce ice : skuIces) {


					if (null!=skuRelationService&&!map.containsKey(ice.SkuNo)){ //海外库保留尚品SKU和供货商SKU对照关系
						SkuRelationDTO skuRelationDTO = new SkuRelationDTO();
						skuRelationDTO.setSupplierId(supplier);
						skuRelationDTO.setSupplierSkuId(ice.SupplierSkuNo);
						skuRelationDTO.setSopSkuId(ice.SkuNo);
						skuRelationDTO.setCreateTime(date);
						try {
							skuRelationService.saveSkuRelateion(skuRelationDTO);
						} catch (ServiceException e) {
						    logger.error(skuRelationDTO.toString() + "保存失败");
						}
					}

					if(null!=ice.SkuNo&&!"".equals(ice.SkuNo)&&null!=ice.SupplierSkuNo&&!"".equals(ice.SupplierSkuNo)){
						if(1!=ice.IsDeleted){
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
	 * @see {@link #grabStock(List) 抓取库存 }
	 * @return 更新失败数
	 * @throws Exception
	 */
	public int updateProductStock(final String supplier,String start,String end) throws Exception{
		//初始化 sopMarketPriceMap
		getSopMarketPriceMap(supplier);

		//ice的skuid与本地库拉到的skuId的关系，value是ice中skuId,key是本地中的skuId

		final Map<String,String> localAndIceSku=new HashMap<String, String>();
		final Map<String,String> sopSkuAndSupplierSku =new HashMap<>();
		final Collection<String> skuNoSet=grabProduct(supplier, start, end,localAndIceSku);
		logger.warn("待更新库存数据总数："+skuNoSet.size());
		//logger.warn("需要更新ice,supplier sku关系是："+JSON.serialize(localAndIceSku));
		final List<Integer> totoalFailCnt=Collections.synchronizedList(new ArrayList<Integer>());
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
			int fct=0;
			for(int k=0;k<totoalFailCnt.size();k++){
				fct+=totoalFailCnt.get(k);
			}
			this.updateStockTime(supplier);
			return fct;
		}else{
			Map<String,String> sopPriceMap = new HashMap<>();
			int i= updateStock(supplier, localAndIceSku, skuNoSet,sopPriceMap);

			this.updateStockTime(supplier);
			return i;
		}
	}

	/**
	 * 更新库存时间
	 * @param supplier
	 */
	private void updateStockTime(String supplier){
		try {
			if(null!=updateStockService){
//				updateStockService.updateTime(supplier);
				StockUpdateDTO stockUpdateDTO = new StockUpdateDTO();
				stockUpdateDTO.setSupplierId(supplier);
				stockUpdateDTO.setUpdateTime(new Date());

				updateStockService.saveOrUpdateDTO(stockUpdateDTO);
			}
		} catch (Exception e) {
			loggerError.error("更新库存更新时间业务失败");
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
		OpenApiServantPrx servant = null;
		try {
			servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
		} catch (Exception e) {
			loggerError.error("Ice 代理失败");
			e.printStackTrace();
			throw e;
		}
		//logger.warn("{}---更新ice--,数量：{}",Thread.currentThread().getName(),iceStock.size());
		//获取尚品库存
		Set<String> skuNoShangpinSet = iceStock.keySet();
		int skuNum = 1;
		List<String> skuNoShangpinList = new ArrayList<>();
		Map<String,Integer> toUpdateIce=new HashMap<>();
		for(Iterator<String> itor =skuNoShangpinSet.iterator();itor.hasNext();){
			if(skuNum%100==0){
				//调用接口 查找库存
				removeNoChangeStockRecord(supplier, iceStock, servant, skuNoShangpinList,toUpdateIce);

				skuNoShangpinList = new ArrayList<>();
			}
			skuNoShangpinList.add(itor.next());
			skuNum++;
		}
		//排除最后一次
		removeNoChangeStockRecord(supplier, iceStock, servant, skuNoShangpinList,toUpdateIce);




		//更新库存
		int failCount=0;
		Iterator<Entry<String, Integer>> iter=toUpdateIce.entrySet().iterator();
		loggerInfo.info("待更新的数据总和：--------"+toUpdateIce.size());
		logger.warn("待更新的数据总和：--------"+toUpdateIce.size());
		while (iter.hasNext()) {
			Entry<String, Integer> entry = iter.next();
			Boolean result =true;
			try{
				logger.warn("待更新的数据：--------"+entry.getKey()+":"+entry.getValue());
				result = servant.UpdateStock(supplier, entry.getKey(), entry.getValue());
				loggerInfo.info("待更新的数据：--------"+entry.getKey()+":"+entry.getValue()+" ,"+ result);
			}catch(Exception e){
				result=false;
				logger.error("更新sku错误："+entry.getKey()+":"+entry.getValue(),e);
				loggerError.error("更新sku错误："+entry.getKey()+":"+entry.getValue(),e);
			}
			if(!result){
				failCount++;
				logger.warn("更新iceSKU：{}，库存量：{}失败",entry.getKey(),entry.getValue());
			}
		}
		loggerInfo.info("更新库存 失败的数量：" + failCount);
		loggerError.error("更新库存 失败的数量：" + failCount);
		return failCount;
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
		SopSkuInventoryIce[] skuIceArray =servant.FindStockInfo(supplier, skuNoShangpinList);

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

		for(SopSkuInventoryIce skuIce:skuIceArray){
	        if(iceStock.containsKey(skuIce.SkuNo)){
				logger.warn("sop skuNo ：--------"+skuIce.SkuNo +" suppliersku: "+ skuIce.SupplierSkuNo +"  supplier quantity =" + iceStock.get(skuIce.SkuNo) + " shangpin quantity = "+ skuIce.InventoryQuantity );
				loggerInfo.info("sop skuNo ：--------" + skuIce.SkuNo + " suppliersku: " + skuIce.SupplierSkuNo +" supplier quantity =" + iceStock.get(skuIce.SkuNo) + " shangpin quantity = " + skuIce.InventoryQuantity );
				System.out.println("sop skuNo ：--------"+skuIce.SkuNo  +" suppliersku: "+ skuIce.SupplierSkuNo +" supplier quantity =" + iceStock.get(skuIce.SkuNo) + " shangpin quantity = "+ skuIce.InventoryQuantity);
	             if( iceStock.get(skuIce.SkuNo)!=skuIce.InventoryQuantity){
	                toUpdateIce.put(skuIce.SkuNo, iceStock.get(skuIce.SkuNo));
	            }
	        }else{
				loggerError.error(" iceStock not contains  "+"sop skuNo ：--------"+skuIce.SkuNo +" suppliersku: "+ skuIce.SupplierSkuNo );
			}
		}
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
		try {
			Map<String, String> supplierStock=grabStock(skuNos);  //

			int stockResult=0;

			Map<String,Integer> sopPurchaseMap = new HashMap<>();
			if(!ORDER){
                 sopPurchaseMap = this.getSopPuchase(supplierId);
			}

			String result = "",stockTemp="",priceResult="";
			boolean sendMail=true;
			for (String skuNo : skuNos) {
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

				if(!ORDER){

					if(sopPurchaseMap.containsKey(skuNo)){

						logger.error("采购单supplierId："+skuNo +" ; 数量 : " + sopPurchaseMap.get(skuNo));
						loggerInfo.info("采购单supplierId：" + skuNo + " ; 数量 : " + sopPurchaseMap.get(skuNo));
						stockResult =  stockResult - sopPurchaseMap.get(skuNo);
						logger.error("最终库存 ：" + stockResult);
						loggerInfo.info("最终库存 ：" + stockResult);
						if(stockResult<0) stockResult=0;

					}
				}

				String iceSku=localAndIceSkuId.get(skuNo);
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
			e.printStackTrace();
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

		String startTime = format.format(getAppointDayFromSpecifiedDay(endDate, -2, "M"));
		List<java.lang.Integer> statusList = new ArrayList<>();
		statusList.add(1);
		while(hasNext){
			List<PurchaseOrderDetail> orderDetails = null;
			try {

				PurchaseOrderQueryDto orderQueryDto = new PurchaseOrderQueryDto(startTime,endTime,statusList
						,pageIndex,pageSize);
				PurchaseOrderDetailPage orderDetailPage=
						servant.FindPurchaseOrderDetailPaged(supplierId, orderQueryDto);


				orderDetails = orderDetailPage.PurchaseOrderDetails;
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (PurchaseOrderDetail orderDetail : orderDetails) {
				supplierSkuNo  = orderDetail.SupplierSkuNo;
				if(sopPurchaseMap.containsKey(supplierSkuNo)){
					//

					sopPurchaseMap.put(supplierSkuNo,sopPurchaseMap.get(supplierSkuNo)+1);
				}else{

					sopPurchaseMap.put(supplierSkuNo,1);
				}


			}
			pageIndex++;
			hasNext=(pageSize==orderDetails.size());

		}

		logger.warn("获取ice采购单 结束");

		return sopPurchaseMap;


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
				e.printStackTrace();
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
}
