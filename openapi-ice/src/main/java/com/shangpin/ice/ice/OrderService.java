package com.shangpin.ice.ice;

import ShangPin.SOP.Entity.Api.Product.SopProductSkuIce;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPage;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPageQuery;
import ShangPin.SOP.Entity.Api.Product.SopSkuIce;
import ShangPin.SOP.Entity.Api.Purchase.DeliveryOrderAdd;
import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetail;
import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetailPage;
import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderEx;
import ShangPin.SOP.Entity.Where.OpenApi.Purchase.PurchaseOrderQueryDto;
import ShangPin.SOP.Servant.OpenApiServantPrx;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.common.utils.SendMail;
import com.shangpin.iog.ice.dto.ResMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by loyalty on 15/9/9.
 */
public  class OrderService {

    static Logger logger = LoggerFactory.getLogger(OrderService.class);
    private static final String YYYY_MMDD_HH = "yyyy-MM-dd HH:mm:ss";
    static String url="/purchase/createdeliveryorder";

    private static ResourceBundle bdl = null;
    private static  String email = null;
    static {
        if(null==bdl){
            bdl=ResourceBundle.getBundle("openice");
        }
        email = bdl.getString("email");
    }


    /**
     * 获取采购单
     * 需要注意
     * SOP处理采购单 一条记录代表一个库存  同样的产品 购买两件 生成两条记录
     *
     * @return
     */
    public Map<String,List<PurchaseOrderDetail>> getPurchaseOrder(String supplierId,String startTime ,String endTime,List<Integer> statusList) throws Exception{
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
     * 采购异常 推送采购单下单异常
     * @param purchaseDetail 采购单明细编号
     */
    public  void  cancelPurchaseOrder(String purchaseDetail,String memo ,String supplierId) throws ServiceException{
        try {
            List<Long> sopPurchaseOrderDetailNos = new ArrayList<>();
            String[] purchaseOrderDetailArray = purchaseDetail.split(";");
            if(null!=purchaseOrderDetailArray){
                for(String purchaseDetailNo:purchaseOrderDetailArray){
                    if(org.apache.commons.lang.StringUtils.isNotBlank(purchaseDetailNo)){
                        try {
                            sopPurchaseOrderDetailNos.add(Long.valueOf(purchaseDetailNo));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
                OpenApiServantPrx servant = null;
                try {
                    servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                PurchaseOrderEx purchaseOrderEx = new PurchaseOrderEx(sopPurchaseOrderDetailNos,memo);
                String  result = servant.PurchaseDetailEx(purchaseOrderEx,supplierId);
                Gson gson = new Gson();
                ResMessage message = gson.fromJson(result,ResMessage.class);
                if(null==message){
                    logger.error("推送取消采购单失败，无信息返回。");
                    Thread t = new Thread(new MailThread(supplierId,"gilt 线上发生错误","推送取消采购单失败，无信息返回。"));
                    t.start();


                }else {
                    if (200 != message.getResCode()) {
                      throw  new ServiceMessageException(message.getMessageRes());
                    }
                }

            }

        }catch(ServiceException e){
            throw  e;
        }
        catch (Exception e) {
            throw  new ServiceMessageException("处理失败。原因："+ e.getMessage());
        }

    }

    /**
     *获取发货单编号
     * 首先推送发货单
     * @return
     * @throws Exception
     */
    public  String getPurchaseDeliveryOrderNo(String supplierId,String logisticsName, String logisticsOrderNo,
                                                    String dateDeliver, int estimateArrivedTime, String deliveryContacts,
                                                    String deliveryContactsPhone, String deliveryAddress,
                                                    String deliveryMemo, String warehouseNo, String warehouseName,
                                                    java.util.List<java.lang.String> sopPurchaseOrderDetailNo, int printStatus) throws Exception{
        OpenApiServantPrx servant = null;
        try {
            servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(StringUtils.isEmpty(logisticsName)) logisticsName = "顺丰";
        if(StringUtils.isEmpty(logisticsOrderNo)) logisticsOrderNo = getRandomNum();
        if(StringUtils.isEmpty(dateDeliver)) dateDeliver = convertFormat(new Date(),YYYY_MMDD_HH);
        if(StringUtils.isEmpty(deliveryContacts)) deliveryContacts = "尼古拉斯";
        if(StringUtils.isEmpty(deliveryContactsPhone)) deliveryContactsPhone = "18547477474";
        if(StringUtils.isEmpty(deliveryAddress)) deliveryAddress = "北京市通州区马驹桥物流基地兴贸一街 11号华润物流园区5号库";
        if(StringUtils.isEmpty(deliveryMemo)) deliveryMemo = "贵重物品，轻拿轻放";
        if(StringUtils.isEmpty(warehouseNo)) warehouseNo = "B";
        if(StringUtils.isEmpty(warehouseName)) warehouseName = "北京代销实体仓";
        boolean hasNext=true;
        Set<String> skuIds = new HashSet<String>();
        Map<String,List<PurchaseOrderDetail>>  purchaseOrderMap = new HashMap<>();


        DeliveryOrderAdd deliveryOrderAdd= new DeliveryOrderAdd(logisticsName,logisticsOrderNo,dateDeliver,
                estimateArrivedTime,deliveryContacts
        ,deliveryContactsPhone,deliveryAddress,deliveryMemo,warehouseNo
                ,warehouseName, sopPurchaseOrderDetailNo,printStatus);
        String sopLogisticsOrderNo= servant.CreateDeliveryOrder(supplierId, deliveryOrderAdd);
        return sopLogisticsOrderNo;
    }


//    public abstract void  placeOrder();


//    public void purchaseOrder(String supplierId,String startDate,String endDate){
//
//
//        try {
//            //获取订单数组
//            List<Integer> status = new ArrayList<>();
//            status.add(1);
//            Map<String,List<PurchaseOrderDetail>> orderMap =  getPurchaseOrder(supplierId, startDate, endDate, status);
//            //  正常下单
//            String url = "https://api-sandbox.gilt.com/global/orders/";
//            transData( url, supplierId,orderMap);
//            //下单异常 再次下单
//            handlePurchaseOrderException();
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }


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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String convertFormat(Date date ,String format){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return (sdf.format(date));

    }

    //发邮件
    class MailThread implements  Runnable{

        String supplier = "";
        String content="";
        String title="";

        public MailThread(String  supplierId,String title,String content){
            this.supplier = supplierId;
            this.title = title;
            this.content = content;
        }

        @Override
        public void run() {
            try {
                SendMail.sendGroupMail("smtp.shangpin.com", "chengxu@shangpin.com",
                        "shangpin001", email, title,
                        content,
                        "text/html;charset=utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
