package com.shangpin.ice.ice;

import ShangPin.SOP.Entity.Api.Product.SopProductSkuIce;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPage;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPageQuery;
import ShangPin.SOP.Entity.Api.Product.SopSkuIce;
import ShangPin.SOP.Servant.OpenApiServantPrx;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.service.SkuPriceService;
import com.shangpin.iog.service.SkuRelationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by lizhongren on 2015/10/9.
 * 价格更新接口
 */
public class AbsPriceService {

    static Logger logger = LoggerFactory.getLogger(AbsPriceService.class);

    private boolean useThread=false;
    private int skuCount4Thread=100;
    private static ReentrantLock lock = new ReentrantLock();

    public static Map<String,String>   sopMarketPriceMap = null;

    @Autowired
    public SkuPriceService skuPriceService;

    @Autowired
    SkuRelationService skuRelationService;

    /**
     *
     * @param supplier
     * @param start
     * @param end
     * @throws Exception
     */
    public void updateSupplierPrice(final String supplier,String start,String end) throws Exception{
        //初始化 sopMarketPriceMap
        getSopMarketPriceMap(supplier);

        //ice的skuid与本地库拉到的skuId的关系，value是ice中skuId,key是本地中的skuId

        final Map<String,String> localAndIceSku=new HashMap<String, String>();
        final Map<String,String> sopSkuAndSupplierSku =new HashMap<>();
        final Collection<String> skuNoSet=grabProduct(supplier, start, end,localAndIceSku);
        logger.warn("待更新库存数据总数："+skuNoSet.size());
        //logger.warn("需要更新ice,supplier sku关系是："+JSON.serialize(localAndIceSku));
        final List<Integer> totoalFailCnt= Collections.synchronizedList(new ArrayList<Integer>());
//        if(useThread){
//            int poolCnt=skuNoSet.size()/getSkuCount4Thread();
//            ExecutorService exe= Executors.newFixedThreadPool(poolCnt / 4 + 1);//相当于跑4遍
//            final List<Collection<String>> subSkuNos=subCollection(skuNoSet);
//            logger.warn("线程池数："+(poolCnt/4+1)+",sku子集合数："+subSkuNos.size());
//            for(int i = 0 ; i <subSkuNos.size();i++){
//                Map<String,String> sopPriceMap = new HashMap<>();
//                exe.execute(new UpdateThread(subSkuNos.get(i),supplier,localAndIceSku,totoalFailCnt,sopPriceMap));
//            }
//            exe.shutdown();
//            while (!exe.awaitTermination(60, TimeUnit.SECONDS)) {
//
//            }
//            int fct=0;
//            for(int k=0;k<totoalFailCnt.size();k++){
//                fct+=totoalFailCnt.get(k);
//            }
//            return fct;
//        }else{
//            Map<String,String> sopPriceMap = new HashMap<>();
//            return updateStock(supplier, localAndIceSku, skuNoSet,sopPriceMap);
//        }

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
        logger.warn("获取icesku 开始");
        Set<String> skuIds = new HashSet<String>();

        //获取已有的SPSKUID
//		List<SkuRelationDTO> skuRelationDTOList = skuRelationService.findListBySupplierId(supplier);
//		Map<String,String> map = new HashMap<>();
//		for(SkuRelationDTO skuRelationDTO:skuRelationDTOList){
//			map.put(skuRelationDTO.getSopSkuId(),null);
//		}
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

//					if (!map.containsKey(ice.SkuNo)){
//						SkuRelationDTO skuRelationDTO = new SkuRelationDTO();
//						skuRelationDTO.setSupplierId(supplier);
//						skuRelationDTO.setSupplierSkuId(ice.SupplierSkuNo);
//						skuRelationDTO.setSopSkuId(ice.SkuNo);
//						skuRelationDTO.setCreateTime(date);
//						skuRelationService.saveSkuRelateion(skuRelationDTO);
//					}

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
}
