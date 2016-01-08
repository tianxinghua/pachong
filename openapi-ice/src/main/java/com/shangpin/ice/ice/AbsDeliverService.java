package com.shangpin.ice.ice;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.*;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by lizhongren on 2016/1/6
 * 发货.
 */
public abstract   class AbsDeliverService {
    static Logger log = LoggerFactory.getLogger(AbsDeliverService.class);
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("info");
    private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger.getLogger("error");

    private static final String YYYY_MMDD_HH = "yyyy-MM-dd HH:mm:ss";
    /**
     * c
     * @param orderDTO
     * @return
     */
    public abstract List<OrderDTO> handleShippingOrder(List<OrderDTO> orderDTO);


    @Autowired
    com.shangpin.iog.service.OrderService productOrderService;

    private OrderService iceOrderService = new OrderService();

    public void deliveryOrder(String supplierId){
        try {
            //获取已提交的产品信息
            List<OrderDTO> uuIdList =  productOrderService.getOrderBySupplierIdAndOrderStatus(supplierId, OrderStatus.CONFIRMED);
            Map<String,String> param =new HashMap<>();
            String uuId ="";
            String result ="";

            for(OrderDTO orderDTO:uuIdList){
                if ( OrderStatus.SHIPPED.equals(orderDTO.getStatus())) {
                    //通知SOP已发货
                    String  purchaseDetailNo = orderDTO.getSpPurchaseDetailNo();
                    if(StringUtils.isBlank(purchaseDetailNo)) continue;
                    List<String> purchaseOrderIdList = new ArrayList<>();
                    String[] purchaseDetailNoArray = purchaseDetailNo.split(";");
                    if(null!=purchaseDetailNoArray){
                        for(String purchaseDetailNO:purchaseDetailNoArray){
                            purchaseOrderIdList.add(purchaseDetailNO);
                        }

                    }

                    try {
                        String  deliverNo=  iceOrderService.getPurchaseDeliveryOrderNo(supplierId,
                                "","","",5,"","","", "","","",purchaseOrderIdList,0);
                        //更新海外对接库
                        Map<String, String> map = new HashMap<>();
                        map.put("status",orderDTO.getStatus());
                        map.put("uuId", orderDTO.getUuId());
                        map.put("updateTime", com.shangpin.iog.common.utils.DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
                        map.put("deliveryNo",deliverNo);
                        try {
                            productOrderService.updateDeliveryNo(map);
                        } catch (Exception e) {
                            //增加异常信息
                            loggerError.error("订单:" + orderDTO.getUuId() + " 已发货。"
                                    + " 采购单：" + orderDTO.getSpOrderId() + " 推送发货单信息成功 " + " 但保存推送信息时失败 ");
                            e.printStackTrace();
                        }
                    } catch (Exception e) {


                        loggerError.error("订单:" + orderDTO.getUuId() + " 已发货。" + " 采购单：" + orderDTO.getSpOrderId() + "推送发货单信息失败");
                        //增加异常信息

                        String date = com.shangpin.iog.common.utils.DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH) ;
                        Map<String, String> map = new HashMap<>();
//                            map.put("status", dto.getStatus());
                        map.put("uuId", orderDTO.getUuId());
                        map.put("updateTime", date);
                        map.put("excState","1");
                        map.put("excDesc","订单:" + orderDTO.getUuId() + "已发货，但推送发货单信息时失败");
                        map.put("excTime", date);
                        try {
                            productOrderService.updateOrderMsg(map);
                        } catch (ServiceException e1) {
                            loggerError.error("订单:" + orderDTO.getUuId() + " 已发货。" + " 采购单：" + orderDTO.getSpOrderId() + " 推送发货单信息失败。 保存信息失败");
                        }

                    }

                }
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            loggerError.error("获得gilt采购单状态更改失败");
        }
    }
}
