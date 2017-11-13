package com.shangpin.ice.ice;

import ShangPin.SOP.Api.ApiException;
import ShangPin.SOP.Entity.Api.Product.SopSkuInventoryIce;
import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetail;
import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetailPage;
import ShangPin.SOP.Entity.Where.OpenApi.Purchase.PurchaseOrderQueryDto;
import ShangPin.SOP.Servant.OpenApiServantPrx;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.dto.SpecialSkuDTO;
import com.shangpin.iog.service.SpecialSkuService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lizhongren on 2015/11/10.
 */
public class UpdateProductSock {


    static Logger logger = LoggerFactory.getLogger(UpdateProductSock.class);

    private static org.apache.log4j.Logger loggerInfo = org.apache.log4j.Logger.getLogger("info");
    private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger.getLogger("error");


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

    @Autowired
    SpecialSkuService specialSkuService;

    /**
     * 更新库存
     * @param supplierStock 供应商sku编号
     * @return 供应商sku对应的icesku编号的库存，key是ice的sku编号，值是库存
     */
    public void  updateStock(Map<String,Integer> supplierStock,Map<String,String> skuRelationMap,
                                               final String supplierId) {


        Map<String, Integer> iceStock=new HashMap<>();
        try {

            //排除采购异常的
            this.setStockNotUpdateBySop(supplierId,supplierStock);

            int stockResult=0;
            //
            Map<String,Integer> sopPurchaseMap =  new HashMap<>();//this.getSopPuchase(supplierId);
            String  supplierSkuNo="",sopSkuNo="";

            for(Iterator<Map.Entry<String,Integer>> itor= supplierStock.entrySet().iterator();itor.hasNext();){
                Map.Entry<String,Integer> entry  = itor.next();
                supplierSkuNo = entry.getKey();
                sopSkuNo = skuRelationMap.get(supplierSkuNo);
                stockResult = entry.getValue();

                if(sopPurchaseMap.containsKey(supplierSkuNo)){
                    loggerInfo.info("采购单supplierId：" + supplierSkuNo + " ; 数量 : " + sopPurchaseMap.get(supplierSkuNo));
                    stockResult =  stockResult - sopPurchaseMap.get(supplierSkuNo);
                    loggerInfo.info("最终库存 ：" + stockResult);
                    if(stockResult<0) stockResult=0;
                }

                iceStock.put(sopSkuNo,stockResult);


            }

            this.updateIceStock(supplierId,iceStock);


        } catch (Exception e1) {
            logger.error("抓取库存失败:", e1);
        }


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


    private int updateIceStock(String supplier, Map<String, Integer> iceStock)
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
        Iterator<Map.Entry<String, Integer>> iter=toUpdateIce.entrySet().iterator();
        loggerInfo.info("待更新的数据总和：--------"+toUpdateIce.size());
        logger.warn("待更新的数据总和：--------"+toUpdateIce.size());
        while (iter.hasNext()) {
            Map.Entry<String, Integer> entry = iter.next();
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
                if( iceStock.get(skuIce.SkuNo)!=skuIce.InventoryQuantity){
                    toUpdateIce.put(skuIce.SkuNo, iceStock.get(skuIce.SkuNo));
                }
            }else{
                loggerError.error(" iceStock not contains  "+"sop skuNo ：--------"+skuIce.SkuNo +" suppliersku: "+ skuIce.SupplierSkuNo );
            }
        }
    }


    private void setStockNotUpdateBySop(String supplierId,Map<String,Integer> stockMap) throws Exception{
        OpenApiServantPrx servant = null;
        try {
            servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
        } catch (Exception e) {
            loggerError.error("Ice 代理失败");
            e.printStackTrace();
            throw e;

        }

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
                                //排除不需要更新的库存
                                if(stockMap.containsKey(supplierSkuNo)){
                                    stockMap.remove(supplierSkuNo);
                                }
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
}
