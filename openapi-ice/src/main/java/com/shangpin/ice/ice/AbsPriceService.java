package com.shangpin.ice.ice;

import ShangPin.SOP.Api.ApiException;
import ShangPin.SOP.Entity.Api.Product.*;
import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetail;
import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetailPage;
import ShangPin.SOP.Entity.Where.OpenApi.Purchase.PurchaseOrderQueryDto;
import ShangPin.SOP.Servant.OpenApiServantPrx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.SkuPriceDTO;
import com.shangpin.iog.dto.SkuRelationDTO;
import com.shangpin.iog.ice.dto.CategoryBrandDiscountDTO;
import com.shangpin.iog.ice.dto.ResMessage;
import com.shangpin.iog.service.SkuPriceService;
import com.shangpin.iog.service.SkuRelationService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by lizhongren on 2015/10/9.
 * 价格更新接口
 */
public abstract class AbsPriceService {

    static Logger logger = LoggerFactory.getLogger(AbsPriceService.class);
    private boolean useThread=false;
    private int skuCount4Thread=100;
    private static ReentrantLock lock = new ReentrantLock();

    //使用MarketPrice 计算进货价
    public static boolean PurchasePriceUseMarketPriceCalculate = true;

    //不使用品牌品类折扣
    public static boolean NoUseDiscount = false;

    public static BigDecimal DefaultCommission=new BigDecimal(1);

    private String  splitSign="|";

    public static Map<String,String>   sopMarketPriceMap = null;

    public static Map<String,BigDecimal> discountMap = null;






    //
    @Autowired
    public SkuPriceService skuPriceService;

    @Autowired
    SkuRelationService skuRelationService;

    /**
     * 抓取供应商价格
     * @param skuNo 供应商的每个产品的唯一编号：sku

     * @return 每个sku对应的价格  市场价 | 供货价
     * @throws ServiceException
     */
    public abstract Map<String,String> getPriceOfSupplier(Collection<String> skuNo) throws ServiceException, Exception;


