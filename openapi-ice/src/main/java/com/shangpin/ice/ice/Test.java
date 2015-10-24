package com.shangpin.ice.ice;

import ShangPin.SOP.Api.ApiException;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuIce;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPage;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPageQuery;
import ShangPin.SOP.Entity.Api.Product.SopSkuIce;
import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetail;
import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetailPage;
import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderEx;
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
//        sopPurchaseOrderDetailNos.add(2015102302775L);
//        PurchaseOrderEx purchaseOrderEx = new PurchaseOrderEx(sopPurchaseOrderDetailNos,"Fail to create Order because the event already expired");
//        try {
//            System.out.print("　开始处理");
//            String  result = servant.PurchaseDetailEx(purchaseOrderEx,"2015101001584");
//            System.out.print("　返回信息" +result);
//        } catch (ApiException e) {
//            e.printStackTrace();
//        }

        List<Integer> status = new ArrayList<>();
        status.add(5);



        int pageIndex=1,pageSize=20;
        OpenApiServantPrx servant = null;
        try {
            servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean hasNext=true;

            Set<String> skuIds = new HashSet<String>();
            Map<String,List<PurchaseOrderDetail>>  purchaseOrderMap = new HashMap<>();
            String sopPurchaseOrderNo = "";

            while(hasNext){
                List<PurchaseOrderDetail> orderDetails = null;
                try {

                    PurchaseOrderQueryDto orderQueryDto = new PurchaseOrderQueryDto("2015-10-24 T 12:00:00","2015-10-24 T 12:44:00",status
                            ,pageIndex,pageSize);
                    PurchaseOrderDetailPage orderDetailPage=
                            servant.FindPurchaseOrderDetailPaged("2015101001584", orderQueryDto);


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

    }

}
