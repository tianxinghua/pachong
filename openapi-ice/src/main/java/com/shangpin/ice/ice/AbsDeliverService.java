package com.shangpin.ice.ice;

import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetail;
import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetailPage;
import ShangPin.SOP.Servant.OpenApiServantPrx;
import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.*;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.LogisticsDTO;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.service.LogisticsService;
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



    private static ResourceBundle bdl = null;
    private static Integer Delay=20;


    static {
        if(null==bdl){
            bdl=ResourceBundle.getBundle("openice");
        }

        Delay=Integer.parseInt(bdl.getString("delay"));

    }
    /**
     * c
     * @param orderDTO
     * @return
     */
    protected abstract void handleConfirmShippedOrder(OrderDTO orderDTO);


    @Autowired
    com.shangpin.iog.service.OrderService productOrderService;

    @Autowired
    LogisticsService logisticsService;

    private OrderService iceOrderService = new OrderService();



    /**
     * 发货确认
     * @param supplierId
     */
    public void confirmShippedOrder(String supplierId){

        List<com.shangpin.iog.dto.OrderDTO>  orderDTOList= null;
        Date date = new Date();
        String startDate="",endDate="";
        endDate = DateTimeUtil.convertFormat(date,YYYY_MMDD_HH) ;
        startDate = DateTimeUtil.convertFormat(DateTimeUtil.getAppointDayFromSpecifiedDay(date,-Delay,"D"),YYYY_MMDD_HH);
        try {
            orderDTOList  =productOrderService.getOrderBySupplierIdAndOrderStatusAndTime(supplierId,OrderStatus.CONFIRMED,startDate,endDate);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        if(null!=orderDTOList){
            logger.info("已确认的订单数量是："+orderDTOList.size());
            for(OrderDTO orderDTO :orderDTOList){
                //订单支付后查询是否发货
                handleConfirmShippedOrder(orderDTO);

                if(orderDTO.getStatus().equals(OrderStatus.SHIPPED)){
                    logger.info("已发货的采购单："+orderDTO.getSpPurchaseNo());
                    updateOrderMsg(orderDTO);
                    addLogisticsMsg(orderDTO);
                }


            }

        }

    }

    public void deliveryOrder(String supplierId){

        //获取已提交的产品信息
        Date searchDate = new Date();

        List<String> trackNumList = logisticsService.findNotConfirmSupplierLogisticsNumber(supplierId,searchDate);
        for(String trackNum:trackNumList){
            Date date=new Date();
            LogisticsDTO logisticsDTO =  logisticsService.findPurchaseDetailNoBySupplierIdAndTrackNumber(supplierId,trackNum);
            try {
                //获取任意一个采购单的信息
                OpenApiServantPrx servant = null;
                try {
                    servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                PurchaseOrderDetailPage page=servant.FindPurchaseOrderDetail(supplierId,logisticsDTO.getPurchaseNo());
                List<PurchaseOrderDetail> purchaseOrderDetails=  page.PurchaseOrderDetails;
                String warehouseCode="",warehouse="";
                PurchaseOrderDetail purchaseOrderDetail = purchaseOrderDetails.get(0);
                warehouseCode = purchaseOrderDetail.WarehouseNo;
                warehouse = purchaseOrderDetail.WarehouseName;

                String  deliverNo=  iceOrderService.getPurchaseDeliveryOrderNo(supplierId,
                        logisticsDTO.getLogisticsCompany(),logisticsDTO.getTrackNumber(),logisticsDTO.getShippedTime(),4,
                        supplierId,"联系方式无","发货地址无", "发货备注无",warehouseCode,warehouse,logisticsDTO.getPurchaseDetailList(),0);
                //更新状态
                logger.info( " 供货商：" + supplierId+ "　产生的发货单号是："+ deliverNo);
                logisticsService.updateInvoice(supplierId,trackNum,deliverNo,date);


            } catch (Exception e) {
                loggerError.error( " 供货商：" + supplierId+ "　发货单号："+ trackNum + "推送给尚品发货单信息时失败");

            }
        }



    }


    private void updateOrderMsg(OrderDTO spOrder) {
        //更新订单状态
        Map<String,String> map = new HashMap<>();

        map.put("uuId", spOrder.getUuId());
        map.put("supplierOrderNo",spOrder.getSupplierOrderNo());
        map.put("excState",spOrder.getExcState());
        map.put("excDesc",spOrder.getExcDesc());
        if(null!=spOrder.getExcState()&&"1".equals(spOrder.getExcState())){
            map.put("excTime", com.shangpin.iog.common.utils.DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
        }else{
            map.put("status",spOrder.getStatus());
            map.put("updateTime", DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
        }
        try {

            productOrderService.updateOrderMsg(map);
        } catch (ServiceException e) {
            loggerError.error("订单："+spOrder.getSpOrderId()+" 更新订单状态失败");
            System.out.println("订单：" + spOrder.getSpOrderId() + " 更新订单状态失败");
            e.printStackTrace();
        }
    }

    private void addLogisticsMsg(OrderDTO orderDTO){
        String[] logsticsArray = orderDTO.getDeliveryNo().split(";",-1);
        if(null!=logsticsArray&&3==logsticsArray.length){

            try {
                logisticsService.save(orderDTO,logsticsArray[0],logsticsArray[1],logsticsArray[2]);
            } catch (ServiceException e) {
                loggerError.error("采购单:"+ orderDTO.getSpPurchaseNo() + "保存物流信息失败。" );
            }
        }else{
            loggerError.error("采购单:"+ orderDTO.getSpPurchaseNo() + "传入发货单信息不正确:" + orderDTO.getDeliveryNo() );
        }


    }
}