    /**
     * 更新主站库存
     * @param supplier 供应商id
     * @param start 主站产品数据时间开始 yyyy-MM-dd HH:mm
     * @param end 主站产品数据时间结束 yyyy
     *            +   -MM-dd HH:mm
     * @return 更新失败数
     * @throws Exception
     */
    public int updatePirce(final String supplier,String start,String end) throws Exception{
        //初始化 sopMarketPriceMap
        getSopMarketPriceMap(supplier);

        //ice的skuid与本地库拉到的skuId的关系，value是ice中skuId,key是本地中的skuId

        final Map<String,String> localAndIceSku=new HashMap<String, String>();
        final Collection<String> skuNoSet=grabProduct(supplier, start, end,localAndIceSku);
        logger.warn("待更新数据总数："+skuNoSet.size());
        //logger.warn("需要更新ice,supplier sku关系是："+JSON.serialize(localAndIceSku));
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
            while (!exe.awaitTermination(60, TimeUnit.SECONDS)) {

            }
            int fct=0;
            for(int k=0;k<totoalFailCnt.size();k++){
                fct+=totoalFailCnt.get(k);
            }
            return fct;
        }else{
            return updatePrice(supplier, localAndIceSku, skuNoSet);
        }
    }

    /**
     * 获取本地价格数据
     * @param supplierId
     * @throws ServiceException
     */
    private  void  getSopMarketPriceMap(String supplierId) throws ServiceException {
        //TODO 测试
        try{
            lock.lock();
            if(null==sopMarketPriceMap){//第一次初始化
                sopMarketPriceMap =skuPriceService.getSkuPriceMap(supplierId);
            }

        }finally{
            if(lock.isLocked())	lock.unlock();
        }


    }

    /**
     * 赋值sku对应的折扣
     * @param supplierId
     * @param skuBrandCategoryMap    skuNO 对应的品牌和品类组合值
     * @param brandMap                 品牌map
     * @param servant
     * @throws ServiceException
     */
    private void getDiscountMap(String supplierId,Map<String,String> skuBrandCategoryMap, Map<String,String> brandMap,OpenApiServantPrx servant) throws  ServiceException{
        try{
            lock.lock();
            if(null==discountMap){//第一次初始化
                String brandNo="", result="",skuId="" ;
                Gson gson = new Gson();
                Map<String,BigDecimal> tempMap = new HashMap<>();
                for(Iterator<Map.Entry<String,String>> itor=brandMap.entrySet().iterator();itor.hasNext();){
                    brandNo = itor.next().getKey();
                    try {
                        result =  servant.FindCategoryBrandAgreement(supplierId,"",brandNo);
                        ResMessage message = gson.fromJson(result,ResMessage.class);
                        if(null!=message){
                            logger.error("拉取品牌品类折扣错误,返回信息为NULL");
                             //TODO 邮件通知
                        }else{
                            if(200==message.getResCode()){
                                 List<CategoryBrandDiscountDTO> discountDTOList = gson.fromJson( message.getMessageRes(), new TypeToken<List<CategoryBrandDiscountDTO>>(){}.getType());
                                 for(CategoryBrandDiscountDTO discountDTO:discountDTOList){
                                     try {
                                         tempMap.put(discountDTO.getBrandNo()+"|"+discountDTO.getCategoryNo(),new BigDecimal(discountDTO.getDiscountRate()));
                                     } catch (Exception e) {
                                        logger.error("折扣赋值错误");
                                     }
                                 }
                            }else {//错误
                                logger.error("拉取品牌品类折扣错误，错误原因:" + message.getMessageRes());
                            }
                        }

                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                }
                // discountMap  赋值  skuId,discount
                Map.Entry<String,String> skuEntry = null;
                for(Iterator<Map.Entry<String,String>> itor=skuBrandCategoryMap.entrySet().iterator();itor.hasNext();){
                    skuEntry = itor.next();
                    result = skuEntry.getValue();
                    if(tempMap.containsKey(result)){
                        discountMap.put( skuEntry.getKey(),tempMap.get(result));
                    }
                }


            }


        }finally{
            if(lock.isLocked())	lock.unlock();
        }
    }


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
        int pageIndex=1,pageSize=100;
        OpenApiServantPrx servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
        boolean hasNext=true;
        logger.warn("获取icesku 开始");
        Set<String> skuIds = new HashSet<String>();
         Map<String,String> brandMap = new HashMap<>();
        Map<String,String> skuBrandCategoryMap = new HashMap<>();

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
                brandMap.put(sku.BrandENName,"");
                List<SopSkuIce> skuIces = sku.SopSkuIces;
                for (SopSkuIce ice : skuIces) {

                    if(null!=ice.SkuNo&&!"".equals(ice.SkuNo)&&null!=ice.SupplierSkuNo&&!"".equals(ice.SupplierSkuNo)){
                        if(1!=ice.IsDeleted){
                            skuIds.add(ice.SupplierSkuNo);
                            stocks.put(ice.SupplierSkuNo,ice.SkuNo);
                            //TODO  品牌和品类应该为编号
                            skuBrandCategoryMap.put(ice.SkuNo,sku.BrandENName+"|"+sku.CategoryName);
                        }
                    }

                }
            }
            pageIndex++;
            hasNext=(pageSize==skus.size());

        }
        logger.warn("获取icesku 结束");

        //获取品牌品类折扣
        getDiscountMap(supplier,skuBrandCategoryMap,brandMap,servant);

        return skuIds;
    }
    /**
     * 更新市场价进入中间库存
     * @param supplierPriceMap
     * @throws ServiceException
     */
    private void updateMiddleDB(Map<String,String> supplierPriceMap,String supplieId) throws ServiceException{
        Set<Map.Entry<String, String>> supplierPriceSet = supplierPriceMap.entrySet();
        String skuNo="",price = "",marketPrice="",supplierPrice="";
        for(Map.Entry<String, String> entry:supplierPriceSet){
            skuNo =entry.getKey();
            price = entry.getValue();
            marketPrice= price.substring(0,price.indexOf(splitSign));
            supplierPrice=price.substring(price.indexOf(splitSign)+1);
            SkuPriceDTO dto = new SkuPriceDTO();
            dto.setMarketPrice(marketPrice);
            dto.setSupplierPrice(supplierPrice);
            dto.setSkuId(skuNo);
            dto.setSupplierId(supplieId);
            try {
                skuPriceService.updatePrice(dto);
            } catch (ServiceException e) {
                logger.error("skuId :" + skuNo + "sop更新价格成功，但中间库保存失败" );
            }
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
    private int updatePrice(String supplier,
                            Map<String, String> localAndIceSkuId,
                            Collection<String> skuNoSet) throws ServiceException, Exception,
            ApiException {
        Map<String, String> icePriceMap = grab4IcePrice(skuNoSet, localAndIceSkuId);
        updateIcePrice(supplier, icePriceMap);
        return 0;
    }
    /**
     * 更新ice的价格
     * @param supplier 供应商
     * @param icePrice ice的skuNo,与对应的价格
     * @return 更新失败的数目
     * @throws Exception
     */
    private int  updateIcePrice(String supplier, Map<String, String> icePrice)
            throws Exception {
        OpenApiServantPrx servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
        //更新价格
        return   updateChangePriceRecord(supplier,icePrice,servant);
    }




    /**
     * 更改价格     有查询限制 过滤
     * @param supplier
     * @param sopPriceMap 价格变化的MAP
     * @param servant
     * @throws ApiException
     */
    private int updateChangePriceRecord(String supplier, Map<String, String> sopPriceMap,  OpenApiServantPrx servant) throws ApiException {

         //查找价格变化的sku 信息
        String skuNo="",price = "",marketPrice="",supplierPrice="";
        String dbMarketPrice ="",dbSupplierPrice="";
        BigDecimal purchasePrice =null;
        Set<String> sopSkuIdSet = new HashSet<>();
        int failCount =0;
        Map<String,String>  supplierPriceMap = new HashMap<>();//保存需要更新的价格信息
        for(Map.Entry<String, String> entry:sopPriceMap.entrySet()){
            skuNo = entry.getKey();
            price = entry.getValue();
            String[] priceArray = price.split("|");
            if(null!=priceArray&&2==priceArray.length){
                marketPrice = priceArray[0];
                supplierPrice = priceArray[1];
                if(PurchasePriceUseMarketPriceCalculate){ //使用市场价计算供货价

                    if("-1".equals(marketPrice)){//无市场价
                        failCount++;
                        continue;
                    }else{
                        if(discountMap.containsKey(skuNo)){//有折扣
                            purchasePrice = new BigDecimal(marketPrice).multiply(discountMap.get(skuNo)).multiply(DefaultCommission).setScale(2,BigDecimal.ROUND_HALF_UP);

                        }else{//无折扣 不做处理
                            failCount++;
                            continue;
                        }

                    }

                }else{ //直接使用供货价
                   if("-1".equals(supplierPrice)){ //无供货价
                       failCount++;
                       continue;
                   }else{
                       if(NoUseDiscount){ //不使用品牌品类折扣
                           purchasePrice = new BigDecimal(supplierPrice).multiply(DefaultCommission).setScale(2,BigDecimal.ROUND_HALF_UP);
                       }else{ //使用品牌品类折扣
                           if(discountMap.containsKey(skuNo)){//有折扣
                               purchasePrice = new BigDecimal(supplierPrice).multiply(discountMap.get(skuNo)).multiply(DefaultCommission).setScale(2,BigDecimal.ROUND_HALF_UP);
                           }else{//无折扣 不做处理
                               failCount++;
                               continue;
                           }
                       }


                   }
                }
            }else{
                failCount++;
                continue;
            }
            //排除价格没有变化的
            if(sopMarketPriceMap.containsKey(skuNo)){
                String[] dbPriceArray = sopMarketPriceMap.get(skuNo).split("|");
                if(null!=dbPriceArray&&2==dbPriceArray.length) {
                    dbMarketPrice = dbPriceArray[0];
                    dbSupplierPrice = dbPriceArray[1];
                    if(!dbMarketPrice.equals(marketPrice)||!dbSupplierPrice.equals(supplierPrice)){
                        supplierPriceMap.put(skuNo,marketPrice+splitSign+purchasePrice.toString());
                        sopSkuIdSet.add(skuNo);
                    }
                }else{
                    failCount++;
                    continue;
                }

            }else{ //未找到记录
                supplierPriceMap.put(skuNo,marketPrice+splitSign+purchasePrice.toString());
                sopSkuIdSet.add(skuNo);
            }

        }


        if(sopSkuIdSet.size()>0){
            StringBuffer buffer = new StringBuffer();
            for(String skuId:sopSkuIdSet){
                buffer.append(skuId).append(",");
            }

            String skuIdValue = buffer.toString().substring(0,buffer.toString().length()-1);
            Supply supply = new Supply();
            supply.SkuNos =skuIdValue;
            SopSkuPriceApplyIce[] skuPriceApplyIceArray =servant.FindSupplyInfo(supplier, supply);



            String supplierSku="";
            for(SopSkuPriceApplyIce skuPriceIce:skuPriceApplyIceArray){
                if(supplierPriceMap.containsKey(skuPriceIce.SkuNo)){
                    if(skuPriceIce.PriceStatus!=1){
                        continue;
                    }
                    price = supplierPriceMap.get(skuPriceIce.SkuNo);
                    try {


                        marketPrice=price.substring(0,price.indexOf(splitSign));
                        supplierPrice=price.substring(price.indexOf(splitSign)+1);

                    } catch (Exception e) {
                        continue;
                    }

                    SupplyPriceInfo priceInfo = new SupplyPriceInfo();
                    priceInfo.SkuNo=skuPriceIce.SkuNo;
                    priceInfo.MarkePrice=skuPriceIce.MarketPrice;
                    priceInfo.MarketPriceCurrency= skuPriceIce.MarketPriceCurrency;


                    priceInfo.SupplyPrice= supplierPrice;
                    priceInfo.SupplyCurrency = skuPriceIce.SupplyCurrency;


                    priceInfo.PriceEffectDate=skuPriceIce.PriceEffectDate;
                    priceInfo.PriceFailureDate=skuPriceIce.PriceFailureDate;

                    priceInfo.StagePrice=skuPriceIce.StagePrice;
                    //TODO 未找到
//					priceInfo.StagePriceCurrency=;
                    priceInfo.StagePriceEffectDate=skuPriceIce.StagePriceEffectDate;
                    priceInfo.StagePriceFailureDate=skuPriceIce.StagePriceFailureDate;

                    //测试使用  todo 删除
                    if(priceInfo.StagePriceEffectDate.equals(priceInfo.StagePriceFailureDate)){
                        priceInfo.StagePriceFailureDate ="2016/1/1 0:00:00";
                    }

                    if(priceInfo.PriceEffectDate.equals(priceInfo.PriceFailureDate)){
                        priceInfo.PriceFailureDate ="2016/1/1 0:00:00";
                    }


                    try {
                        logger.warn("待更新的数据：--------" + skuPriceIce.SkuNo + ":" + price);

						if(!servant.UpdateSupplyPriceSpecial(supplier, priceInfo)){
							//移除价格更新失败的信息
                            failCount++;
							supplierPriceMap.remove(skuPriceIce.SkuNo);
						}



                    } catch (ApiException e) {

                        e.printStackTrace();
                    }
                }
            }

			//更新中间库
			if(supplierPriceMap.size()>0){
				try {
                    updateMiddleDB(supplierPriceMap, supplier);
                } catch (ServiceException e) {
					e.printStackTrace();
				}
			}

        }

        return  failCount;

    }






    /**
     * 拉取供应商价格，并返回ice中对应的sku的价格
     * @param skuNos 供应商sku编号，从ice中获取到的
     * @param localAndIceSkuId 供应商sku编号与icesku编号的关系
     * @return 供应商sku对应的icesku编号的库存，key是ice的sku编号，值是库存
     */
    private Map<String, String> grab4IcePrice(Collection<String> skuNos,Map<String, String> localAndIceSkuId ) {
        Map<String, String> icePriceMap=new HashMap<>();
        try {
            Map<String, String> supplierPriceMap=getPriceOfSupplier(skuNos);  //  市场价|供货价
            //
            for (String skuNo : skuNos) {

                String iceSku=localAndIceSkuId.get(skuNo);
                if(supplierPriceMap.containsKey(skuNo)){
                    if(!StringUtils.isEmpty(iceSku))	icePriceMap.put(iceSku, supplierPriceMap.get(skuNo));
                }
            }

        } catch (Exception e1) {
            logger.error("抓取库存失败:", e1.getMessage());
        }
        return icePriceMap;
    }


    /**
     * 通过获取采购单，得到每个供货商SKUID对应的未处理的采购单
     * @param supplierId
     * @return
     */

    private Map<String,Integer> getSopPuchase(String supplierId){


        int pageIndex=1,pageSize=20;
        OpenApiServantPrx servant = null;
        try {
            servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
        } catch (Exception e) {
            e.printStackTrace();
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

    class UpdateThread extends Thread{
        final Logger logger = LoggerFactory.getLogger(UpdateThread.class);
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
            Map<String, String> iceStock = grab4IcePrice(skuNos, localAndIceSkuId);
            try {
                logger.warn(Thread.currentThread().getName()+"ice更新处理开始，数："+iceStock.size());
                int failCnt =  updateIcePrice(supplier, iceStock);
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
