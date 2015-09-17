package com.shangpin.ice.ice;

import ShangPin.SOP.Entity.Api.Product.SopProductSkuIce;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPage;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPageQuery;
import ShangPin.SOP.Entity.Api.Product.SopSkuIce;
import ShangPin.SOP.Entity.Api.Purchase.DeliveryOrderAdd;
import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetail;
import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetailPage;
import ShangPin.SOP.Entity.Where.OpenApi.Purchase.PurchaseOrderQueryDto;
import ShangPin.SOP.Servant.OpenApiServantPrx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by loyalty on 15/9/9.
 */
public class OrderService {

    static Logger logger = LoggerFactory.getLogger(OrderService.class);
    static String url="/purchase/createdeliveryorder";
    /**
     * 获取采购单
     * 需要注意
     * SOP处理采购单 一条记录代表一个库存  同样的产品 购买两件 生成两条记录
     *
     * @return
     */
    public Map<String,List<PurchaseOrderDetail>> geturchaseOrder(String supplierId,String startTime ,String endTime,List<Integer> statusList) throws Exception{
        int pageIndex=1,pageSize=20;
        OpenApiServantPrx servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
        boolean hasNext=true;
        logger.warn("获取ice采购单 开始");
        Set<String> skuIds = new HashSet<String>();
        Map<String,List<PurchaseOrderDetail>>  purchaseOrderMap = new HashMap<>();
        String sopPurchaseOrderNo = "";

        while(hasNext){
            List<PurchaseOrderDetail> orderDetails = null;
            try {

                PurchaseOrderQueryDto  orderQueryDto = new PurchaseOrderQueryDto(startTime,endTime,statusList
                        ,pageIndex,pageSize);
                PurchaseOrderDetailPage orderDetailPage=
                servant.FindPurchaseOrderDetailPaged(supplierId, orderQueryDto);


                orderDetails = orderDetailPage.PurchaseOrderDetails;
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (PurchaseOrderDetail orderDetail : orderDetails) {
                sopPurchaseOrderNo  = orderDetail.SopPurchaseOrderNo;
                if(purchaseOrderMap.containsKey(sopPurchaseOrderNo)){
                    //

                    purchaseOrderMap.get(sopPurchaseOrderNo).add(orderDetail);
                }else{
                    List<PurchaseOrderDetail> orderList = new ArrayList<>();
                    orderList.add(orderDetail);
                    purchaseOrderMap.put(sopPurchaseOrderNo,orderList);
                }


            }
            pageIndex++;
            hasNext=(pageSize==orderDetails.size());

        }
//        for(Iterator<Map.Entry<String,List<PurchaseOrderDetail>>> itor = purchaseOrderMap.entrySet().iterator();itor.hasNext();){
//            Map.Entry<String,List<PurchaseOrderDetail>> entry = itor.next();
//            List<PurchaseOrderDetail> orderDetailList = entry.getValue();
//            gson.toJson(orderDetailList);
//
//        }
        logger.warn("获取ice采购单 结束");

        return purchaseOrderMap;

    }

    /**
     *获取发货单编号
     * @return
     * @throws Exception
     */
    public static String getLogistics(String supplierId,String LogisticsName, String LogisticsOrderNo, String DateDeliver, int EstimateArrivedTime, String DeliveryContacts, String DeliveryContactsPhone, String DeliveryAddress, String DeliveryMemo, String WarehouseNo, String WarehouseName, java.util.List<java.lang.String> SopPurchaseOrderDetailNo, int PrintStatus) throws Exception{
        OpenApiServantPrx servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
        boolean hasNext=true;
        logger.warn("获取ice采购单 开始");
        Set<String> skuIds = new HashSet<String>();
        Map<String,List<PurchaseOrderDetail>>  purchaseOrderMap = new HashMap<>();
        SopPurchaseOrderDetailNo.add("2015040800001");
        LogisticsOrderNo = getRandomNum();
        DeliveryOrderAdd deliveryOrderAdd= new DeliveryOrderAdd("顺丰",LogisticsOrderNo,"2015-03-19 17:00",5,"尼古拉斯"
        ,"18547477474","北京市通州区马驹桥物流基地兴贸一街 11号华润物流园区5号库","贵重物品，轻拿轻放","B"
                ,"北京代销实体仓", SopPurchaseOrderDetailNo,1);
        String sopLogisticsOrderNo= servant.CreateDeliveryOrder(supplierId, deliveryOrderAdd);
        return sopLogisticsOrderNo;
    }
    public static String getRandomNum() {
        Random random = new Random();
        String num="";
        for (int i = 0; i <13; i++) {
            int a = random.nextInt(9);
            num = a+num;
        }
        return num;
    }
    public static void main(String[] args)
    {
        java.util.List<java.lang.String> SopPurchaseOrderDetailNo=new ArrayList<>();
        try {
            String no=getLogistics("2015090900158","","","",0,"","","","","","",SopPurchaseOrderDetailNo,1);
            System.out.println("发货单编号"+no);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
