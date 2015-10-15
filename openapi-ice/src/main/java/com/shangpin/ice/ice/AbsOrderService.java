package com.shangpin.ice.ice;

import ShangPin.SOP.Entity.Api.Purchase.*;
import ShangPin.SOP.Entity.Where.OpenApi.Purchase.PurchaseOrderQueryDto;
import ShangPin.SOP.Servant.OpenApiServantPrx;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.*;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.dto.SkuRelationDTO;
import com.shangpin.iog.ice.dto.*;
import com.shangpin.iog.service.ReturnOrderService;
import com.shangpin.iog.service.SkuPriceService;
import com.shangpin.iog.service.SkuRelationService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by loyalty on 15/9/9.
 */
public abstract class AbsOrderService {

    private static String  startDate=null,endDate=null;
    private static String  startDateOfWMS=null,endDateOfWMS=null;
    private static final String YYYY_MMDD_HH = "yyyy-MM-dd HH:mm:ss";
    private static final String YYYY_MMDD_HH_WMS = "yyyy-MM-dd 'T' HH:mm:ss";

    static Logger log = LoggerFactory.getLogger(AbsOrderService.class);

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("info");
    private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger.getLogger("error");

    static String url="/purchase/createdeliveryorder";




    @Autowired
	public SkuPriceService skuPriceService;

    @Autowired
    com.shangpin.iog.service.OrderService productOrderService;

    @Autowired
    ReturnOrderService returnOrderService;

    @Autowired
    SkuRelationService skuRelationService;


    /**
     * 处理供货商订单信息
     * @param orderDTO
     * 订单状态需求参是 orderDTO 里的状态
     * @throws ServiceException
     */
    abstract  public void handleSupplierOrder(OrderDTO orderDTO) ;

    /**
     * 订单从下单到支付后的处理
     * @param orderDTO  订单信息
     */
    abstract public void handleConfirmOrder(OrderDTO orderDTO);

    /**
     * 取消订单
     * @throws ServiceException
     */
    abstract  public void handleCancelOrder(ReturnOrderDTO deleteOrder) ;

    /**
     * 获取真正的供货商编号
     * @param skuMap  key skuNo ,value supplerSkuNo
     * @return
     * @throws ServiceException
     */
    abstract public void getSupplierSkuId(Map<String,String> skuMap) throws ServiceException;

    /**
     *  通过采购单处理下单 包括下单和退单
     * @param supplierId 门户编号  201****
     *
     * @param supplierNo 供货商编号  S*****
     *
     * @param handleCancel 是否处理退单
     */
    public  void  checkoutOrderFromSOP(String supplierId,String supplierNo,boolean handleCancel){

        //初始化时间
        initDate("date.ini");

        //处理异常
        handlePurchaseOrderException(supplierId);

        handleCancelPurchaseOrderException(supplierId);

        //处理订单
        handleOrderOfSOP(supplierId, supplierNo);
        //处理退单
        cancelOrderFromSOP(supplierNo,supplierId,handleCancel);


    }


