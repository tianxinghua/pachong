package com.shangpin.ice.ice;

import ShangPin.SOP.Api.ApiException;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuIce;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPage;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPageQuery;
import ShangPin.SOP.Entity.Api.Product.SopSkuIce;
import ShangPin.SOP.Entity.Api.Purchase.*;
import ShangPin.SOP.Entity.Where.OpenApi.Purchase.PurchaseOrderQueryDto;
import ShangPin.SOP.Servant.OpenApiServantPrx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.ICEOrderDetailDTO;
import com.shangpin.iog.ice.dto.ICEWMSOrderDTO;
import com.shangpin.iog.ice.dto.ICEWMSOrderRequestDTO;
import com.shangpin.iog.ice.dto.OrderStatus;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lizhongren on 2015/7/10.
 */
public class Test {
    public static void main(String[] args){
//        OpenApiServantPrx servant = null;
//        try {
//            servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        List<Long> sopPurchaseOrderDetailNos =new ArrayList<>();
//        sopPurchaseOrderDetailNos.add(2015102600421L);
//        PurchaseOrderEx purchaseOrderEx = new PurchaseOrderEx(sopPurchaseOrderDetailNos,"ERROR! Data or Order Id or Order Site can't be empty!");
//        try {
//            System.out.print("　开始处理");
//            String  result = servant.PurchaseDetailEx(purchaseOrderEx,"2015101001584");
//            System.out.print("　返回信息" +result);
//        } catch (ApiException e) {
//            e.printStackTrace();
//        }

//        List<Integer> status = new ArrayList<>();
//        status.add(1);
//        status.add(2);
//
//
//
//        int pageIndex=1,pageSize=20;
//        OpenApiServantPrx servant = null;
//        try {
//            servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        boolean hasNext=true;
//
//            Set<String> skuIds = new HashSet<String>();
//            Map<String,List<PurchaseOrderDetail>>  purchaseOrderMap = new HashMap<>();
//            String sopPurchaseOrderNo = "";
//
//            while(hasNext){
//                List<PurchaseOrderDetail> orderDetails = null;
//                try {
//
//                    PurchaseOrderQueryDto orderQueryDto = new PurchaseOrderQueryDto("2015-10-24 T 12:00:00","2015-10-24 T 12:44:00",status
//                            ,pageIndex,pageSize);
//                    PurchaseOrderDetailPage orderDetailPage=
//                            servant.FindPurchaseOrderDetailPaged("2015101001584", orderQueryDto);
//
//
//                    orderDetails = orderDetailPage.PurchaseOrderDetails;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                for (PurchaseOrderDetail orderDetail : orderDetails) {
//                    sopPurchaseOrderNo  = orderDetail.SopPurchaseOrderNo;
//                    if(purchaseOrderMap.containsKey(sopPurchaseOrderNo)){
//                        //
//
//                        purchaseOrderMap.get(sopPurchaseOrderNo).add(orderDetail);
//                    }else{
//                        List<PurchaseOrderDetail> orderList = new ArrayList<>();
//                        orderList.add(orderDetail);
//                        purchaseOrderMap.put(sopPurchaseOrderNo,orderList);
//                    }
//
//
//                }
//                pageIndex++;
//                hasNext=(pageSize==orderDetails.size());
//
//            }
//        for(Iterator<Map.Entry<String,List<PurchaseOrderDetail>>> itor = purchaseOrderMap.entrySet().iterator();itor.hasNext();){
//            Map.Entry<String,List<PurchaseOrderDetail>> entry = itor.next();
//            List<PurchaseOrderDetail> orderDetailList = entry.getValue();
//            gson.toJson(orderDetailList);
//
//        }



//        OpenApiServantPrx servant = null;
//        try {
//            servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        String sopPurchaseOrderNo ="";
//
//
//        List<OrderDTO>  orderDTOList= new ArrayList<>();
//        OrderDTO orderDTO1 =new OrderDTO();
//        orderDTO1.setSpOrderId("201510250199901");
//
//        orderDTOList.add(orderDTO1);
//        OrderDTO orderDTO2 =new OrderDTO();
//        orderDTO2.setSpOrderId("201510250199932");
//
//        orderDTOList.add(orderDTO2);

//        OrderDTO orderDTO3 =new OrderDTO();
//        orderDTO3.setSpOrderId("201510250196093");
//
//        orderDTOList.add(orderDTO3);
//
//        try{
//            for(OrderDTO orderDTO:orderDTOList){
//                Map<String,List<PurchaseOrderDetailSpecial>>  purchaseOrderMap = new HashMap<>();
//
//                PurchaseOrderDetailSpecialPage orderDetailSpecialPage = servant.FindPurchaseOrderDetailSpecial("2015101001584","",orderDTO.getSpOrderId());
//                if(null!=orderDetailSpecialPage&&null!=orderDetailSpecialPage.PurchaseOrderDetails&&orderDetailSpecialPage.PurchaseOrderDetails.size()>0){  //存在采购单 就代表已支付
//
//                    for (PurchaseOrderDetailSpecial orderDetail : orderDetailSpecialPage.PurchaseOrderDetails) {
//                        sopPurchaseOrderNo  = orderDetail.SopPurchaseOrderNo;
//                        if(purchaseOrderMap.containsKey(sopPurchaseOrderNo)){
//                            purchaseOrderMap.get(sopPurchaseOrderNo).add(orderDetail);
//                        }else{
//                            List<PurchaseOrderDetailSpecial> orderList = new ArrayList<>();
//                            orderList.add(orderDetail);
//                            purchaseOrderMap.put(sopPurchaseOrderNo,orderList);
//
//                        }
//
//
//                    }
//
//                    for(Iterator<Map.Entry<String,List<PurchaseOrderDetailSpecial>>> itor = purchaseOrderMap.entrySet().iterator();itor.hasNext();) {
//                        Map.Entry<String, List<PurchaseOrderDetailSpecial>> entry = itor.next();
//                        sopPurchaseOrderNo  = entry.getKey();
//                        StringBuffer purchaseOrderDetailbuffer =new StringBuffer();
//                        //获取同一产品的数量
//                        for(PurchaseOrderDetailSpecial purchaseOrderDetail:entry.getValue()){
//                            purchaseOrderDetailbuffer.append(purchaseOrderDetail.SopPurchaseOrderDetailNo).append(";");
//                            //赋值状态 海外商品每个采购单 只有一种茶品
//                            orderDTO.setSpPurchaseNo(sopPurchaseOrderNo);
//                            orderDTO.setPurchasePriceDetail(purchaseOrderDetail.SkuPrice);
//                            if(5!=purchaseOrderDetail.DetailStatus){ //5 为退款  1=待处理，2=待发货，3=待收货，4=待补发，5=已取消，6=已完成
//                                orderDTO.setStatus(OrderStatus.PAYED);
//                            }else{
//                                orderDTO.setStatus(OrderStatus.CANCELLED);
//                            }
//
//                        }
//                        orderDTO.setSpPurchaseDetailNo(purchaseOrderDetailbuffer.toString().substring(0,purchaseOrderDetailbuffer.toString().length()-1));
//
//
//                    }
//
//
//
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        String kk="111";
//        if(kk.startsWith("1")){
//            System.out.println("true----" + kk.trim().startsWith("1"));
//        }else{
//            System.out.println(" false  ");
//        }

//        Gson gson = new Gson();
//        ICEWMSOrderRequestDTO  dto = new ICEWMSOrderRequestDTO();
//
//        dto.setBeginTime("2015-11-17 13:16:00");
//        dto.setEndTime("2015-11-17 13:16:20");
//        dto.setSupplierNo("S0000514");
//
//        String jsonParameter= "="+ gson.toJson(dto);
//        String result ="";
//
//
//        try {
//            result =  HttpUtil45.operateData("post","form","http://spwmsinventory.spidc1.com/Api/StockQuery/SupplierInventoryLogQuery",new OutTimeConfig(1000*5,1000*5,1000*5),null,
//                    jsonParameter,"","");
//            System.out.println("result = " + result);
//
//        } catch (ServiceException e) {
//            e.printStackTrace();
//        }
        Test tesst = new Test();
        try {
            tesst.getSopPuchase("2015081701440");//2015081701443       2015081701440    2015111601665
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public  Map<String,Integer> getSopPuchase(String supplierId) throws  Exception {


        int pageIndex = 1, pageSize = 20;
        OpenApiServantPrx servant = null;
        try {
            servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
        } catch (Exception e) {
//			e.printStackTrace();
            throw e;

        }
        boolean hasNext = true;
        Set<String> skuIds = new HashSet<String>();
        Map<String, Integer> sopPurchaseMap = new HashMap<>();
        String supplierSkuNo = "";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date endDate = new Date();
        String endTime = format.format(endDate);

        String startTime = format.format(getAppointDayFromSpecifiedDay(endDate, -10, "D"));
        List<java.lang.Integer> statusList = new ArrayList<>();
        statusList.add(1);
        statusList.add(2);
        while (hasNext) {
            List<PurchaseOrderDetail> orderDetails = null;
            boolean fetchSuccess = true;
            for (int i = 0; i < 2; i++) {  //允许调用失败后，再次调用一次
                try {

                    PurchaseOrderQueryDto orderQueryDto = new PurchaseOrderQueryDto(startTime, endTime, statusList
                            , pageIndex, pageSize);
                    PurchaseOrderDetailPage orderDetailPage =
                            servant.FindPurchaseOrderDetailPaged(supplierId, orderQueryDto);
                    orderDetails = orderDetailPage.PurchaseOrderDetails;
                    if (null == orderDetails) {
                        fetchSuccess = false;
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                    fetchSuccess = false;
                } catch (Exception e) {
                    e.printStackTrace();
                    fetchSuccess = false;
                }
                if (fetchSuccess) {
                    i = 2;
                } else {
                }
            }

            for (PurchaseOrderDetail orderDetail : orderDetails) {
                supplierSkuNo = orderDetail.SupplierSkuNo;
                if (sopPurchaseMap.containsKey(supplierSkuNo)) {
                    //

                    sopPurchaseMap.put(supplierSkuNo, sopPurchaseMap.get(supplierSkuNo) + 1);
                } else {

                    sopPurchaseMap.put(supplierSkuNo, 1);
                }


            }
            pageIndex++;
            hasNext = (pageSize == orderDetails.size());

        }
        return   sopPurchaseMap;
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
