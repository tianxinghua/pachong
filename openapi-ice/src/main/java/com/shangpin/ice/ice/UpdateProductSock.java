package com.shangpin.ice.ice;

import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetail;
import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetailPage;
import ShangPin.SOP.Entity.Where.OpenApi.Purchase.PurchaseOrderQueryDto;
import ShangPin.SOP.Servant.OpenApiServantPrx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    /**
     * 更新库存
     * @param supplierStock 供应商sku编号
     * @return 供应商sku对应的icesku编号的库存，key是ice的sku编号，值是库存
     */
    public void  updateStock(Map<String,Integer> supplierStock,
                                               final String supplierId) {
        Map<String, Integer> iceStock=new HashMap<>();
        try {


            int stockResult=0;

            Map<String,Integer> sopPurchaseMap =  this.getSopPuchase(supplierId);



            String  supplierSkuNo="";

            for(Iterator<Map.Entry<String,Integer>> itor= supplierStock.entrySet().iterator();itor.hasNext();){
                Map.Entry<String,Integer> entry  = itor.next();
                supplierSkuNo = entry.getKey();
                stockResult = entry.getValue();

                if(sopPurchaseMap.containsKey(supplierSkuNo)){

                    loggerInfo.info("采购单supplierId：" + supplierSkuNo + " ; 数量 : " + sopPurchaseMap.get(supplierSkuNo));
                    stockResult =  stockResult - sopPurchaseMap.get(supplierSkuNo);
                    loggerInfo.info("最终库存 ：" + stockResult);
                    if(stockResult<0) stockResult=0;

                }

            }



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