    /**
     * 通过WMS下单 包换退单的处理
     * @param supplierNo 供货商编号    S******
     * @param supplierId 供货商门户编号 2015****
     */
    public  void  checkoutOrderFromWMS(String supplierNo,String supplierId,boolean handleCancel){
        //初始化时间
        initWMSDate("dateWMS.ini");
        //获取订单数组
        Gson gson = new Gson();
        ICEWMSOrderRequestDTO  dto = new ICEWMSOrderRequestDTO();
        dto.setBeginTime(startDateOfWMS);
        dto.setEndTime(endDateOfWMS);
        dto.setSupplierNo(supplierNo);

        String jsonParameter= "="+ gson.toJson(dto);
        String result ="";
        try {
            result =  HttpUtil45.operateData("post","form","http://wmsinventory.liantiao.com/Api/StockQuery/SupplierInventoryLogQuery",new OutTimeConfig(1000*5,1000*5,1000*5),null,
                    jsonParameter,"","");
            logger.info("获取的订单信息为:" + result);
            System.out.println("kk = " + result);
            result =  result.substring(1,result.length()-1).replace("\\","");
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        List<ICEWMSOrderDTO> orderDTOList  = null;
        try {
            orderDTOList = gson.fromJson(result,new TypeToken<List<ICEWMSOrderDTO>>(){}.getType());
        } catch (JsonSyntaxException e) {
            loggerError.error("订单转化异常,退出");
            return;
        }
        String uuid="",skuNo="";
        //由于拉取时可能更改供货商的SKU的编号需要 继承者确认
        Map<String,String> skuMap = new HashMap<>();
        List<ICEWMSOrderDTO> orderList = new ArrayList<>();
        List<ICEWMSOrderDTO> refundList = new ArrayList<>();
        for(ICEWMSOrderDTO icewmsOrderDTO:orderDTOList){
            SkuRelationDTO skuRelationDTO= null;
            if(icewmsOrderDTO.getChangeForOrderQuantity()<0){   //订单
                orderList.add(icewmsOrderDTO);
            }else{
                refundList.add(icewmsOrderDTO);
            }
            try {
                skuRelationDTO=  skuRelationService.getSkuRelationBySkuId(icewmsOrderDTO.getSkuNo());
                if(null!=skuRelationDTO){
                    skuMap.put(skuRelationDTO.getSopSkuId(), skuRelationDTO.getSupplierSkuId());
                }else{   //获取供货商的SKU编号

                }
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
        try {
            //获取真正的供货商SKUID
            this.getSupplierSkuId(skuMap);
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        // 检查订单是否已经被支付  若支付修改其状态
        checkPayed(supplierId);

        //处理异常
        handlePurchaseOrderException(supplierId);

        handleCancelPurchaseOrderException(supplierId);

        //处理订单
        handleOrderOfWMS(supplierNo, supplierId, skuMap, orderList);

        //处理退单
        handleRefundOrderOfWMS(supplierNo, supplierId, skuMap, refundList,handleCancel);
    }



    /**
     * 订单确认
     * @param supplierId
     */
    public void confirmOrder(String supplierId){

        List<com.shangpin.iog.dto.OrderDTO>  orderDTOList= null;
        try {
            orderDTOList  =productOrderService.getOrderBySupplierIdAndOrderStatus(supplierId,OrderStatus.PAYED);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        if(null!=orderDTOList){
            for(OrderDTO orderDTO :orderDTOList){
                //订单支付后的处理
                handleConfirmOrder(orderDTO);
                updateOrderMsg(orderDTO);
            }

        }

    }

    /**
     * 检查订单是否支付
     * @param supplierId
     */
    private  void checkPayed(String supplierId){


        List<OrderDTO>  orderDTOList= null;

        try {
            //获取已下单的订单信息
            orderDTOList  =productOrderService.getOrderBySupplierIdAndOrderStatus(supplierId, OrderStatus.PLACED);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        try {
            // 获取采购单    更新订单状态
            OpenApiServantPrx servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
            for(OrderDTO orderDTO:orderDTOList){

                PurchaseOrderDetailSpecialPage  orderDetailSpecialPage = servant.FindPurchaseOrderDetailSpecial(supplierId,"",orderDTO.getSpOrderId());
                if(null!=orderDetailSpecialPage&&null!=orderDetailSpecialPage.PurchaseOrderDetails&&orderDetailSpecialPage.PurchaseOrderDetails.size()>0){  //存在采购单 就代表已支付
                   //更新其已支付状态
                    orderDTO.setStatus(OrderStatus.PAYED);

                    productOrderService.update(orderDTO);

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /*
    处理发生错误的订单
     */
    private void handlePurchaseOrderException(String supplierId){
        //拉取采购单存入本地库
        List<OrderDTO>  orderDTOList= null;
        try {
            orderDTOList  =productOrderService.getOrderBySupplierIdAndOrderStatus(supplierId, OrderStatus.WAITPLACED);
            if(null!=orderDTOList){

                for(OrderDTO orderDTO:orderDTOList){

                    try {
                        //处理供货商订单
                        handleSupplierOrder(orderDTO);
                        //更新海外购订单信息
                        updateOrderMsg(orderDTO);


                    } catch (Exception e) {
                        e.printStackTrace();
                        loggerError.error("订单处理失败。失败信息 " + orderDTO.toString()+" 原因 ：" + e.getMessage() );

                    }
                }
            }

        } catch (ServiceException e) {
            e.printStackTrace();
        }

    }

    /**
     * 处理尚未有退单操作记录的异常
     * @param supplierId
     */
    private void handleCancelPurchaseOrderException(String supplierId){
        //拉取采购单存入本地库
        List<ReturnOrderDTO>  orderDTOList= null;
        try {
            orderDTOList  =returnOrderService.getReturnOrderBySupplierIdAndOrderStatus(supplierId, OrderStatus.WAITCANCEL);
            if(null!=orderDTOList){

                for(ReturnOrderDTO deleteOrder:orderDTOList){

                    try {
                        //处理取消订单
                        handleCancelOrder(deleteOrder);
                        //更改退单状态无论成功或失败 还需要更改订单状态

                        updateRefundOrderMsg(deleteOrder);


                    } catch (Exception e) {
                        e.printStackTrace();
                        loggerError.error("退单处理失败。失败信息 " + deleteOrder.toString()+" 原因 ：" + e.getMessage() );

                    }
                }
            }

        } catch (ServiceException e) {
            e.printStackTrace();
        }

    }



    private void handleOrderOfSOP(String supplierId, String supplierNo) {
        //获取订单数组
        List<Integer> status = new ArrayList<>();
        status.add(1);

        Map<String,List<PurchaseOrderDetail>> orderMap = null;
        try {
            orderMap = this.getPurchaseOrder(supplierId, startDate, endDate, status);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String uuid;
        for(Iterator<Map.Entry<String,List<PurchaseOrderDetail>>> itor = orderMap.entrySet().iterator();itor.hasNext();){
            Map.Entry<String, List<PurchaseOrderDetail>> entry = itor.next();
            ICEOrderDTO orderDTO = new ICEOrderDTO();
            Map<String,Integer> stockMap = new HashMap<>();
            StringBuffer purchaseOrderDetailbuffer =new StringBuffer();
            //获取同一产品的数量

            for(PurchaseOrderDetail purchaseOrderDetail:entry.getValue()){

                if(stockMap.containsKey(purchaseOrderDetail.SupplierSkuNo)){
                    stockMap.put(purchaseOrderDetail.SupplierSkuNo, stockMap.get(purchaseOrderDetail.SupplierSkuNo)+1);
                }else{
                    stockMap.put(purchaseOrderDetail.SupplierSkuNo, 1);

                }

            }
            List<ICEOrderDetailDTO>list=new ArrayList<>();
            StringBuffer buffer = new StringBuffer();
            StringBuffer sopbuffer= new StringBuffer();
            for(PurchaseOrderDetail purchaseOrderDetail:entry.getValue()){
                //记录采购单明细信息 以便发货
                purchaseOrderDetailbuffer.append(purchaseOrderDetail.SopPurchaseOrderDetailNo).append(";");
                //计算同一采购单的相同产品的数量
                if(stockMap.containsKey(purchaseOrderDetail.SupplierSkuNo)){
                    ICEOrderDetailDTO detailDTO = new ICEOrderDetailDTO();
                    detailDTO.setSku_id(purchaseOrderDetail.SupplierSkuNo);
                    detailDTO.setQuantity(stockMap.get(purchaseOrderDetail.SupplierSkuNo));
                    buffer.append(detailDTO.getSku_id()).append(":").append(detailDTO.getQuantity()).append(",");
                    sopbuffer.append(purchaseOrderDetail.SkuNo).append(":").append(detailDTO.getQuantity()).append(",");
                    list.add(detailDTO);
                    stockMap.remove(purchaseOrderDetail.SupplierSkuNo);
                }

            }

            orderDTO.setOrder_items(list);
            uuid=UUID.randomUUID().toString();

            //存储
            OrderDTO spOrder =new OrderDTO();
            spOrder.setUuId(uuid);
            spOrder.setSupplierId(supplierId);
            spOrder.setSupplierNo(supplierNo);
            spOrder.setStatus(OrderStatus.WAITPLACED);
            spOrder.setSpPurchaseNo(entry.getKey());
            spOrder.setSpPurchaseDetailNo(purchaseOrderDetailbuffer.toString());
            spOrder.setDetail(buffer.toString());
            spOrder.setMemo(sopbuffer.toString());
            spOrder.setCreateTime(new Date());
            try {
                logger.info("采购单信息转化订单后信息："+spOrder.toString());
                System.out.println("采购单信息转化订单后信息："+spOrder.toString());
                productOrderService.saveOrder(spOrder);

                try {
                   //处理供货商订单
                    handleSupplierOrder(spOrder);
                    //更新海外购订单信息
                    updateOrderMsg(spOrder);


                } catch (Exception e) {
                    e.printStackTrace();
                    loggerError.error("采购单 ："+ spOrder.getSpPurchaseNo() + "处理失败。失败信息 " + spOrder.toString()+" 原因 ：" + e.getMessage() );
                    Map<String,String> map = new HashMap<>();
                    map.put("excDesc",e.getMessage());
                    setErrorMsg(spOrder.getUuId(),map);

                }

            } catch (ServiceException e) {
                loggerError.error("采购单 ："+ spOrder.getSpPurchaseNo() + "失败,失败信息 " + spOrder.toString()+" 原因 ：" + e.getMessage() );
                System.out.println("采购单 ："+ spOrder.getSpPurchaseNo() + "失败,失败信息 " + spOrder.toString()+" 原因 ：" + e.getMessage());
                e.printStackTrace();
            } catch (Exception e){
                loggerError.error("下单错误 " + e.getMessage());
                e.printStackTrace();
            }


            logger.info("---- SOP订单操作完成----");
        }
    }



    /**
     * 处理订单
     * @param supplierNo
     * @param supplierId
     * @param skuMap
     * @param orderLit
     */
    private void handleOrderOfWMS(String supplierNo, String supplierId, Map<String, String> skuMap, List<ICEWMSOrderDTO> orderLit) {
        String uuid;
        for(ICEWMSOrderDTO icewmsOrderDTO:orderLit){
            if(!skuMap.containsKey(icewmsOrderDTO.getSkuNo()))
                continue;

            uuid= UUID.randomUUID().toString();

            //存储
            OrderDTO spOrder =new OrderDTO();
            spOrder.setUuId(uuid);
            spOrder.setSupplierId(supplierId);
            spOrder.setSupplierNo(supplierNo);
            spOrder.setStatus(OrderStatus.WAITPLACED);
            spOrder.setSpOrderId(icewmsOrderDTO.getFormNo());
            spOrder.setDetail(skuMap.get(icewmsOrderDTO.getSkuNo())+":"+Math.abs(icewmsOrderDTO.getChangeForOrderQuantity()));
            spOrder.setMemo(icewmsOrderDTO.getSkuNo()+":"+icewmsOrderDTO.getChangeForOrderQuantity());
            spOrder.setCreateTime(new Date());
            try {
                logger.info("订单信息："+spOrder.toString());
                System.out.println("订单信息："+spOrder.toString());
                productOrderService.saveOrder(spOrder);

                try {
                    //处理供货商订单
                    handleSupplierOrder(spOrder);
                    //更新海外购订单信息
                    updateOrderMsg(spOrder);


                } catch (Exception e) {
                    e.printStackTrace();
                    loggerError.error("订单 ："+ spOrder.getSpOrderId() + "处理失败。失败信息 " + spOrder.toString()+" 原因 ：" + e.getMessage() );
                    Map<String, String> map = new HashMap<>();
                    map.put("excDesc", e.getMessage());
                    setErrorMsg(spOrder.getUuId(), map);

                }

            } catch (Exception e){
                loggerError.error("下单错误 " + e.getMessage());
                e.printStackTrace();
            }
            logger.info("----订单处理完成----");
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
            map.put("excTime", DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
        }else{
            map.put("status",spOrder.getStatus());
            map.put("updateTime",DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
        }
        try {
            productOrderService.updateOrderMsg(map);
        } catch (ServiceException e) {
            loggerError.error("订单："+spOrder.getSpOrderId()+" 下单成功。但更新订单状态失败");
            System.out.println("订单：" + spOrder.getSpOrderId() + " 下单成功。但更新订单状态失败");
            e.printStackTrace();
        }
    }

    private void handleRefundOrderOfWMS(String supplierNo,String supplierId,Map<String,String> skuMap,List<ICEWMSOrderDTO>  refundList,boolean handleCancel){

        for(ICEWMSOrderDTO refundOrder:refundList) {

            String uuid = null;
            OrderDTO order = null;
            try {
                order = productOrderService.getOrderByOrderNo(refundOrder.getFormNo());
            } catch (ServiceException e) {
                e.printStackTrace();
            }
            //存储
            if(null==order){//采购单已到退款状态  未有已支付状态
                loggerError.error("退单信息：" + refundOrder.toString() + "  未发现订单，异常不做处理。");
                continue;
            }

            ReturnOrderDTO deleteOrder =new ReturnOrderDTO();
            deleteOrder.setUuId(order.getUuId());
            deleteOrder.setSupplierId(supplierId);
            deleteOrder.setSupplierNo(supplierNo);
            deleteOrder.setSupplierOrderNo(order.getSupplierOrderNo());
            if(handleCancel) {
                deleteOrder.setStatus(OrderStatus.WAITCANCEL);
            }else{
                deleteOrder.setStatus(OrderStatus.NOHANDLE);
            }
            deleteOrder.setSpOrderId(refundOrder.getFormNo());
            deleteOrder.setDetail(skuMap.get(refundOrder.getSkuNo())+":"+refundOrder.getChangeForOrderQuantity());
            deleteOrder.setMemo(refundOrder.getSkuNo()+":"+refundOrder.getChangeForOrderQuantity());
            deleteOrder.setCreateTime(new Date());
            try{
                logger.info("退单信息："+deleteOrder.toString());
                returnOrderService.saveOrder(deleteOrder);
                if(!handleCancel){
                    continue;
                }
                try {
                    //处理退单
                    handleCancelOrder(deleteOrder);
                    //更改退单状态无论成功或失败
                    updateRefundOrderMsg( deleteOrder);


                } catch (Exception e) {
                    //下单失败
                    loggerError.error("退单："+deleteOrder.getSpOrderId()+" 处理失败");
                    System.out.println("退单：" + deleteOrder.getSpOrderId() + " 处理失败");
                    e.printStackTrace();
                }

            }catch (Exception e){
                loggerError.error("下单错误 " + e.getMessage());
                e.printStackTrace();
            }


        }
    }

    private void updateRefundOrderMsg( ReturnOrderDTO deleteOrder) {
        try {
            Map<String,String> map = new HashMap<>();
            map.put("status",deleteOrder.getStatus());
            map.put("uuid",deleteOrder.getUuId());
            map.put("excState",deleteOrder.getExcState());
            map.put("excDesc",deleteOrder.getExcDesc());
            if(null!=deleteOrder.getExcState()&&"1".equals(deleteOrder.getExcState())){
                map.put("excTime", DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
            }else{
                map.put("status","cancelled");
                map.put("updateTime",DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
            }
            returnOrderService.updateReturnOrderMsg(map);
        } catch (ServiceException e) {
            loggerError.error("退单："+deleteOrder.getUuId()+" 操作成功。但更新订单状态失败 原因:" +e.getMessage());
            System.out.println("退单：" + deleteOrder.getUuId() + " 操作成功。但更新订单状态失败  原因:" +e.getMessage());
            e.printStackTrace();

        }
        /**
         * 退单成功时修改订单状态
         */
        if(null!=deleteOrder.getExcState()&&"1".equals(deleteOrder.getExcState())){//通知供货商退款异常

        }else{
            this.updateOrderMsgOnCancelOrder(deleteOrder.getUuId());
        }

    }

    /**
     * 当取消订单时 更改订单状态
     * @param uuId 订单编号
     */
    private void updateOrderMsgOnCancelOrder(String uuId){
        OrderDTO order = null;
        try {
            order = productOrderService.getOrderByUuId(uuId);
            order.setStatus(OrderStatus.CANCELLED);
            productOrderService.update(order);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    public void cancelOrderFromSOP(String supplierNo,String supplierId,boolean handleCancel){

        try {
            //获取订单数组
            List<Integer> status = new ArrayList<>();
            status.add(5);
            Map<String,List<PurchaseOrderDetail>> orderMap =  this.getPurchaseOrder(supplierId, startDate, endDate, status);

            for(Iterator<Map.Entry<String,List<PurchaseOrderDetail>>> itor = orderMap.entrySet().iterator();itor.hasNext();) {
                Map.Entry<String, List<PurchaseOrderDetail>> entry = itor.next();
                Map<String, Integer> stockMap = new HashMap<>();
                for (PurchaseOrderDetail purchaseOrderDetail : entry.getValue()) {
                    if (stockMap.containsKey(purchaseOrderDetail.SupplierSkuNo)) {
                        stockMap.put(purchaseOrderDetail.SupplierSkuNo, stockMap.get(purchaseOrderDetail.SupplierSkuNo) + 1);
                    } else {
                        stockMap.put(purchaseOrderDetail.SupplierSkuNo, 1);
                    }
                }
                List<ICEOrderDetailDTO> list = new ArrayList<>();
                StringBuffer buffer = new StringBuffer();
                for (PurchaseOrderDetail purchaseOrderDetail : entry.getValue()) {
                    if (stockMap.containsKey(purchaseOrderDetail.SupplierSkuNo)) {
                        buffer.append(purchaseOrderDetail.SupplierSkuNo).append(":").append(stockMap.get(purchaseOrderDetail.SupplierSkuNo)).append(",");
                        stockMap.remove(purchaseOrderDetail.SupplierSkuNo);
                    }
                }
                /**
                 * 根据sp_order_id查询UUID
                 */
                String uuid = productOrderService.getUuIdBySpOrderId(entry.getKey());

                if(org.apache.commons.lang.StringUtils.isBlank(uuid)){//采购单已到退款状态  未有已支付状态 为下单 不做存储
                    continue;
                }

                ReturnOrderDTO deleteOrder =new ReturnOrderDTO();
                deleteOrder.setUuId(uuid);
                deleteOrder.setSupplierId(supplierId);
                deleteOrder.setSupplierNo(supplierNo);
                if(handleCancel){
                    deleteOrder.setStatus(OrderStatus.WAITCANCEL);
                }else{
                    deleteOrder.setStatus(OrderStatus.NOHANDLE);
                    deleteOrder.setMemo("退单不做处理只做记录");
                }

                deleteOrder.setSpOrderId(entry.getKey());
                deleteOrder.setDetail(buffer.toString());
                deleteOrder.setCreateTime(new Date());
                try{
                    logger.info("采购单信息转化退单后信息："+deleteOrder.toString());
                    System.out.println("采购单信息转化退单后信息："+deleteOrder.toString());
                    returnOrderService.saveOrder(deleteOrder);
                    if(!handleCancel){
                        //不处理退单
                        continue;
                    }
                    try {
                        //处理取消订单
                        handleCancelOrder(deleteOrder);
                        //更改退单状态无论成功或失败 还需要更改订单状态

                        updateRefundOrderMsg( deleteOrder);

                    } catch (Exception e) {
                        e.printStackTrace();

                        loggerError.error("订单 ："+ deleteOrder.getUuId() + "处理退单失败。 原因 ：" + e.getMessage() );

                        Map<String,String> map = new HashMap<>();
                        map.put("excDesc",e.getMessage());

                        setErrorMsg(deleteOrder.getUuId(),map);

                    }

                } catch (ServiceException e) {

                        e.printStackTrace();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 获取采购单
     * 需要注意
     * SOP处理采购单 一条记录代表一个库存  同样的产品 购买两件 生成两条记录
     *
     * @return
     */
    private Map<String,List<PurchaseOrderDetail>> getPurchaseOrder(String supplierId,String startTime ,String endTime,List<Integer> statusList) throws Exception{
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
     * 首先推送发货单
     * @return
     * @throws Exception
     */
    public  String getPurchaseDeliveryOrderNo(String supplierId,String logisticsName, String logisticsOrderNo,
                                                    String dateDeliver, int estimateArrivedTime, String deliveryContacts,
                                                    String deliveryContactsPhone, String deliveryAddress,
                                                    String deliveryMemo, String warehouseNo, String warehouseName,
                                                    List<String> sopPurchaseOrderDetailNo, int printStatus) throws Exception{
        OpenApiServantPrx servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
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





    private void setErrorMsg(String uuid, Map<String, String> map) {
        map.put("uuid",uuid);
        map.put("excState","1");

        map.put("excTime", DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
        try {
            productOrderService.updateExceptionMsg(map);
        } catch (ServiceException e) {
            loggerError.error("保存订单号：" + uuid + "，错误信息时失败");
            e.printStackTrace();
        }
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
        List<String> SopPurchaseOrderDetailNo=new ArrayList<>();
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


    private  static void initDate(String  fileName) {
        Date tempDate = new Date();

        endDate = com.shangpin.iog.common.utils.DateTimeUtil.convertFormat(tempDate, YYYY_MMDD_HH);

        String lastDate=getLastGrapDate(fileName);
        startDate= org.apache.commons.lang.StringUtils.isNotEmpty(lastDate) ? lastDate: com.shangpin.iog.common.utils.DateTimeUtil.convertFormat(DateUtils.addDays(tempDate, -180), YYYY_MMDD_HH);



        writeGrapDate(endDate,fileName);


    }


    private  static void initWMSDate(String  fileName) {
        Date tempDate = new Date();

        endDateOfWMS = com.shangpin.iog.common.utils.DateTimeUtil.convertFormat(tempDate, YYYY_MMDD_HH_WMS);

        String lastDate=getLastGrapDate(fileName);
        startDateOfWMS= org.apache.commons.lang.StringUtils.isNotEmpty(lastDate) ? lastDate: com.shangpin.iog.common.utils.DateTimeUtil.convertFormat(DateUtils.addDays(tempDate, -180), YYYY_MMDD_HH_WMS);



        writeGrapDate(endDateOfWMS,fileName);


    }

    private static File getConfFile(String fileName) throws IOException {
        String realPath = AbsOrderService.class.getClassLoader().getResource("").getFile();
        realPath= URLDecoder.decode(realPath, "utf-8");
        File df = new File(realPath+fileName);//"date.ini"
        if(!df.exists()){
            df.createNewFile();
        }
        return df;
    }
    private static String getLastGrapDate(String fileName){
        File df;
        String dstr=null;
        try {
            df = getConfFile(fileName);
            try(BufferedReader br = new BufferedReader(new FileReader(df))){
                dstr=br.readLine();
            }
        } catch (IOException e) {
            logger.error("读取日期配置文件错误");
        }
        return dstr;
    }

    private static void writeGrapDate(String date,String fileName){
        File df;
        try {
            df = getConfFile(fileName);

            try(BufferedWriter bw = new BufferedWriter(new FileWriter(df))){
                bw.write(date);
            }
        } catch (IOException e) {
            logger.error("写入日期配置文件错误");
        }
    }



}
